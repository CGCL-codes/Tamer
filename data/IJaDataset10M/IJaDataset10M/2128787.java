package sssvmtoolbox.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import sssvmtoolbox.dataprovider.Persister;

public class ExperimentView extends ViewPart {

    public static final String ID = "sssvmtoolbox.ui.ExperimentView";

    private CTabFolder tabFolder;

    private AbstractTabViewer inputTabViewer;

    private SVMTabViewer svmTabViewer;

    private PreprocessingTabViewer preprocessingTabViewer;

    private SSSVMTabViewer sssvmTabViewer;

    private WVToolTabViewer wvToolTabViewer;

    private ResultTabViewer resultTabViewer;

    private Persister persister;

    private Project project;

    /**
	 * The constructor.
	 */
    public ExperimentView() {
        if (Project.newProject != null) {
            project = Project.newProject;
            Project.newProject = null;
        } else {
            project = new Project("/Users/guan/Documents/workspace/ssSVMToolbox/src/experiment/abalone/abalone.xml");
        }
    }

    @Override
    public String getTitle() {
        return super.getTitle() + " - " + project.getName();
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    @Override
    public void createPartControl(final Composite parent) {
        tabFolder = new CTabFolder(parent, SWT.NONE);
        tabFolder.setBorderVisible(true);
        tabFolder.marginWidth = 5;
        tabFolder.setTabHeight(24);
        tabFolder.setSimple(false);
        final String path = project.getFullPath();
        persister = new Persister(path);
        inputTabViewer = new InputTabViewer(tabFolder, path, persister);
        wvToolTabViewer = new WVToolTabViewer(tabFolder, path, persister);
        preprocessingTabViewer = new PreprocessingTabViewer(tabFolder, path, persister);
        svmTabViewer = new SVMTabViewer(tabFolder, path, persister);
        sssvmTabViewer = new SSSVMTabViewer(tabFolder, path, persister);
        resultTabViewer = new ResultTabViewer(tabFolder, path, persister);
        svmTabViewer.setResultListener(resultTabViewer);
        sssvmTabViewer.setResultListener(resultTabViewer);
        tabFolder.setSelection(0);
        tabFolder.setSize(400, 200);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    @Override
    public void setFocus() {
        tabFolder.setFocus();
    }
}
