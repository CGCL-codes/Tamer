package org.aksw.resparql;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.UnsupportedDataTypeException;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import org.aksw.commons.sparql.core.SparqlEndpoint;
import org.aksw.commons.sparql.core.impl.HttpSparqlEndpoint;
import org.aksw.commons.util.strings.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.collections15.MultiMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.linkedgeodata.core.ILGDVocab;
import org.linkedgeodata.core.LGDVocab;
import org.linkedgeodata.dao.HibernateSessionProvider;
import org.linkedgeodata.dao.IConnectionFactory;
import org.linkedgeodata.dao.ISessionProvider;
import org.linkedgeodata.dao.JDBCConnectionProvider;
import org.linkedgeodata.dao.LGDDAO;
import org.linkedgeodata.dao.LGDRDFDAO;
import org.linkedgeodata.dao.TagMapperDAO;
import org.linkedgeodata.dao.gragh.RdfGraphDaoGraph;
import org.linkedgeodata.jtriplify.methods.DefaultCoercions;
import org.linkedgeodata.jtriplify.methods.IInvocable;
import org.linkedgeodata.jtriplify.methods.Pair;
import org.linkedgeodata.osm.mapping.CachingTagMapper;
import org.linkedgeodata.osm.mapping.ITagMapper;
import org.linkedgeodata.osm.mapping.TagMappingDB;
import org.linkedgeodata.util.ConnectionConfig;
import org.linkedgeodata.util.ExceptionUtil;
import org.linkedgeodata.util.HTMLJenaWriter;
import org.linkedgeodata.util.ModelUtil;
import org.linkedgeodata.util.StreamUtil;
import org.linkedgeodata.util.StringUtil;
import org.linkedgeodata.util.URIUtil;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

interface IMyHandler {

    /**
	 * 
	 * @param x
	 * @return true if accepted, false otherwise
	 */
    boolean handle(HttpExchange x) throws Exception;
}

class MyHttpHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(MyHttpHandler.class);

    private List<IMyHandler> subHandlers = new ArrayList<IMyHandler>();

    public List<IMyHandler> getSubHandlers() {
        return subHandlers;
    }

    @Override
    public void handle(HttpExchange x) {
        try {
            _handle(x);
        } catch (Throwable t) {
            logger.error(ExceptionUtil.toString(t));
        }
    }

    public void _handle(HttpExchange x) throws Exception {
        for (IMyHandler item : subHandlers) {
            if (item.handle(x)) {
                return;
            }
        }
        String msg = "Internal server error. Maybe a misspelled or non-existent resource was requested?";
        MyHandler.sendResponse(x, 500, "text/plain", msg);
    }
}

class DataHandler implements IMyHandler {

    private static final Logger logger = Logger.getLogger(DataHandler.class);

    private RegexInvocationContainer ric = new RegexInvocationContainer();

    public RegexInvocationContainer getRIC() {
        return ric;
    }

