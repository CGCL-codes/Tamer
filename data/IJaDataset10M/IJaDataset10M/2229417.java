package org.isi.monet.modelling.core.wizards.pages;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WorkingSetGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;
import org.isi.monet.modelling.core.dialogs.AddLanguageDialog;
import org.isi.monet.modelling.core.wizards.NewWizardMessages;
import org.eclipse.swt.widgets.Canvas;

@SuppressWarnings("restriction")
public class InfoPageProject extends WizardPage {

    private Text headerText;

    private Text headerImage;

    private Text logoImage;

    public InfoPageProject(String pageName) {
        super(pageName);
    }

    @Override
    public void createControl(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NULL);
        initializeDialogUnits(parent);
        GridLayout gl_composite = new GridLayout();
        gl_composite.numColumns = 3;
        composite.setLayout(gl_composite);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        setPageComplete(validatePage());
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);
        Label lblHeaderText = new Label(composite, SWT.NONE);
        lblHeaderText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblHeaderText.setText("Header Text");
        headerText = new Text(composite, SWT.BORDER);
        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_text.widthHint = 296;
        gd_text.minimumWidth = 200;
        headerText.setLayoutData(gd_text);
        new Label(composite, SWT.NONE);
        Label lblImageHeader = new Label(composite, SWT.NONE);
        lblImageHeader.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblImageHeader.setText("Image Header");
        headerImage = new Text(composite, SWT.BORDER);
        headerImage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        Button btnLoadHeaderImage = new Button(composite, SWT.NONE);
        btnLoadHeaderImage.setText("Load Image");
        btnLoadHeaderImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                FileDialog fd = new FileDialog(composite.getShell(), SWT.OPEN);
                fd.setText("Load Image");
                fd.setFilterPath("C:/");
                String[] filterExt = { "*.png", "*.*" };
                fd.setFilterExtensions(filterExt);
                headerImage.setText(fd.open());
            }
        });
        Label lblImageLogo = new Label(composite, SWT.NONE);
        lblImageLogo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblImageLogo.setText("Image Logo");
        logoImage = new Text(composite, SWT.BORDER);
        logoImage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        Button btnLoadLogoImage = new Button(composite, SWT.NONE);
        btnLoadLogoImage.setText("Load Image");
        btnLoadLogoImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                FileDialog fd = new FileDialog(composite.getShell(), SWT.OPEN);
                fd.setText("Load Image");
                fd.setFilterPath("C:/");
                String[] filterExt = { "*.png", "*.*" };
                fd.setFilterExtensions(filterExt);
                logoImage.setText(fd.open());
            }
        });
    }

    private void handleBrowse() {
    }

    protected boolean validatePage() {
        return true;
    }

    private IErrorMessageReporter getErrorReporter() {
        return new IErrorMessageReporter() {

            public void reportError(String errorMessage, boolean infoOnly) {
                if (infoOnly) {
                    setMessage(errorMessage, IStatus.INFO);
                    setErrorMessage(null);
                } else setErrorMessage(errorMessage);
                boolean valid = errorMessage == null;
                if (valid) {
                    valid = validatePage();
                }
                setPageComplete(valid);
            }
        };
    }
}
