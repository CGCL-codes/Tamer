package org.embl.ebi.SoaplabClient;

import org.embl.ebi.SoaplabShare.*;
import org.apache.axis.client.*;
import embl.ebi.tools.*;
import embl.ebi.utils.*;
import embl.ebi.soap.axis.*;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Provides support for registering Soaplab services.
 *<P>
 * See details in help:
 * <pre>
 *    java RegistryAdmin -h
 * </pre>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: RegistryAdminWorker.java,v 1.1.1.1 2005/06/15 09:00:19 marsenger Exp $
 */
public abstract class RegistryAdminWorker {

    protected String[] services;

    protected RegistryRecord[] records;

    protected SharedRecord shared;

    protected String host, port, spath, endpoint;

    protected boolean verbose = true;

    protected boolean exitOnError = true;

    String lineSeparator;

    String fileSeparator;

    String rrStart, rrEnd;

    public static final String WSDL_URL_TEMPLATE = "wsdl_url_template";

    public static final String USERID = "userid";

    public static final String PASSWORD = "password";

    public static final String PUBLISHURL = "publishurl";

    public static final String INQUIRYURL = "inquiryurl";

    public static final String PROVIDER = "provider";

    public static final String TAXONOMY = "taxonomy";

    public static final int NORMAL_SERVICES = 0;

    public static final int ALL_SERVICES = 1;

    public static final int ONLY_DERIVED_SERVICES = 2;

    public static final String FORMAT_XML = "xml";

    public static final String FORMAT_UDDI4J = "uddi4j";

    public static final String FORMAT_BIOMOBY = "biomoby";

    static final String SUFFIX = ".derived";

    /*************************************************************************
     * Return name of a service providing list of services.
     *************************************************************************/
    protected abstract String getListServiceName();

    /*************************************************************************
     * Constructor gets some general properties. Here are their names
     * and default values:
     *
     *<pre>
     *   host     localhost
     *   port     8080
     *   spath    /axis/services/             prefix (in URL) for services
     *   endpoint http://localhost:8080/axis/services
     *   verbose  true                        report what is happening
     *   exit     true                        exit on error
     *</pre>
     *
     *************************************************************************/
    public RegistryAdminWorker(Properties opts) throws SoaplabException {
        host = opts.getProperty("host");
        if (host == null) host = "localhost";
        port = opts.getProperty("port");
        if (port == null) port = "8080";
        spath = opts.getProperty("spath");
        if (spath == null) spath = "/axis/services/";
        if (!spath.endsWith("/")) spath += "/";
        endpoint = opts.getProperty("endpoint");
        if (endpoint == null) endpoint = "http://" + host + ":" + port + spath;
        if (!endpoint.endsWith("/")) endpoint += "/";
        String tmp = opts.getProperty("verbose");
        if (tmp != null && !UUtils.is(tmp)) verbose = false;
        tmp = opts.getProperty("exit");
        if (tmp != null && !UUtils.is(tmp)) exitOnError = false;
        lineSeparator = System.getProperty("line.separator");
        fileSeparator = System.getProperty("file.separator");
        rrStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + lineSeparator + "<registry_records>" + lineSeparator;
        rrEnd = "</registry_records>" + lineSeparator;
        services = new String[] {};
    }

    /*************************************************************************
     * Constructor gets a list of service names to work on.  If the
     * list of services is empty, the Factory Service is used to get
     * all available services.
     *
     * It also gets some general properties - see details above.
     *************************************************************************/
    public RegistryAdminWorker(String[] services, Properties opts) throws SoaplabException {
        this(opts);
        if (services == null) {
            String factoryEndpoint = endpoint + getListServiceName();
            report("Fetching names of available analyses...\n" + "(from " + endpoint + ")");
            this.services = (String[]) doCall(factoryEndpoint, "getAvailableAnalyses", new Object[] {});
        } else {
            this.services = services;
        }
        if (this.services == null || this.services.length == 0) {
            warning("No service names available!");
            this.services = new String[] {};
        } else report(this.services.length + " analyses available");
    }

    /*************************************************************************
     * Report a message...
     *************************************************************************/
    protected void report(String msg) {
        if (verbose) System.out.println(msg);
    }

    /*************************************************************************
     * Report a warning...
     *************************************************************************/
    protected void warning(String msg) {
        System.err.println("WARNING: " + msg);
    }

    /*************************************************************************
     * Report an error... and exit or throw an exception.
     *************************************************************************/
    protected void error(String msg) throws SoaplabException {
        if (exitOnError) {
            System.err.println(msg);
            System.exit(1);
        } else {
            throw new SoaplabException(msg);
        }
    }

