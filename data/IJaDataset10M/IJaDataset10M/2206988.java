package de.fzi.injectj.frontend.eclipse.wizards;

import java.io.File;
import java.util.Vector;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import de.fzi.injectj.backend.GlobalSettings;
import de.fzi.injectj.backend.Project;
import de.fzi.injectj.frontend.eclipse.Plugin;
import de.fzi.injectj.frontend.eclipse.components.FileDirEditor;
import de.fzi.injectj.frontend.eclipse.components.FileHistoryWrapper;

/**
 * @author Sebastian Mies
 * 
 * This wizard page asks for the Inject/J project to be openend,
 * if no project is entered, a new one will be created. 
 */
public class PageOpenProject extends InjectJWizardPage {

    private FileDirEditor prjSelect;

    private Project prj;

    /**
	 * Contruct page
	 */
    public PageOpenProject() {
        super("openprj");
        this.setMessage("Enter Projectfile and Sourcedirectory");
    }

    /**
	 * Creates page control
	 */
    public void createControl(Composite parent) {
        Composite control = new Composite(parent, 0);
        control.setLayout(new GridLayout());
        Image ilogoBig = MainWizard.getBigLogo().createImage();
        Label label = new Label(control, 0);
        ilogoBig.setBackground(label.getBackground());
        GridData gridData;
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.grabExcessHorizontalSpace = true;
        label.setLayoutData(gridData);
        label.setImage(ilogoBig);
        Composite panel2 = new Composite(control, 0);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        panel2.setLayoutData(gridData);
        panel2.setLayout(new GridLayout());
        prjSelect = new FileDirEditor(panel2, FileDirEditor.FILES, new FileHistoryWrapper() {

            public void addRecentFile(String fileName) {
                GlobalSettings.addRecentProjectFile(fileName);
            }

            public Vector getFileHistory() {
                return GlobalSettings.getRecentProjectFiles();
            }
        });
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        prjSelect.getControl().setLayoutData(gridData);
        prjSelect.setFileFilter(new String[] { "*.ijp", "Inject/J Project Files (*.ijp)" });
        prjSelect.setRootDirectory(GlobalSettings.getDefaultProjectPath());
        prjSelect.setEditorLabel("Select a project file");
        this.setControl(control);
    }

    /**
	 * Update page contents
	 */
    public void updateContent() {
        Project prj = getProject();
        if (prj != null) prjSelect.setObject(prj.getFileName());
        prjSelect.updateContent();
    }

    public boolean vetoLeave() {
        MainWizard wizard = getMainWizard();
        Project prj = wizard.getProject();
        String prjfile = (String) prjSelect.getObject();
        if (prjfile != null && (new File(prjfile)).exists()) {
            prj = Plugin.getDefault().getKernel().loadProject(prjfile);
            wizard.setProject(prj);
        } else if (prj == null) {
            prj = Plugin.getDefault().getKernel().createProject();
            prj.setFileName("newproject.ijp");
            wizard.setProject(prj);
        }
        GlobalSettings.writeGlobalSettings();
        return false;
    }
}
