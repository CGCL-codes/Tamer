package net.sf.dhwt.tree;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import net.sf.dhwt.Composite;
import net.sf.dhwt.tracer.Debug;
import net.sf.dhwt.util.CompositeUtils;

/**
 *  This class can build a tree in browser , which contains nodes can be clicked
 *  and load other pages.
 *
 * @author     Huijing Sheng
 * @version    2.0 2003-12-30
 */
public class HLinkTree extends AbstractTree {

    public static final String BLANK_TARGET = "_blank";

    public static final String PARENT_TARGET = "_parent";

    public static final String SELF_TARGET = "_self";

    public static final String TOP_TARGET = "_top";

    public static final String HTTP_PROTOCOL = "http";

    public static final String FTP_PROTOCOL = "ftp";

    public static final String HTTPS_PROTOCOL = "https";

    public static final String MAIL_PROTOCOL = "mailto";

    private boolean highlight = false;

    private String highlightBgColor = "#ffffff";

    private String highlightColor = "#000000";

    private String defaultTarget = BLANK_TARGET;

    private String defaultProtocol;

    private String nodeUrlName;

    private String nodeProtocolName;

    private String nodeTargetName;

    private String emptyUrlItemIcon = getItemIcon();

    /**  Constructor for the HLinkTree object */
    public HLinkTree() {
    }

    /**
     *  Constructor for the HLinkTree object
     *
     * @param  name
     */
    public HLinkTree(String name) {
        super(name);
    }

    /**
     *  Gets the highlight attribute of the HLinkTree object
     *
     * @return    The highlight value
     */
    public boolean getHighlight() {
        return highlight;
    }

    /**
     *  Sets the highlight attribute of the HLinkTree object
     *
     * @param  highlight  The new highlight value
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    /**
     *  Gets the highlightColor attribute of the HLinkTree object
     *
     * @return    The highlightColor value
     */
    public String getHighlightColor() {
        return highlightColor;
    }

    /**
     *  Sets the highlightColor attribute of the HLinkTree object
     *
     * @param  highlightColor  The new highlightColor value
     */
    public void setHighlightColor(String highlightColor) {
        this.highlightColor = highlightColor;
    }

    /**
     *  Gets the highlightBgColor attribute of the HLinkTree object
     *
     * @return    The highlightBgColor value
     */
    public String getHighlightBgColor() {
        return highlightBgColor;
    }

    /**
     *  Sets the highlightBgColor attribute of the HLinkTree object
     *
     * @param  highlightBgColor  The new highlightBgColor value
     */
    public void setHighlightBgColor(String highlightBgColor) {
        this.highlightBgColor = highlightBgColor;
    }

    /**
     *  Gets the target attribute of the HLinkTree object
     *
     * @return    The target value
     */
    public String getDefaultTarget() {
        return defaultTarget;
    }

    /**
     *  Sets the target attribute of the HLinkTree object
     *
     * @param  target  The new target value
     */
    public void setDefaultTarget(String target) {
        defaultTarget = target;
    }

    /**
     *  Gets the protocol attribute of the HLinkTree object
     *
     * @return    The protocol value
     */
    public String getDefaultProtocol() {
        return defaultProtocol;
    }

    /**
     *  Sets the protocol attribute of the HLinkTree object
     *
     * @param  protocol  The new protocol value
     */
    public void setDefaultProtocol(String protocol) {
        defaultProtocol = protocol;
    }

    /**
     *  Gets the protocolName attribute of the HLinkTree object
     *
     * @return    The protocolName value
     */
    public String getNodeProtocolName() {
        return nodeProtocolName;
    }

    /**
     *  Sets the protocolName attribute of the HLinkTree object
     *
     * @param  protocolName  The new protocolName value
     */
    public void setNodeProtocolName(String protocolName) {
        nodeProtocolName = protocolName;
    }

    /**
     *  Gets the targetName attribute of the HLinkTree object
     *
     * @return    The targetName value
     */
    public String getNodeTargetName() {
        return nodeTargetName;
    }

    /**
     *  Sets the targetName attribute of the HLinkTree object
     *
     * @param  targetName  The new targetName value
     */
    public void setNodeTargetName(String targetName) {
        nodeTargetName = targetName;
    }

    /**
     *  Gets the emptyUrlItemIcon attribute of the HLinkTree object
     *
     * @return    The emptyUrlItemIcon value
     */
    public String getEmptyUrlItemIcon() {
        return emptyUrlItemIcon;
    }

    /**
     *  Sets the emptyUrlItemIcon attribute of the HLinkTree object
     *
     * @param  emptyUrlItemIcon  The new emptyUrlItemIcon value
     */
    public void setEmptyUrlItemIcon(String emptyUrlItemIcon) {
        this.emptyUrlItemIcon = emptyUrlItemIcon;
    }

