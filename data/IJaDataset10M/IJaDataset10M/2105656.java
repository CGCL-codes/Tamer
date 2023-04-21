package org.xaware.testing.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xaware.api.BizViewRequestOptions;
import org.xaware.api.XABizView;
import org.xaware.server.engine.utils.TimerUtil;
import org.xaware.shared.util.FileUtils;
import org.xaware.shared.util.XAHttpClient;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.Zip;

public class MultiThreadedLoadTest {

    protected static String m_userId = "";

    protected static String m_password = "";

    private static Namespace xaNamespace = XAwareConstants.xaNamespace;

    private static String m_xaPubFilename;

    private static String m_type;

    private static String m_host;

    protected static int m_numLoops = 1;

    private static int m_threadCnt = 1;

    protected Hashtable<String, Vector> m_statsTable;

    private static ExecutorService m_execSvc;

    protected static boolean m_output;

    private static String m_basedir;

    private static String m_filter;

    public static final String XAWARE_ATTR_APPLICATION = "application";

    public static final String PARM_TYPE = "type";

    public static final String PARM_LOOPS = "loops";

    public static final String PARM_THREADS = "threads";

    public static final String PARM_HOST = "host";

    public static final String PARM_USERID = "user";

    public static final String PARM_PASSWORD = "password";

    public static final String PARM_TYPE_SERVER = "server";

    public static final String PARM_TYPE_BATCH = "batch";

    private static final String PARM_OUTPUT = "output";

    private static final String PARM_BASE_DIR = "basedir";

    private static final String PARM_FILTER = "filter";

