package de.mpiwg.vspace.languages.preferencepage.navigation;

import java.io.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import de.mpiwg.vspace.common.project.ProjectManager;
import de.mpiwg.vspace.filehandler.services.FileHandler;
import de.mpiwg.vspace.generation.navigation.service.NavigationEntry;
import de.mpiwg.vspace.navigation.NavigationEntryProvider;
import de.mpiwg.vspace.navigation.fieldeditor.IProperty;

public class ImageVSpaceFieldEditor extends AVSpaceFieldEditor {

    private String imagePath;

    private String defaultImagePath = null;

    private Label imageLabel;

    private Image image;

    private Button chooseButton;

    public ImageVSpaceFieldEditor(Composite parent, int style, IProperty property, NavigationEntry entry) {
        super(parent, style, property, entry);
    }

    public void createContent() {
        this.setLayout(new GridLayout(3, false));
        Label labelWidget = new Label(this, SWT.NONE);
        labelWidget.setText(label);
        chooseButton = new Button(this, SWT.NONE);
        chooseButton.setText("Choose...");
        chooseButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                imagePath = dialog.open();
                setImage(imagePath);
            }
        });
        Button removeButton = new Button(this, SWT.NONE);
        removeButton.setText("Remove image");
        removeButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                imagePath = "";
                setImage("");
            }
        });
        Label preview = new Label(this, SWT.NONE);
        preview.setText("Preview image");
        imageLabel = new Label(this, SWT.NONE);
        imageLabel.setLayoutData(new GridData(GridData.BEGINNING));
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        imageLabel.setLayoutData(gd);
        String storedImagePath = ps.getString(propField);
        IProject project = ProjectManager.getInstance().getCurrentProject();
        if ((defaultImagePath != null) && !ps.contains(propField)) setImage(defaultImagePath); else if ((storedImagePath != null) && !(storedImagePath.trim().equals(""))) setImage(project.getLocation().toOSString() + File.separator + storedImagePath);
    }

    protected void setImage(String path) {
        if ((path != null) && (!path.trim().equals(""))) {
            File file = new File(path);
            if (file.isFile()) {
                image = new Image(PlatformUI.getWorkbench().getDisplay(), file.getAbsolutePath());
                if (image != null) {
                    imageLabel.setImage(image);
                    imageLabel.pack();
                    imageLabel.getParent().getParent().layout();
                    imageLabel.getParent().layout();
                }
            }
        }
        if (path.trim().equals("")) {
            imageLabel.setImage(null);
            imageLabel.pack();
            imageLabel.getParent().getParent().layout();
            imageLabel.getParent().layout();
        }
    }

    public String getValue() {
        return null;
    }

    public void store() {
        if (imagePath == null) return;
        if (imagePath.trim().equals("")) ps.setValue(propField, "");
        File file = new File(imagePath);
        if (!file.exists()) return;
        File navFolder = ProjectManager.getInstance().getFolder(NavigationEntryProvider.INSTANCE.getImageFolderName());
        File copiedImage = new File(navFolder.getAbsolutePath() + File.separator + file.getName());
        copiedImage = FileHandler.copyFile(file, copiedImage);
        if (copiedImage == null) return;
        ps.setValue(propField, NavigationEntryProvider.INSTANCE.getImageFolderName() + File.separator + file.getName());
    }

    @Override
    public void dispose() {
        image.dispose();
        super.dispose();
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        defaultImagePath = defaultValue;
    }

    @Override
    public void setEnabled(boolean enabled) {
        chooseButton.setEnabled(enabled);
    }
}