    /*************************************************************************
     * Call 'method' with 'parameters' and return its result.
     *************************************************************************/
    protected Object doCall(String endpoint, String method, Object[] parameters) throws SoaplabException {
        try {
            AxisCall call = new AxisCall(new URL(endpoint));
            return call.doCall(method, parameters);
        } catch (MalformedURLException e) {
            error("Bad service factory location '" + endpoint + "'. " + e.getMessage());
        } catch (Exception e) {
            error(e.toString());
        }
        return null;
    }

    /*************************************************************************
     * Remove "strange" characters from the 'filename'. I do not
     * expect a full path to be sent here - therefore I can change also
     * colons without danger to hurt MS-* platform.
     *************************************************************************/
    protected String filenameEscape(String filename) {
        return filename.replace(':', '_').replace('/', '_').replace(' ', '_');
    }

    /*************************************************************************
     * Make 'value' a (almost) valid Java identifier by replacing some
     * characters by underscores. "almost" means that it can still
     * start with a digid (because of the way how it is used it does
     * not matter).
     *************************************************************************/
    protected String javaEscape(String value) {
        boolean found = false;
        char[] s = value.toCharArray();
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_')) continue;
            found = true;
            s[i] = '_';
        }
        return (found ? new String(s) : value);
    }

    /*************************************************************************
     * Make 'value' a valid string constant in Java:
     *    - replace each newline by \n
     *    - escape quote characters
     *************************************************************************/
    protected String stringEscape(String value) {
        if (value.indexOf("\"") > -1) value = StringUtils.replace(value, "\"", "\\\"");
        if (value.indexOf("\n") > -1) value = StringUtils.replace(value, "\n", "\\n");
        return value;
    }

    /*************************************************************************
     * Make sure that 'value' can become a value of an attribute in an
     * XML format.
     * TBD: More escaped characters to consider?
     *************************************************************************/
    protected String xmlEscape(String value) {
        StringBuffer buf = new StringBuffer(value);
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '"') {
                buf.replace(i, i + 1, "&quot;");
                i += 5;
            }
        }
        return new String(buf);
    }

    /*************************************************************************
     * Create 'filename' from 'buf'.
     *************************************************************************/
    protected void createFile(String filename, StringBuffer buf) throws SoaplabException {
        try {
            PrintWriter fileout = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            fileout.print(buf);
            fileout.close();
        } catch (IOException e) {
            error("Cannot create file '" + filename + "'. " + e.toString());
        }
    }

    /*************************************************************************
     * If 'name' begins with the current directory, remove it and
     * return the new name
     *************************************************************************/
    protected String removeBase(String name) {
        String currDir = System.getProperty("user.dir");
        if (name.startsWith(currDir)) return name.substring(currDir.length() + 1); else return name;
    }

    /*************************************************************************
     * Create file 'filename' from 'value'.
     *************************************************************************/
    protected void createFile(String filename, byte[] value) throws SoaplabException {
        try {
            BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(new File(filename)));
            fileout.write(value);
            fileout.close();
        } catch (IOException e) {
            error("Cannot create file '" + filename + "'. " + e.toString());
        }
    }

    /*************************************************************************
     * Read and return a file 'templateFile'.
     *************************************************************************/
    protected String loadTemplate(String templateFile) throws SoaplabException {
        try {
            return new String(FileUtils.findAndGetBinaryFile(templateFile));
        } catch (GException e) {
            error("Cannot find a template file '" + templateFile + "'." + e.getMessage());
        }
        return null;
    }

    /*************************************************************************
     * 'fullname' contains a web-service name, try to remove a category
     * from there and return only a simple service name
     *************************************************************************/
    protected String guessName(String fullname) {
        int pos = fullname.lastIndexOf(AnalysisFactoryWS.SOAPLAB_SERVICE_NAME_DELIMITER);
        if (pos > -1) return fullname.substring(pos + AnalysisFactoryWS.SOAPLAB_SERVICE_NAME_DELIMITER.length()); else return fullname;
    }

    /*************************************************************************
     * Fetch and return registry records for all 'services'.
     * See 'createRR' for explanation for 'props' and 'whatServices'
     *************************************************************************/
    protected RegistryRecord[] fetchRR(Hashtable props, int whatServices) throws SoaplabException {
        Vector v = new Vector();
        for (int i = 0; i < services.length; i++) {
            report("Asking '" + services[i] + "' for description...");
            Map aAttrs = (Map) doCall(endpoint + services[i], "getAnalysisType", new Object[] {});
            switch(whatServices) {
                case NORMAL_SERVICES:
                    v.addElement(populateRecord(services[i], aAttrs, props, ""));
                    break;
                case ONLY_DERIVED_SERVICES:
                    v.addElement(populateRecord(services[i], aAttrs, props, SUFFIX));
                    break;
                case ALL_SERVICES:
                    v.addElement(populateRecord(services[i], aAttrs, props, ""));
                    v.addElement(populateRecord(services[i], aAttrs, props, SUFFIX));
                    break;
            }
        }
        RegistryRecord[] rrs = new RegistryRecord[v.size()];
        v.copyInto(rrs);
        return rrs;
    }

    protected RegistryRecord populateRecord(String serviceName, Map aAttrs, Hashtable props, String suffix) {
        RegistryRecord rr = new RegistryRecord();
        if (aAttrs == null) {
            rr.name = guessName(serviceName) + suffix;
        } else {
            String attr = (String) aAttrs.get(AnalysisWS.ANALYSIS_NAME);
            rr.name = (attr == null ? guessName(serviceName) : attr) + suffix;
            if ((attr = (String) aAttrs.get(AnalysisWS.ANALYSIS_TYPE)) != null) rr.type = attr;
            if ((attr = (String) aAttrs.get(AnalysisWS.ANALYSIS_VERSION)) != null) rr.version = attr;
            if ((attr = (String) aAttrs.get(AnalysisWS.ANALYSIS_INSTALLATION)) != null) rr.provider = attr;
            if ((attr = (String) aAttrs.get(AnalysisWS.ANALYSIS_DESCRIPTION)) != null) rr.description = attr;
            rr.endpoint = endpoint + serviceName + suffix;
            rr.WSDL = getWSDL_URL(serviceName + suffix, props);
        }
        return rr;
    }

    /*************************************************************************
     * Create (from 'props' and some defaults) a record with attributes
     * shared by all services.
     *************************************************************************/
    protected SharedRecord createShared(Hashtable props) {
        SharedRecord sr = new SharedRecord();
        if (props.containsKey(TAXONOMY)) sr.taxonomy = (String) props.get(TAXONOMY);
        if (props.containsKey(USERID)) sr.userId = (String) props.get(USERID);
        if (props.containsKey(PASSWORD)) sr.password = (String) props.get(PASSWORD);
        if (props.containsKey(INQUIRYURL)) sr.inquiryURL = (String) props.get(INQUIRYURL);
        if (props.containsKey(PUBLISHURL)) sr.publishURL = (String) props.get(PUBLISHURL);
        if (props.containsKey(PROVIDER)) {
            sr.provider = (String) props.get(PROVIDER);
        } else {
            for (int i = 0; i < records.length; i++) {
                if (!UUtils.isEmpty(records[i].provider)) {
                    sr.provider = records[i].provider;
                    break;
                }
            }
        }
        return sr;
    }

    /*************************************************************************
     * Create registry records for all 'services' (as given in
     * constructor). Return a list of created filenames, or an empty list
     * if records were sent to the standard output.
     * 
     * if 'output' is an existing directory, create each registry record
     * in a separate file in this directory; otherwise use
     * 'output' as a file name where you put all records (consider '-'
     * a special file name meaning stdout). If 'output' is null, send
     * all records to the standard output.
     *
     * 'format' specifies how the created registry records will look like:
     *     FORMAT_XML
     *     FORMAT_UDD4J
     *     FORMAT_BIOMOBY  (not yet written)
     *
     * 'props' are additional optional properties used for some formats;
     * the recognized are now:
     *    WSDL_URL_TEMPLATE
     *       a template used for constructing URLs for WSDL of the
     *       the services (the template can contain $NAME and $ENAME which
     *       are replaced byte the real service name (escaped in case of $ENAME)
     *
     * 'whatServices' specify what set of services we work on:
     *    NORMAL_SERVICES
     *    ONLY_DERIVED_SERVICES
     *    ALL_SERVICES (which means NORMAL_SERVICES + ONLY_DERIVED_SERVICES)
     *
     *************************************************************************/
    public String[] createRR(String format, String output, Hashtable props, int whatServices) throws SoaplabException {
        if (output == null) output = "-";
        boolean useStdout = (output.equals("-"));
        boolean useDirectory = (!useStdout && new File(output).isDirectory());
        if (records == null) {
            records = fetchRR(props, whatServices);
            if (records.length == 0) return new String[] {};
            shared = createShared(props);
        }
        Vector files = new Vector();
        StringBuffer buf = new StringBuffer();
        if (useDirectory) {
            for (int i = 0; i < records.length; i++) {
                buf.setLength(0);
                buf.append(createRecords(records, i, i, format, props, null));
                String file = createFileName(output, records[i], format);
                createFile(file, buf);
                files.addElement(new File(file).getAbsolutePath());
                report("Created: " + removeBase(files.lastElement().toString()));
            }
        } else {
            buf.append(createRecords(records, 0, records.length - 1, format, props, output));
            if (useStdout) {
                System.out.println(buf);
            } else {
                createFile(output, buf);
                files.addElement(output);
                report("Created: " + output);
            }
        }
        String[] results = new String[files.size()];
        files.copyInto(results);
        return results;
    }

    /*************************************************************************
     * Create a file name from 'output' for 'record' regarding 'format'.
     *************************************************************************/
    protected String createFileName(String output, RegistryRecord record, String format) {
        if (format == null || format.equalsIgnoreCase(FORMAT_XML)) return output + fileSeparator + filenameEscape(record.name) + "_register.xml"; else if (format.equalsIgnoreCase(FORMAT_UDDI4J)) return output + fileSeparator + "Publish_" + javaEscape(record.name) + ".java"; else if (format.equalsIgnoreCase(FORMAT_BIOMOBY)) return output + fileSeparator + filenameEscape(record.name) + "_register.biomoby"; else return output + fileSeparator + filenameEscape(record.name) + "_register.unknown_format";
    }

    /*************************************************************************
     * Create registry records from those given in 'records' (from index
     * 'starting' to index 'ending' - in the given 'format'.
     *
     * For Java formats we need also to know what is the 'outputFileName'
     * (because it must match with the created class name).
     *
     * For some formats we need also additional properties shared byte all
     * services (such as the service provider).
     *************************************************************************/
    protected String createRecords(RegistryRecord[] records, int starting, int ending, String format, Hashtable props, String outputFileName) throws SoaplabException {
        if (format == null || format.equalsIgnoreCase(FORMAT_XML)) return createXMLRecords(records, starting, ending); else if (format.equalsIgnoreCase(FORMAT_UDDI4J)) return createUDDI4JRecords(records, starting, ending, outputFileName); else if (format.equalsIgnoreCase(FORMAT_BIOMOBY)) return createBioMobyRecords(records, starting, ending); else return "";
    }

    /*************************************************************************
     * Create registry records in the XML format.
     *************************************************************************/
    protected String createXMLRecords(RegistryRecord[] records, int starting, int ending) throws SoaplabException {
        StringBuffer buf = new StringBuffer();
        buf.append(rrStart);
        buf.append("<general publish_url=\"" + xmlEscape(shared.publishURL) + "\"" + lineSeparator);
        buf.append("         inquiry_url=\"" + xmlEscape(shared.inquiryURL) + "\"" + lineSeparator);
        buf.append("         provider=\"" + xmlEscape(shared.provider) + "\"" + lineSeparator);
        buf.append("         taxonomy=\"" + xmlEscape(shared.taxonomy) + "\"" + lineSeparator);
        buf.append("         userid=\"" + xmlEscape(shared.userId) + "\"" + lineSeparator);
        buf.append("         password=\"" + xmlEscape(shared.password) + "\"" + lineSeparator);
        buf.append("         />" + lineSeparator);
        for (int i = starting; i <= ending; i++) {
            RegistryRecord record = records[i];
            buf.append("<service name=\"" + record.name + "\"" + lineSeparator);
            if (!UUtils.isEmpty(record.type)) buf.append("         type=\"" + xmlEscape(record.type) + "\"" + lineSeparator);
            if (!UUtils.isEmpty(record.version)) buf.append("         version=\"" + xmlEscape(record.version) + "\"" + lineSeparator);
            if (!UUtils.isEmpty(record.endpoint)) buf.append("         endpoint=\"" + xmlEscape(record.endpoint) + "\"" + lineSeparator);
            if (!UUtils.isEmpty(record.WSDL)) buf.append("         wsdl_url=\"" + xmlEscape(record.WSDL) + "\"" + lineSeparator);
            if (!UUtils.isEmpty(record.function)) buf.append("   <function>" + xmlEscape(record.function) + "</function>" + lineSeparator);
            buf.append("         >" + lineSeparator);
            if (!UUtils.isEmpty(record.description)) buf.append("   <description>" + xmlEscape(record.description) + "</description>" + lineSeparator);
            buf.append("</service>" + lineSeparator);
        }
        buf.append(rrEnd);
        return new String(buf);
    }

    /*************************************************************************
     * Create a registry records in the UDDI4J format (as Java source code).
     *************************************************************************/
    protected String createUDDI4JRecords(RegistryRecord[] records, int starting, int ending, String outputFileName) throws SoaplabException {
        String classTemplate = loadTemplate("templates/PublishServices.java.template");
        String servicesTemplate = loadTemplate("templates/ServiceAttributes.java.template");
        StringBuffer srvs = new StringBuffer();
        for (int i = starting; i <= ending; i++) {
            RegistryRecord record = records[i];
            String genService = servicesTemplate;
            genService = StringUtils.replace(genService, "$INDEX", "" + i);
            genService = StringUtils.replace(genService, "$SERVICE_NAME", stringEscape(records[i].name));
            genService = StringUtils.replace(genService, "$SERVICE_URL", stringEscape(records[i].endpoint));
            genService = StringUtils.replace(genService, "$SERVICE_WSDL_URL", stringEscape(records[i].WSDL));
            genService = StringUtils.replace(genService, "$SERVICE_VERSION", stringEscape(records[i].version));
            genService = StringUtils.replace(genService, "$SERVICE_DESCRIPTION", stringEscape(records[i].description));
            genService = StringUtils.replace(genService, "$SERVICE_TYPE", stringEscape(records[i].type));
            srvs.append(genService);
        }
        String genClass = classTemplate;
        String className;
        if (outputFileName == null) className = "Publish_" + javaEscape(records[starting].name); else if (outputFileName.equals("-")) className = "Publish_"; else {
            className = new File(outputFileName).getName();
            int pos = className.lastIndexOf(".");
            if (pos > -1) className = className.substring(0, pos);
        }
        genClass = StringUtils.replace(genClass, "$NAME", javaEscape(className));
        genClass = StringUtils.replace(genClass, "$DATE", new Date().toString());
        genClass = StringUtils.replace(genClass, "$SERVICE_COUNT", "" + (ending - starting + 1));
        genClass = StringUtils.replace(genClass, "$SERVICES", new String(srvs));
        genClass = StringUtils.replace(genClass, "$USERID", stringEscape(shared.userId));
        genClass = StringUtils.replace(genClass, "$PASSWORD", stringEscape(shared.password));
        genClass = StringUtils.replace(genClass, "$PUBLISHURL", stringEscape(shared.publishURL));
        genClass = StringUtils.replace(genClass, "$INQUIRYURL", stringEscape(shared.inquiryURL));
        genClass = StringUtils.replace(genClass, "$PROVIDER", stringEscape(shared.provider));
        genClass = StringUtils.replace(genClass, "$TAXONOMY", stringEscape(shared.taxonomy));
        return genClass;
    }

    /*************************************************************************
     * Create a registry records in the BioMoby format.
     *************************************************************************/
    protected String createBioMobyRecords(RegistryRecord[] records, int starting, int ending) throws SoaplabException {
        StringBuffer buf = new StringBuffer();
        return new String(buf);
    }

    /*************************************************************************
     * Create a URL for WSDL for 'service', possibly using a template found
     * int 'props' (see 'createRR' about it).
     *************************************************************************/
    protected String getWSDL_URL(String service, Hashtable props) {
        String wsdl = (String) props.get(WSDL_URL_TEMPLATE);
        if (wsdl == null) return endpoint + service + "?wsdl";
        if (wsdl.indexOf("$NAME") > -1) wsdl = StringUtils.replace(wsdl, "$NAME", service); else if (wsdl.indexOf("$ENAME") > -1) wsdl = StringUtils.replace(wsdl, "$ENAME", filenameEscape(service));
        return wsdl;
    }

    /*************************************************************************
     *
     *          A container for shared attributes by all registry records.
     *
     *************************************************************************/
    class SharedRecord {

        public String provider = "unknown provider";

        public String taxonomy = "unknown taxonomy name";

        public String userId = "anonymous";

        public String password = "";

        public String inquiryURL = "http://localhost:8080/inquiry";

        public String publishURL = "http://localhost:8080/publish";
    }

    /*************************************************************************
     *
     *          A container for attributes for one registry record.
     *
     *************************************************************************/
    class RegistryRecord {

        public String name = "";

        public String type = "";

        public String version = "";

        public String provider = "";

        public String endpoint = "";

        public String WSDL = "";

        public String function = "";

        public String description = "";
    }
}
