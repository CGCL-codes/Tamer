package org.primordion.xholon.io;

import java.io.IOException;
import java.io.Reader;
import org.primordion.xholon.app.IApplication;
import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.ISignal;
import org.primordion.xholon.base.IXPath;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.base.IXholonClass;
import org.primordion.xholon.base.Xholon;
import org.primordion.xholon.service.AbstractXholonService;
import org.primordion.xholon.service.IXholonService;
import org.primordion.xholon.service.NodeSelectionService;

/**
 * Read and process a xholonWorkbook.xml file, url, or string.
 * Details about the format of the .xml document:
 * <ul>
 * <li>The root node is &lt;XholonWorkbook>.</li>
 * <li>All Xholon chunks are direct children of the root node.</li>
 * <li>The document contains exactly one composite structure hierarchy (CSH) chunk.</li>
 * <li>The CSH chunk begins with a tag that ends with "System" (ex: &lt;PhysicalSystem>).</li>
 * <li>There are 0 or more chunks that end with "behavior" (ex: &lt;Blockbehavior>)</li>
 * <li>The IH chunk begins with the tag &lt;_-.XholonClass>.</li>
 * <li>The CH chunk begins with the tag &lt;xholonClassDetails>.</li>
 * <li>There are 0 or 1 &lt;Notes> chunks.</li>
 * </ul>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on January 27, 2012)
 */
public class XholonWorkbook extends Xholon {

    private int bufferCapacity = 1000;

    private char[] buffer = null;

    private IApplication app = null;

    private IXholon xholonHelperService = null;

    /** To prevent the insertion of unreasonably large workbooks. 100 * bufferCapacity chars */
    private int maxReads = 100;

    private IXholon contextNode = null;

    private String paramsStr = "params::";

    private String paramStr = "param_";

    private boolean shouldInsertSvgClient = true;

    private boolean shouldInsertBehaviors = false;

    /**
	 * Read content from a source in xholonWorkbook.xml format.
	 * Parse it into XML chunks,
	 * and pass the chunks off to other classes for insertion into the Xholon app.
	 * @param app This app's instance of IApplication.
	 * @param reader A Reader.
	 * @return null
	 */
    public IXholon xml2Xh(IApplication app, Object reader) {
        this.app = app;
        String workbookContents = readWorkbookContents((Reader) reader);
        int pos = workbookContents.indexOf("<XholonWorkbook");
        if (pos == -1) {
            return null;
        }
        contextNode = getContextNode();
        if (contextNode == null) {
            contextNode = app.getRoot();
        } else if (!(contextNode.getRootNode() == app.getRoot())) {
            contextNode = app.getRoot();
        }
        pos = workbookContents.indexOf("<", pos + 1);
        while (pos != -1) {
            int startTagBeginIndex = pos;
            int startTagEndIndex = workbookContents.indexOf(">", startTagBeginIndex);
            String tagName = workbookContents.substring(startTagBeginIndex + 1, startTagEndIndex);
            if ("/XholonWorkbook".equals(tagName)) {
                break;
            } else if (tagName.endsWith("/")) {
                pos = workbookContents.indexOf("<", startTagEndIndex);
                continue;
            }
            int tagNameEndIndex = tagName.indexOf(" ");
            if (tagNameEndIndex != -1) {
                tagName = tagName.substring(0, tagNameEndIndex);
            }
            int endTagBeginIndex = workbookContents.indexOf("</" + tagName, startTagEndIndex + 1);
            int endTagEndIndex = workbookContents.indexOf(">", endTagBeginIndex);
            String subTree = workbookContents.substring(startTagBeginIndex, endTagEndIndex + 1);
            processSubTree(tagName, subTree);
            pos = workbookContents.indexOf("<", endTagEndIndex + 1);
        }
        return null;
    }

