package org.codemon.gui;

import org.codemon.analysis.TestCase;
import org.codemon.gui.guihelper.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This Class will be shown when New a TestCase.
 * @author Xu Mingming(������)
 */
public class CodemonNewTestCaseGui {

    private Shell shell = null;

    private Text testCaseDescriptionText = null;

    private Button okButton = null;

    private CodemonGui codemonGui;

    private String description = new String();

    public CodemonNewTestCaseGui(CodemonGui codemonGui) {
        this.codemonGui = codemonGui;
    }

    /**
	 * This CodemonGui call this function to show the CodemonNewTestCase GUI.
	 *
	 */
    public void go() {
        Display display = Display.getDefault();
        this.createShell();
        this.dealEvent();
        this.shell.open();
        while (!display.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    /**
	 * This function deal the event of CodemonNewTestCaseGui.
	 *
	 */
    public void dealEvent() {
        shell.addShellListener(new ShellCloseListener(this));
        okButton.addSelectionListener(new OkButtonListener(this));
    }

    /**
	 * This function create the shell and all its children.
	 *
	 */
    public void createShell() {
        shell = new Shell(SWT.DIALOG_TRIM);
        shell.setText("TestCase Description");
        shell.setLayout(null);
        shell.setBounds(50, 50, 250, 150);
        testCaseDescriptionText = WidgetFactory.createText(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        testCaseDescriptionText.setBounds(5, 5, 170, 100);
        okButton = WidgetFactory.createButton(shell, SWT.PUSH);
        okButton.setText("Ok");
        okButton.setBounds(176, 75, 60, 30);
    }

    /**
	 * This is the SelectionListener of OK Button.
	 * @author Xu Mingming(������)
	 * @see CodemonNewTestCaseGui
	 */
    public class OkButtonListener extends SelectionAdapter {

        private CodemonNewTestCaseGui cntcg;

        public OkButtonListener(CodemonNewTestCaseGui cntcg) {
            this.cntcg = cntcg;
        }

        public void widgetSelected(SelectionEvent e) {
            description = testCaseDescriptionText.getText();
            cntcg.codemonGui.setTestCaseDescription(description);
            int number = cntcg.codemonGui.getCurrentProject().getTestCaseList().size();
            TestCase tc = new TestCase(number, description, cntcg.codemonGui.getCurrentProject().getTestCaseList().get(0), cntcg.codemonGui.getCurrentProject().getMetaFileList(), cntcg.codemonGui.getCurrentProject().getMetaInfoList());
            cntcg.codemonGui.getCurrentProject().getTestCaseList().add(tc);
            cntcg.codemonGui.setCurrentTestCase(tc);
            cntcg.codemonGui.doRefresh();
            cntcg.codemonGui.getShell().setEnabled(true);
            cntcg.shell.dispose();
        }
    }

    /**
	 * This is the ShellClose listener of CodemonNewTestCaseGUi.
	 * @author Xu Mingming(������)
	 * @see CodemonNewTestCaseGui
	 */
    public class ShellCloseListener extends ShellAdapter {

        private CodemonNewTestCaseGui demo;

        public ShellCloseListener(CodemonNewTestCaseGui demo) {
            this.demo = demo;
        }

        public void shellClosed(ShellEvent e) {
            demo.codemonGui.getShell().setEnabled(true);
            demo.shell.dispose();
        }
    }
}
