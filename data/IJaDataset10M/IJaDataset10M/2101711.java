package org.ontospread.gui.view.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import resources.ApplicationResources;
import com.swtdesigner.SWTResourceManager;

public class ShowConcept extends Dialog {

    private Combo combo;

    protected Object result;

    protected Shell shell;

    private String[] concepts;

    /**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
    public ShowConcept(Shell parent, int style) {
        super(parent, style);
    }

    /**
	 * Create the dialog
	 * @param parent
	 */
    public ShowConcept(Shell parent) {
        this(parent, SWT.NONE);
    }

    /**
	 * Open the dialog
	 * @return the result
	 */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return result;
    }

    /**
	 * Create contents of the dialog
	 */
    protected void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setImage(SWTResourceManager.getImage(ShowConcept.class, ApplicationResources.getString("ShowConcept.0")));
        shell.addShellListener(new ShellAdapter() {

            public void shellActivated(ShellEvent arg0) {
                combo.setItems(concepts);
            }
        });
        shell.setSize(566, 102);
        shell.setText(ApplicationResources.getString("ShowConcept.1"));
        final Button showButton = new Button(shell, SWT.NONE);
        showButton.setImage(SWTResourceManager.getImage(ShowConcept.class, ApplicationResources.getString("ShowConcept.2")));
        showButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                result = combo.getText();
                shell.close();
                shell.dispose();
            }
        });
        showButton.setText(ApplicationResources.getString("ShowConcept.3"));
        showButton.setBounds(442, 22, 88, 23);
        final Button cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setImage(SWTResourceManager.getImage(ShowConcept.class, ApplicationResources.getString("ShowConcept.4")));
        cancelButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                shell.close();
                shell.dispose();
            }
        });
        cancelButton.setBounds(465, 51, 88, 22);
        cancelButton.setText(ApplicationResources.getString("ShowConcept.5"));
        combo = new Combo(shell, SWT.NONE);
        combo.setBounds(10, 24, 426, 21);
    }

    public void setConcepts(String[] allConcepts) {
        this.concepts = allConcepts;
    }
}
