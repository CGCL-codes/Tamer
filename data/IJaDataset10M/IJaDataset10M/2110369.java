package net.sourceforge.dalutils4j.eclipse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class DialogNewDaoXmlFile extends WizardDialog {

    public DialogNewDaoXmlFile(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
    }

    public static IFile open(Shell parentShell, IJavaProject javaProject, String xmlRoot) {
        NewFileWizard wiz = new NewFileWizard(javaProject, xmlRoot);
        IStructuredSelection selection1 = new StructuredSelection(javaProject);
        wiz.setWindowTitle("New File");
        wiz.init(null, selection1);
        DialogNewDaoXmlFile dialog = new DialogNewDaoXmlFile(parentShell, wiz);
        dialog.create();
        dialog.setHelpAvailable(false);
        int res = dialog.open();
        if (res == WizardDialog.OK) {
            return wiz.getNewfile();
        }
        return null;
    }

    public static class NewFileWizard extends Wizard implements INewWizard {

        private IJavaProject javaProject;

        private String xmlRoot;

        private IFile newfile = null;

        NewFileWizard(IJavaProject javaProject, String xmlRoot) {
            this.javaProject = javaProject;
            this.xmlRoot = xmlRoot;
        }

        public class Page extends WizardPageNewDaoXmlFile {

            public Page(IStructuredSelection selection) {
                super(Page.class.getName());
            }

            @Override
            public void createControl(Composite parent) {
                super.createControl(parent);
                setTitle("Create DAO XML file");
                setDescription("Enabled file name patterns: 'dao.*.xml' | '*.dao.xml'");
                try {
                    init(javaProject.getProject(), xmlRoot);
                    setResource("class.dao.xml");
                } catch (Exception e) {
                    InternalHelpers.showError(e);
                }
            }

            @Override
            protected InputStream getInitialContents() {
                try {
                    String xml = InternalHelpers.readFromJARFile(InternalHelpers.EMPTY_DAO_XML);
                    InputStream is = new ByteArrayInputStream(xml.getBytes());
                    return is;
                } catch (Exception e) {
                    return null;
                }
            }
        }

        private IStructuredSelection selection;

        private Page page;

        @Override
        public void addPages() {
            page = new Page(selection);
            addPage(page);
        }

        @Override
        public boolean canFinish() {
            boolean res = super.canFinish();
            if (!res) {
                return false;
            }
            String name = page.getResource();
            res = (name.startsWith("dao.") && name.endsWith(".xml") || name.endsWith(".dao.xml"));
            return res;
        }

        @Override
        public boolean performFinish() {
            newfile = page.createNewFile();
            if (newfile != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void init(IWorkbench workbench, IStructuredSelection selection) {
            this.selection = selection;
        }

        public IFile getNewfile() {
            return newfile;
        }
    }
}