    /**
     * read XAPublish.xml from the name provided and create a vector of deployed biz file names
     * 
     * @param xaPubName
     */
    protected HashMap<String, String> getComponentsFromXAPublish(String xaPubName) throws Exception {
        File xaPubFile = new File(xaPubName);
        HashMap<String, String> packageFiles = null;
        try {
            if (xaPubFile.exists()) {
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(xaPubFile);
                Element root = doc.getRootElement();
                Element pubAppl = root.getChild(XAWARE_ATTR_APPLICATION, xaNamespace);
                List children = pubAppl.getChildren(XAwareConstants.XAWARE_COMPONENT, xaNamespace);
                packageFiles = new HashMap<String, String>(children.size());
                Iterator itr = children.iterator();
                while (itr.hasNext()) {
                    Element component = (Element) itr.next();
                    String relLoc = component.getAttributeValue(XAwareConstants.XAWARE_LOCATION, xaNamespace);
                    if (relLoc == null || "".equals(relLoc)) {
                        relLoc = component.getAttributeValue(XAwareConstants.XAWARE_LOCATION);
                    }
                    String type = component.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
                    if (type == null || "".equals(type)) {
                        type = component.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE);
                    }
                    String params = component.getAttributeValue(XAwareConstants.BIZDRIVER_PARAMS, xaNamespace);
                    if (type == null || "".equals(type)) {
                        type = component.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE);
                    }
                    String bdType = "xa:bizdoc";
                    if (relLoc != null && bdType.equals(type)) {
                        packageFiles.put(relLoc, params);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception reading " + xaPubName + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return packageFiles;
    }

    /**
     * Use the XAPublish file to get a list of bizdocs that will be run on the server specified by
     * host location. It will loop thru the files the number of times specified by numberLoops.
     * 
     * @param xaPubFile
     * @param hostLoc
     * @param numberLoops
     */
    public void runServerLoad(String xaPubFile, String hostLoc, int numLoops) throws Exception {
        HashMap<String, String> bdFiles = getComponentsFromXAPublish(xaPubFile);
        m_statsTable = new Hashtable<String, Vector>(100);
        for (int x = 0; x < numLoops; x++) {
            Set<String> keys = bdFiles.keySet();
            for (String bdName : keys) {
                String commandStr = "http://" + hostLoc + "/xaware/XAServlet?_BIZVIEW=" + getParameterString(bdFiles, bdName);
                BizDocRunnable rt = new BizDocRunnable();
                rt.setBizDocCallString(commandStr);
                rt.setType(m_type);
                m_execSvc.execute(rt);
            }
        }
        m_execSvc.shutdown();
        while (m_execSvc.isTerminated() == false) {
            Thread.sleep(1000);
        }
        outputStats(m_statsTable, xaPubFile);
    }

    /**
     * Use the XAPublish file to get a list of bizdocs that will be run on the server specified by
     * host location. It will loop thru the files the number of times specified by numberLoops.
     * 
     * @param xaPubFile
     * @param hostLoc
     * @param numberLoops
     */
    public void runBatchLoad(String xaPubFile, int numLoops) throws Exception {
        HashMap<String, String> bdFiles = getComponentsFromXAPublish(xaPubFile);
        XABizView xaBizView = new XABizView(XABizView.MODE_BATCH);
        m_statsTable = new Hashtable<String, Vector>(100);
        for (int x = 0; x < numLoops; x++) {
            Set<String> keys = bdFiles.keySet();
            for (String bdName : keys) {
                try {
                    BizViewRequestOptions requestOptions = getRequestOptions(bdFiles, bdName);
                    BizDocRunnable runThread = new BizDocRunnable();
                    runThread.set_commandArgs(requestOptions);
                    runThread.set_xaBizDoc(xaBizView);
                    runThread.setType(PARM_TYPE_BATCH);
                    m_execSvc.execute(runThread);
                } catch (RuntimeException e) {
                    System.out.println("BizView Failed with exception:" + e.getMessage());
                }
            }
        }
        m_execSvc.shutdown();
        while (m_execSvc.isTerminated() == false) {
            Thread.sleep(1000);
        }
        outputStats(m_statsTable, xaPubFile);
        XABizView.release();
    }

    /**
     * write out stats to csv file
     * @param table
     * @param xaPubFile 
     */
    private void outputStats(Hashtable<String, Vector> bizNameTable, String xaPubFile) {
        File pubFile = new File(xaPubFile);
        String pubName = pubFile.getName();
        String pubDir = xaPubFile.substring(0, xaPubFile.length() - pubName.length());
        File outputFile = new File(pubDir + "Output_" + pubName.substring(0, pubName.length() - 3) + "csv");
        System.out.println("Writing " + outputFile.getAbsolutePath());
        try {
            FileWriter outstream = new FileWriter(outputFile);
            outstream.write("BizView Name, Number of Runs, Average, Fastest, Slowest\n");
            Set<String> keys = bizNameTable.keySet();
            for (String bizNameKey : keys) {
                int totalTime = 0;
                int shortestTime = 1000000;
                int longestTime = 0;
                Vector statsVector = bizNameTable.get(bizNameKey);
                int size = statsVector.size();
                int i = 0;
                for (i = 0; i < size; i++) {
                    String time = (String) statsVector.get(i);
                    int timeInt = Integer.parseInt(time);
                    totalTime += timeInt;
                    if (timeInt < shortestTime) {
                        shortestTime = timeInt;
                    }
                    if (timeInt > longestTime) {
                        longestTime = timeInt;
                    }
                }
                outstream.write(bizNameKey + "," + size + "," + totalTime / size + "," + shortestTime + "," + longestTime + "\n");
            }
            outstream.close();
        } catch (IOException e) {
            System.out.println("Writing output file " + outputFile.getAbsolutePath() + " Failed with exception: " + e.getMessage());
        }
    }

    /**
     * Get params for bizdoc and build the request options
     * 
     * @param bdFiles
     * @param bdName
     * @return
     * @throws IOException 
     * @throws Exception 
     */
    private BizViewRequestOptions getRequestOptions(HashMap<String, String> bdFiles, String bdName) throws Exception {
        BizViewRequestOptions requestOptions = new BizViewRequestOptions();
        requestOptions.setBizViewName(bdName);
        requestOptions.setResourcePath("");
        String params = bdFiles.get(bdName);
        if (params != null) {
            HashMap<String, Object> paramsTable = new HashMap<String, Object>(params.length());
            String[] paramsArray = params.split("&");
            for (String parm : paramsArray) {
                String[] keyValueParm = parm.split("=");
                if (keyValueParm.length == 2) {
                    if (keyValueParm[0].equals("_XADATA")) {
                        SAXBuilder builder = new SAXBuilder();
                        Document doc = builder.build(keyValueParm[1]);
                        requestOptions.setInputXmlDocument(doc);
                    } else {
                        paramsTable.put(keyValueParm[0], keyValueParm[1]);
                    }
                }
            }
            requestOptions.setInputParams(paramsTable);
        }
        return requestOptions;
    }

    /**
     * Get params for bizdoc and build an array. The bizdoc name will be the first array field.
     * 
     * @param bdFiles
     * @param bdName
     * @return
     * @throws IOException 
     * @throws Exception 
     */
    private String getParameterString(HashMap<String, String> bdFiles, String bdName) throws Exception {
        String params = bdFiles.get(bdName);
        if (params == null) {
            params = bdName;
        } else {
            params = bdName + "&" + params;
        }
        return params;
    }

    class ThreadPool extends ThreadPoolExecutor {

        public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
    }

    /**
     * Thread class for executing each bizdoc against the server.
     * 
     */
    class BizDocRunnable implements Runnable {

        String m_bizDocCallString;

        String m_runType;

        XABizView m_xaBizDoc;

        BizViewRequestOptions m_commandArgs;

        BizDocRunnable() {
        }

        public void run() {
            if (PARM_TYPE_SERVER.equals(m_runType)) {
                System.out.println("Executing: " + m_bizDocCallString);
                executeServer(m_bizDocCallString);
            } else if (PARM_TYPE_BATCH.equals(m_runType)) {
                executeBatch();
            } else {
                System.out.println("Invalid type: " + m_runType);
            }
        }

        private void executeBatch() {
            XABizView bizDocExec = get_xaBizDoc();
            TimerUtil timer = new TimerUtil();
            try {
                if (bizDocExec == null) {
                    System.out.println("XABizDoc null internal error");
                    return;
                }
                System.out.println("Begin Execute of " + m_commandArgs.getBizViewName());
                timer.start();
                Document results = (Document) bizDocExec.executeBizView(m_commandArgs, XABizView.RESULT_TYPE_DOCUMENT);
                timer.stop();
                updateTimerStats(m_commandArgs.getBizViewName(), timer);
                System.out.println("Execution time for " + m_commandArgs.getBizViewName() + " is: " + timer.elapsedTimeLong());
                logBizDocResults(m_commandArgs.getBizViewName(), results.getRootElement());
            } catch (XAwareException e) {
                System.out.println("Exeception executing " + m_commandArgs.getBizViewName() + " : " + e.getMessage());
            } catch (Exception e) {
                timer.stop();
                updateTimerStats(m_commandArgs.getBizViewName(), timer);
                System.out.println("Execution time for " + m_commandArgs.getBizViewName() + " is: " + timer.elapsedTimeLong());
                System.out.println("Exeception executing " + m_commandArgs.getBizViewName() + " : " + e.getMessage());
            }
        }

        /**
         * add timer stats to statsTable
         * @param bizViewName
         * @param timer
         */
        @SuppressWarnings("unchecked")
        private void updateTimerStats(String bizViewName, TimerUtil timer) {
            Vector stats = m_statsTable.get(bizViewName);
            if (stats == null) {
                stats = new Vector(m_numLoops);
            }
            stats.add("" + timer.elapsedTimeLong());
            m_statsTable.put(bizViewName, stats);
        }

        /**
         * connect to http server get and print results
         * 
         * @param argumentToConnector
         * @param timer 
         */
        protected void executeServer(String argumentToConnector) {
            XAHttpClient httpConnector = null;
            TimerUtil timer = new TimerUtil();
            try {
                httpConnector = new XAHttpClient(argumentToConnector);
                httpConnector.init(m_userId, m_password);
                System.out.println("Begin Execute of " + argumentToConnector);
                SAXBuilder xsb = new SAXBuilder();
                timer.start();
                InputStream responseStream = httpConnector.executeGet();
                timer.stop();
                System.out.println("Execution time for " + argumentToConnector + " is: " + timer.elapsedTimeLong());
                Element res = xsb.build(responseStream).getRootElement();
                updateTimerStats(argumentToConnector, timer);
                logBizDocResults(argumentToConnector, res);
            } catch (IllegalArgumentException ilgex) {
                System.out.println("Error posting message to server." + ilgex.toString());
            } catch (Exception exception) {
                timer.stop();
                System.out.println("Execution time for " + argumentToConnector + " is: " + timer.elapsedTimeLong());
                updateTimerStats(argumentToConnector, timer);
                System.out.println("Error posting message to server." + exception.toString());
            } finally {
                if (httpConnector != null) {
                    httpConnector.closeGet();
                }
            }
        }

        /**
         * Log the BizDoc results
         * 
         * @param rElem
         */
        private void logBizDocResults(String bizDocName, Element rElem) {
            if (rElem != null && m_output) {
                XMLOutputter xmlout = new XMLOutputter();
                xmlout.setFormat(Format.getPrettyFormat());
                String xmlData = xmlout.outputString(rElem);
                System.out.println("BizDoc " + bizDocName + " results:\n" + xmlData);
            } else {
                System.out.println("No BizDoc " + bizDocName + " results");
            }
        }

        public String getBizDocCallString() {
            return m_bizDocCallString;
        }

        public void setBizDocCallString(String bizDocCallString) {
            m_bizDocCallString = bizDocCallString;
        }

        /**
         * set the type to either server or
         * 
         * @param m_type
         */
        public void setType(String runType) {
            m_runType = runType;
        }

        public XABizView get_xaBizDoc() {
            return m_xaBizDoc;
        }

        public void set_xaBizDoc(XABizView xaBizView) {
            m_xaBizDoc = xaBizView;
        }

        public BizViewRequestOptions get_commandArgs() {
            return m_commandArgs;
        }

        public void set_commandArgs(BizViewRequestOptions options) {
            m_commandArgs = options;
        }
    }

    /**
     * Utility for building a xar file. All subdirectories of the baseDir excluding
     * hidden files and directories ie ".something" names. Then copy the xar file to the deploy dir.
     * 
     * @param baseDir
     * @param xarName
     * @param dirMatch
     * @return String
     * @throws Exception
     */
    public static String buildXarFromDirectory(final String baseDir, final String xarName, final String dirMatch) throws Exception {
        return Zip.buildXarFromDirectory(baseDir, xarName, dirMatch);
    }

    /**
     * Static helper method for external scripts it will copy the xarfile to deploy dir
     * 
     * @param deployDir
     * @param zipFilename
     * @throws IOException
     */
    public static void deployXar(final String deployDir, final String zipFilename) throws IOException {
        final File newZipFile = new File(zipFilename);
        final File deployDirFile = new File(deployDir + "/" + newZipFile.getName());
        FileUtils.copyFile(newZipFile, deployDirFile);
    }

    /**
     * loop thru all args setting their value. If there are additional or misspelled args ignore
     * them.
     * 
     * @param args
     */
    private static void parseArgs(String[] args) {
        m_xaPubFilename = args[0];
        m_type = "server";
        m_host = "localhost:8090";
        m_output = true;
        String parmName = null;
        String parmValue = null;
        for (String param : args) {
            String[] nameValueArray = param.split("=");
            if (nameValueArray.length == 2) {
                parmName = nameValueArray[0];
                parmValue = nameValueArray[1];
                setParmValue(parmName, parmValue);
            } else {
                continue;
            }
        }
    }

    /**
     * set the named parm with the value
     * 
     * @param name
     * @param value
     */
    private static void setParmValue(String name, String value) {
        if (PARM_TYPE.equals(name)) {
            m_type = value;
        }
        if (PARM_LOOPS.equals(name)) {
            m_numLoops = Integer.parseInt(value);
        }
        if (PARM_THREADS.equals(name)) {
            m_threadCnt = Integer.parseInt(value);
        }
        if (PARM_HOST.equals(name)) {
            m_host = value;
        }
        if (PARM_USERID.equals(name)) {
            m_userId = value;
        }
        if (PARM_PASSWORD.equals(name)) {
            m_password = value;
        }
        if (PARM_OUTPUT.equals(name)) {
            if ("no".equals(value)) {
                m_output = false;
            }
        }
        if (PARM_BASE_DIR.equals(name)) {
            m_basedir = value;
        }
        if (PARM_FILTER.equals(name)) {
            m_filter = value;
        }
    }

    /**
     * create the number of threads
     * 
     * @param numThreads
     * @return
     */
    private void createThreadPool(int numThreads) {
        m_execSvc = Executors.newFixedThreadPool(numThreads);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            showUsage();
            return;
        }
        MultiThreadedLoadTest mtlt = new MultiThreadedLoadTest();
        parseArgs(args);
        mtlt.createThreadPool(m_threadCnt);
        try {
            if ("batch".equals(m_type)) {
                mtlt.runBatchLoad(m_xaPubFilename, m_numLoops);
            } else if ("server".equals(m_type)) {
                mtlt.runServerLoad(m_xaPubFilename, m_host, m_numLoops);
            } else if ("xar".equals(m_type)) {
                MultiThreadedLoadTest.buildXarFromDirectory(m_basedir, m_xaPubFilename, m_filter);
                System.out.println(m_xaPubFilename + " created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MultiThreadLoadTest Utility complete");
    }

    private static void showUsage() {
        System.out.println("Usage java MultiThreadedLoad c:\\myDir\\XAPublish.xml");
        System.out.println("    <optional " + PARM_TYPE + "= either server, batch or xar default is server>");
        System.out.println("    <optional " + PARM_LOOPS + "= number of times to loop thru files default 1>");
        System.out.println("    <optional  " + PARM_THREADS + "= number of threads to use default 1>");
        System.out.println("    <optional  " + PARM_HOST + "= hostname:port default is localhost:8090>");
        System.out.println("    <optional  " + PARM_USERID + "= userid default empty string>");
        System.out.println("    <optional  " + PARM_PASSWORD + "= password default empty string>");
        System.out.println("    <optional  " + PARM_OUTPUT + "= yes or no should bizview results be printed default yes>");
        System.out.println("    <optional  " + PARM_BASE_DIR + "= base directory to be xar'ed no default>");
        System.out.println("    <optional  " + PARM_FILTER + "= include only directories that start with filter, no default >");
    }
}
