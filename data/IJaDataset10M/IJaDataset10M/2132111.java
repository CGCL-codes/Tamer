package org.globaltester.testmanager.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.globaltester.testmanager.Activator;

/**
 * Wizard to create new GlobalTester Test Suite.
 * 
 * @version		Release 2.2.0
 * @author 		Holger Funke
 *
 */
public class NewTestSuiteWizardMainpage extends WizardNewFileCreationPage {

    private static final String EXTENSION = "xml";

    public NewTestSuiteWizardMainpage(String pageName, IStructuredSelection selection) {
        super(pageName, selection);
    }

    protected IFile createFileHandle(IPath filePath) {
        String ext = filePath.getFileExtension();
        if (!EXTENSION.equalsIgnoreCase(ext)) {
            filePath = filePath.addFileExtension(EXTENSION);
        }
        return super.createFileHandle(filePath);
    }

    protected InputStream getInitialContents() {
        IPath pluginDir = Activator.getPluginDir();
        String templatePath = pluginDir.toPortableString() + "templates/testsuite_template.xml";
        File file = new File(templatePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }
}