    /**
     *  paint the component
     *
     * @exception  IOException  Description of the Exception
     * @param  writer
     */
    public void paint(Writer writer) throws IOException {
        super.paint(writer);
        Debug.getDebug().debug("HLinkTree paint() starts!");
        StringBuffer details = new StringBuffer();
        initScriptParameters(details);
        Debug.getDebug().debug("Initialization script properties finished!");
        loadTreeNodes(details);
        organizeEndingScript(details);
        writer.write(details.toString());
        Debug.getDebug().debug("HLinkTree paint() ends!");
    }

    /**
     *  Gets the nodeURLName attribute of the HLinkTree object. This field
     *  defines the property name of node URL.
     *
     * @return    The nodeURLName value
     */
    public String getNodeUrlName() {
        return nodeUrlName;
    }

    /**
     *  Sets the nodeURLName attribute of the HLinkTree object. This field
     *  defines the property name of node URL.
     *
     * @param  nodeUrlName  The new nodeUrlName value
     */
    public void setNodeUrlName(String nodeUrlName) {
        this.nodeUrlName = nodeUrlName;
    }

    private void initScriptParameters(StringBuffer details) {
        details.append("<div style='position:absolute;top:0;left:0;visibility:hidden'>" + "<a href='http://www.treemenu.net/' >Script Source</a>" + "</div>\n <script>").append("\n STARTALLOPEN = " + BooleanUtils.toInteger(getStartAllOpen())).append(";\n USETEXTLINKS = 1").append(";\n PERSERVESTATE = 1").append(";\n USEICONS = " + BooleanUtils.toInteger(getUseIcons())).append(";\n WRAPTEXT = " + BooleanUtils.toInteger(getWrapText())).append(";\n HIGHLIGHT = " + BooleanUtils.toInteger(getHighlight())).append(";\n HIGHLIGHT_COLOR = \"" + getHighlightColor() + "\"").append(";\n HIGHLIGHT_BG = \"" + getHighlightBgColor() + "\"").append(";\n TARGET = \"" + getDefaultTarget() + "\"").append(";\n ICONPATH = \"" + getIconPath() + "\"").append(";\n FOLDEROPENICON = \"" + getFolderOpenIcon() + "\"").append(";\n FOLDERCLOSEDICON = \"" + getFolderClosedIcon() + "\"").append(";\n ITEMICON = \"" + getItemIcon() + "\"").append(";\n foldersTree = gFld(\"" + getRootDescription() + "\")").append(";\n foldersTree.iconSrc = ICONPATH + \"" + getRootIcon() + "\"");
    }

    private void loadTreeNodes(StringBuffer details) throws IOException {
        Composite treeNodes = CompositeUtils.getReadyCompiste(this, getTreeNodesCompositeName());
        Iterator keys = treeNodes.getCompositeNames().iterator();
        Composite treeNode;
        String key;
        Object parentNodeKey;
        Boolean isDir = new Boolean(true);
        ArrayList parentKeyList = new ArrayList();
        while (keys.hasNext()) {
            key = (String) keys.next();
            treeNode = treeNodes.getComposite(key);
            parentNodeKey = treeNode.getProperty(getParentNodeKeyName());
            if (parentNodeKey != null) {
                parentKeyList.add(parentNodeKey);
            }
        }
        keys = treeNodes.getCompositeNames().iterator();
        while (keys.hasNext()) {
            treeNode = treeNodes.getComposite((String) keys.next());
            if (parentKeyList.contains(treeNode.getProperty(getNodeKeyName()))) {
                treeNode.setProperty(IS_DIRECTORY_KEY, isDir);
            }
            loadCommonTreeNode(details, treeNode);
        }
    }

    private void loadDirectChildOfRoot(StringBuffer details, String nodeKey, String nodeDescription, String nodeUrl) {
        String url = "null";
        details.append(";\n " + getTreeNodesCompositeName() + nodeKey + " = insFld(foldersTree, gFld(\"" + nodeDescription + "\"," + getValidScriptUrl(nodeUrl) + "))");
    }

    private void loadFolder(StringBuffer details, String nodeKey, String parentNodeKey, String nodeDescription, String nodeUrl) {
        details.append(";\n " + getTreeNodesCompositeName() + nodeKey + " = insFld(" + getTreeNodesCompositeName() + parentNodeKey + ", gFld(\"" + nodeDescription + "\"," + getValidScriptUrl(nodeUrl) + "))");
    }

    private String getValidScriptUrl(String urlString) {
        String url = "null";
        if (StringUtils.isNotEmpty(urlString)) {
            url = "\"" + urlString + "\"";
        }
        return url;
    }

