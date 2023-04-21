package org.xaware.ide.xadev.gui;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.datamodel.FileBizDriverInfo;
import org.xaware.ide.xadev.datamodel.LocalObjectDataFlavor;
import org.xaware.ide.xadev.datamodel.MapTreeNode;
import org.xaware.ide.xadev.datamodel.Transferable;
import org.xaware.ide.xadev.datamodel.XATreeNode;
import org.xaware.ide.xadev.gui.dialog.CopybookRedefinesDlg;
import org.xaware.ide.xadev.gui.mapper.MapperHelper;
import org.xaware.ide.xadev.gui.mapper.MapperProcessor;
import org.xaware.ide.xadev.wizardpanels.FileBizCompInfo;
import org.xaware.server.common.XMLNamespaceUtil;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Extending MapDNDTreeHandler to change drop functionality.
 * 
 * @author G Bharath Kumar
 * @version 1.0
 */
public class FileMapDNDTreeHandler extends MapDNDTreeHandler {

    /** Holds instance of XA_Designer_Plugin translator */
    public static final Translator t = XA_Designer_Plugin.getTranslator();

    /** Logger for MapDNDTreeHandler */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(FileMapDNDTreeHandler.class.getName());

    /** Reference to the namespace. */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** Holds reference to requesttype. */
    protected String requestType = t.getString("read");

    /** Holds reference to recordtype. */
    protected String recordType = t.getString("Fixed length");

    /** Holds reference to source element. */
    private Element fieldRootElem;

    /**
     * Creates a new DNDTreeDragSourceListener object.
     * 
     * @param parent
     *            Composite which holds the tree.
     * @param rootElement
     *            root of the tree.
     * @param inAllowDatapathPopup
     *            To allow datapathpopup.
     * @param functoid
     *            to allow functoid
     * @param treeImageAndLabelProvider
     *            labelprovider for tree.
     * @param requestType
     *            type of request
     * @param recordType
     *            type of record
     * @param editorRequired
     *            to check wheteher editor is required.
     */
    public FileMapDNDTreeHandler(final Composite parent, final XATreeNode rootElement, final boolean inAllowDatapathPopup, final boolean functoid, final XAMapTreeImageAndLabelProvider treeImageAndLabelProvider, final String requestType, final String recordType, final boolean editorRequired) {
        super(parent, rootElement, treeImageAndLabelProvider, inAllowDatapathPopup, functoid, editorRequired);
        allowDatapathPopup = inAllowDatapathPopup;
        allowFunctoid = functoid;
        this.requestType = requestType;
        this.recordType = recordType;
    }

    /**
     * Use this to pass the popup and functoid parms to MapDnDJTree Super
     * 
     * @param parent
     * @param rootElement
     *            Root of the tree
     * @param treeImageAndLabelProvider
     *            label provider for tree
     * @param inAllowDatapathPopup
     * @param functoid
     * @param requestType
     * @param recordType
     * @param passFullParms
     * @param editorRequired
     *            specifies whether editor is required.
     */
    public FileMapDNDTreeHandler(final Composite parent, final XATreeNode rootElement, final XAMapTreeImageAndLabelProvider treeImageAndLabelProvider, final boolean inAllowDatapathPopup, final boolean functoid, final String requestType, final String recordType, final boolean passFullParms, final boolean editorRequired) {
        super(parent, rootElement, treeImageAndLabelProvider, inAllowDatapathPopup, functoid, editorRequired);
        allowDatapathPopup = inAllowDatapathPopup;
        allowFunctoid = functoid;
        this.requestType = requestType;
        this.recordType = recordType;
    }

