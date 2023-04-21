package org.emmannuel.virtues.client.panel;

import java.util.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class AccountPanel {

    private static final HashMap<String, String> userinfo = new HashMap<String, String>();

    static {
        if (0 == Random.nextInt(1)) {
        } else {
            userinfo.put("name", "Michal");
        }
    }

    public static String getUserName() {
        return userinfo.get("name");
    }

    public static Boolean isOnline() {
        return userinfo.get("name") != null;
    }

    public static boolean logout() {
        userinfo.clear();
        return true;
    }

    public static void register() {
        final DialogBox dialog = new DialogBox(false, true);
        dialog.setAnimationEnabled(true);
        dialog.setText("Create Account");
        Grid grid = new Grid(4, 3);
        Label nameLabel = new Label("Name");
        TextBox name = new TextBox();
        Label passwordLabel = new Label("Password");
        PasswordTextBox password = new PasswordTextBox();
        Label eMailLabel = new Label("Email");
        TextBox eMail = new TextBox();
        grid.setWidget(0, 0, nameLabel);
        grid.setWidget(0, 1, name);
        grid.setWidget(1, 0, passwordLabel);
        grid.setWidget(1, 1, password);
        grid.setWidget(2, 0, eMailLabel);
        grid.setWidget(2, 1, eMail);
        Button crtBtn = new Button("Create Account");
        crtBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
            }
        });
        grid.setWidget(3, 1, crtBtn);
        Button closeBtn = new Button("Close");
        closeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
            }
        });
        grid.setWidget(3, 2, closeBtn);
        dialog.add(grid);
        dialog.center();
    }

    public static void login() {
        final DialogBox dialog = new DialogBox(false, true);
        dialog.setAnimationEnabled(true);
        dialog.setText("Login");
        Grid grid = new Grid(4, 3);
        Label nameLabel = new Label("Name");
        final TextBox name = new TextBox();
        Label passwordLabel = new Label("Password");
        PasswordTextBox password = new PasswordTextBox();
        grid.setWidget(0, 0, nameLabel);
        grid.setWidget(0, 1, name);
        grid.setWidget(1, 0, passwordLabel);
        grid.setWidget(1, 1, password);
        Button logBtn = new Button("Login");
        logBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
                userinfo.put("name", name.getText());
                Window.alert("Loged as : " + name.getText());
            }
        });
        grid.setWidget(3, 0, logBtn);
        Button crtBtn = new Button("Create Account");
        crtBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                AccountPanel.register();
            }
        });
        grid.setWidget(3, 1, crtBtn);
        Button closeBtn = new Button("Close");
        closeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
            }
        });
        grid.setWidget(3, 2, closeBtn);
        dialog.add(grid);
        dialog.center();
    }

    public static void showAccount() {
        final DialogBox dialog = new DialogBox(true, true);
        dialog.setText("Account");
        Grid grid = new Grid(4, 3);
        Label nameLabel = new Label("Name");
        final TextBox name = new TextBox();
        Label passwordLabel = new Label("Password");
        PasswordTextBox password = new PasswordTextBox();
        Label eMailLabel = new Label("Email");
        TextBox eMail = new TextBox();
        grid.setWidget(0, 0, nameLabel);
        grid.setWidget(0, 1, name);
        grid.setWidget(1, 0, passwordLabel);
        grid.setWidget(1, 1, password);
        grid.setWidget(1, 0, eMailLabel);
        grid.setWidget(1, 1, eMail);
        Button logBtn = new Button("Edit");
        logBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
                Window.alert("Changed: " + name.getText());
            }
        });
        grid.setWidget(3, 0, logBtn);
        Button crtBtn = new Button("Logout");
        crtBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
                AccountPanel.logout();
            }
        });
        grid.setWidget(3, 1, crtBtn);
        Button closeBtn = new Button("Close");
        closeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialog.hide();
            }
        });
        grid.setWidget(3, 2, closeBtn);
        dialog.add(grid);
        dialog.center();
    }
}
