package org.jazzteam.snipple.plugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PlatformUI;
import org.jazzteam.snipple.plugin.SnipplePluginActivator;
import org.jazzteam.snipple.plugin.dialogs.AddSnippetDialog;
import org.jazzteam.snipple.plugin.exceptions.localstorage.LocalStorageException;
import org.jazzteam.snipple.plugin.exceptions.remotestorage.RemoteStorageException;
import org.jazzteam.snipple.plugin.model.Category;
import org.jazzteam.snipple.plugin.model.Snippet;
import org.jazzteam.snipple.plugin.preferences.PreferenceConstants;
import org.jazzteam.snipple.plugin.storage.local.FileSystemLocalStorage;
import org.jazzteam.snipple.plugin.storage.local.ILocalStorage;
import org.jazzteam.snipple.plugin.storage.local.LocalSnippetsMemoryStorage;
import org.jazzteam.snipple.plugin.storage.remote.IRemoteStorage;
import org.jazzteam.snipple.plugin.storage.remote.WebServicesRemoteStorage;
import org.jazzteam.snipple.plugin.views.local.LocalSnippetsView;

/**
 * Action to add snippet to local (allows to add it to remote snippets too)
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
public class AddToSnippetsAction implements IEditorActionDelegate {

    public LocalSnippetsMemoryStorage storage;

    IPreferenceStore preferences;

    public AddToSnippetsAction() {
        storage = LocalSnippetsMemoryStorage.getInstance();
        preferences = SnipplePluginActivator.getDefault().getPreferenceStore();
    }

    /**
	 * Code, which runs when action executed
	 */
    @Override
    public void run(IAction action) {
        String[] cathegoryNames = new String[storage.getCategories().size()];
        for (int i = 0; i < storage.getCategories().size(); i++) {
            cathegoryNames[i] = storage.getCategories().get(i).getName();
        }
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        AddSnippetDialog dialog = new AddSnippetDialog(shell);
        dialog.setCategories(cathegoryNames);
        if (dialog.open() == InputDialog.OK) {
            String baseDir = preferences.getString(PreferenceConstants.P_PATH);
            ILocalStorage localStorage = new FileSystemLocalStorage(baseDir);
            Snippet snippet = new Snippet();
            String snippetName = dialog.getSnippetName();
            String snippetDescription = dialog.getSnippetDescription();
            String snippetAuthor = dialog.getSnippetAuthor();
            String snippetCreationDate = dialog.getSnippetCreationDate();
            int cathegoryNum = dialog.getSnippetCategoryNum();
            snippet.setName(snippetName);
            snippet.setDescription(snippetDescription);
            snippet.setAuthor(snippetAuthor);
            snippet.setCreationDate(snippetCreationDate);
            snippet.setRating(0);
            snippet.setId(0);
            String selectedText = getSelectedEditorCode();
            snippet.setSource(selectedText);
            Category cath = storage.getCategories().get(cathegoryNum);
            try {
                localStorage.addSnippet(cath, snippet);
            } catch (LocalStorageException e1) {
                e1.printStackTrace();
            }
            storage.addSnippet(cath, snippet);
            if (dialog.isLoadToServer()) {
                String uri = preferences.getString(PreferenceConstants.P_SERVER_URI);
                String login = preferences.getString(PreferenceConstants.P_LOGIN);
                String password = preferences.getString(PreferenceConstants.P_PASSWORD);
                IRemoteStorage remoteStorage = new WebServicesRemoteStorage(uri, login, password);
                try {
                    remoteStorage.addSnippet(snippet);
                } catch (RemoteStorageException e) {
                    e.printStackTrace();
                }
            }
            LocalSnippetsView.getViewer().refresh();
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

    @Override
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
    }

    /**
	 * Getting selected source code fragment in the editor
	 * 
	 * @return
	 * @version 1
	 */
    public String getSelectedEditorCode() {
        IEditorPart editorPart = SnipplePluginActivator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        IEditorSite iEditorSite = editorPart.getEditorSite();
        ISelectionProvider selectionProvider = iEditorSite.getSelectionProvider();
        ISelection iSelection = selectionProvider.getSelection();
        return ((ITextSelection) iSelection).getText();
    }
}