    /**
     * DropTargetListener interface method - What we do when drag is released
     * 
     * @param e
     *            DropTargetEvent instance.
     */
    @Override
    public void drop(final DropTargetEvent e) {
        try {
            final Transferable temp = (Transferable) e.data;
            try {
                transferData = temp.getTransferData(LocalObjectDataFlavor.getLocalObjectDataFlavorInstance());
            } catch (final Exception exp) {
                transferData = temp.getTransferData(TextTransfer.getInstance());
            }
            if (!dropEnabled) {
                e.detail = DND.DROP_NONE;
                return;
            }
            if (e.item == null) {
                e.detail = DND.DROP_NONE;
                return;
            }
            toDropOn = (MapTreeNode) e.item.getData();
            setToDropOn();
            if (toDropOn instanceof MapTreeNode) {
                if ((toDropOn).myContent instanceof Element) {
                    if (((Element) (toDropOn).myContent).getQualifiedName().equals("xa:map")) {
                        e.detail = DND.DROP_NONE;
                        ControlFactory.showMessageDialog("Mapping not allowed on xa:map element.", "Information");
                        return;
                    }
                } else if ((toDropOn).myContent instanceof Attribute) {
                    if (((Attribute) (toDropOn).myContent).getQualifiedName().equals("xa:map_match")) {
                        e.detail = DND.DROP_NONE;
                        ControlFactory.showMessageDialog("Mapping not allowed on xa:map_match attribute.", "Information");
                        return;
                    }
                    if (requestType.equals(FileBizDriverInfo.FILE_WRITE) || requestType.equals(FileBizDriverInfo.FILE_APPEND)) {
                        e.detail = DND.DROP_NONE;
                        ControlFactory.showMessageDialog("Please do not map on the field attributes.", "Information");
                        return;
                    }
                }
            }
            lastNodeModified = toDropOn;
            if (transferData instanceof MapTreeNode) {
                if (((MapTreeNode) transferData).myContent instanceof Attribute) {
                    if (requestType.equals(FileBizDriverInfo.FILE_READ)) {
                        e.detail = DND.DROP_NONE;
                        ControlFactory.showMessageDialog("Please do not map a record or field attribute.", "Information");
                        return;
                    } else if (requestType.equals(FileBizDriverInfo.FILE_WRITE) || requestType.equals(FileBizDriverInfo.FILE_APPEND)) {
                        if (isValueMapped(toDropOn.getPathValue(), toDropOn)) {
                            initPopup();
                            return;
                        }
                        toDropOn.setPathValue("%" + ((MapTreeNode) transferData).getXAPathString(showPlusSign, showDoubleSlash, showDocRef(), replaceSourceRootWithDot) + "%");
                        XMLNamespaceUtil.updateTargetElementNamespaces((Element) toDropOn.myContent, ((MapTreeNode) transferData).myContent);
                        expandTreeNode(toDropOn);
                    }
                } else if (((MapTreeNode) transferData).myContent instanceof Element) {
                    final Element transferElem = (Element) ((MapTreeNode) transferData).myContent;
                    if (requestType.equals(FileBizDriverInfo.FILE_READ)) {
                        if (((MapTreeNode) transferData).getParent() == null) {
                            e.detail = DND.DROP_NONE;
                            return;
                        }
                        final Element[] redefinesElems = getRedefinesElems(transferElem);
                        final String name = getRedefineName(transferElem);
                        final String occursStr = transferElem.getAttributeValue("occurs", ns);
                        if (redefinesElems != null) {
                            processRedefines("Map Redefined Fields", redefinesElems, name, transferElem, toDropOn, e);
                        } else if (occursStr != null) {
                            processOccursElem(transferElem, toDropOn, e);
                            expandTreeNode(toDropOn);
                        } else {
                            if (isValueMapped(toDropOn.getPathValue(), toDropOn)) {
                                initPopup();
                                return;
                            }
                            if (recordType.equals(FileBizCompInfo.DELIMITED)) {
                                final String fieldName = transferElem.getName();
                                toDropOn.setPathValue("%" + fieldName.substring(5) + "%");
                                expandTreeNode(toDropOn);
                            } else if (recordType.equals(FileBizCompInfo.FIXED_LENGTH)) {
                                toDropOn.setPathValue("%" + transferElem.getName() + "%");
                                expandTreeNode(toDropOn);
                            }
                        }
                    } else if (requestType.equals(FileBizDriverInfo.FILE_WRITE) || requestType.equals(FileBizDriverInfo.FILE_APPEND)) {
                        if (isValueMapped(toDropOn.getPathValue(), toDropOn)) {
                            initPopup();
                            return;
                        }
                        toDropOn.setPathValue("%" + ((MapTreeNode) transferData).getXAPathString(showPlusSign, showDoubleSlash, showDocRef(), replaceSourceRootWithDot) + "%");
                        XMLNamespaceUtil.updateTargetElementNamespaces((Element) toDropOn.myContent, ((MapTreeNode) transferData).myContent);
                        expandTreeNode(toDropOn);
                    }
                }
                refreshTree(toDropOn);
                setSelectionPath(toDropOn);
                transferData = null;
                toDropOn = null;
            } else if (transferData instanceof String) {
                if (isValueMapped(toDropOn.getPathValue(), toDropOn)) {
                    initPopup();
                    return;
                }
                toDropOn.setPathValue("%" + transferData.toString() + "%");
                expandTreeNode(toDropOn);
                refreshTree(toDropOn);
                setSelectionPath(toDropOn);
                toDropOn = null;
                transferData = null;
            }
            e.detail = DND.DROP_COPY;
        } catch (final Throwable t) {
            logger.printStackTrace(t);
            logger.fine("Error mapping. Exception:" + t, "FileMapDNDTreeHandler", "drop");
            t.printStackTrace();
            e.detail = DND.DROP_NONE;
        }
        fireActionInvokedListener(TREE_MAPPING);
    }

