package net.confex.schema.action;

import net.confex.schema.editor.ConfexDiagramEditor;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Contributes actions to the Editor
 * 
 * @author Phil Zoio
 */
public class SchemaActionBarContributor extends ActionBarContributor {

    FlyoutChangeLayoutAction changeLayoutAction;

    IEditorPart editor;

    protected void buildActions() {
        addRetargetAction(new UndoRetargetAction());
        addRetargetAction(new RedoRetargetAction());
        addRetargetAction(new DeleteRetargetAction());
        buildChangeLayoutAction();
        addAction(changeLayoutAction);
    }

    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(getAction(IWorkbenchActionConstants.UNDO));
        toolBarManager.add(getAction(IWorkbenchActionConstants.REDO));
        toolBarManager.add(changeLayoutAction);
        toolBarManager.add(new Separator());
        String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
        toolBarManager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
    }

    private void buildChangeLayoutAction() {
        changeLayoutAction = new FlyoutChangeLayoutAction(editor);
        changeLayoutAction.setToolTipText("2 Automatic Layout");
        changeLayoutAction.setId("net.confex.schema.action.ChangeLayoutAction");
        changeLayoutAction.setImageDescriptor(create("icons/", "layout.gif"));
        changeLayoutAction.setDisabledImageDescriptor(create("icons/", "layout_disabled.gif"));
    }

    public void setActiveEditor(IEditorPart editor) {
        this.editor = editor;
        changeLayoutAction.setActiveEditor(editor);
        super.setActiveEditor(editor);
    }

    protected void declareGlobalActionKeys() {
        addGlobalActionKey(IWorkbenchActionConstants.PRINT);
    }

    private static ImageDescriptor create(String iconPath, String name) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("net.confex.schema.schemaeditor", iconPath + name);
    }
}
