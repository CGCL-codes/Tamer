package org.isi.monet.modelling.core.wizards;

import java.sql.SQLException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.isi.monet.modelling.core.utils.SQLProvider;
import org.isi.monet.modelling.core.wizards.pages.DocumentPage;

public class NewDocumentFile extends Wizard implements INewWizard {

    private IWorkbench _workbench;

    private IStructuredSelection _selection;

    private DocumentPage _pageOne;

    public NewDocumentFile() {
    }

    @Override
    public boolean performFinish() {
        boolean result = false;
        IFile file = _pageOne.createNewFile();
        result = file != null;
        if (result) {
            try {
                IDE.openEditor(_workbench.getActiveWorkbenchWindow().getActivePage(), file);
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
        String idDefinition = _pageOne.getCode();
        String path = file.getProjectRelativePath().toString();
        try {
            SQLProvider provider = SQLProvider.getSqlProvider(file.getProject().getName());
            provider.insertDefinition(idDefinition, path);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        _workbench = workbench;
        _selection = selection;
    }

    @Override
    public void addPages() {
        super.addPages();
        _pageOne = new DocumentPage(_selection);
        addPage(_pageOne);
    }
}