    /**
	 * Process a subtree found in the workbook.
	 * @param tagName The XML tagName for the subTree node.
	 * @param subTree The string contents of the XML subTree node.
	 */
    protected void processSubTree(String tagName, String subTree) {
        if ("Notes".equals(tagName)) {
            int paramsIndex = subTree.indexOf(paramsStr);
            if (paramsIndex != -1) {
                String paramName = paramStr + "shouldInsertSvgClient=";
                int paramIndex = subTree.indexOf(paramName, paramsIndex);
                if (paramIndex != -1) {
                    paramIndex += paramName.length();
                    String bStr = subTree.substring(paramIndex, paramIndex + 4);
                    shouldInsertSvgClient = Boolean.parseBoolean(bStr);
                }
                paramName = paramStr + "shouldInsertBehaviors=";
                paramIndex = subTree.indexOf(paramName, paramsIndex);
                if (paramIndex != -1) {
                    paramIndex += paramName.length();
                    String bStr = subTree.substring(paramIndex, paramIndex + 4);
                    shouldInsertBehaviors = Boolean.parseBoolean(bStr);
                }
            }
        } else if ("_-.XholonClass".equals(tagName)) {
            IXholonClass xhcRoot = app.getXhcRoot();
            sendXholonHelperService(ISignal.ACTION_PASTE_LASTCHILD_FROMSTRING, subTree, xhcRoot);
        } else if ("xholonClassDetails".equals(tagName)) {
            IXholonClass xhcRoot = app.getXhcRoot();
            sendXholonHelperService(ISignal.ACTION_PASTE_LASTCHILD_FROMSTRING, subTree, xhcRoot);
        } else if (tagName.endsWith("System")) {
            sendXholonHelperService(ISignal.ACTION_PASTE_LASTCHILD_FROMSTRING, subTree, contextNode);
        } else if ("SvgClient".equals(tagName)) {
            if (shouldInsertSvgClient) {
                IXholon viewRoot = app.getView();
                sendXholonHelperService(ISignal.ACTION_PASTE_LASTCHILD_FROMSTRING, subTree, viewRoot);
            }
        } else if ("script".equals(tagName)) {
        } else if (tagName.endsWith("behavior")) {
            if (shouldInsertBehaviors) {
                String parentTagName = tagName.substring(0, tagName.indexOf("behavior"));
                IXPath xp = (IXPath) app.getService(AbstractXholonService.XHSRV_XPATH);
                IXholon behaviorParentNode = xp.evaluate("descendant::" + parentTagName, contextNode);
                if (behaviorParentNode != null) {
                    sendXholonHelperService(ISignal.ACTION_PASTE_LASTCHILD_FROMSTRING, subTree, behaviorParentNode);
                }
            }
        } else {
        }
    }

    /**
	 * Send a synchronous message to the XholonHelperService.
	 * @param signal
	 * @param data
	 * @param sender
	 * @return A response message.
	 */
    protected IMessage sendXholonHelperService(int signal, Object data, IXholon sender) {
        if (xholonHelperService == null) {
            xholonHelperService = app.getService(IXholonService.XHSRV_XHOLON_HELPER);
        }
        if (xholonHelperService == null) {
            return null;
        } else {
            if (sender == null) {
                sender = app;
            }
            return xholonHelperService.sendSyncMessage(signal, data, sender);
        }
    }

    /**
	 * Read the entire contents of the workbook.
	 * TODO limit the size of the file that can be read
	 * @param reader The reader.
	 * @return The entire string content of the workbook.
	 */
    protected String readWorkbookContents(Reader reader) {
        StringBuffer sb = new StringBuffer();
        int counter = maxReads;
        try {
            reader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (reader == null) {
            return null;
        }
        buffer = new char[bufferCapacity];
        int numCharsRead = read(reader, buffer);
        while (numCharsRead != -1) {
            counter--;
            if (numCharsRead == bufferCapacity) {
                sb.append(buffer);
            } else {
                sb.append(new String(buffer).substring(0, numCharsRead));
            }
            if (counter <= 0) {
                break;
            }
            numCharsRead = read(reader, buffer);
        }
        return sb.toString();
    }

    /**
	 * Read characters into an array.
	 * This method will block until some input is available,
	 * an I/O error occurs, or the end of the stream is reached.
	 * @param reader The reader.
	 * @param buffer Destination buffer.
	 * @return The number of characters read, or -1 if the end of the stream has been reached.
	 */
    protected int read(Reader reader, char[] buffer) {
        int numCharsRead = 0;
        try {
            numCharsRead = reader.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numCharsRead;
    }

    /**
	 * Get the IXholon context node.
	 * This is the node into which the workbook was pasted or dropped as a last child.
	 * It's the node in the tree on which a MouseEvent.BUTTON3 selection (Right-Click) event occured.
	 * @return A node, or null.
	 */
    protected IXholon getContextNode() {
        IXholon nss = app.getService(IXholonService.XHSRV_NODE_SELECTION);
        if (nss != null) {
            IMessage nodesMsg = nss.sendSyncMessage(NodeSelectionService.SIG_GET_BUTTON3_NODE_REQ, null, this);
            return (IXholon) nodesMsg.getData();
        }
        return null;
    }
}
