package org.xaware.ide.xadev.richui.editor.page.helper;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.PopupController;
import org.xaware.ide.xadev.gui.editor.IXAwareEditor;
import org.xaware.ide.xadev.gui.editor.TreeEditorPanel;
import org.xaware.ide.xadev.gui.model.DocumentModel;

/**
 * Helper class for Design View Page.
 * 
 * @author Vasu Thadaka
 * */
public class DesignViewPageHelper {

    /**
     * Constructs the tree view
     * 
     * @return
     * @throws PartInitException
     */
    public static TreeEditorPanel createTreeViewer(boolean showExecutionResults, DocumentModel documentModel, MultiPageEditorPart editor, Composite container) throws PartInitException {
        TreeEditorPanel treeEditorPanel = new TreeEditorPanel(editor, documentModel);
        treeEditorPanel.setupTreeHandler(container);
        treeEditorPanel.getTree().expandTree(UserPrefs.getTreeExpanionLevel());
        final XMLTreeNode nextRoot = (XMLTreeNode) treeEditorPanel.getTree().getRoot();
        treeEditorPanel.getTree().setSelectionPath(nextRoot);
        treeEditorPanel.setPopupController(new PopupController((IXAwareEditor) editor));
        return treeEditorPanel;
    }
}
