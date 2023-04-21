package org.xaware.ide.xadev.gui.actions;

import org.eclipse.swt.widgets.Display;
import org.jdom.Element;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.datamodel.ColumnMapTreeNode;
import org.xaware.ide.xadev.datamodel.XATreeNode;
import org.xaware.ide.xadev.gui.ColumnMapDNDTreeHandler;
import org.xaware.ide.xadev.gui.DNDTreeHandler;
import org.xaware.ide.xadev.gui.dialog.ColumnDefaultDlg;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;

/**
 * The class RemoveColumnDefaultAction used to remove action on db table
 * columns.
 *
 * @author Saritha
 * @author Bharath
 * @version 1.0
*/
public class RemoveColumnDefaultAction extends GlobalTreeEditAction {

    /** Holds instance of XA_Designer_Plugin translator */
    private static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Tree handler instance. */
    DNDTreeHandler theTree;

    /**
	 * Creates a new RemoveColumnDefaultAction object.
	 */
    RemoveColumnDefaultAction() {
        super();
        setText(translator.getString("Remove Default Value..."));
        setToolTipText(translator.getString("Remove default value for column (set null value for default)..."));
    }

    /**
	 * used to remove default value action on db column action.
	 */
    @Override
    public void run() {
        ColumnDefaultDlg dlg = null;
        try {
            XATreeNode selNode = null;
            theTree = getTree();
            if (theTree instanceof ColumnMapDNDTreeHandler) {
                selNode = theTree.getSelectedNode();
                if (((ColumnMapTreeNode) selNode).myContent instanceof Element) {
                    final Element selElem = (Element) ((ColumnMapTreeNode) selNode).myContent;
                    dlg = new ColumnDefaultDlg(Display.getCurrent().getActiveShell(), translator.getString("Remove Default Value"), translator.getString("Remove default value for all columns"), selElem);
                    dlg.setName(((Element) ((ColumnMapTreeNode) selNode).myContent).getName());
                    String val = ((Element) ((ColumnMapTreeNode) selNode).myContent).getAttributeValue("default", XAwareConstants.xaNamespace);
                    if (val == null) {
                        val = "";
                    }
                    dlg.setValue(val);
                    dlg.setValueEnabled(false);
                    dlg.show();
                    if (dlg.isOkClicked()) {
                        if (((ColumnMapTreeNode) selNode).myContent instanceof Element) {
                            boolean isApplyAll = dlg.isApplyAll();
                            if (!isApplyAll) {
                                ((Element) ((ColumnMapTreeNode) selNode).myContent).removeAttribute("default", XAwareConstants.xaNamespace);
                                ((ColumnMapTreeNode) selNode).resetChildren();
                                theTree.refreshTree();
                            } else {
                                final ColumnMapTreeNode rootNode = (ColumnMapTreeNode) theTree.getRoot();
                                final int count = rootNode.getChildCount();
                                for (int i = 0; i < count; i++) {
                                    final ColumnMapTreeNode node = (ColumnMapTreeNode) rootNode.getChildAt(i);
                                    ((Element) (node).myContent).removeAttribute("default", XAwareConstants.xaNamespace);
                                    (node).resetChildren();
                                    theTree.refreshTree();
                                }
                            }
                        }
                    }
                }
            }
        } catch (final Throwable ex) {
            ControlFactory.showInfoDialog(translator.getString("Error removing default value."), ex.toString());
        }
    }
}
