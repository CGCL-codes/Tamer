package net.sourceforge.scrollrack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class JabberDialog implements SelectionListener {

    private Game game;

    private Shell dialog;

    private Text server_entry;

    private Text username_entry;

    private Text password_entry;

    private Button ok_button;

    private Button cancel_button;

    public static final String jabber_server_key = "jabber_server";

    public static final String jabber_username_key = "jabber_username";

    public static final String jabber_password_key = "jabber_password";

    public JabberDialog(Game game) {
        Label label;
        GridLayout layout;
        GridData data;
        String value;
        this.game = game;
        dialog = new Shell(game.window.getShell(), SWT.SHELL_TRIM);
        dialog.setData(this);
        dialog.setText("Connect to Jabber");
        layout = new GridLayout(2, false);
        layout.marginWidth = 12;
        layout.marginHeight = 12;
        dialog.setLayout(layout);
        label = new Label(dialog, 0);
        value = "While connected to a Jabber server, you can challenge,\n" + "and be challenged by, other players using Jabber.\n";
        label.setText(value);
        data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.horizontalSpan = 2;
        label.setLayoutData(data);
        label = new Label(dialog, 0);
        label.setText("Server:");
        server_entry = new Text(dialog, SWT.BORDER | SWT.SINGLE);
        data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.widthHint = 120;
        server_entry.setLayoutData(data);
        value = game.prefs.getProperty(jabber_server_key);
        if (value != null) server_entry.setText(value);
        label = new Label(dialog, 0);
        label.setText("Username:");
        username_entry = new Text(dialog, SWT.BORDER | SWT.SINGLE);
        data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        username_entry.setLayoutData(data);
        value = game.prefs.getProperty(jabber_username_key);
        if (value != null) username_entry.setText(value);
        label = new Label(dialog, 0);
        label.setText("Password:");
        password_entry = new Text(dialog, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
        data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        password_entry.setLayoutData(data);
        value = game.prefs.getProperty(jabber_password_key);
        if (value != null) password_entry.setText(value);
        label = new Label(dialog, 0);
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.horizontalSpan = 2;
        label.setLayoutData(data);
        Composite composite = new Composite(dialog, 0);
        composite.setLayout(new GridLayout(2, true));
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.horizontalSpan = 2;
        composite.setLayoutData(data);
        ok_button = new Button(composite, SWT.PUSH);
        ok_button.setText("OK");
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.CENTER;
        data.widthHint = 60;
        ok_button.setLayoutData(data);
        ok_button.addSelectionListener(this);
        cancel_button = new Button(composite, SWT.PUSH);
        cancel_button.setText("Cancel");
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.CENTER;
        data.widthHint = 60;
        cancel_button.setLayoutData(data);
        cancel_button.addSelectionListener(this);
        value = server_entry.getText();
        if ((value == null) || value.equals("")) server_entry.setFocus(); else ok_button.setFocus();
        dialog.pack();
        dialog.open();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
    }

    public void widgetSelected(SelectionEvent event) {
        if (event.widget == ok_button) {
            process_ok_button();
            return;
        }
        if (event.widget == cancel_button) {
            dialog.close();
            return;
        }
    }

    private void process_ok_button() {
        String server = server_entry.getText();
        String username = username_entry.getText();
        String password = password_entry.getText();
        if ((server == null) || server.equals("") || (username == null) || username.equals("") || (password == null) || password.equals("")) return;
        game.prefs.setProperty(jabber_server_key, server);
        game.prefs.setProperty(jabber_username_key, username);
        game.prefs.setProperty(jabber_password_key, password);
        if (game.connection != null) return;
        ok_button.setEnabled(false);
        cancel_button.setEnabled(false);
        game.connection = new JabberConnection(server, username, password, game.queue);
    }

    public void close() {
        dialog.close();
    }
}
