package org.processbase.openesb.monitor;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Locale;

/**
 *
 * @author mgubaidullin
 */
public class LoginWindow extends Window implements Handler {

    private GridLayout grid = new GridLayout(3, 2);

    private Panel panel = new Panel();

    public FormLayout form = new FormLayout();

    private VerticalLayout vlayout = new VerticalLayout();

    private Button btnLogin = null;

    private Action action_ok = null;

    public TextField address = new TextField("Server address", "");

    private TextField port = new TextField("Port", "8686");

    private TextField username = new TextField("Username", "admin");

    private TextField password = new TextField("Password", "adminadmin");

    private Label labelLeft = new Label("");

    private Label labelRight = new Label("");

    private Locale locale = null;

    private Embedded logo = null;

    public LoginWindow() {
        super("POEM");
        setName("POEM");
        initUI();
    }

    private void initUI() {
        setTheme("reindeermods");
        addComponent(grid);
        grid.setWidth("100%");
        grid.setHeight("100%");
        grid.addComponent(labelLeft, 0, 0);
        grid.addComponent(labelRight, 2, 0);
        grid.addComponent(panel, 1, 1);
        grid.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        panel.setWidth("285px");
        form.addComponent(address);
        address.setWidth("100%");
        form.addComponent(port);
        port.setWidth("100%");
        username.setCaption("Username");
        form.addComponent(username);
        username.setWidth("100%");
        password.setCaption("Password");
        password.setSecret(true);
        password.setWidth("100%");
        password.focus();
        form.addComponent(password);
        btnLogin = new Button("Login", this, "okHandler");
        action_ok = new ShortcutAction("Default key", ShortcutAction.KeyCode.ENTER, null);
        form.addComponent(btnLogin);
        form.setComponentAlignment(btnLogin, Alignment.BOTTOM_RIGHT);
        createLogo();
        vlayout.addComponent(logo);
        vlayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        vlayout.addComponent(form);
        vlayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
        vlayout.setMargin(false, true, false, true);
        vlayout.setSpacing(true);
        panel.setContent(vlayout);
        panel.addActionHandler(this);
    }

    private void createLogo() {
        ThemeResource themeResource = new ThemeResource("icons/ProcessBase_OpenESB_Monitor.png");
        logo = new Embedded("", themeResource);
        logo.setType(Embedded.TYPE_IMAGE);
    }

    public Action[] getActions(Object target, Object sender) {
        return new Action[] { action_ok };
    }

    public void handleAction(Action action, Object sender, Object target) {
        if (action == action_ok) {
            okHandler();
        }
    }

    public void okHandler() {
        try {
            POEM.getCurrent().authenticate((String) address.getValue(), (String) port.getValue(), (String) username.getValue(), (String) password.getValue());
            open(new ExternalResource(POEM.getCurrent().getURL()));
        } catch (Exception ex) {
            ex.printStackTrace();
            showNotification("Error", ex.getMessage(), Notification.TYPE_ERROR_MESSAGE);
        }
    }
}
