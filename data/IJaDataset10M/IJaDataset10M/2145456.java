package qat.parser.qaxmlparser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import javax.swing.JLabel;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import qat.common.Common;
import qat.common.ProtocolConstants;
import qat.common.Utils;
import qat.parser.AgentInstance;
import qat.parser.HtmlPrintStream;
import qat.parser.ParserInterface;

/**
 * This file loads a single QAT file, and will attempt to resolve all keywords in this qat file
 * file by first including any .INC statements, and their parent statements etc, until all neccesary files
 * have been included.
 *
 * @author webhiker
 * @version 2.3, 17 June 1999
 *
 */
public class QAXMLParser extends Object implements ParserInterface {

    private static final String TRUE = "true";

    private static final String FALSE = "false";

    private static final String INTERNAL_TRACE_LIST = "qaxml.internal.traces";

    private static javax.xml.parsers.DocumentBuilder documentBuilder;

    private static final int QAXML_NODE = 0;

    private static final int TEST_NODE = 1;

    private static final int PRINTENV_NODE = 2;

    private static final int SLEEP_NODE = 3;

    private static final int PROPERTY_NODE = 4;

    private static final int REQUESTAGENT_NODE = 5;

    private static final int RELEASEAGENT_NODE = 6;

    private static final int CHECKAGENT_NODE = 7;

    private static final int LOOP_NODE = 8;

    private static final int ECHO_NODE = 9;

    private static final int SENDZIP_NODE = 10;

    private static final int CLEANZIP_NODE = 11;

    private static final int START_NODE = 12;

    private static final int STATUS_NODE = 13;

    private static final int TRACE_NODE = 14;

    private static final int CLEAN_NODE = 15;

    private static final int INCLUDE_NODE = 16;

    private static final int REPORT_NODE = 17;

    private static final int RANDOM_NODE = 18;

    private static final int COMMENT_NODE = 19;

    private static final int TEXT_NODE = 20;

    private static final int IGNORE_NODE = 21;

    private static final String keywords[] = { "qaxml", "test", "printenv", "sleep", "property", "requestagent", "releaseagent", "checkagent", "loop", "echo", "sendzip", "cleanzip", "start", "status", "trace", "clean", "include", "report", "random", "#comment", "#text", "#ignore" };

    private JLabel statusLabel;

    private boolean evaluating;

    private HtmlPrintStream printStream;

    private Properties properties;

    private String name, author, description, buginfo;

    private boolean interrupted = false;

    private String testPath;

    private String filePath;

    private HashMap<String, AgentInstance> agentMap;

    private int statusCode = ProtocolConstants.PASSED;

    private String projectResultsDir;

    private List<String> qaxmlIncludeList, propertiesIncludeList;

    private static Random randomVar = new Random();

    private ArrayList<Node> ignoreNodes;

    public QAXMLParser() {
        agentMap = new HashMap<String, AgentInstance>();
        ignoreNodes = new ArrayList<Node>();
        qaxmlIncludeList = new ArrayList<String>();
        propertiesIncludeList = new ArrayList<String>();
        setStatus(ProtocolConstants.PASSED);
    }

    /**
	 * This method is used to clone values of an existing parser, for example
	 * when processing an include file.
	 */
    public QAXMLParser(QAXMLParser parser) {
        this();
        setTestPath(parser.getTestPath());
        prepare(parser.getProjectResultsDir());
        setStatusLabel(parser.getStatusLabel());
        setProperties(parser.getProperties());
        setEvaluationMode(parser.inEvaluationMode());
        setPrintStream(parser.getPrintStream(), parser.getPrintStream() instanceof HtmlPrintStream);
    }

    /**
	 * This method sets the path to root of the current project.
	 */
    public void setProjectRoot(String projectRoot) {
    }

    /**
	 * This method lists all the keywords supported by this syntax, for use in
	 * the Notepad syntax highlighting. It must be declared static.
	 */
    public String[] getSyntaxKeyWords() {
        return keywords;
    }

    /**
	 * This method sets the path to file containing the &lt;test&gt; syntax
	 * to be parsed, it does not change when parsing include files.
	 */
    public void setTestPath(String testPath) {
        this.testPath = testPath;
        setFilePath(testPath);
    }

    /**
	 * This method gets the path to file containing the &lt;test&gt; syntax
	 * to be parsed.
	 */
    private String getTestPath() {
        return this.testPath;
    }

    /**
	 * This method sets the path for the file currently being parsed.  This
	 * path differs from testPath in that this is set to the value of an
	 * include file while parsing the include file.  Therefore, filePath
	 * changes for each include file, while testPath does not.
	 */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
	 * This method sets the PrintStream to use for reporting errors
	 * and other types of output from the script.
	 */
    public void setPrintStream(PrintStream printStream, boolean useHtml) {
        if (printStream instanceof HtmlPrintStream) {
            this.printStream = (HtmlPrintStream) printStream;
        } else {
            this.printStream = new HtmlPrintStream(printStream, useHtml);
        }
    }

    /**
	 * This method sets the PrintStream to use for reporting errors
	 * and other types of output from the script.
	 */
    public PrintStream getPrintStream() {
        return printStream;
    }

    /**
	 * This method sets any default properties which will be required
	 * for parsing this file.
	 */
    public void setProperties(java.util.Properties p) {
        this.properties = p;
        properties.setProperty(INTERNAL_TRACE_LIST, properties.getProperty(INTERNAL_TRACE_LIST, ""));
    }