    /**
     * To get redefines elements
     * 
     * @param elem
     *            Element
     * 
     * @return array with redefines elements
     */
    public Element[] getRedefinesElems(final Element elem) {
        Element[] redefinesElems = null;
        if (elem.getQualifiedName().equals("xa:redefines_group")) {
            redefinesElems = getRedefinesFromGroup(elem);
        } else if (elem.getQualifiedName().equals("xa:redefines")) {
            final Element parentElem = elem.getParentElement();
            if (parentElem != null) {
                redefinesElems = getRedefinesFromGroup(parentElem);
            }
        } else {
            final Element parentElem = elem.getParentElement();
            if ((parentElem != null) && parentElem.getQualifiedName().equals("xa:redefines")) {
                final Element grandParentElem = parentElem.getParentElement();
                if (grandParentElem != null) {
                    redefinesElems = getRedefinesFromGroup(grandParentElem);
                }
            }
        }
        return redefinesElems;
    }

    /**
     * Return the value to be set for xa:name attribute for xa:switch element
     * 
     * @param elem
     *            Element
     * 
     * @return string
     */
    public String getRedefineName(final Element elem) {
        String name = "";
        if (elem.getQualifiedName().equals("xa:redefines_group")) {
            name = elem.getAttributeValue("name", ns);
        } else if (elem.getQualifiedName().equals("xa:redefines")) {
            final Element parentElem = elem.getParentElement();
            if (parentElem != null) {
                name = parentElem.getAttributeValue("name", ns);
            }
        } else {
            final Element parentElem = elem.getParentElement();
            if ((parentElem != null) && parentElem.getQualifiedName().equals("xa:redefines")) {
                final Element grandParentElem = parentElem.getParentElement();
                if (grandParentElem != null) {
                    name = grandParentElem.getAttributeValue("name", ns);
                }
            }
        }
        return name;
    }

    /**
     * Method used by getRedefinesElems() method to get redefines elements
     * 
     * @param elem
     *            Element
     * 
     * @return array with redefines elements
     */
    public Element[] getRedefinesFromGroup(final Element elem) {
        Element[] redefinesElems = null;
        final java.util.List childList = elem.getChildren("redefines", ns);
        redefinesElems = new Element[childList.size()];
        for (int i = 0; i < childList.size(); i++) {
            redefinesElems[i] = (Element) ((Element) childList.get(i)).clone();
        }
        return redefinesElems;
    }

