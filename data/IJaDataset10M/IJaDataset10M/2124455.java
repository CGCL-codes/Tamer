package ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipsePlugin;
import ca.ucalgary.cpsc.ebe.fitClipse.connector.BeanConnector;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.editor.WikiEditorController;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.model.WikiPageModel;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.TestResultController;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.TestResultDetailsEditorController;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.model.SingleTestResult;

public class TestResultDetailsAction extends Action implements ISelectionChangedListener {

    private TreeViewer control = null;

    public TestResultDetailsAction(TreeViewer control) {
        this.control = control;
        this.setText("Result Details...");
        this.setToolTipText("Show Test Result Details");
        this.setImageDescriptor(FitClipsePlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.ebe.fitClipsePlugin", "images/icon_test_detail.gif"));
        this.setEnabled(false);
    }

    public void run() {
        try {
            IStructuredSelection selection = (IStructuredSelection) control.getSelection();
            if (selection == null) return;
            SingleTestResult model = (SingleTestResult) selection.getFirstElement();
            long suiteID = model.getSuiteID();
            String name = TestResultController.getName(model.getQName());
            TestResultDetailsEditorController.newTestResultDetailsViewer(name, model.getQName(), suiteID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) control.getSelection();
        if (selection == null) this.setEnabled(false);
        Object element = selection.getFirstElement();
        if (element instanceof SingleTestResult) this.setEnabled(true); else this.setEnabled(false);
    }
}
