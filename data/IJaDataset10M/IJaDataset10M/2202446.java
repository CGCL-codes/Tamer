package uk.ac.manchester.ac.uk.server.repository;

import info.aduna.xml.XMLWriter;
import org.apache.commons.io.output.WriterOutputStream;
import org.openrdf.model.*;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.rio.*;
import uk.ac.manchester.ac.uk.server.io.CSVQueryResultHandler;
import uk.ac.manchester.ac.uk.server.io.HTMLQueryResultHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 14, 2011<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class KUPKBManager {

    private RepositoryManager repositoryManager;

    private static Repository repository;

    private static RepositoryConnection repositoryConnection;

    private static Graph graph;

    private static Resource repositoryNode = null;

    public KUPKBManager() throws RepositoryException, RepositoryConfigException {
        new KUPKBManager(new FileBasedKUPKBConfig());
    }

    public KUPKBManager(KUPKBConfig config) throws RepositoryConfigException, RepositoryException {
        if (config.isLocal()) {
            parseConfig(config.getConfigFilePath());
            repositoryManager = KUPKBConnection.getLocalConnection(config.getLocalRepositoryConnectionPath());
            RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
            repositoryManager.addRepositoryConfig(repositoryConfig);
        } else {
            System.out.println("Connecting to: " + config.getRemoteRepositoryConnectionURL());
            repositoryManager = KUPKBConnection.getHTTPConnection(config.getRemoteRepositoryConnectionURL());
            System.out.println("Connected!");
        }
        repository = repositoryManager.getRepository(config.getRepositoryID());
        repositoryConnection = repository.getConnection();
    }

    public RepositoryResult<Namespace> getNameSpaces() throws RepositoryException {
        return repositoryConnection.getNamespaces();
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public static Repository getRepository() {
        return repository;
    }

    public static RepositoryConnection getRepositoryConnection() {
        return repositoryConnection;
    }

    public TupleQueryResultHandler evaluateQuery(String queryid, String format, PrintWriter pw) throws Exception {
        TupleQueryResultHandler result;
        String[] queries = collectQueries(queryid, "./test_queries");
        for (int i = 0; i < queries.length; i++) {
            final String name = queries[i].substring(0, queries[i].indexOf(":"));
            final String query = queries[i].substring(name.length() + 2).trim();
            System.out.println("Executing query '" + name + "'");
            executeSingleQuery(query, format, pw);
        }
        return null;
    }

    public TupleQuery prepareTupleQuery(String query) throws RepositoryException, MalformedQueryException {
        return repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
    }

    public void executeSingleQuery(String query, String format, PrintWriter pw) {
        try {
            TupleQuery tuple = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
            tuple.setIncludeInferred(true);
            TupleQueryResultHandler handler = new SPARQLResultsXMLWriter(new XMLWriter(pw));
            if (format.equals("html")) {
                System.out.println("html result handler");
                handler = new HTMLQueryResultHandler(pw, repositoryConnection.getNamespaces());
            } else if (format.equals("json")) {
                handler = new SPARQLResultsJSONWriter(new WriterOutputStream(pw));
            } else if (format.equals("csv")) {
                handler = new CSVQueryResultHandler(pw, repositoryConnection.getNamespaces());
            }
            tuple.evaluate(handler);
        } catch (MalformedQueryException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (TupleQueryResultHandlerException e) {
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Query prepareQuery(String query) throws Exception {
        try {
            return repositoryConnection.prepareQuery(QueryLanguage.SPARQL, query);
        } catch (UnsupportedQueryLanguageException e) {
        } catch (MalformedQueryException e) {
        }
        return null;
    }

    private void parseConfig(String pathToConfig) {
        File configFile = new File(pathToConfig);
        try {
            graph = parseFile(configFile, RDFFormat.TURTLE, "http://example.org#");
            Iterator<Statement> iter = graph.match(null, RDF.TYPE, new URIImpl("http://www.openrdf.org/config/repository#Repository"));
            if (iter.hasNext()) {
                Statement st = iter.next();
                repositoryNode = st.getSubject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse the given RDF file and return the contents as a Graph
     *
     * @param configurationFile The file containing the RDF data
     * @return The contents of the file as an RDF graph
     */
    private Graph parseFile(File configurationFile, RDFFormat format, String defaultNamespace) throws Exception {
        final Graph graph = new GraphImpl();
        RDFParser parser = Rio.createParser(format);
        RDFHandler handler = new RDFHandler() {

            public void endRDF() throws RDFHandlerException {
            }

            public void handleComment(String arg0) throws RDFHandlerException {
            }

            public void handleNamespace(String arg0, String arg1) throws RDFHandlerException {
            }

            public void handleStatement(Statement statement) throws RDFHandlerException {
                graph.add(statement);
            }

            public void startRDF() throws RDFHandlerException {
            }
        };
        parser.setRDFHandler(handler);
        parser.parse(new FileReader(configurationFile), defaultNamespace);
        return graph;
    }

    /**
     * Parse the query file and return the queries defined there for further
     * evaluation. The file can contain several queries; each query starts with
     * an id enclosed in square brackets '[' and ']' on a single line; the text
     * in between two query ids is treated as a SeRQL query. Each line starting
     * with a '#' symbol will be considered as a single-line comment and
     * ignored. Query file syntax example:
     *
     * #some comment [queryid1] <query line1> <query line2> ... <query linen>
     * #some other comment [nextqueryid] <query line1> ... <EOF>
     *
     * @param queryFile
     * @return an array of strings containing the queries. Each string starts
     *         with the query id followed by ':', then the actual query string
     */
    private static String[] collectQueries(String queryid, String queryFile) throws Exception {
        List<String> queries = new ArrayList<String>();
        BufferedReader inp = new BufferedReader(new FileReader(queryFile));
        String nextLine = null;
        for (; ; ) {
            String line = nextLine;
            nextLine = null;
            if (line == null) {
                line = inp.readLine();
            }
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("^[") && line.endsWith("]") && line.contains(queryid)) {
                StringBuffer buff = new StringBuffer(line.substring(2, line.length() - 1));
                buff.append(": ");
                for (; ; ) {
                    line = inp.readLine();
                    if (line == null) {
                        break;
                    }
                    line = line.trim();
                    if (line.length() == 0) {
                        continue;
                    }
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (line.startsWith("^[")) {
                        nextLine = line;
                        break;
                    }
                    buff.append(line);
                    buff.append(System.getProperty("line.separator"));
                }
                queries.add(buff.toString());
            }
        }
        String[] result = new String[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            result[i] = queries.get(i);
        }
        return result;
    }

    public void shutdown() {
        System.out.println("===== Shutting down ==========");
        if (repository != null) {
            try {
                repositoryConnection.close();
                repository.shutDown();
                repositoryManager.shutDown();
            } catch (Exception e) {
                System.out.println("An exception occurred during shutdown: " + e.getMessage());
            }
        }
    }

    public ValueFactory getValueFactory() {
        return repositoryConnection.getValueFactory();
    }
}
