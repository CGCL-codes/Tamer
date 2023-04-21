package com.life.audiotageditor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import com.life.audiotageditor.model.AudioFolder;
import com.life.audiotageditor.model.AudioModel;
import com.life.audiotageditor.model.AudioModelManager;
import com.life.audiotageditor.utils.StringUtil;
import com.life.audiotageditor.views.AudioView;

public class OpenFolderHandler extends AbstractHandler {

    /**
	 * The constructor.
	 */
    public OpenFolderHandler() {
    }

    /**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        String path = openFolderDialog(window);
        if (path == null || path.isEmpty()) {
            return null;
        }
        AudioView audioView = (AudioView) window.getActivePage().findView(AudioView.ID);
        AudioModel audioModel = (AudioModel) AudioModelManager.instance().getRoot();
        AudioFolder audioFolder = (AudioFolder) audioModel.getFolder(StringUtil.formatPath(path));
        audioView.getTreeViewer().setInput(audioFolder);
        audioView.getTreeViewer().setSelection(new StructuredSelection(audioFolder.members()[0]), true);
        return null;
    }

    private String openFolderDialog(IWorkbenchWindow window) {
        DirectoryDialog dialog = new DirectoryDialog(window.getShell(), SWT.OPEN);
        dialog.setText(Messages.OpenFolderHandler_dialog_text);
        return dialog.open();
    }
}
