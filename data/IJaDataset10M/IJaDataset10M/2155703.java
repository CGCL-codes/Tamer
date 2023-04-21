package com.safi.workshop.sqlexplorer.sqleditor.actions;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartConstants;
import com.safi.workshop.sqlexplorer.Messages;
import com.safi.workshop.sqlexplorer.plugin.editors.SQLEditor;
import com.safi.workshop.sqlexplorer.util.ImageUtil;

/**
 * Executes SQL in response to clicking the toolbar or the key mapping
 * 
 * @modified John Spackman
 * 
 */
public class SaveEditorAction extends AbstractEditorAction implements IPropertyListener {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.Save");

    public SaveEditorAction(SQLEditor editor) {
        super(editor);
        editor.addPropertyListener(this);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    @Override
    public String getText() {
        return Messages.getString("SQLEditor.Actions.SaveEditor");
    }

    @Override
    public String getToolTipText() {
        return Messages.getString("SQLEditor.Actions.SaveEditor.ToolTip");
    }

    @Override
    public void run() {
        try {
            _editor.doSave(new NullProgressMonitor());
        } catch (final Exception e) {
            e.printStackTrace();
            _editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    MessageDialog.openError(_editor.getSite().getShell(), Messages.getString("SQLResultsView.Error.Title"), e.getClass().getCanonicalName() + ": " + e.getMessage());
                }
            });
        }
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !isDisabled();
    }

    @Override
    public void propertyChanged(Object source, int propId) {
        if (propId == IWorkbenchPartConstants.PROP_DIRTY) {
            setEnabled(true);
        }
    }
}