    /**
     * To get target elements
     * 
     * @param node
     *            Selected node in target tree.
     * @param count
     *            number of redefines
     * 
     * @return element array
     */
    public Element[] getTargetElems(final MapTreeNode node, final int count) {
        final Element[] targetElems = new Element[count];
        final Element defElem = new Element("Element");
        for (int i = 0; i < count; i++) {
            targetElems[i] = (Element) defElem.clone();
        }
        if (node.myContent instanceof Element) {
            final MapTreeNode parentNode = (MapTreeNode) node.getParent();
            if (parentNode != null) {
                final int childIndex = parentNode.getIndex(node);
                final int childCount = parentNode.getChildCount();
                for (int i = childIndex, targetIndex = 0; (i < childCount) && (targetIndex < count); i++, targetIndex++) {
                    final Object obj = ((MapTreeNode) parentNode.getChildAt(i)).myContent;
                    if (obj instanceof Element) {
                        targetElems[targetIndex] = (Element) ((Element) obj).clone();
                    }
                }
            }
        }
        return targetElems;
    }

    /**
     * To set the field root element.
     * 
     * @param elem
     *            Element to be set as root.
     */
    public void setFieldRootElem(final Element elem) {
        fieldRootElem = (Element) elem.clone();
    }

    /**
     * To process occurs Element.
     * 
     * @param recElem
     *            Element with occurs attribute.
     * @param targetNode
     *            node on which drop is to be performed.
     * @param e
     *            DropTargetEvent instance.
     */
    public void processOccursElem(final Element recElem, final MapTreeNode targetNode, final DropTargetEvent e) {
        Element elem = null;
        final String name = recElem.getName();
        final String occurs = recElem.getAttributeValue("occurs", ns);
        elem = new Element(name);
        if (occurs != null) {
            elem.setAttribute("occurs", occurs, ns);
            elem.setAttribute("stop_when_true", "false", ns);
            final MapTreeNode occursNode = new MapTreeNode(elem);
            final java.util.List occursChildList = recElem.getChildren();
            for (int index = 0; index < occursChildList.size(); index++) {
                final Element occursChildElem = (Element) occursChildList.get(index);
                if (occursChildElem.getQualifiedName().equals("xa:redefines_group")) {
                    final Element[] redefinesElems = getRedefinesElems(occursChildElem);
                    final String redefName = getRedefineName(occursChildElem);
                    processRedefines("Map Redefined Fields Found Within Occurs", redefinesElems, redefName, occursChildElem, occursNode, e);
                } else {
                    processOccursElem(occursChildElem, occursNode, e);
                }
            }
            targetNode.add(occursNode);
        } else {
            if ((requestType != null) && requestType.equals(FileBizDriverInfo.FILE_READ) && (recordType != null) && recordType.equals(FileBizCompInfo.DELIMITED)) {
                final String numStr = name.substring("Field".length());
                elem.setText("%" + numStr + "%");
            } else {
                elem.setText("%" + name + "%");
            }
            final MapTreeNode node = new MapTreeNode(elem);
            targetNode.add(node);
        }
        if (MapperHelper.getCurrentMapperProcessor() != null) {
            MapperHelper.getCurrentMapperProcessor().rebuildMappingsHashTable();
        }
    }