    private void loadLeafItem(StringBuffer details, String nodeKey, String parentNodeKey, String nodeDescription, String nodeUrl, String target, String protocol) {
        if (StringUtils.isNotEmpty(nodeUrl)) {
            details.append(";\n " + getTreeNodesCompositeName() + nodeKey + " = insDoc(" + getTreeNodesCompositeName() + parentNodeKey + ", gLnk(\"" + getOptionFlags(target, protocol) + "\", \"" + nodeDescription + "\",\"" + nodeUrl + "\"))");
        } else {
            details.append(";\n " + getTreeNodesCompositeName() + nodeKey + " = insFld(" + getTreeNodesCompositeName() + parentNodeKey + ", gFld(\"" + nodeDescription + "\", null))").append(";\n " + getTreeNodesCompositeName() + nodeKey + ".iconSrc = ICONPATH + \"" + getEmptyUrlItemIcon() + "\"").append(";\n " + getTreeNodesCompositeName() + nodeKey + ".iconSrcClosed = ICONPATH + \"" + getEmptyUrlItemIcon() + "\"");
        }
    }

    private void organizeEndingScript(StringBuffer details) {
        details.append(";\n initializeDocument()");
        if (StringUtils.isNotEmpty(getOpenNodeKeys())) {
            String[] openKeys = getOpenNodeKeys().split(NODE_KEY_SEPARATOR);
            String prefix = getTreeNodesCompositeName();
            for (int i = 0; i < openKeys.length; i++) {
                details.append(";\n if(typeof " + prefix + openKeys[i] + " != 'undefined' && typeof " + prefix + openKeys[i] + ".isOpen != 'undefined' && !" + prefix + openKeys[i] + ".isOpen){clickOnNodeObj(" + prefix + openKeys[i] + ")};");
            }
        }
        details.append(";\n </script>");
    }

    private void loadCommonTreeNode(StringBuffer details, Composite treeNode) {
        String nodeKey = CompositeUtils.getStringProperty(treeNode, getNodeKeyName());
        String nodeDescription = CompositeUtils.getStringProperty(treeNode, getNodeDescriptionName());
        String parentNodeKey = CompositeUtils.getStringProperty(treeNode, getParentNodeKeyName());
        String nodeURL = CompositeUtils.getStringProperty(treeNode, getNodeUrlName());
        String target = CompositeUtils.getStringProperty(treeNode, getNodeTargetName());
        if (StringUtils.isEmpty(target)) {
            target = getDefaultTarget();
        }
        String protocol = CompositeUtils.getStringProperty(treeNode, getNodeProtocolName());
        if (StringUtils.isEmpty(target)) {
            protocol = getDefaultProtocol();
        }
        if (StringUtils.isEmpty(parentNodeKey)) {
            loadDirectChildOfRoot(details, nodeKey, nodeDescription, nodeURL);
        } else if (treeNode.getProperty(IS_DIRECTORY_KEY) == null) {
            loadLeafItem(details, nodeKey, parentNodeKey, nodeDescription, nodeURL, target, protocol);
        } else {
            loadFolder(details, nodeKey, parentNodeKey, nodeDescription, nodeURL);
        }
    }

    /**
     *  overridden the parent method.
     *
     * @return    The validForPainting value
     */
    protected boolean isValidForPainting() {
        boolean isValid = false;
        if (StringUtils.isEmpty(getNodeKeyName())) {
            return isValid;
        } else if (StringUtils.isEmpty(getNodeDescriptionName())) {
            return isValid;
        } else if (StringUtils.isEmpty(getParentNodeKeyName())) {
            return isValid;
        } else if (StringUtils.isEmpty(getNodeUrlName())) {
            return isValid;
        } else if (StringUtils.isEmpty(getTreeNodesCompositeName())) {
            return isValid;
        }
        Composite treeNodes = getComposite(getTreeNodesCompositeName());
        if (treeNodes == null) {
            return isValid;
        }
        isValid = true;
        return isValid;
    }

    private String getOptionFlags(String target, String protocol) {
        String optionFlags = "B";
        if (StringUtils.isEmpty(target)) {
            return optionFlags;
        }
        if (target.equals(PARENT_TARGET)) {
            optionFlags = "P";
        } else if (target.equals(SELF_TARGET)) {
            optionFlags = "S";
        } else if (target.equals(TOP_TARGET)) {
            optionFlags = "T";
        } else if (target.equals(BLANK_TARGET)) {
            optionFlags = "B";
        } else {
            optionFlags = "R";
        }
        if (StringUtils.isEmpty(protocol)) {
            return optionFlags;
        }
        if (protocol.equals(HTTP_PROTOCOL)) {
            optionFlags += "h";
        } else if (protocol.equals(HTTPS_PROTOCOL)) {
            optionFlags += "s";
        } else if (protocol.equals(FTP_PROTOCOL)) {
            optionFlags += "f";
        } else if (protocol.equals(MAIL_PROTOCOL)) {
            optionFlags += "m";
        }
        return optionFlags;
    }
}