    /**
	 * This method returns all the properties obtained by parsing this test file.
	 */
    public java.util.Properties getProperties() {
        return properties;
    }

    /**
	 * If set to true, the parser does not actually make contact with the agents
	 * but merely simulates the agent responses to allow standalone parsing.
	 */
    public final void setEvaluationMode(boolean mode) {
        evaluating = mode;
    }

    public final boolean inEvaluationMode() {
        return evaluating;
    }

    /**
	 * This method parses the specified file.
	 * If not in evaluation mode, it should return the status of the test run :
	 * ProtocolConstants.PASSED
	 * ProtocolConstants.FAILED
	 * ProtocolConstants.NOTRUN
	 * ProtocolConstants.UNRESOLVED
	 */
    public int parseFile() throws Exception {
        org.w3c.dom.Document document = documentBuilder.parse(new BufferedInputStream(new FileInputStream(filePath)));
        Element root = document.getDocumentElement();
        properties.setProperty("qaxml.basedir", new File(filePath).getParentFile().getCanonicalPath());
        processNode(root);
        if (agentMap.size() > 0) {
            printError(REQUESTAGENT_NODE, "one or more agents not properly cleaned");
        }
        return getStatus();
    }

    private void processNode(org.w3c.dom.Node node) {
        int nodeType = getNodeType(node);
        switch(nodeType) {
            case IGNORE_NODE:
                break;
            case COMMENT_NODE:
                break;
            case TEXT_NODE:
                break;
            case TEST_NODE:
                processTestNode(node);
                break;
            case QAXML_NODE:
                processTestNode(node);
                break;
            case PROPERTY_NODE:
                processPropertyNode(node);
                break;
            case PRINTENV_NODE:
                processPrintEnvNode(node);
                break;
            case SLEEP_NODE:
                processSleepNode(node);
                break;
            case REQUESTAGENT_NODE:
                processRequestAgentNode(node);
                break;
            case RELEASEAGENT_NODE:
                processReleaseAgentNode(node);
                break;
            case CHECKAGENT_NODE:
                processCheckAgentNode(node);
                break;
            case LOOP_NODE:
                processLoopNode(node);
                break;
            case ECHO_NODE:
                processEchoNode(node);
                break;
            case SENDZIP_NODE:
                processSendZipNode(node);
                break;
            case CLEANZIP_NODE:
                processCleanZipNode(node);
                break;
            case START_NODE:
                processStartNode(node);
                break;
            case STATUS_NODE:
                processStatusNode(node);
                break;
            case TRACE_NODE:
                processTraceNode(node);
                break;
            case CLEAN_NODE:
                processCleanNode(node);
                break;
            case INCLUDE_NODE:
                processIncludeNode(node);
                break;
            case REPORT_NODE:
                processReportNode(node);
                break;
            case RANDOM_NODE:
                processRandomNode(node);
                break;
            default:
                System.out.println("Unknown node type " + nodeType + " for node " + node.getNodeName());
                setStatus(ProtocolConstants.UNRESOLVED);
                printError(-1, "Unknown node type (" + nodeType + ") " + node.getNodeName());
        }
        org.w3c.dom.Node child = node.getFirstChild();
        while ((child != null) && (!isInterrupted())) {
            processNode(child);
            child = child.getNextSibling();
        }
        if (isInterrupted()) {
            setStatus(ProtocolConstants.UNRESOLVED);
        }
    }

    private int getNodeType(org.w3c.dom.Node node) {
        if (ignoreNodes.remove(node)) return IGNORE_NODE;
        for (int i = 0; i < keywords.length; i++) if (keywords[i].equals(node.getNodeName())) return i;
        return -1;
    }

    /**
	 * This <code>processTestNode</code> is the start point for a single unit test.<br>
     e.g. &lt;test<br>
     name="test name"<br>
     author="test author"<br>
     description="this is a description of the test"<br>
     buginfo="any important bug information associated with this test"/&gt;<br>
     @param testNode an <code>org.w3c.dom.Node</code> value
	 */
    public void processTestNode(org.w3c.dom.Node testNode) {
        setStatusText(testNode.getNodeName());
        try {
            if (testNode.hasAttributes()) {
                org.w3c.dom.NamedNodeMap attributes = testNode.getAttributes();
                name = resolveVariable(getAttribute(attributes, "name")).toString();
                author = resolveVariable(getAttribute(attributes, "author")).toString();
                description = resolveVariable(getAttribute(attributes, "description")).toString();
                buginfo = resolveVariable(getAttribute(attributes, "buginfo")).toString();
            }
        } catch (Exception e) {
            printError(TEST_NODE, "invalid testnode found (" + testNode.toString() + ")");
        }
    }

    /**
	 * This tag will print out the current environment settings.<br>
     e.g. &lt;printenv/&gt;<br>
	 */
    @SuppressWarnings("unchecked")
    public void processPrintEnvNode(org.w3c.dom.Node printEnvNode) {
        setStatusText(printEnvNode.getNodeName());
        try {
            NamedNodeMap attributes = printEnvNode.getAttributes();
            if (onlyIf(attributes)) {
                printStream.println("Defined variables:");
                String key;
                for (Enumeration<String> e = (Enumeration<String>) properties.propertyNames(); e.hasMoreElements(); ) {
                    key = (String) e.nextElement();
                    printStream.println(key + "=" + properties.getProperty(key));
                }
            } else {
                printStream.println("printenv ignored (onlyif false)");
            }
        } catch (Exception e) {
            printError(PRINTENV_NODE, "invalid printenv reported (" + printEnvNode.toString() + ")");
        }
    }