    /**
     * To process redefines element.
     * 
     * @param title
     *            Titl for redefines screen.
     * @param redefinesElems
     *            array containing redefines elements.
     * @param name
     *            redefines element name.
     * @param transferElem
     *            dragnode.
     * @param targetNode
     *            dropnode.
     * @param e
     *            DroptargetEvent instance.
     */
    public void processRedefines(final String title, final Element[] redefinesElems, final String name, final Element transferElem, final MapTreeNode targetNode, final DropTargetEvent e) {
        final Element[] targetElems = getTargetElems(toDropOn, redefinesElems.length);
        final Shell parentShell = e.display.getActiveShell();
        final MapperProcessor currentProcessor = MapperHelper.getCurrentMapperProcessor();
        final CopybookRedefinesDlg dlg = new CopybookRedefinesDlg(parentShell, title, redefinesElems, targetElems, requestType, recordType, name, getInputParams(), (fieldRootElem == null) ? (Element) transferElem : fieldRootElem);
        final MapTreeNode tmpToDropOn = targetNode;
        MapperHelper.setCurrentMapperProcessor(currentProcessor);
        try {
            if (dlg.isOKClicked()) {
                try {
                    final Element switchElem = dlg.getSwitchElem();
                    final MapTreeNode switchNode = new MapTreeNode(switchElem);
                    tmpToDropOn.add(switchNode);
                    tmpToDropOn.resetChildren();
                    expandTreeNode(tmpToDropOn);
                    refreshTree(tmpToDropOn);
                    setSelectionPath(tmpToDropOn);
                    if (MapperHelper.getCurrentMapperProcessor() != null) {
                        MapperHelper.getCurrentMapperProcessor().rebuildMappingsHashTable();
                    }
                } catch (final Throwable ex) {
                    final String msg = logger.printStackTrace(ex);
                    logger.finest(msg);
                }
            }
        } catch (final Exception ex) {
            logger.finest("Exception mapping redefined field. Exception" + ex);
        }
    }

    /**
     * Sets value mapping to the target element.
     * 
     * @param oldValueMapping
     *            holds previous value mapping
     * @param seperator
     *            holds separator used to separate the mappings
     */
    protected void setValueMapping(final String oldValueMapping, final String separator) {
        toDropOn.isAutomapped = false;
        Element toDropOnElement = null;
        if (toDropOn.myContent instanceof Element) {
            toDropOnElement = (Element) toDropOn.myContent;
        } else if (toDropOn.myContent instanceof Attribute) {
            toDropOnElement = ((Attribute) toDropOn.myContent).getParent();
        }
        if (transferData instanceof MapTreeNode) {
            if (requestType.equals(FileBizDriverInfo.FILE_READ)) {
                final Element transferElem = (Element) ((MapTreeNode) transferData).myContent;
                if (recordType.equals(FileBizCompInfo.DELIMITED)) {
                    toDropOn.setPathValue(oldValueMapping + separator + "%" + transferElem.getName().substring(5) + "%");
                } else if (recordType.equals(FileBizCompInfo.FIXED_LENGTH)) {
                    toDropOn.setPathValue(oldValueMapping + separator + "%" + transferElem.getName() + "%");
                }
            } else if (requestType.equals(FileBizDriverInfo.FILE_WRITE) || requestType.equals(FileBizDriverInfo.FILE_APPEND)) {
                toDropOn.setPathValue(oldValueMapping + separator + "%" + ((MapTreeNode) transferData).getXAPathString(showPlusSign, showDoubleSlash, showDocRef(), replaceSourceRootWithDot) + "%");
                if (toDropOnElement != null) {
                    XMLNamespaceUtil.updateTargetElementNamespaces(toDropOnElement, ((MapTreeNode) transferData).myContent);
                }
            }
        } else if (transferData instanceof String) {
            toDropOn.setPathValue(oldValueMapping + separator + "%" + transferData.toString() + "%");
        }
        refreshTree(toDropOn);
    }

    @Override
    protected void performAppendMenuItemAction() {
        final String oldValueMapping = toDropOn.getPathValue();
        final String separator = UserPrefs.getMappedFieldSeparator();
        setValueMapping(oldValueMapping, separator);
        fireActionInvokedListener(TREE_MAPPING);
    }

    @Override
    protected void performReplaceMenuItemAction() {
        setValueMapping("", "");
        fireActionInvokedListener(TREE_MAPPING);
    }

    @Override
    protected void performCancelMenuItemAction() {
    }

    /**
     * @return Returns the requestType.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Returns the recordType.
     * 
     * @return Returns the recordType.
     */
    public String getRecordType() {
        return recordType;
    }
}
