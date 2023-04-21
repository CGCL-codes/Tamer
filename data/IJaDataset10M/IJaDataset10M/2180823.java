package org.xaware.ide.xadev.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.StringUtil;
import org.xaware.ide.xadev.common.XMLUtil;
import org.xaware.ide.xadev.datamodel.DefaultMutableTreeNode;
import org.xaware.ide.xadev.datamodel.JDOMContent;
import org.xaware.ide.xadev.datamodel.JDOMContentFactory;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.DNDTreeHandler;
import org.xaware.ide.xadev.gui.editor.XAInternalFrame;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class is used to paste text from the clipboard as XML on the selected node.
 * 
 * @version 1.0
 */
public class PasteXmlAction extends Action {

    /** Xaware Logger. */
    private final XAwareLogger logger = XAwareLogger.getXAwareLogger(PasteXmlAction.class.getName());

    /** Stores DNDTreeHandler instance of BizRefEditor */
    protected DNDTreeHandler theTreeHandler;

    /** Stores DNDTreeHandler instance of BizRefEditor */
    protected String actionType;

    public static final String PASTE_BEFORE = "Before";

    public static final String PASTE_AFTER = "After";

    public static final String PASTE_AS_CHILD = "As Child";

    protected String errMsg = "Invalid XML for paste action.";

    private PasteXmlAction() {
    }

    /**
     * Creates a new PasteNodeAction object.
     */
    public PasteXmlAction(final String menuText) {
        super();
        actionType = menuText;
        setText("Paste " + menuText);
        setToolTipText("Paste " + menuText);
        if (PASTE_AS_CHILD.equals(menuText)) {
            setActionDefinitionId("org.eclipse.ui.edit.paste");
            setText("Paste");
        }
        try {
            setImageDescriptor(UserPrefs.getImageDescriptorIconFor(UserPrefs.PASTE));
        } catch (final Exception e) {
            setImageDescriptor(UserPrefs.getImageDescriptorIconFor(UserPrefs.XA_DESIGNER));
            logger.fine("Throwable caught creating ImageIcon : " + UserPrefs.PASTE + " " + e);
        }
    }

    /**
     * Opens the file dialog with which user can select XML file.
     */
    @Override
    public void run() {
        theTreeHandler = XA_Designer_Plugin.getActiveEditedInternalFrame().getTreeHandler();
        try {
            final Control control = Display.getDefault().getFocusControl();
            if (control instanceof Text) {
                final String plainText = (String) XA_Designer_Plugin.getClipboard().getContents(TextTransfer.getInstance());
                if (plainText != null) {
                    final Text text = (Text) control;
                    text.insert(StringUtil.removeSlashesFromNodeVal(plainText, true));
                    text.forceFocus();
                } else {
                    super.run();
                }
            } else if (control instanceof StyledText) {
                ((StyledText) control).paste();
            } else if (control instanceof Combo) {
                ((Combo) control).paste();
            } else {
                final XMLTreeNode selNode = validateAction();
                if (selNode != null) {
                    final String xml = getClipboardContents().trim();
                    final Element tempElem = JDOMContentFactory.createElementFromXML(xml);
                    final List newNodes = tempElem.getContent();
                    final int size = newNodes.size();
                    final boolean isPasteMultiple = size > 1;
                    for (int i = 0; i < size; i++) {
                        JDOMContent jdomc = JDOMContentFactory.createJDOMContent(newNodes.get(i));
                        XMLTreeNode toInsert = new XMLTreeNode(jdomc);
                        if (jdomc.getType() == JDOMContent.STRING && i == 0) {
                            jdomc = getAttributeContent(selNode);
                            toInsert = new XMLTreeNode(jdomc);
                            if (PASTE_AS_CHILD.equals(actionType)) {
                                if (jdomc.getType() != JDOMContent.STRING) {
                                    pasteAttribute(jdomc, selNode, toInsert, theTreeHandler);
                                } else {
                                    ControlFactory.showMessageDialog(errMsg, "Information");
                                }
                            } else {
                                ControlFactory.showMessageDialog("Cannot paste attribute before or after node.", "Information");
                            }
                        } else {
                            if (jdomc.getType() != JDOMContent.STRING) {
                                processInsertElementAction(selNode, toInsert, isPasteMultiple);
                            }
                        }
                    }
                }
            }
        } catch (final JDOMException e) {
            errMsg = errMsg + "Exception: " + e.getMessage();
            ControlFactory.showMessageDialog(errMsg, "Information");
        } catch (final IOException e) {
            errMsg = errMsg + "Exception: " + e.getMessage();
            ControlFactory.showMessageDialog(errMsg, "Information");
        }
    }