    /**
	 * This tag will cause execution to sleep for the specified period of time.<br>
     e.g. &lt;sleep<br>
     onlyif="variable"<br>
     seconds="10"<br>
     minutes="0"<br>
     hours="0"/&gt;<br>
     At least one of the attributes must be specified, any which are missing are
     assumed to be zero.
	 */
    public void processSleepNode(org.w3c.dom.Node sleepNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = sleepNode.getAttributes();
            String timeS = resolveVariable(getAttribute(attributes, "seconds", "0")).toString();
            String timeM = resolveVariable(getAttribute(attributes, "minutes", "0")).toString();
            String timeH = resolveVariable(getAttribute(attributes, "hours", "0")).toString();
            if (onlyIf(attributes)) {
                int seconds = Integer.parseInt(timeS);
                seconds += 60 * Integer.parseInt(timeM);
                seconds += 3600 * Integer.parseInt(timeH);
                setStatusText(sleepNode.getNodeName() + " " + Integer.toString(seconds));
                if (!inEvaluationMode()) {
                    printStream.println("sleeping for " + seconds + " seconds");
                    while ((seconds > 0) && (!isInterrupted())) {
                        Utils.safeSleep(1);
                        seconds--;
                        setStatusText(sleepNode.getNodeName() + " " + Integer.toString(seconds));
                    }
                }
            } else {
                printStream.println("sleep cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(SLEEP_NODE, "Invalid sleep node (" + sleepNode.toString() + ")");
        }
    }

    /**
	 * This tag causes a propery to be set.<br>
     Two forms may be used :<br>
     e.g. 1) &lt;property name="mypropertyname" value="mypropertyvalue"/&gt;<br>
     e.g. 2) &lt;property name="mypropertyname" refid="${someotherproperty}"/&gt;<br>
     which will first resolve the variable references, and then set the property.<br><br>
     Variable references can also be used to form the 'name' value, for example:<br>
     <code><pre>
     &lt;loop name="i" start="1" end="${qat.agent.count}" inc="1"&gt;
     	&lt;property name="mypropertyname${i}" value="mypropertyvalue"/&gt;
     	&lt;property name="myotherproperty${i}" refid="${someotherproperty}"/&gt;
     &lt;/loop&gt;
     </pre></code>
     The above code would create a two properties, one named 'mypropertyname1' and
     the other named 'myotherproperty2'.
	 */
    public void processPropertyNode(org.w3c.dom.Node propertyNode) {
        setStatusText(propertyNode.getNodeName());
        try {
            org.w3c.dom.NamedNodeMap attributes = propertyNode.getAttributes();
            StringBuffer name = getAttribute(attributes, "name");
            name = resolveVariable(getAttribute(attributes, "name", ""));
            if (onlyIf(attributes)) {
                StringBuffer value = getAttribute(attributes, "value", "");
                if (value.length() == 0) value = resolveVariable(getAttribute(attributes, "refid", ""));
                properties.setProperty(name.toString(), value.toString());
            } else if (verbose(attributes)) {
                printStream.println("property " + name + " ignored (onlyif false)");
            }
        } catch (Exception e) {
            printError(PROPERTY_NODE, "invalid property node (" + e.getMessage() + ")");
        }
    }

    /**
	 * This tag is the start point for a single unit test.<br>
     e.g. &lt;requestagent<br>
     onlyif="variable"      (optional)    <br>
     agentid="agentid"      (the id of the agent you want to use)<br>
     agentworkdir="workdir"/&gt; (optional)<br>
	 */
    public void processRequestAgentNode(org.w3c.dom.Node requestAgentNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = requestAgentNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer agentid = resolveVariable(getAttribute(attributes, "agentid"));
                StringBuffer agenthost = resolveVariable(new StringBuffer("${agent").append(agentid).append(".name}"));
                StringBuffer agentport = resolveVariable(new StringBuffer("${agent").append(agentid).append(".port}"));
                StringBuffer agentworkdir = resolveVariable(getAttribute(attributes, "agentworkdir", "${" + agentid.toString() + ".java.io.tmpdir}${" + agentid.toString() + ".file.separator}agent" + agentid.toString()));
                printStream.println("requesting agent " + agentid + " at " + agenthost + ":" + agentport + " with workdir " + agentworkdir);
                setStatusText(requestAgentNode.getNodeName() + " " + agentid);
                if (agentMap.get(agentid) != null) {
                    throw new Exception("agent identifier already exists :" + agentid.toString());
                }
                AgentInstance agentInstance = new AgentInstance(agenthost.toString(), new Integer(agentport.toString()).intValue(), agentworkdir.toString(), inEvaluationMode());
                agentMap.put(agentid.toString(), agentInstance);
            } else {
                printStream.println("request agent cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(REQUESTAGENT_NODE, "invalid requestagent node (" + e.toString() + ")");
        }
    }

    /**
	 * This tag is the start point for a single unit test.<br>
     e.g. &lt;releaseagent<br>
     onlyif="variable"<br>
     agentid="agentid"/&gt;<br>
	 */
    public void processReleaseAgentNode(org.w3c.dom.Node releaseAgentNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = releaseAgentNode.getAttributes();
            StringBuffer agentid = resolveVariable(getAttribute(attributes, "agentid"));
            if (onlyIf(attributes)) {
                printStream.println("releasing agent " + agentid);
                setStatusText(releaseAgentNode.getNodeName() + " " + agentid);
                AgentInstance agentInstance = (AgentInstance) agentMap.get(agentid.toString());
                if (agentInstance == null) throw new Exception("unknown agentid " + agentid);
                try {
                    agentInstance.DELAGENT();
                } catch (Exception ex) {
                    printError(RELEASEAGENT_NODE, "error cleaning up agent " + agentid + "(" + ex.toString() + ") :");
                } finally {
                    agentMap.remove(agentid.toString());
                }
            } else {
                printStream.println("release agent cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(RELEASEAGENT_NODE, "invalid releaseagent node (" + e.getMessage() + ")");
        }
    }

    /**
	 * This tag checks if an agent is running.<br>
     e.g. &lt;checkagent<br>
     result="result"<br>
     agentid="1"/&gt;<br>
     If the agent is found to be running, result will be set to "true", and all the agent properties
     will be then be available in the QAXML script. If there was a problem contacting the
     agent, result will be set to "false".
     When running in evaluation mode (e.g. when reparsing the file) the agent will not actually be contacted,
     so the method will always return true. The environment properties of the script will be used
     to re-create a set of properties as would be returned by the agent.<br>
     After a succesfull call to this method, all java properties of the agent will be set locally,
     with a prefix of the agent id. e.g. 1.file.separator="/", 1.user.home="/home"
	 */
    @SuppressWarnings("unchecked")
    public void processCheckAgentNode(org.w3c.dom.Node checkAgentNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = checkAgentNode.getAttributes();
            StringBuffer result = resolveVariable(getAttribute(attributes, "result"));
            StringBuffer agentid = resolveVariable(getAttribute(attributes, "agentid"));
            java.net.Socket socket = null;
            java.io.DataInputStream inStream = null;
            java.io.DataOutputStream outStream = null;
            String resultCode = FALSE;
            if (onlyIf(attributes)) {
                if (!inEvaluationMode()) {
                    try {
                        StringBuffer agenthost = resolveVariable(new StringBuffer("${agent").append(agentid).append(".name}"));
                        StringBuffer agentport = resolveVariable(new StringBuffer("${agent").append(agentid).append(".port}"));
                        printStream.println("checking agent at " + agenthost + ":" + agentport);
                        setStatusText(checkAgentNode.getNodeName());
                        setStatusText(checkAgentNode.getNodeName() + " " + agentid);
                        socket = new java.net.Socket(agenthost.toString(), (new Integer(agentport.toString())).intValue());
                        socket.setSoTimeout(3000);
                        inStream = new DataInputStream(socket.getInputStream());
                        outStream = new DataOutputStream(socket.getOutputStream());
                        outStream.writeInt(ProtocolConstants.CHECKAGENT);
                        if (inStream.readInt() != ProtocolConstants.RESPONSE_PROCESSING) throw new Exception("Error response from agent");
                        int propertyCount = inStream.readInt();
                        String key, value;
                        for (int i = 0; i < propertyCount; i++) {
                            key = agentid.toString() + "." + inStream.readUTF();
                            value = inStream.readUTF();
                            properties.setProperty(key, value);
                        }
                        if (inStream.readInt() != ProtocolConstants.RESPONSE_FINISHED_OK) {
                            throw new Exception("Error response from agent");
                        }
                        resultCode = TRUE;
                    } catch (ConnectException e) {
                        resultCode = FALSE;
                    } finally {
                        if (inStream != null) inStream.close();
                        if (outStream != null) outStream.close();
                        if (socket != null) socket.close();
                    }
                } else {
                    try {
                        resolveVariable(new StringBuffer("${agent").append(agentid).append(".name}"));
                        resolveVariable(new StringBuffer("${agent").append(agentid).append(".port}"));
                        resultCode = TRUE;
                        Properties propertiesT = System.getProperties();
                        String key, value;
                        for (Enumeration e = propertiesT.propertyNames(); e.hasMoreElements(); ) {
                            key = e.nextElement().toString();
                            value = propertiesT.getProperty(key);
                            key = agentid.toString() + "." + key;
                            properties.setProperty(key, value);
                        }
                    } catch (Throwable t) {
                        resultCode = FALSE;
                    }
                }
                properties.put(result.toString(), resultCode);
                printStream.println("checkagent reported " + resultCode);
            } else {
                printStream.println("checkagent ignored (onlyif false)");
            }
        } catch (Exception e) {
            printError(CHECKAGENT_NODE, "invalid checkagent node (" + e.toString() + ")");
        }
    }

    /**
	 * This tag will loop all nested tasks until the while condition evaluates to true.<br>
     e.g. &lt;loop<br>
     name="i"<br>
     start="0"<br>
     end="5"<br>
     inc="1"<br>
     echo message="The current value is ${i}"/&gt;<br>
     loop/&gt;<br>
     <br>
     Which is equivalent to "for (int i = 0; i = 5; i+=1) ...."
     At least one of the attributes must be specified, any which are missing are
     assumed to be zero.
	 */
    public void processLoopNode(org.w3c.dom.Node loopNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = loopNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer name = getAttribute(attributes, "name");
                StringBuffer start = resolveVariable(getAttribute(attributes, "start", "0"));
                StringBuffer end = resolveVariable(getAttribute(attributes, "end"));
                StringBuffer inc = resolveVariable(getAttribute(attributes, "inc", "1"));
                printStream.println(loopNode.getNodeName() + " " + name + " from " + start + " by " + inc + " until " + end);
                int endint = Integer.parseInt(end.toString());
                int incint = Integer.parseInt(inc.toString());
                for (int loop_var = Integer.parseInt(start.toString()); loop_var <= endint; loop_var += incint) {
                    properties.put(name.toString(), Integer.toString(loop_var));
                    org.w3c.dom.NodeList children = loopNode.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++) {
                        processNode(children.item(i));
                    }
                }
                properties.remove(name.toString());
                org.w3c.dom.NodeList children = loopNode.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    ignoreNodes.add(children.item(i));
                }
            } else {
                printStream.println("loop ignored (onlyif false)");
            }
        } catch (Exception e) {
            printError(LOOP_NODE, "invalid loop node (" + loopNode.toString() + ")");
        }
    }

    /**
	 * This tag allows you to echo traces to the harness.<br>
     e.g. &lt;echo message="A java property is ${java.home}"/&gt;
	 */
    public void processEchoNode(org.w3c.dom.Node echoNode) {
        setStatusText(echoNode.getNodeName());
        try {
            org.w3c.dom.NamedNodeMap attributes = echoNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer message = resolveVariable(getAttribute(attributes, "message"));
                printStream.println(HtmlPrintStream.GREEN, message.toString());
            }
        } catch (Exception e) {
            printError(ECHO_NODE, "invalid echo node found (" + e.toString() + ")");
        }
    }

    /**
	 * This tag is the start point for a single unit test.<br>
     e.g. &lt;sendzip<br>
     onlyif="variable" (optional, execute only if variable="true")<br>
     agentid="agentid" (the id of the agent you want to send the zip to)<br>
     zipfile="zipfile"/&gt;<br>
	 */
    public void processSendZipNode(org.w3c.dom.Node sendzipNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = sendzipNode.getAttributes();
            StringBuffer agentid = resolveVariable(getAttribute(attributes, "agentid"));
            StringBuffer zipfile = resolveVariable(getAttribute(attributes, "zipfile"));
            if (onlyIf(attributes)) {
                setStatusText(sendzipNode.getNodeName() + " " + agentid);
                printStream.println(sendzipNode.getNodeName() + " " + zipfile + " to agent " + agentid);
                AgentInstance agentInstance = (AgentInstance) agentMap.get(agentid.toString());
                if (agentInstance == null) throw new Exception("unknown agentid " + agentid);
                try {
                    properties.setProperty(agentid.toString() + zipfile.toString(), agentInstance.ZIPSEND(zipfile.toString(), agentid.toString() + zipfile.toString()));
                } catch (Exception ex) {
                    printError(SENDZIP_NODE, "problem sending zip file to agent (" + ex.toString() + ")");
                }
            } else {
                printStream.println("sendzip cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(SENDZIP_NODE, "invalid sendzip node (" + e.toString() + ")");
        }
    }

    /**
	 * This tag is the start point for a single unit test.<br>
     e.g. &lt;cleanzip<br>
     onlyif="variable" (optional, execute only of variable="true")<br>
     agentid="agentid" (the id of the agent you want to send the zip to)<br>
     zipfile="zipfile"/&gt;<br>
	 */
    public void processCleanZipNode(org.w3c.dom.Node cleanzipNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = cleanzipNode.getAttributes();
            StringBuffer agentid = resolveVariable(getAttribute(attributes, "agentid"));
            StringBuffer zipfile = resolveVariable(getAttribute(attributes, "zipfile"));
            if (onlyIf(attributes)) {
                setStatusText(cleanzipNode.getNodeName() + " " + agentid);
                printStream.println(cleanzipNode.getNodeName() + " from agent " + agentid);
                AgentInstance agentInstance = (AgentInstance) agentMap.get(agentid.toString());
                if (agentInstance == null) throw new Exception("unknown agentid " + agentid);
                agentInstance.ZIPCLEAN(agentid.toString() + zipfile.toString());
                properties.remove(agentid.toString() + zipfile.toString());
            } else {
                printStream.println("cleanzip cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(CLEANZIP_NODE, "invalid cleanzip node (" + e.toString() + ")");
        }
    }

    /**
	 * This tag is the start point for a single unit test.<br>
     e.g. &lt;start<br>
     processid="variable"        (the id of the process for future reference)<br>
     agentid="agentid"           (the id of the agent you want to start the process on)<br>
     command="command string"    (the command you want to execute)<br>
     arguments="argument string" (the arguments to the command)<br>
     timeout="timeout"           (the timeout for this command)<br>
     onlyif="variable"/&gt;     (optional - execute only if true)<br>
	 */
    public void processStartNode(org.w3c.dom.Node startNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = startNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer processid = resolveVariable(getAttribute(attributes, "processid"));
                StringBuffer agentid = resolveVariable(getAttribute(attributes, "agentid"));
                StringBuffer command = resolveVariable(getAttribute(attributes, "command"));
                StringBuffer arguments = resolveVariable(getAttribute(attributes, "arguments", ""));
                StringBuffer timeout = resolveVariable(getAttribute(attributes, "timeout", "0"));
                setStatusText(startNode.getNodeName() + " " + command.toString() + " " + arguments.toString() + " on agent " + agentid.toString());
                printStream.println(startNode.getNodeName() + " " + command.toString() + " " + arguments.toString() + " on agent " + agentid.toString());
                if (properties.getProperty(processid.toString()) != null) {
                    throw new Exception("processid " + processid + " already exists");
                }
                StringTokenizer tokens = new StringTokenizer(arguments.toString(), " ");
                String[] cmdArray = new String[1 + tokens.countTokens()];
                cmdArray[0] = command.toString();
                int i = 1;
                while (tokens.hasMoreTokens()) {
                    cmdArray[i++] = tokens.nextToken();
                }
                AgentInstance agentInstance = (AgentInstance) agentMap.get(agentid.toString());
                if (agentInstance == null) throw new Exception("unknown agentid " + agentid);
                properties.setProperty(processid.toString(), agentInstance.CMDSTART(cmdArray, timeout.toString()));
            } else {
                printStream.println("start cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(START_NODE, "invalid start node (" + e.toString() + ")");
        }
    }

    /**
	 * This task will retrieve the status of an executed process.<br>
     e.g. &lt;status<br>
     processid="variable"  (the id of the process for future reference)<br>
     status="result"       (the status of processid will be placed in this variable)<br>
     onlyif="result"/&gt;  (optional - execute only if true)<br>
	 */
    public void processStatusNode(org.w3c.dom.Node statusNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = statusNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer processidname = getAttribute(attributes, "processid");
                StringBuffer processid = resolveVariable(processidname);
                processidname = QAXMLExpression.removeVariableBraces(processidname);
                processidname = resolveVariable(processidname);
                StringBuffer result = resolveVariable(getAttribute(attributes, "status"));
                setStatusText(statusNode.getNodeName() + " " + processidname.toString() + " " + result.toString());
                printStream.println(statusNode.getNodeName() + " " + processidname.toString() + " " + result.toString());
                AgentInstance agentInstance = getAgentRunningProcess(processid);
                if (agentInstance == null) throw new Exception("unknown processid " + processidname);
                String statusCode = agentInstance.CMDSTATUS(processid.toString());
                if (Integer.parseInt(statusCode) == 0) properties.setProperty(result.toString(), "passed"); else properties.setProperty(result.toString(), "failed");
                int value = Integer.parseInt(statusCode);
                if (value == qat.agent.ExecProcess.TIMEDOUT_STATE) {
                    printDebug("command " + processidname + " timed out!");
                } else {
                    if (value < 0) {
                        printDebug("command " + processidname.toString() + " failed!");
                    } else {
                        printDebug("command " + processidname.toString() + " exited normally");
                    }
                }
            } else {
                printStream.println("start cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(STATUS_NODE, "invalid status node (" + e.toString() + ")");
        }
    }

    /**
	 * This task will retrieve the traces of an executed process.<br>
     e.g. &lt;trace<br>
     processid="variable"  (the processid to retrieve the trace for)<br>
     onlyif="result"/&gt;  (optional - execute only if true)<br>
	 */
    public void processTraceNode(org.w3c.dom.Node traceNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = traceNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer processidname = getAttribute(attributes, "processid");
                StringBuffer processid = resolveVariable(processidname);
                processidname = QAXMLExpression.removeVariableBraces(processidname);
                processidname = resolveVariable(processidname);
                setStatusText(traceNode.getNodeName() + " " + processidname.toString());
                printStream.println(traceNode.getNodeName() + " " + processidname.toString());
                AgentInstance agentInstance = getAgentRunningProcess(processid);
                if (agentInstance == null) throw new Exception("unknown processid " + processid);
                agentInstance.CMDGETTRACE(processid.toString(), projectResultsDir + File.separator + Common.getUniqueTestIdentifier(testPath) + "_" + processidname.toString());
                addToPropertiesList(INTERNAL_TRACE_LIST, processidname.toString());
            } else {
                printStream.println("trace cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(TRACE_NODE, "invalid trace node (" + e.toString() + ")");
        }
    }

    /**
	 * This task will retrieve the traces of an executed process.<br>
     e.g. &lt;clean<br>
     processid="variable"  // the processid to retrieve the trace for<br>
     onlyif="result"/&gt;  // optional - execute only if true<br>
	 */
    public void processCleanNode(org.w3c.dom.Node cleanNode) {
        try {
            org.w3c.dom.NamedNodeMap attributes = cleanNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer processidname = getAttribute(attributes, "processid");
                StringBuffer processid = resolveVariable(processidname);
                processidname = QAXMLExpression.removeVariableBraces(processidname);
                setStatusText(cleanNode.getNodeName() + " " + resolveVariable(processidname).toString());
                printStream.println(cleanNode.getNodeName() + " " + resolveVariable(processidname).toString());
                AgentInstance agentInstance = getAgentRunningProcess(processid);
                if (agentInstance == null) throw new Exception("Unknown processID :" + processid.toString());
                agentInstance.CMDCLEAN(processid.toString());
                properties.remove(processidname.toString());
            } else {
                printStream.println("trace cancelled (onlyif false)");
            }
        } catch (Exception e) {
            printError(CLEAN_NODE, "invalid clean node (" + e.toString() + ")");
        }
    }

    /**
	 * This task will retrieve the traces of an executed process.<br>
     e.g. &lt;include<br>
     filename="filename"  // the file to include. May be either a QAXML file or a Java style properties file<br>
     onlyif="result"/&gt;  // optional - execute only if true<br>
	 */
    public void processIncludeNode(org.w3c.dom.Node includeNode) {
        StringBuffer fileName = null;
        try {
            org.w3c.dom.NamedNodeMap attributes = includeNode.getAttributes();
            if (onlyIf(attributes)) {
                fileName = resolveVariable(getAttribute(attributes, "filename"));
                File f = new File(fileName.toString());
                if (!f.exists()) {
                    throw new Exception("Include file not found :" + fileName);
                }
                if (fileName.toString().toLowerCase().endsWith(Common.PROPERTIES_EXTENSION)) {
                    printDebug("including properties file " + fileName.toString());
                    Properties newProperties = new Properties();
                    newProperties.load(new FileInputStream(fileName.toString()));
                    properties = Utils.mergeProperties(properties, newProperties);
                    propertiesIncludeList.add(fileName.toString());
                } else {
                    printDebug("including qaxml file " + fileName.toString());
                    QAXMLParser childParser = new QAXMLParser(this);
                    childParser.setFilePath(fileName.toString());
                    int includeStatus = childParser.parseFile();
                    Properties newProperties = childParser.getProperties();
                    properties = Utils.mergeProperties(properties, newProperties);
                    if (includeStatus != ProtocolConstants.PASSED) {
                        setStatus(includeStatus);
                    }
                    qaxmlIncludeList.add(fileName.toString());
                }
            } else {
                printStream.println("ignoring include (onlyif false)");
            }
        } catch (Exception e) {
            printError(INCLUDE_NODE, "problem with include file '" + fileName.toString() + "' (" + e.toString() + ") ");
        }
    }

    /**
	 * This tag sets the designated variable to a random value. <br>
     e.g. &lt;random<br>
     name="myrandomvar"  // variable to set<br>
     onlyif="result" /&gt;  // optional - execute only if true<br>
	 */
    public void processRandomNode(org.w3c.dom.Node randomNode) {
        setStatusText(randomNode.getNodeName());
        try {
            org.w3c.dom.NamedNodeMap attributes = randomNode.getAttributes();
            if (onlyIf(attributes)) {
                properties.setProperty(getAttribute(attributes, "name").toString(), Integer.toString(Math.abs(randomVar.nextInt())));
            }
        } catch (Exception e) {
            printError(RANDOM_NODE, "invalid random node (" + e.getMessage() + ")");
        }
    }

    /**
	 * This tag causes a global property to be set.
     &lt;report status="passed, failed"/&gt;<br>
     Note, an unresolved result can not be over-ridden.
	 */
    public void processReportNode(org.w3c.dom.Node reportNode) {
        setStatusText(reportNode.getNodeName());
        try {
            org.w3c.dom.NamedNodeMap attributes = reportNode.getAttributes();
            if (onlyIf(attributes)) {
                StringBuffer statusStr = resolveVariable(getAttribute(attributes, "status", "failed"));
                setStatusText(reportNode.getNodeName() + " " + statusStr.toString());
                printStream.println(reportNode.getNodeName() + " " + statusStr.toString());
                if (getStatus() != ProtocolConstants.UNRESOLVED) {
                    if (inEvaluationMode()) {
                        setStatus(ProtocolConstants.PASSED);
                    } else {
                        if (statusStr.toString().toLowerCase().equals("passed")) {
                            setStatus(ProtocolConstants.PASSED);
                        } else {
                            if (getTestBugInfo().equals("")) {
                                setStatus(ProtocolConstants.FAILED);
                            } else {
                                setStatus(ProtocolConstants.PENDING);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            printError(REPORT_NODE, "invalid report node (" + e.getMessage() + ")");
        }
    }

    private void addToPropertiesList(String key, String prop) {
        String list = properties.getProperty(key, "");
        if (list.length() > 0) {
            if (list.indexOf(prop) < 0) properties.setProperty(key, list + File.pathSeparator + prop);
        } else {
            properties.setProperty(key, prop);
        }
    }

    /**
	 * Looks through currently running agents for an identifier matching processID.
	 * Returns the matching AgentInstance, or null if it wasn't found.
	 */
    private AgentInstance getAgentRunningProcess(StringBuffer processid) {
        try {
            AgentInstance agentInstance;
            for (Iterator<String> e = agentMap.keySet().iterator(); e.hasNext(); ) {
                agentInstance = (AgentInstance) agentMap.get(e.next());
                if (agentInstance.isHandlingProcess(processid.toString())) {
                    return agentInstance;
                }
            }
        } catch (Exception e) {
            printError(23, "no agent was running this process!");
        }
        return null;
    }

    /**
	 * Simple wrapper function to handle exceptions when attributes are not set.
	 */
    private StringBuffer getAttribute(org.w3c.dom.NamedNodeMap attributes, String attributeName) {
        return getAttribute(attributes, attributeName, "");
    }

    /**
	 * Simple wrapper function to handle exceptions when attributes are not set.
	 */
    private StringBuffer getAttribute(org.w3c.dom.NamedNodeMap attributes, String attributeName, String defaultValue) {
        org.w3c.dom.Node attribute = attributes.getNamedItem(attributeName);
        if (attribute != null) return new StringBuffer(attribute.getNodeValue());
        return new StringBuffer(defaultValue);
    }

    private boolean isInterrupted() {
        return interrupted;
    }

    /**
	 * This method is responsible for killing any processes already started on the agents,
	 * and immediately halt parsing any files.
	 */
    public void interrupt() {
        interrupted = true;
        if (!inEvaluationMode()) {
        }
    }

    /**
	 * This method retrieves the specified property from the results of parsing this file.
	 */
    public String getProperty(String key) {
        return "NOT YET IMPLEMENTED";
    }

    /**
	 * This method retrieves the specified property from the results of parsing this file.
	 * If the value is not found, the defaultValue is returned.
	 */
    public String getProperty(String key, String defaultValue) {
        return "NOT YET IMPLEMENTED";
    }

    /**
	 * This method should return a test name which will be used to display the test in
	 * the test tree.
	 */
    public String getTestName() {
        return name;
    }

    /**
	 * This method should return a test Author which will be used to display the test in
	 * the test tree.
	 */
    public String getTestAuthor() {
        return author;
    }

    /**
	 * This method should return a test Description which will be used to display the test in
	 * the test tree.
	 */
    public String getTestDescription() {
        return description;
    }

    /**
	 * This method should return a test BugInfo which will be used for displaying the test in
	 * the test tree.
	 */
    public String getTestBugInfo() {
        return buginfo;
    }

    /**
	 * This method should return all keywords associated with this test. These will be used
	 * in using the keywords to select/deselect tests in the harness.
	 */
    public String[] getKeyWords() {
        return new String[0];
    }

    /**
	 * This should return the list of files other than standard java.util.Properties files which were
	 * included to parse this test file.
	 */
    public String[] getIncludeList() {
        String[] list = new String[qaxmlIncludeList.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = (String) qaxmlIncludeList.get(i);
        }
        return list;
    }

    /**
	 * This should return the list of standard java.util.Properties files which were
	 * included to parse this test file.
	 */
    public String[] getPropertiesIncludeList() {
        String[] list = new String[propertiesIncludeList.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = (String) propertiesIncludeList.get(i);
        }
        return list;
    }

    /**
	 * This method should list all available output files produced by this test when run on the agent,
	 * but relative to the harness.
	 */
    public String[] getTraceList() {
        return Utils.toStringArray(properties.getProperty(INTERNAL_TRACE_LIST));
    }

    /**
	 * This is called at the beginning of a parser run on
	 * one or more tests.
	 * @param projectResultsDir - the canonical pathname of
	 * the project file, used to decide where to place
	 * the parser trace files relative to the harness.
	 */
    public void prepare(String projectResultsDir) {
        this.projectResultsDir = projectResultsDir;
    }

    /**
	 * This is called when a new parser is created for parsing an 'include'
	 * file.  The return value is passed to prepare() so that
	 * projectResultsDir remains consistent in the child parsers.
	 */
    public String getProjectResultsDir() {
        return this.projectResultsDir;
    }

    /**
	 * Returns a handle to the Printstream the parser will use for any output
	 * resulting from parsing this test.
	 */
    public PrintStream openPrintStream(String fileName) throws java.io.FileNotFoundException {
        return new HtmlPrintStream(new PrintStream(new FileOutputStream(fileName), true), true);
    }

    public void printDebug(String msg) {
    }

    /**
	 * This method indicates we are finished with this parser, and disposes
	 * any reserved resources.
	 */
    public void finish() {
    }

    /**
	 * This is the handle to to QAT parent GUI to display which commands
	 * the parser is processing in real-time.
	 */
    public void setStatusLabel(JLabel status) {
        statusLabel = status;
    }

    /**
	 * This is the handle to to QAT parent GUI to display which commands
	 * the parser is processing in real-time.
	 */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    private void setStatusText(String s) {
        if (!inEvaluationMode()) statusLabel.setText(s);
    }

    private void printError(int code, String msg) {
        setStatus(ProtocolConstants.FAILED);
        printStream.printBold("[ ");
        printStream.print(HtmlPrintStream.GREEN, keywords[code]);
        printStream.print(HtmlPrintStream.RED, " error :");
        printStream.print(msg);
        printStream.print(" in file ");
        printStream.print(HtmlPrintStream.RED, filePath);
        printStream.printBoldln(" ]");
    }

    static {
        try {
            documentBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public StringBuffer resolveVariable(StringBuffer expression) throws Exception {
        return QAXMLExpression.resolveVariable(expression, properties);
    }

    public boolean onlyIf(org.w3c.dom.NamedNodeMap attributes) throws Exception {
        StringBuffer variable = getAttribute(attributes, "onlyif", TRUE);
        StringBuffer onlyif = resolveVariable(variable);
        if (onlyif.toString().equals(TRUE)) {
            return true;
        } else {
            BooleanExpression expr = new BooleanExpression(onlyif);
            boolean result = expr.evaluate();
            return result;
        }
    }

    public boolean verbose(org.w3c.dom.NamedNodeMap attributes) {
        StringBuffer verbose = getAttribute(attributes, "verbose", TRUE);
        if (verbose.toString().equals(TRUE)) {
            return true;
        } else {
            return false;
        }
    }

    public int getStatus() {
        return this.statusCode;
    }

    public void setStatus(int newStatus) {
        if (statusCode != ProtocolConstants.UNRESOLVED) {
            this.statusCode = newStatus;
        }
    }

    public static final void main(String args[]) {
        try {
            System.getProperties().setProperty("qat.agent.count", "1");
            System.getProperties().setProperty("agent1.name", "triolet");
            System.getProperties().setProperty("agent1.port", "9000");
            QAXMLParser parser = new QAXMLParser();
            parser.setTestPath("examples" + File.separator + "qaxml_examples" + File.separator + "positive_tests" + File.separator + "pass_test_all_agents.qaxml");
            parser.setProperties(System.getProperties());
            parser.setPrintStream(System.out, false);
            parser.setEvaluationMode(true);
            parser.setStatusLabel(new JLabel());
            parser.parseFile();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