    @Override
    public boolean handle(HttpExchange x) {
        try {
            return _handle(x);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateHTMLRepresentation(String body) {
        String cssPath = MyBeanFactory.getSingleton().get("triplifyCSSFile", String.class);
        String content = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" " + "\"http://www.w3.org/TR/html4/loose.dtd\">\n" + "<html>\n" + "<head>\n" + "<title></title>\n" + "<link rel='stylesheet' type='text/css' href='" + cssPath + "' />\n" + "</head>\n" + "<body>\n" + body + "</body>\n" + "</html>\n";
        return content;
    }

    private boolean isDataUri(String uri) {
        return uri.contains("/data");
    }

    private boolean _handle(HttpExchange x) throws Exception {
        System.out.println("Request method = " + x.getRequestMethod());
        System.out.println("Request body = " + StreamUtil.toString(x.getRequestBody()));
        Map.Entry<String, ContentType> resultType;
        Map<String, ContentType> accepts = MyHandler.getPreferredFormats(x.getRequestHeaders());
        String requestURI = x.getRequestURI().toString();
        if (isDataUri(requestURI)) {
            accepts.remove("HTML");
        }
        String extension = MyHandler.getExtension(x.getRequestURI().toString());
        String requestedFormat = null;
        if (extension != null) {
            requestedFormat = MyHandler.getFormatFromExtension(extension);
            if (requestedFormat == null) {
                extension = null;
            }
        }
        String qsFormat = MyHandler.getJenaFormatByQueryString(x.getRequestURI());
        requestedFormat = StringUtil.coalesce(requestedFormat, qsFormat);
        resultType = MyHandler.getContentType(requestedFormat, accepts);
        if (requestURI.contains("triplify")) {
            String targetURL = (resultType.getValue().match("text/html")) ? requestURI.replace("triplify", "page") : requestURI.replace("triplify", "data");
            MyHandler.sendRedirect(x, targetURL);
            return true;
        }
        if (requestURI.endsWith("/")) {
            requestURI = requestURI.substring(0, requestURI.length() - 1);
        }
        if (extension != null) {
            requestURI = requestURI.substring(0, requestURI.length() - extension.length() - 1);
        }
        if (isDataUri(requestURI) && extension == null) {
            resultType = Pair.create("RDF/XML", MyHandler.jenaFormatToContentType.get("RDF/XML"));
        }
        Model model = null;
        try {
            model = (Model) ric.invoke(requestURI);
        } catch (Throwable t) {
            logger.error(ExceptionUtil.toString(t));
        }
        if (model == null) return false;
        RDFWriter writer = MyHandler.getWriter(resultType.getKey());
        String body = ModelUtil.toString(model, writer);
        if ("HTML".equalsIgnoreCase(resultType.getKey())) {
            String url = requestURI;
            String note = "" + "<p>" + "You are viewing the html representation of this document." + "<br />Other formats: " + "<a href='" + url + ".rdf'>rdf/xml</a> " + "<a href='" + url + ".nt'>n-triples</a> " + "<a href='" + url + ".ttl'>turtle</a> " + "<a href='" + url + ".n3'>n3</a> " + "</p>";
            body = note + body;
            body = generateHTMLRepresentation(body);
        }
        if (resultType != null) {
            MyHandler.sendResponse(x, 200, resultType.getValue().toString(), body);
            return true;
        }
        return false;
    }
}

class ICPair {

    private IInvocable invocable;

    private Object[] argMap;

    public ICPair(IInvocable invocable, Object[] argMap) {
        this.invocable = invocable;
        this.argMap = argMap;
    }

    public IInvocable getInvocable() {
        return invocable;
    }

    public Object[] getArgMap() {
        return argMap;
    }
}

class RedirectInvocable implements IInvocable {

    private String pattern;

    public RedirectInvocable(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String invoke(Object... args) throws Exception {
        String url = pattern;
        for (int i = 0; i < args.length; ++i) {
            url = url.replace("$" + i, args[i] == null ? "" : args[i].toString());
        }
        return url;
    }
}

class LinkedDataRedirectHandler implements IMyHandler {

    private RegexInvocationContainer pageRIC = new RegexInvocationContainer();

    private RegexInvocationContainer dataRIC = new RegexInvocationContainer();

    public RegexInvocationContainer getPageRIC() {
        return pageRIC;
    }

    public RegexInvocationContainer getDataRIC() {
        return dataRIC;
    }

    @Override
    public boolean handle(HttpExchange x) throws Exception {
        Map<String, ContentType> accepts = MyHandler.getPreferredFormats(x.getRequestHeaders());
        if (accepts.isEmpty()) return false;
        RegexInvocationContainer ric = accepts.containsValue(new ContentType("text/html")) ? pageRIC : dataRIC;
        String targetURL = (String) ric.invoke(x.getRequestURI().toString());
        if (targetURL == null) {
            return false;
        }
        MyHandler.sendRedirect(x, targetURL);
        return true;
    }
}

class SimpleResponse {

    private int statusCode;

    private Map<String, List<String>> header = new HashMap<String, List<String>>();

    private String contentType;

    private String body;

    public SimpleResponse() {
    }

    public SimpleResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public static SimpleResponse redirect(String url) {
        SimpleResponse result = new SimpleResponse(303);
        result.getHeader().put("Location", Collections.singletonList(url));
        return result;
    }

    public SimpleResponse(int statusCode, String contentType, String body) {
        this.statusCode = statusCode;
        this.header.put("Content-Type", Collections.singletonList(contentType));
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public String getBody() {
        return body;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }
}

class HTTPErrorException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int errorCode;

    public HTTPErrorException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

class MyHandler {

    private static final Logger logger = Logger.getLogger(JTriplifyServer.class);

    private RegexInvocationContainer ric = null;

    private static Map<ContentType, String> contentTypeToJenaFormat = new HashMap<ContentType, String>();

    public static Map<String, ContentType> jenaFormatToContentType = new HashMap<String, ContentType>();

    private static Map<String, String> formatToJenaFormat = new HashMap<String, String>();

    private static Map<String, String> extensionToJenaFormat = new HashMap<String, String>();

    public static RDFWriter getWriter(String format) {
        if ("HTML".equalsIgnoreCase(format)) return new HTMLJenaWriter();
        RDFWriter writer = ModelFactory.createDefaultModel().getWriter(format);
        return writer;
    }

    {
        try {
            Model model = ModelFactory.createDefaultModel();
            contentTypeToJenaFormat.put(new ContentType("text/html"), "HTML");
            contentTypeToJenaFormat.put(new ContentType("application/rdf+xml"), "RDF/XML");
            contentTypeToJenaFormat.put(new ContentType("application/x-turtle"), "TURTLE");
            contentTypeToJenaFormat.put(new ContentType("text/turtle"), "TURTLE");
            contentTypeToJenaFormat.put(new ContentType("text/n3;"), "N3");
            contentTypeToJenaFormat.put(new ContentType("text/rdf+n3"), "N3");
            jenaFormatToContentType.put("RDF/XML", new ContentType("application/rdf+xml"));
            jenaFormatToContentType.put("TURTLE", new ContentType("application/x-turtle; charset=utf-8"));
            jenaFormatToContentType.put("N3", new ContentType("text/rdf+n3; charset=utf-8"));
        } catch (Exception e) {
            logger.fatal(ExceptionUtil.toString(e));
            System.exit(1);
        }
        formatToJenaFormat.put("rdfxml", "RDF/XML");
        formatToJenaFormat.put("n3", "N3");
        formatToJenaFormat.put("nt", "N-TRIPLE");
        formatToJenaFormat.put("turtle", "TURTLE");
        extensionToJenaFormat.put("rdfxml", "RDF/XML");
        extensionToJenaFormat.put("rdf", "RDF/XML");
        extensionToJenaFormat.put("n3", "N3");
        extensionToJenaFormat.put("nt", "N-TRIPLE");
        extensionToJenaFormat.put("ttl", "TURTLE");
        extensionToJenaFormat.put("htm", "HTML");
        extensionToJenaFormat.put("html", "HTML");
    }

    public void setInvocationMap(RegexInvocationContainer ric) {
        this.ric = ric;
    }

    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable == null) return null;
        Iterator<T> it = iterable.iterator();
        if (!it.hasNext()) return null;
        T result = it.next();
        return result;
    }

    /**
	 * .rdf, .n3, .ttl, .nt, .html
	 *
	 */
    public static String getExtension(String str) {
        int index = str.lastIndexOf('.');
        if (index == -1) return null;
        return str.substring(index + 1);
    }

    public static String getFormatFromExtension(String ext) {
        if (ext == null) return null;
        String result = extensionToJenaFormat.get(ext);
        return result;
    }

    public static Map<String, ContentType> getPreferredFormats(Headers requestHeaders) throws ParseException {
        List<String> accepts = requestHeaders.get("Accept");
        if (accepts == null) accepts = Collections.emptyList();
        logger.info("Accept header: " + accepts);
        Map<String, ContentType> result = new HashMap<String, ContentType>();
        int acceptCounter = 0;
        for (String accept : accepts) {
            String[] items = accept.split(",");
            for (String item : items) {
                ++acceptCounter;
                ContentType ct = null;
                try {
                    ct = new ContentType(item);
                } catch (Exception e) {
                    logger.warn("Error parsing content type", e);
                    continue;
                }
                if (ct.match("text/plain") || ct.match("text/html") || ct.match("*/*")) {
                    if (!result.containsKey("N-TRIPLE")) {
                        result.put("N-TRIPLE", new ContentType("text/plain"));
                    }
                }
                for (Map.Entry<ContentType, String> entry : contentTypeToJenaFormat.entrySet()) {
                    if (!ct.match(entry.getKey())) continue;
                    String tmp = entry.getValue();
                    if (tmp != null) {
                        if (!result.containsKey(tmp)) {
                            result.put(tmp, ct);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
	 * Returns a pair of serializiation format and content-type
	 * 
	 */
    public static Map.Entry<String, ContentType> getContentType(String requestedFormat, Map<String, ContentType> accepts) throws ParseException {
        if (requestedFormat == null) {
            if (accepts.isEmpty()) {
                return new Pair<String, ContentType>("N-TRIPLE", new ContentType("text/plain; charset=utf-8"));
            } else {
                return accepts.entrySet().iterator().next();
            }
        } else if (!accepts.containsKey(requestedFormat)) {
            ContentType contentType = StringUtils.coalesce(jenaFormatToContentType.get(requestedFormat), new ContentType("text/plain; charset=utf-8"));
            return new Pair<String, ContentType>(requestedFormat, contentType);
        } else {
            return new Pair<String, ContentType>(requestedFormat, accepts.get(requestedFormat));
        }
    }

    public static SimpleResponse respondModel(Model model, Map.Entry<String, ContentType> contentType) {
        String response = ModelUtil.toString(model, contentType.getKey());
        SimpleResponse result = new SimpleResponse(200, contentType.getValue().toString(), response);
        return result;
    }

    private SimpleResponse process(HttpExchange t) throws Exception {
        String request = t.getRequestURI().toString();
        Object o = ric.invoke(request);
        if (o instanceof SimpleResponse) return (SimpleResponse) o;
        throw new UnsupportedDataTypeException();
    }

    public static String getJenaFormatByExtension(URI uri) {
        String host = uri.toString();
        String ext = getExtension(host);
        String result = getFormatFromExtension(ext);
        return result;
    }

    public static String getJenaFormatByQueryString(URI uri) {
        String query = uri.getQuery();
        MultiMap<String, String> params = URIUtil.getQueryMap(query);
        String rawFormat = getFirst(params.get("format"));
        rawFormat = rawFormat == null ? null : rawFormat.trim().toLowerCase();
        String requestFormat = formatToJenaFormat.get(rawFormat);
        if (rawFormat != null && requestFormat == null) {
            return null;
        }
        return requestFormat;
    }

    public static void sendRedirect(HttpExchange x, String targetURL) throws IOException {
        x.getResponseHeaders().set("Location", targetURL);
        x.sendResponseHeaders(303, -1);
    }

    public static void sendResponse(HttpExchange x, int statusCode, String contentType, String body) throws IOException {
        try {
            Model model = ModelFactory.createDefaultModel();
            Resource r = ResourceFactory.createResource("http://ex.org/resource/a");
            Property p = ResourceFactory.createProperty("http://ex.org/ontology/b");
            model.add(r, p, r);
            x.getResponseHeaders().set("Content-Type", contentType);
            x.sendResponseHeaders(200, 0);
            OutputStream out = x.getResponseBody();
            String str = ModelUtil.toString(model, "RDF/XML");
            out.write(body.getBytes());
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void sendResponseX(HttpExchange x, int statusCode, String contentType, String body) throws IOException {
        if (contentType == null) contentType = "text/plain";
        int responseLength = 0;
        if (body == null) {
            responseLength = -1;
        }
        Headers headers = x.getResponseHeaders();
        headers.set("Content-Type", contentType);
        x.sendResponseHeaders(statusCode, responseLength);
        OutputStream os = x.getResponseBody();
        OutputStreamWriter osw;
        try {
            osw = new OutputStreamWriter(os, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseLength != -1) {
            osw.write(body);
        }
        osw.close();
        os.close();
    }
}

public class JTriplifyServer {

    private static final Logger logger = Logger.getLogger(JTriplifyServer.class);

    protected static final Options cliOptions = new Options();

    private static void initCLIOptions() {
        cliOptions.addOption("p", "port", true, "Server port");
        cliOptions.addOption("c", "context", true, "Context e.g. /triplify/");
        cliOptions.addOption("b", "backlog", true, "Maximum number of connections");
        cliOptions.addOption("t", "type", true, "Database type (posgres, mysql,...)");
        cliOptions.addOption("d", "database", true, "Database name");
        cliOptions.addOption("u", "user", true, "");
        cliOptions.addOption("w", "password", true, "");
        cliOptions.addOption("h", "host", true, "");
        cliOptions.addOption("n", "batchSize", true, "Batch size");
    }

    public static String test(String a, String b) {
        System.out.println("In test");
        return "Hello " + a + " and " + b + "!";
    }

    /*************************************************************************/
    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("log4j.properties");
        initCLIOptions();
        CommandLineParser cliParser = new GnuParser();
        CommandLine commandLine = cliParser.parse(cliOptions, args);
        String portStr = commandLine.getOptionValue("p", "7000");
        String backLogStr = commandLine.getOptionValue("b", "100");
        String context = commandLine.getOptionValue("c", "/triplify");
        int port = Integer.parseInt(portStr);
        int backLog = Integer.parseInt(backLogStr);
        String hostName = commandLine.getOptionValue("h", "localhost");
        String dbName = commandLine.getOptionValue("d", "lgd");
        String userName = commandLine.getOptionValue("u", "lgd");
        String passWord = commandLine.getOptionValue("w", "lgd");
        String batchSizeStr = commandLine.getOptionValue("n", "1000");
        int batchSize = Integer.parseInt(batchSizeStr);
        if (batchSize <= 0) throw new RuntimeException("Invalid argument for batchsize");
        new MyHandler();
        logger.info("Connecting to db");
        ConnectionConfig connectionConfig = new ConnectionConfig(hostName, dbName, userName, passWord);
        MyHttpHandler myHandler = new MyHttpHandler();
        RegexInvocationContainer ric = new RegexInvocationContainer();
        initCurrent(myHandler, connectionConfig);
        runServer(context, port, backLog, myHandler);
    }

    private static void initCurrent(MyHttpHandler mainHandler, ConnectionConfig connectionConfig) throws Exception {
        TagMappingDB.getSession();
        ISessionProvider sessionFactory = new HibernateSessionProvider();
        IConnectionFactory connectionFactory = new JDBCConnectionProvider(connectionConfig);
        Connection conn = connectionFactory.getConnection();
        String prefixModelPath = "Namespaces.2.0.ttl";
        logger.info("Loading uri namespaces");
        Model prefixModel = ModelFactory.createDefaultModel();
        ModelUtil.read(prefixModel, new File(prefixModelPath), "TTL");
        Map<String, String> prefixMap = prefixModel.getNsPrefixMap();
        logger.info("Loading mapping rules");
        TagMapperDAO dbTagMapper = new TagMapperDAO();
        ITagMapper tagMapper = new CachingTagMapper(dbTagMapper);
        LGDDAO innerDAO = new LGDDAO(conn);
        ILGDVocab vocab = new LGDVocab();
        Set<String> defaultGraphNames = new HashSet<String>();
        defaultGraphNames.add("http://linkedgeodata.org/110406/dbpedia");
        defaultGraphNames.add("http://linkedgeodata.org/110406/geonames");
        SparqlEndpoint sparqlEndpoint = new HttpSparqlEndpoint("http://linkedgeodata.org/sparql", defaultGraphNames);
        LGDRDFDAO dao = new LGDRDFDAO(innerDAO, tagMapper, vocab, sparqlEndpoint);
        dao.setConnection(conn);
        Session session = sessionFactory.createSession();
        dao.setSession(session);
        dbTagMapper.setSession(session);
        Graph metaOntologyGraph = MyBeanFactory.getSingleton().getMetaOntologyModel().getGraph();
        RdfGraphDaoGraph dataOntologyGraph = new RdfGraphDaoGraph(dao);
        Graph ontologyGraph = new Union(metaOntologyGraph, dataOntologyGraph);
        Model ontologyModel = ModelFactory.createModelForGraph(ontologyGraph);
        IRestApi methods = new ServerMethods(dao, prefixMap, connectionFactory, sessionFactory, ontologyModel);
        Method m;
        DataHandler dataHandler = new DataHandler();
        mainHandler.getSubHandlers().add(dataHandler);
        m = ServerMethods.class.getMethod("getWayNode", Long.class);
        dataHandler.getRIC().put(".*/way([^.]*)/nodes.*", DefaultCoercions.wrap(methods, "getWayNode"), "$1");
        m = ServerMethods.class.getMethod("getNode", Long.class);
        dataHandler.getRIC().put(".*/node([0-9]+).*", DefaultCoercions.wrap(methods, "getNode"), "$1");
        m = ServerMethods.class.getMethod("getWay", Long.class);
        dataHandler.getRIC().put(".*/way([^./]*)(\\.[^/]*)?/?(\\?.*)?", DefaultCoercions.wrap(methods, "getWay"), "$1");
        String pattern = "";
        IInvocable nearFn = DefaultCoercions.wrap(methods, "publicGetEntitiesWithinRadius.*");
        pattern = ".*/near/(-?[^,-]*),(-?[^-/]*)/([^/?]*)(/class/([^/]*))?(/label/([^/]*)/([^/]*)/([^/]*))?(/(\\d+))?(/(\\d+))?/?(\\?.*)?";
        dataHandler.getRIC().put(pattern, nearFn, "$1", "$2", "$3", "$5", "$7", "$8", "$9", "$11", "$13");
        IInvocable areaFn = DefaultCoercions.wrap(methods, "publicGetAreaStatistics.*");
        pattern = ".*/area/(-?[^-]+)-(-?[^,]+),(-?[^-]+)-(-?[^/]+)?/?(\\?.*)?";
        dataHandler.getRIC().put(pattern, areaFn, "$1", "$2", "$3", "$4");
        IInvocable bboxFn = DefaultCoercions.wrap(methods, "publicGetEntitiesWithinRect.*");
        pattern = ".*/near/(-?[^-]+)-(-?[^,]+),(-?[^-]+)-(-?[^/]+)(/class/([^/]*))?(/label/([^/]*)/([^/]*)/([^/]*))?(/(\\d+))?(/(\\d+))?/?(\\?.*)?";
        dataHandler.getRIC().put(pattern, bboxFn, "$1", "$2", "$3", "$4", "$6", "$8", "$9", "$10", "$12", "$14");
        IInvocable getOntologyFn = DefaultCoercions.wrap(methods, "publicGetOntology.*");
        dataHandler.getRIC().put(".*/ontology(\\.[^/]*)?/?(\\?.*)?", getOntologyFn);
        IInvocable describeFn = DefaultCoercions.wrap(methods, "publicDescribe.*");
        dataHandler.getRIC().put(".*/(ontology/[^/\\.]+)(\\.[^/\\?]*)?(\\?.*)?", describeFn, "$1");
    }

    private static void runServer(String context, int port, int backLog, HttpHandler handler) throws IOException {
        logger.info("Starting JTriplify Server");
        InetSocketAddress socketAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(socketAddress, backLog);
        server.createContext(context, handler);
        server.setExecutor(null);
        server.start();
    }
}
