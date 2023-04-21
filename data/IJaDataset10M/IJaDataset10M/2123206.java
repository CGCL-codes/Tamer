package org.xaware.ide.xadev.gui.actions;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.datamodel.JDOMContent;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.EditorTreeHandler;
import org.xaware.ide.xadev.gui.dialog.EditElementDlg;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Class for Apply Edit Node Action on Selected Element
 *
 * @author Chandresh Gandhi
 * @author GSVSN Murthy
 * @version 4.3
 */
public class EditElementAction extends GlobalTreeEditAction implements IEditElementAction {

    /** Namespace */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** XAwareLogger instance. */
    public static XAwareLogger logger = XAwareLogger.getXAwareLogger(EditElementAction.class.getName());

    /** classname */
    public static String classname = "EditElementAction";

    /** Translator */
    protected Translator translator = XA_Designer_Plugin.getTranslator();

    /**
     * Creates a new EditElementAction object.
     */
    public EditElementAction() {
        super();
        setText(translator.getString("Edit Element..."));
        setToolTipText(translator.getString("Edit Element"));
    }

    /**
     * opens the editor in the edit tab pane.
     */
    @Override
    public void run() {
        final String methodName = "run";
        try {
            final EditorTreeHandler theTree = XA_Designer_Plugin.getActiveEditedInternalFrame().getTreeHandler();
            final XMLTreeNode selNode = getSelectedNode(theTree);
            if (selNode != null) {
                final JDOMContent jdomc = selNode.getJDOMContent();
                if (jdomc.getContent() instanceof Element) {
                    final Element elem = (Element) jdomc.getContent();
                    processSelectedNode(elem, theTree);
                } else if (jdomc.getContent() instanceof Attribute) {
                    final Element elem = ((Attribute) jdomc.getContent()).getParent();
                    processSelectedNode(elem, theTree);
                }
                XA_Designer_Plugin.getActiveEditedInternalFrame().setFocus();
            } else {
                ControlFactory.showMessageDialog(translator.getString("Please select an element to edit from the document."), translator.getString("Information"));
                return;
            }
        } catch (final XAwareException xaException) {
            logger.fine(xaException.getMessage(), classname, methodName);
            xaException.printStackTrace();
        } catch (final Exception exception) {
            logger.fine(exception.getMessage(), classname, methodName);
            exception.printStackTrace();
        }
    }

    /**
     * Return the selected node or throw an XAwareException.  The exception 
     * is used to break out of normal processing.  This method will handle
     * all Error message dialogs.
     * 
     * @param theTree
     * @return
     * @throws XAwareException
     */
    public XMLTreeNode getSelectedNode(final EditorTreeHandler theTree) throws XAwareException {
        if (theTree == null) {
            ControlFactory.showMessageDialog(translator.getString("Please select an element to edit."), translator.getString("Information"));
            throw new XAwareException("No Element selected");
        }
        final XMLTreeNode selNode = (XMLTreeNode) theTree.getSelectedNode();
        if (selNode == null) {
            ControlFactory.showMessageDialog(translator.getString("Please select an element to edit."), translator.getString("Information"));
            throw new XAwareException("No Element selected");
        }
        return selNode;
    }

    /**
     * Default edit elment will pop the base edit tab dialog
     * @param elem
     */
    protected void processSelectedNode(final Element elem, final EditorTreeHandler theTree) {
        final EditElementDlg editElemDlg = new EditElementDlg(XA_Designer_Plugin.getShell(), XA_Designer_Plugin.getActiveEditedInternalFrame());
        editElemDlg.open();
    }

    /**
     * Return true if the selected node can be edited with this action
     * @return
     */
    public boolean isSpecialEditAction(final Element elem) {
        return false;
    }
}