    private JDOMContent getAttributeContent(final DefaultMutableTreeNode selNode) throws JDOMException, IOException {
        final String xml = XA_Designer_Plugin.getClipboard().getContents(TextTransfer.getInstance()).toString().trim();
        JDOMContent jdomc = JDOMContentFactory.createInstanceFromXML(xml);
        XMLTreeNode toInsert = new XMLTreeNode(jdomc);
        if (jdomc.getContent() instanceof String) {
            toInsert = XMLUtil.getAttributeNode(toInsert);
            if (toInsert == null) {
                throw new JDOMException("Unable to create attribute from text: " + xml);
            }
            toInsert.setParent(selNode);
            jdomc = toInsert.getJDOMContent();
        }
        return jdomc;
    }

    /**
     * get the selected node and return it if it is valid for the selected action. otherwise return null
     * 
     * @return
     */
    protected XMLTreeNode validateAction() {
        XMLTreeNode selNode = null;
        if (theTreeHandler == null) {
            ControlFactory.showMessageDialog("Please select an element to insert child element from XML file.", "Information");
            return null;
        }
        selNode = (XMLTreeNode) theTreeHandler.getSelectedNode();
        if (selNode == null) {
            ControlFactory.showMessageDialog("Please select an element under which you want to add another element.", "Information");
            return null;
        } else if ((selNode.getParent() == null) && (PASTE_AFTER.equals(actionType) || PASTE_BEFORE.equals(actionType))) {
            ControlFactory.showMessageDialog("Cannot insert before or after root node.", "Information");
            selNode = null;
        } else if (selNode.getJDOMContent().getContent() instanceof Attribute) {
            ControlFactory.showMessageDialog("Cannot insert node under attribute node.", "Information");
            selNode = null;
        }
        return selNode;
    }

