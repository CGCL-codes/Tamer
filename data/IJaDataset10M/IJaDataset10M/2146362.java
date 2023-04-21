package publications.presentation.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import publications.Publication;

public class OpenWikiAction extends Action {

    private Publication publication;

    private IFolder literatureFolder = ResourcesPlugin.getWorkspace().getRoot().getProject("bibliography").getFolder("literature");

    public OpenWikiAction() {
        setText("open Wiki notes");
        setEnabled(false);
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        if (publication == this.publication) {
            return;
        }
        this.publication = publication;
        setEnabled(publication != null && publication.getLocalKey() != null && !"".equals(publication.getLocalKey()) && getPdf().exists());
    }

    private IFile getPdf() {
        return literatureFolder.getFile(publication.getLocalKey() + ".wiki");
    }

    @Override
    public void run() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            IDE.openEditor(page, getPdf());
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
