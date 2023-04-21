package ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.internal.UIPlugin;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipsePlugin;
import ca.ucalgary.cpsc.ebe.fitClipse.connector.BeanConnector;
import ca.ucalgary.cpsc.ebe.fitClipse.runner.FitManager;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.model.WikiPageModel;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.TestResultController;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.TestResultView;

public class RunLastAction extends Action implements ISelectionChangedListener {

    private TreeViewer control = null;

    public RunLastAction(TreeViewer control) {
        this.control = control;
        this.setText("Run Last");
        this.setToolTipText("Run Last...");
        this.setImageDescriptor(FitClipsePlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.ebe.fitClipsePlugin", "images/icon_run_last.gif"));
    }

    public void run() {
        FitManager fit = FitManager.getFitManager();
        try {
            TestResultView view = TestResultController.getTestResultView();
            if (fit.getTests().size() == 0) {
                MessageDialog.openWarning(control.getTree().getShell(), "Warning!", "There have been no tests running before!");
                return;
            } else {
                view.setInput(fit.runTests(view.getInput()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectionChanged(SelectionChangedEvent event) {
    }
}