    /**
     * Insert the toInsert into the tree based on selNode, handle the namespace issues and undo/redo processing.
     * 
     * @param selNode
     * @param toInsert
     * @return TODO
     */
    protected boolean processInsertElementAction(final XMLTreeNode selNode, final XMLTreeNode toInsert, final boolean isPasteMultiple) {
        try {
            final JDOMContent jdomc = toInsert.getJDOMContent();
            boolean operationStatus = false;
            if (selNode != null) {
                if (PASTE_AFTER.equals(actionType)) {
                    operationStatus = theTreeHandler.addBelowSelectedNode(selNode, toInsert);
                } else if (PASTE_BEFORE.equals(actionType)) {
                    operationStatus = theTreeHandler.addAboveSelectedNode(selNode, toInsert);
                } else if (PASTE_AS_CHILD.equals(actionType)) {
                    operationStatus = theTreeHandler.addUnderSelected(selNode, toInsert);
                }
            }
            if (!operationStatus) {
                return false;
            }
            theTreeHandler.expandTreeNodeToAllLevels(toInsert);
            theTreeHandler.setSelectionPath(selNode);
            if (isPasteMultiple == false) {
                theTreeHandler.setSelectionPath(toInsert);
            }
            if (jdomc.getType() == JDOMContent.ELEMENT) {
                final Element elem = (Element) jdomc.getContent();
                if (!XAInternalFrame.isDefaultNamespaceDefined(elem)) {
                    final Namespace defaultNamespace = XAInternalFrame.getDefaultNamespaceOfParent(elem);
                    if (defaultNamespace != null) {
                        XAInternalFrame.setDefaultNamespace(elem, defaultNamespace);
                    }
                }
            }
            final XMLTreeNode parent = (XMLTreeNode) toInsert.getParent();
            int index = 0;
            UndoableInfoEdit uie = null;
            short type = -1;
            if (PASTE_AFTER.equals(actionType) || PASTE_BEFORE.equals(actionType)) {
                type = UndoableInfoEdit.TYPE_INSERT_ELEMENT;
            } else {
                type = UndoableInfoEdit.TYPE_INSERT;
            }
            index = parent.getIndex(toInsert);
            uie = new UndoableInfoEdit(theTreeHandler, type, null, -1, parent, index, toInsert, false);
            uie.addContext(XA_Designer_Plugin.getActiveEditedInternalFrame().getUndoContext());
            XA_Designer_Plugin.getDefault().getWorkbench().getOperationSupport().getOperationHistory().add(uie);
        } catch (final Exception e) {
            logger.warning("Exception occurred inserting XML into Biz Doc : " + e);
            logger.printStackTrace(e);
            errMsg = errMsg + "Exception: " + e.getMessage();
            ControlFactory.showMessageDialog(errMsg, "Information");
            return false;
        }
        return true;
    }

    /**
     * Get the String residing on the clipboard.
     * 
     * @return any text found on the Clipboard; if none found, return an empty
     *         String.
     */
    public String getClipboardContents() {
        String result = "";
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable contents = clipboard.getContents(null);
        final boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (final UnsupportedFlavorException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            } catch (final IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    protected void pasteAttribute(final JDOMContent jdomc, final XMLTreeNode selNode, final XMLTreeNode toInsert, final DNDTreeHandler theTree) {
        final Attribute attribute = (Attribute) jdomc.getContent();
        final List attrList = ((Element) selNode.getJDOMContent().getContent()).getAttributes();
        int index = ((Element) selNode.getJDOMContent().getContent()).getAttributes().size() - 1;
        if (index < 0) {
            index = 0;
        }
        for (int i = 0; i < attrList.size(); i++) {
            final Attribute attrObj = (Attribute) attrList.get(i);
            if (attrObj.getNamespace().equals(attribute.getNamespace()) && attrObj.getName().equals(attribute.getName())) {
                final int result = ControlFactory.showConfirmDialog("Attribute exists, overwrite?", "Information", false, MessageDialog.WARNING);
                if (result == 0) {
                    selNode.add(toInsert);
                    theTree.refreshTree(selNode);
                    final UndoableInfoEdit undoableInfoEdit = new UndoableInfoEdit(theTree, UndoableInfoEdit.TYPE_PASTE, null, index, selNode, index, toInsert, false);
                    undoableInfoEdit.addContext(XA_Designer_Plugin.getActiveEditedInternalFrame().getUndoContext());
                    XA_Designer_Plugin.getDefault().getWorkbench().getOperationSupport().getOperationHistory().add(undoableInfoEdit);
                }
                return;
            }
        }
        selNode.add(toInsert);
        theTree.refreshTree(selNode);
        index = ((Element) selNode.getJDOMContent().getContent()).getAttributes().size() - 1;
        if (index < 0) {
            index = 0;
        }
        final UndoableInfoEdit uie = new UndoableInfoEdit(theTree, UndoableInfoEdit.TYPE_PASTE, selNode, index, selNode, index, toInsert, false);
        uie.addContext(XA_Designer_Plugin.getActiveEditedInternalFrame().getUndoContext());
        XA_Designer_Plugin.getDefault().getWorkbench().getOperationSupport().getOperationHistory().add(uie);
        return;
    }
}
