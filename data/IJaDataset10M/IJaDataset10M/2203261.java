package br.com.klis.batendoumabola.client.widgets;

import br.com.klis.batendoumabola.shared.LoginInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MenuVerticalPanel extends Composite {

    private VerticalPanel menuPanel = new VerticalPanel();

    private final StackPanel stackPanel = new StackPanel();

    private final StackPanel menuStackPanel = new StackPanel();

    private VerticalPanel loginVerticalPanel = new VerticalPanel();

    private VerticalPanel menuVerticalPanel = new VerticalPanel();

    private Anchor signOutLink = new Anchor("Sair");

    private Anchor signInLink = new Anchor("Sign In");

    private MenuItem homeMenuItem;

    private MenuItem novoBateBolaMenuItem;

    private MenuItem meusBateBolasMenuItem;

    private MenuItem uploadMenuItem;

    public MenuVerticalPanel(LoginInfo loginInfo) {
        GWT.log("init menu panel...");
        menuPanel.setWidth("100%");
        stackPanel.setWidth("100%");
        menuStackPanel.setWidth("100%");
        loginVerticalPanel.setWidth("100%");
        if (loginInfo.isLoggedIn()) {
            Label bemvindo = new Label("Bem-vindo " + loginInfo.getNickname());
            loginVerticalPanel.add(bemvindo);
            loginVerticalPanel.setCellHeight(bemvindo, "40px");
            loginVerticalPanel.setCellVerticalAlignment(bemvindo, VerticalPanel.ALIGN_MIDDLE);
            signOutLink.setHref(loginInfo.getLogoutUrl());
            loginVerticalPanel.add(signOutLink);
        } else {
            signInLink.setHref(loginInfo.getLoginUrl());
            loginVerticalPanel.add(signInLink);
            loginVerticalPanel.setCellHeight(signInLink, "30px");
            loginVerticalPanel.setCellVerticalAlignment(signInLink, VerticalPanel.ALIGN_MIDDLE);
        }
        stackPanel.add(loginVerticalPanel, "Registro", false);
        MenuBar menuBar = new MenuBar(true);
        homeMenuItem = new MenuItem("Procurando um Bate-Bola?", false, (Command) null);
        novoBateBolaMenuItem = new MenuItem("Novo Bate-Bola", false, (Command) null);
        meusBateBolasMenuItem = new MenuItem("Meus Bate-Bolas", false, (Command) null);
        uploadMenuItem = new MenuItem("Upload", false, (Command) null);
        menuBar.addItem(homeMenuItem);
        if (loginInfo.isLoggedIn()) {
            menuBar.addItem(novoBateBolaMenuItem);
            menuBar.addItem(meusBateBolasMenuItem);
        }
        if (loginInfo.getEmailAddress() != null && loginInfo.getEmailAddress().equals("inacio@klis.com.br")) {
            menuBar.addItem(uploadMenuItem);
        }
        menuVerticalPanel.add(menuBar);
        menuVerticalPanel.setCellHeight(menuBar, "100px");
        menuVerticalPanel.setCellVerticalAlignment(menuBar, VerticalPanel.ALIGN_MIDDLE);
        menuStackPanel.add(menuVerticalPanel, "Menu", false);
        menuPanel.add(stackPanel);
        menuPanel.add(menuStackPanel);
        initWidget(menuPanel);
    }

    public MenuItem getHomeMenuItem() {
        return homeMenuItem;
    }

    public MenuItem getNovoBateBolaMenuItem() {
        return novoBateBolaMenuItem;
    }

    public MenuItem getMeusBateBolasMenuItem() {
        return meusBateBolasMenuItem;
    }

    public MenuItem getUploadMenuItem() {
        return uploadMenuItem;
    }

    public Anchor getSignOutLink() {
        return signOutLink;
    }

    public Anchor getSignInLink() {
        return signInLink;
    }
}
