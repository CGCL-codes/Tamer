package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
* This class demonstrates SashForm
*/
public class prueb2 {

    /**
 * Runs the application
 */
    public void run() {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SashForm Advanced");
        createContents(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
 * Creates the main window's contents
 * 
 * @param parent the parent window
 */
    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        Composite buttonBar = new Composite(parent, SWT.NONE);
        buttonBar.setLayout(new RowLayout());
        Button flip = new Button(buttonBar, SWT.PUSH);
        flip.setText("Switch Orientation");
        Button weights = new Button(buttonBar, SWT.PUSH);
        weights.setText("Restore Weights");
        Composite sash = new Composite(parent, SWT.NONE);
        sash.setLayout(new FillLayout());
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        final SashForm sashForm = new SashForm(sash, SWT.HORIZONTAL);
        sashForm.SASH_WIDTH = 5;
        sashForm.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        final Button one = new Button(sashForm, SWT.PUSH);
        one.setText("One");
        one.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                maximizeHelper(one, sashForm);
            }
        });
        final Button two = new Button(sashForm, SWT.PUSH);
        two.setText("Two");
        two.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                maximizeHelper(two, sashForm);
            }
        });
        final Button three = new Button(sashForm, SWT.PUSH);
        three.setText("Three");
        three.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                maximizeHelper(three, sashForm);
            }
        });
        sashForm.setWeights(new int[] { 1, 2, 3 });
        flip.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                switch(sashForm.getOrientation()) {
                    case SWT.HORIZONTAL:
                        sashForm.setOrientation(SWT.VERTICAL);
                        break;
                    case SWT.VERTICAL:
                        sashForm.setOrientation(SWT.HORIZONTAL);
                        break;
                }
            }
        });
        weights.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                sashForm.setWeights(new int[] { 1, 2, 3 });
            }
        });
    }

    /**
 * Helper method for our maximize behavior. If the passed control is already
 * maximized, restore it. Otherwise, maximize it
 * 
 * @param control the control to maximize or restore
 * @param sashForm the parent SashForm
 */
    private void maximizeHelper(Control control, SashForm sashForm) {
        if (control == sashForm.getMaximizedControl()) {
            sashForm.setMaximizedControl(null);
        } else {
            sashForm.setMaximizedControl(control);
        }
    }

    /**
 * The application entry point
 * 
 * @param args the command line arguments
 */
    public static void main(String[] args) {
        new prueb2().run();
    }
}
