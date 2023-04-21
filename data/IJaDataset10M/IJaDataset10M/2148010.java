package at.ssw.coco.ide.model.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import at.ssw.coco.builder.Activator;
import at.ssw.coco.builder.BuilderUtilities;

/**
 *
 * @author Dominik Hurnaus (coco-dev@hupfis.com)
 * @author Andreas Woess <andwoe@users.sf.net>
 */
public class CocoProjectPropertyPage extends PropertyPage {

    private Button chkCustomOutputDir;

    private Text txtCustomOutputDir;

    private Button btnSelectOutputDir;

    private Button chkCustomNamespace;

    private Text txtCustomNamespace;

    @Override
    protected Control createContents(final Composite parent) {
        final Font font = parent.getFont();
        final Composite mainGroup = new Composite(parent, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        mainGroup.setLayoutData(gd);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        mainGroup.setLayout(layout);
        mainGroup.setFont(font);
        chkCustomOutputDir = new Button(mainGroup, SWT.CHECK);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        chkCustomOutputDir.setText("Use custom output directory:");
        chkCustomOutputDir.setLayoutData(gd);
        chkCustomOutputDir.setFont(font);
        chkCustomOutputDir.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                txtCustomOutputDir.setEnabled(chkCustomOutputDir.getSelection());
                btnSelectOutputDir.setEnabled(chkCustomOutputDir.getSelection());
            }
        });
        new Label(mainGroup, SWT.NONE);
        txtCustomOutputDir = new Text(mainGroup, SWT.BORDER);
        txtCustomOutputDir.setEnabled(false);
        txtCustomOutputDir.setLayoutData(gd);
        btnSelectOutputDir = new Button(mainGroup, SWT.PUSH);
        btnSelectOutputDir.setText("Browse ...");
        btnSelectOutputDir.setEnabled(false);
        btnSelectOutputDir.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.MULTI);
                dialog.setText("Select output folder for Coco/R generated files.");
                if (getProject() != null) {
                    dialog.setFilterPath(getProject().getLocation().toOSString());
                }
                String res = dialog.open();
                if (res != null) {
                    txtCustomOutputDir.setText(Path.fromOSString(res).toPortableString());
                }
            }
        });
        new Label(mainGroup, SWT.None);
        new Label(mainGroup, SWT.None);
        chkCustomNamespace = new Button(mainGroup, SWT.CHECK);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        chkCustomNamespace.setText("Use custom package/namespace:");
        chkCustomNamespace.setLayoutData(gd);
        chkCustomNamespace.setFont(font);
        chkCustomNamespace.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                txtCustomNamespace.setEnabled(chkCustomNamespace.getSelection());
            }
        });
        new Label(mainGroup, SWT.NONE);
        txtCustomNamespace = new Text(mainGroup, SWT.BORDER);
        txtCustomNamespace.setEnabled(false);
        txtCustomNamespace.setLayoutData(gd);
        readValues();
        return mainGroup;
    }

    @Override
    public boolean performOk() {
        storeValues();
        return super.performOk();
    }

    private void storeValues() {
        final IProject project = getProject();
        if (project != null && isCocoProject(project)) {
            try {
                project.setPersistentProperty(Activator.CUSTOM_COCO_OUTPUT_DIR, this.txtCustomOutputDir.getText());
                project.setPersistentProperty(Activator.USE_CUSTOM_COCO_OUTPUT_DIR, this.chkCustomOutputDir.getSelection() ? "true" : "false");
                project.setPersistentProperty(Activator.CUSTOM_COCO_NAMESPACE, this.txtCustomNamespace.getText());
                project.setPersistentProperty(Activator.USE_CUSTOM_COCO_NAMESPACE, this.chkCustomNamespace.getSelection() ? "true" : "false");
            } catch (CoreException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void readValues() {
        final IProject project = getProject();
        if (project != null && isCocoProject(project)) {
            try {
                String dir = project.getPersistentProperty(Activator.CUSTOM_COCO_OUTPUT_DIR);
                if (dir == null) {
                    dir = "";
                }
                this.txtCustomOutputDir.setText(dir);
                String useDir = project.getPersistentProperty(Activator.USE_CUSTOM_COCO_OUTPUT_DIR);
                if (useDir == null) {
                    useDir = "false";
                }
                this.chkCustomOutputDir.setSelection("true".equals(useDir));
                this.txtCustomOutputDir.setEnabled(this.chkCustomOutputDir.getSelection());
                this.btnSelectOutputDir.setEnabled(this.chkCustomOutputDir.getSelection());
                String namespace = project.getPersistentProperty(Activator.CUSTOM_COCO_NAMESPACE);
                if (namespace == null) {
                    namespace = "";
                }
                this.txtCustomNamespace.setText(namespace);
                String useNamespace = project.getPersistentProperty(Activator.USE_CUSTOM_COCO_NAMESPACE);
                if (useNamespace == null) {
                    useNamespace = "false";
                }
                this.chkCustomNamespace.setSelection("true".equals(useNamespace));
                this.txtCustomNamespace.setEnabled(this.chkCustomNamespace.getSelection());
            } catch (CoreException ex) {
                ex.printStackTrace();
            }
        }
    }

    private IProject getProject() {
        IAdaptable adaptable = getElement();
        return adaptable == null ? null : (IProject) adaptable.getAdapter(IProject.class);
    }

    private boolean isCocoProject(IProject proj) {
        try {
            return proj.hasNature(BuilderUtilities.NATURE_ID);
        } catch (CoreException e) {
        }
        return false;
    }
}
