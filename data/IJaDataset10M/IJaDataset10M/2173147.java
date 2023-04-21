package net.taylor.mda.reverse.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class ImportWizardPage extends WizardNewFileCreationPage {

    protected FileFieldEditor editor;

    public ImportWizardPage(String pageName, IStructuredSelection selection) {
        super(pageName, selection);
        setTitle(pageName);
        setDescription("Import a file from the local file system into the workspace");
    }

    protected void createAdvancedControls(Composite parent) {
        Composite fileSelectionArea = new Composite(parent, SWT.NONE);
        GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        fileSelectionArea.setLayoutData(fileSelectionData);
        GridLayout fileSelectionLayout = new GridLayout();
        fileSelectionLayout.numColumns = 3;
        fileSelectionLayout.makeColumnsEqualWidth = false;
        fileSelectionLayout.marginWidth = 0;
        fileSelectionLayout.marginHeight = 0;
        fileSelectionArea.setLayout(fileSelectionLayout);
        editor = new FileFieldEditor("fileSelect", "Select File: ", fileSelectionArea);
        editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                IPath path = new Path(ImportWizardPage.this.editor.getStringValue());
                setFileName(path.lastSegment());
            }
        });
        String[] extensions = new String[] { "*.*" };
        editor.setFileExtensions(extensions);
        fileSelectionArea.moveAbove(null);
    }

    protected void createLinkTarget() {
    }

    protected InputStream getInitialContents() {
        try {
            return new FileInputStream(new File(editor.getStringValue()));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    protected String getNewFileLabel() {
        return "New File Name:";
    }

    protected IStatus validateLinkedResource() {
        return new Status(IStatus.OK, "net.taylor.mda.importwizards", IStatus.OK, "", null);
    }
}
