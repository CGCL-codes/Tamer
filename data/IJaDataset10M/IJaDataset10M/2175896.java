package ch.sahits.codegen.ui.internal.wizards;

import java.io.File;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import ch.sahits.codegen.help.ISahitsHelpSystem;
import ch.sahits.codegen.ui.i18n.CodegenUIMessages;
import ch.sahits.codegen.ui.CodegenUIPlugin;
import ch.sahits.codegen.CodegenPlugin;
import ch.sahits.codegen.IModelSerializer;

/**
 * This is an abstract superclass for the wizard pages.<br> This abstract WizardPage provides the functionality of the save button and its connection to the  {@link BasicCodeGenWizard} This class provides some basic implementation for the methods {@link #getHelpContext()}  and  {@link #setImage()} . It is strongly suggested that any subclass override these methods.
 * @author  Andi Hotz
 * @since  0.9.1
 */
public abstract class AbstractCodeGenWizardPage extends WizardPage {

    /** Save Button */
    protected Button btnSave;

    /**
	 * Caller object
	 * @uml.property  name="parent"
	 * @uml.associationEnd  
	 */
    protected BasicCodeGenWizard parent = null;

    protected String headerImageURL = CodegenUIPlugin.getJavaHeaderImageURL();

    /**
	 * Constructor with the page name
	 * @param pageName
	 * @param caller Calling wizard
	 */
    protected AbstractCodeGenWizardPage(String pageName, BasicCodeGenWizard caller) {
        super(pageName);
        parent = caller;
    }

    /**
	 * Set the image for the wizard Page
	 * 
	 * This method retrieves an ImageDescriptor from an URL
	 * and set this as the image of the page.
	 * This Method should be overridden since the images are
	 * ressources defined in the same package as the implementation
	 * of the page.
	 */
    protected void setImage() {
        ImageDescriptor image = CodegenUIPlugin.getImageDescriptorJava(headerImageURL);
        setImageDescriptor(image);
    }

    /**
	 * This method handles the serialisation event
	 * Serialisation is only defined for the 
	 * caller class
	 * @since 0.9.4
	 */
    protected final void handleSerialize() {
        String fileName = handleBrowseSaveFile();
        if (fileName == null) {
            return;
        }
        if (parent instanceof IModelSerializer) {
            ((IModelSerializer) parent).serialize(fileName);
        }
    }

    /**
	 * Handle the browsing for a file to save.
	 * If the selected filename does not end on xml
	 * it is appended.
	 * @return Name of the file to save
	 * @since 0.9.4
	 */
    private String handleBrowseSaveFile() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText(CodegenUIMessages.AbstractCodeGenWizardPage_1);
        dialog.setFilterExtensions(new String[] { ".xml" });
        boolean done = false;
        String fileName = null;
        while (!done) {
            fileName = dialog.open();
            if (fileName == null) {
                done = true;
            } else {
                File file = new File(fileName);
                if (file.exists()) {
                    MessageBox mb = new MessageBox(dialog.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
                    mb.setMessage(NLS.bind(CodegenUIMessages.AbstractCodeGenWizardPage_3, fileName));
                    done = mb.open() == SWT.YES;
                } else {
                    done = true;
                }
            }
        }
        if (fileName != null && !fileName.endsWith(".xml")) {
            fileName += ".xml";
        }
        return fileName;
    }

    /**
	 * Create the save button
	 * @param composite
	 */
    protected final void createSaveButton(Composite composite) {
        btnSave = new Button(composite, SWT.PUSH);
        btnSave.setText(CodegenUIMessages.AbstractCodeGenWizardPage_6);
        btnSave.setToolTipText(CodegenUIMessages.AbstractCodeGenWizardPage_7);
        btnSave.setEnabled(false);
        btnSave.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleSerialize();
            }
        });
    }

    /**
	 * Enable/Diasble the save button
	 * @param enable
	 */
    public final void enableSaveButton(boolean enable) {
        btnSave.setEnabled(enable);
    }

    /**
	   * Compute the context help string for the current selection
	   * This Method is intended to override
	   * @return Context-ID
	   * @since 1.1.0
	   */
    protected String getHelpContext() {
        return "org.eclipse.help.ui.new_ressource";
    }

    /**
	   * Define the Help for this page
	   * @since 1.1.0
	   */
    public final void performHelp() {
        String contextID = getHelpContext();
        getShell().setData(ISahitsHelpSystem.HELP_KEY, contextID);
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(contextID);
    }
}
