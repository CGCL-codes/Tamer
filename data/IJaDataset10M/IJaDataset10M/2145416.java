package net.assimilator.tools.deploymentdirectory.commands;

import java.io.*;
import java.util.logging.Logger;

/**
 * Replace the environment parameters section in the opstring with the actual
 * webster host and port.
 *
 * @author Larry Mitchell
 * @version $Id: OpstringRewriterCommand.java 395 2008-03-01 01:12:53Z khartig $
 */
public class OpstringRewriterCommand implements Command {

    /**
     * the logger for this class
     */
    private static final Logger logger = Logger.getLogger("net.assimilator.tools.deploymentdirectory.commands");

    /**
     * the header to replace with
     */
    private static final String replacementHeader = "<!DOCTYPE opstring SYSTEM \"java://net/assimilator/dtd/opstring.dtd\" >";

    /**
     * the start of the document header
     */
    private static final String headerStart = "<!DOCTYPE";

    /**
     * code base start tag
     */
    private static final String codebaseStartTag = "<Codebase>";

    /**
     * codebase end tag
     */
    private static final String codebaseEndTag = "</Codebase>";

    /**
     * the end of the document header
     */
    private static final String headerEnd = "]>";

    /**
     * the group start tag
     */
    private static final String groupStartTag = "<Group>";

    /**
     * the group end tag
     */
    private static final String groupEndTag = "</Group>";

    /**
     * the target deployment directory
     */
    private String deploymentDirectory;

    /**
     * the oar directory that was unzipped
     */
    private String oarDirectory;

    /**
     * the path in the oar directory to this opstring
     */
    private String opstring;

    /**
     * the webster host name
     */
    private String hostName;

    /**
     * the webster port number
     */
    private int hostport;

    /**
     * the assimilator group
     */
    private String assimilatorGroup;

    /**
     * the string buffer that we manipulate
     */
    private StringBuffer opstringBuffer = new StringBuffer();

    /**
     * op string rewriter
     *
     * @param dDirectory the deployment directory
     * @param oDirectory the oar directory
     * @param ops        the opstring
     * @param name       the host name
     * @param port       the port number
     * @param group      the assimilator group to add
     */
    public OpstringRewriterCommand(String dDirectory, String oDirectory, String ops, String name, int port, String group) {
        this.deploymentDirectory = dDirectory;
        this.oarDirectory = oDirectory;
        this.opstring = ops;
        this.hostName = name;
        hostport = port;
        assimilatorGroup = group;
    }

    /**
     * execute the command
     */
    public void execute() {
        try {
            loadOpstring();
            replaceHeaderCategory();
            replaceCodeServerCategory();
            replaceGroupsCategory();
            storeOpstring();
        } catch (UnexpectedOpstringFormatException uofe) {
            logger.severe("Opstring transform failed due to:" + uofe);
        } catch (IOException ioe) {
            logger.severe("Opstring transform failed due to:" + ioe);
        }
    }

    private void loadOpstring() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(getFileLocation()));
        StringWriter out = new StringWriter();
        String readString;
        while ((readString = in.readLine()) != null) {
            out.write(readString);
        }
        in.close();
        opstringBuffer = out.getBuffer();
    }

    private void storeOpstring() throws IOException {
        StringReader in = new StringReader(opstringBuffer.toString());
        BufferedWriter out = new BufferedWriter(new FileWriter(getFileLocation()));
        int readInt;
        while ((readInt = in.read()) != -1) {
            out.write(readInt);
        }
        in.close();
        out.flush();
        out.close();
    }

    private void replaceHeaderCategory() throws UnexpectedOpstringFormatException {
        int start = opstringBuffer.indexOf(headerStart);
        int end = opstringBuffer.indexOf(headerEnd) + 2;
        if ((start == -1) || (end == -1)) {
            throw new UnexpectedOpstringFormatException("header section is not what we expect");
        }
        opstringBuffer.replace(start, end, replacementHeader);
    }

    private void replaceCodeServerCategory() throws UnexpectedOpstringFormatException {
        int start = opstringBuffer.indexOf(codebaseStartTag);
        int end = opstringBuffer.indexOf(codebaseEndTag) + codebaseEndTag.length();
        if ((start == -1) || (end == -1)) {
            throw new UnexpectedOpstringFormatException("code server section is not what we expect");
        }
        opstringBuffer.replace(start, end, makeNewCodeBase());
    }

    private void replaceGroupsCategory() throws UnexpectedOpstringFormatException {
        int start = opstringBuffer.indexOf(groupStartTag);
        int end = opstringBuffer.indexOf(groupEndTag) + groupEndTag.length();
        if ((start == -1) || (end == -1)) {
            throw new UnexpectedOpstringFormatException("group server section is not what we expect");
        }
        opstringBuffer.replace(start, end, makeGroup());
    }

    private String getFileLocation() {
        return deploymentDirectory + File.separator + oarDirectory + File.separator + "opstrings" + File.separator + opstring;
    }

    private String makeNewCodeBase() {
        StringBuffer codebase = new StringBuffer(codebaseStartTag);
        codebase.append("http://").append(hostName).append(":").append(hostport);
        codebase.append(codebaseEndTag);
        return codebase.toString();
    }

    private String makeGroup() {
        StringBuffer codebase = new StringBuffer(groupStartTag);
        codebase.append(assimilatorGroup);
        codebase.append(groupEndTag);
        return codebase.toString();
    }

    /**
     * the main
     *
     * @param args the input args which are none
     */
    public static void main(String[] args) {
        OpstringRewriterCommand opRewriter = new OpstringRewriterCommand("d:/deployer", "hello", "hello.xml", "192.168.1.100", 9000, "ASSIMILATOR_EXAMPLES");
        opRewriter.execute();
    }
}
