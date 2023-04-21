package org.eclipse.wst.xml.security.core.decrypt;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;

/**
 * <p>This class prepares and adds all wizard pages to the wizard and launches the <i>XML Decryption
 * Wizard</i> afterwards.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewDecryptionWizard extends Wizard implements INewWizard {

    /** PageAlgorithms first wizard page. */
    private PageAlgorithms pageAlgorithms = null;

    /** XML document to decrypt. */
    private IFile xmlDocument;

    /** The Decryption model. */
    private Decryption decryption;

    /** Path of the openend project. */
    private String path;

    /**
     * Constructor for the wizard launcher.
     */
    public NewDecryptionWizard() {
        super();
        decryption = new Decryption();
        setWindowTitle(Messages.decryptionWizard);
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(XmlSecurityPlugin.getId(), "icons/wiz_dec.gif");
        setDefaultPageImageDescriptor(image);
        setNeedsProgressMonitor(true);
    }

    /**
     * Initializes the wizard with a text selection.
     *
     * @param workbench The workbench
     * @param selection The text selection
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }

    /**
     * Initializes the wizard with a selected file.
     *
     * @param project The opened project
     * @param file The selected IFile
     */
    public void init(final IProject project, final IFile file) {
        xmlDocument = file;
        path = project.getLocation().toOSString();
    }

    /**
     * Adds a single page (<code>PageAlgorithms</code>) to the wizard.
     */
    public void addPages() {
        pageAlgorithms = new PageAlgorithms(decryption, xmlDocument, path);
        addPage(pageAlgorithms);
    }

    /**
     * Checks the currently active wizard page. Only the first wizard page can successfully generate
     * a decryption.
     *
     * @return Wizard completion status
     */
    public boolean canFinish() {
        return pageAlgorithms.isPageComplete();
    }

    /**
     * Finishes the wizard.
     *
     * @return true
     */
    public boolean performFinish() {
        return true;
    }

    /**
     * Returns the Encryption Wizard model.
     *
     * @return The model
     */
    public Decryption getModel() {
        return decryption;
    }
}
