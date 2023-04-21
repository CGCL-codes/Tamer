package org.codemon.gui;

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
 * This Class is the Gui of CodemonChangeTestCase.
 * @author Xu Mingming(������)
 * @see CodemonGui
 */
public class CodemonChangeTestCaseDescriptionGui {

    private Shell shell = null;

    private Text testCaseDescriptionText = null;

    private Button okButton = null;

    private CodemonGui codemonGui;

    private String description = new String();

    public CodemonChangeTestCaseDescriptionGui(CodemonGui codemonGui) {
        this.codemonGui = codemonGui;
    }

    /**
	 * The CodemonGui call this function to show the ChangeTestCaseDescription.
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
	 * This function deal the event of the ChangeTestCaseDescription GUI.
	 */
    public void dealEvent() {
        shell.addShellListener(new ShellCloseListener(this));
        okButton.addSelectionListener(new OkButtonListener(this));
    }

    /**
	 * This function create the shell and all its children.
	 */
    public void createShell() {
        shell = new Shell(SWT.DIALOG_TRIM);
        shell.setText("Change TestCase Description");
        shell.setLayout(null);
        shell.setBounds(50, 50, 250, 150);
        testCaseDescriptionText = WidgetFactory.createText(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        testCaseDescriptionText.setBounds(5, 5, 170, 100);
        okButton = WidgetFactory.createButton(shell, SWT.PUSH);
        okButton.setText("Ok");
        okButton.setBounds(176, 75, 60, 30);
    }

    /**
	 * This is the SelectionListener for Ok Button.
	 * @author Xu Mingming(������)
	 * @see CodemonChangeTestCaseDescriptionGui
	 */
    public class OkButtonListener extends SelectionAdapter {

        private CodemonChangeTestCaseDescriptionGui cctcd;

        public OkButtonListener(CodemonChangeTestCaseDescriptionGui cctcd) {
            this.cctcd = cctcd;
        }

        public void widgetSelected(SelectionEvent e) {
            description = testCaseDescriptionText.getText();
            cctcd.codemonGui.getCurrentTestCase().setDescription(description);
            cctcd.codemonGui.setTestCaseDescription(description);
            cctcd.codemonGui.getShell().setEnabled(true);
            cctcd.shell.dispose();
        }
    }

    /**
	 * This is the Shell Close Listener.
	 * @author Xu Mingming(������)
	 * @see CodemonChangeTestCaseDescriptionGui
	 */
    public class ShellCloseListener extends ShellAdapter {

        private CodemonChangeTestCaseDescriptionGui cctcd;

        public ShellCloseListener(CodemonChangeTestCaseDescriptionGui cctcd) {
            this.cctcd = cctcd;
        }

        public void shellClosed(ShellEvent e) {
            cctcd.codemonGui.getShell().setEnabled(true);
            cctcd.shell.dispose();
        }
    }
}
