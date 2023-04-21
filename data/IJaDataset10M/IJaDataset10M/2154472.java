package org.eclipse.wst.xml.security.core.encrypt;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;

/**
 * <p>This class prepares and adds all wizard pages to the wizard and launches the <i>XML Encryption
 * Wizard</i> afterwards.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewEncryptionWizard extends Wizard implements INewWizard {

    /** PageResource first wizard page. */
    private PageResource pageResource = null;

    /** PageUseKey second default wizard page. */
    private PageOpenKey pageOpenKey = null;

    /** PageCreateKey second alternative wizard page. */
    private PageCreateKey pageCreateKey = null;

    /** PageCreateKeystore second alternative wizard page. */
    private PageCreateKeystore pageCreateKeystore = null;

    /** PageAlgorithms third wizard page. */
    private PageAlgorithms pageAlgorithms = null;

    /** The XML document to encrypt. */
    private IFile xmlDocument;

    /** The text selection in the editor. */
    private ITextSelection xmlSelection;

    /** The Encryption model. */
    private Encryption encryption;

    /** Path of the opened project. */
    private String path;

    /** Name of the opened project. */
    private String name;

    /**
     * Constructor for the wizard launcher.
     */
    public NewEncryptionWizard() {
        super();
        encryption = new Encryption();
        setWindowTitle(Messages.encryptionWizard);
        setDialogSettings(getEncryptionWizardSettings());
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(XmlSecurityPlugin.getId(), "icons/wiz_enc.gif");
        setDefaultPageImageDescriptor(image);
        setNeedsProgressMonitor(true);
    }

    /**
     * Return the settings used for all Encryption Wizard pages.
     *
     * @return The IDialogSettings for the Encryption Wizard
     */
    private IDialogSettings getEncryptionWizardSettings() {
        IDialogSettings workbenchSettings = XmlSecurityPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("EncryptionWizard");
        if (section == null) {
            section = workbenchSettings.addNewSection("EncryptionWizard");
        }
        return section;
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
        init(project, file, null);
    }

    /**
     * Initializes the wizard with a selected file and a text selection.
     *
     * @param project The opened project
     * @param file The selected file
     * @param textSelection The text selection
     */
    public void init(final IProject project, final IFile file, final ITextSelection textSelection) {
        xmlDocument = file;
        xmlSelection = textSelection;
        path = project.getLocation().toOSString();
        name = project.getName();
    }

    /**
     * Adds the five pages (<code>PageResource</code>,
     * <code>PageOpenKey</code>, <code>PageCreateKey</code>,
     * <code>PageCreateKeystore</code> and <code>PageAlgorithms</code>)
     * to the wizard.
     */
    public void addPages() {
        if (xmlSelection == null) {
            pageResource = new PageResource(encryption, xmlDocument, path, false);
        } else {
            pageResource = new PageResource(encryption, xmlDocument, path, true);
        }
        addPage(pageResource);
        pageOpenKey = new PageOpenKey(encryption, path);
        addPage(pageOpenKey);
        pageCreateKey = new PageCreateKey(encryption, path);
        addPage(pageCreateKey);
        pageCreateKeystore = new PageCreateKeystore(encryption, path, name);
        addPage(pageCreateKeystore);
        pageAlgorithms = new PageAlgorithms(encryption, xmlDocument);
        addPage(pageAlgorithms);
    }

    /**
     * Checks the currently active wizard page. It is impossible to finish the <i>Encryption
     * Wizard</i> from the first page. Only the second wizard page can successfully generate an
     * encryption.
     *
     * @return Wizard completion status
     */
    public boolean canFinish() {
        if (this.getContainer().getCurrentPage() != pageAlgorithms) {
            return false;
        }
        return pageAlgorithms.isPageComplete();
    }

    /**
     * Finishes the wizard.
     *
     * @return Finishing status
     */
    public boolean performFinish() {
        pageResource.storeSettings();
        pageOpenKey.storeSettings();
        pageAlgorithms.storeSettings();
        return pageAlgorithms.performFinish();
    }

    /**
     * Returns the Decryption Wizard model.
     *
     * @return The model
     */
    public Encryption getModel() {
        return encryption;
    }

    /**
     * @return the pageResource
     */
    public PageResource getPageResource() {
        return pageResource;
    }

    /**
     * @return the pageOpenKey
     */
    public PageOpenKey getPageOpenKey() {
        return pageOpenKey;
    }

    /**
     * @return the pageCreateKey
     */
    public PageCreateKey getPageCreateKey() {
        return pageCreateKey;
    }

    /**
     * @return the pageCreateKeystore
     */
    public PageCreateKeystore getPageCreateKeystore() {
        return pageCreateKeystore;
    }

    /**
     * @return the pageAlgorithms
     */
    public PageAlgorithms getPageAlgorithms() {
        return pageAlgorithms;
    }
}
