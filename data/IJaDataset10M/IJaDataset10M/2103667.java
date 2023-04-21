package eulergui.gui.controller;

import javax.swing.SwingWorker;
import n3_project.ProjectGUI;
import eulergui.gui.inference.InferenceProgressFrame;
import eulergui.project.N3Source;

/** use {@link SwingWorker} and {@link InferenceProgressFrame}
 * for long tasks in background.
 */
public class N3SourceWorker extends SwingWorker<Void, Void> {

    private final ProjectGUI projectGUI;

    /**
	 * @param projectGUI
	 * @param n3
	 * @param actionName
	 */
    public N3SourceWorker(ProjectGUI projectGUI, N3Source n3, String actionName) {
        super();
        this.projectGUI = projectGUI;
        this.n3 = n3;
        this.actionName = actionName;
    }

    private InferenceProgressFrame progressFrame;

    private String actionName;

    private N3Source n3;

    @Override
    protected Void doInBackground() throws Exception {
        progressFrame = InferenceProgressFrame.makeProgressFrame(n3, this);
        if (projectGUI != null) {
            this.projectGUI.getResultManagement().insertStringAtPaneEnd(this.projectGUI, "# Action [" + actionName + " - " + n3.showShortName() + "] performed! ");
        }
        return null;
    }

    @Override
    protected void done() {
        progressFrame.dispose();
    }
}
