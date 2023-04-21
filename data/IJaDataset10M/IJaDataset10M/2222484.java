package n3_project;

import java.awt.event.ActionEvent;

/** GUI Action for running Drools reasoning engine */
class RunDroolsAction extends RunAbstractAction {

    private static final long serialVersionUID = 1L;

    public RunDroolsAction(ProjectGUI projectGUI) {
        setProperties(this, "Run Drools", "drools.png", "Run Drools", 'C');
        this.projectGUI = projectGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResultManagement resultManagement = projectGUI.resultManagement;
        try {
            Project project = projectGUI.project;
            resultManagement.redirectStandardError();
            resultManagement.resultTriples = project.runDroolsTriples();
            resultManagement.result = project.getN3StringResult();
            displayResultsAndErrors();
        } catch (Exception e1) {
            System.err.println(e1);
            resultManagement.displayStandardError();
        }
    }
}
