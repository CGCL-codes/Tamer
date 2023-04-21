package be.yildiz.client.connection.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import be.yildiz.client.connection.logic.ConnectionService;
import be.yildiz.client.window.WindowManager;

/**
 * Connection window, ask for login and password to connect to server.
 *
 * @author Van Den Borre Grégory
 * @version 1.0 (29/01/09)
 * @since 0.1
 */
public class Connection {

    /**
     * Logic for the connection.
     */
    private ConnectionService service;

    /**
     * The SWT shell.
     */
    private Shell shell;

    /**
     * Display used by the window.
     */
    private Display display;

    /**
     * Full constructor.
     *
     * @param commonDisplay
     *            {@inheritDoc}
     */
    public Connection(final Display commonDisplay) {
        super();
        this.display = commonDisplay;
        shell = new Shell(display);
        service = new ConnectionService();
        Label loginLabel = new Label(shell, SWT.NONE);
        loginLabel.setText("Login:");
        final Text login = new Text(shell, SWT.BORDER);
        Label passwordLabel = new Label(shell, SWT.NONE);
        passwordLabel.setText("Password:");
        final Text password = new Text(shell, SWT.BORDER);
        Button connect = new Button(shell, SWT.PUSH);
        Button option = new Button(shell, SWT.ESC);
        connect.setText("Connect");
        option.setText("Options");
        shell.setText("Connection");
        shell.setLayout(new RowLayout());
        shell.pack();
        connect.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent event) {
                service.connect(login.getText(), password.getText());
                shell.close();
            }
        });
        option.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent event) {
                WindowManager.getInstance().runOption();
            }
        });
        shell.open();
    }

    /**
     * {@inheritDoc}
     */
    public final void run() {
        System.out.println("[WINDOW] Start connection window display.");
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
