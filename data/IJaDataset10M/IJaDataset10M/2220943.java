package com.ufro.blackjack.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Index implements EntryPoint {

    private SignIn signin = new SignIn();

    private VerticalPanel verticalPanelBody = new VerticalPanel();

    private UserPanel userPanel = new UserPanel();

    public void onModuleLoad() {
        RootPanel root = RootPanel.get();
        root.setSize("1024", "768");
        root.setStyleName("gwt-BGBody");
        verticalPanelBody.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        root.add(verticalPanelBody);
        verticalPanelBody.setStyleName("gwt-BGMain");
        verticalPanelBody.setSize("800px", "500px");
        Label lblBlackjack = new Label("BlackJack");
        lblBlackjack.setStyleName("gwt-NormalTitle");
        lblBlackjack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanelBody.add(lblBlackjack);
        FlexTable flexTable = new FlexTable();
        verticalPanelBody.add(flexTable);
        flexTable.setSize("450px", "143px");
        Button btnSignIn = new Button("Sign In");
        btnSignIn.setText("Ingresar");
        flexTable.setWidget(0, 0, btnSignIn);
        btnSignIn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                final DialogBox dialogBox = crearVentanaSignIn(event);
                dialogBox.center();
                dialogBox.show();
            }
        });
        Button btnSignUp = new Button("Sign Up");
        btnSignUp.setText("Registrarse");
        flexTable.setWidget(1, 0, btnSignUp);
        btnSignUp.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                final DialogBox dialogBox = crearVentanaSignUp(event);
                dialogBox.center();
                dialogBox.show();
            }
        });
        flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
    }

    /**
	 * Metodo para crear ventana de Ingreso
	 * @param event
	 * @return
	 */
    private DialogBox crearVentanaSignIn(Object event) {
        final DialogBox dialogBox = new DialogBox();
        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setStyleName("gwt-BGPopup");
        dialogContents.setSpacing(4);
        dialogBox.setWidget(dialogContents);
        Grid grid = new Grid(1, 2);
        grid.setSize("", "");
        dialogContents.add(signin);
        Button deleteButton = new Button("Volver", new ClickHandler() {

            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        Button logButton = new Button("Ingresar", new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (signin.getLogeo() == 1) {
                    verticalPanelBody.clear();
                    verticalPanelBody.add(userPanel);
                }
                dialogBox.hide();
            }
        });
        grid.setWidget(0, 0, logButton);
        grid.setWidget(0, 1, deleteButton);
        dialogContents.add(grid);
        return dialogBox;
    }

    /**
	 * Metodo para crear la ventana de Registro
	 * @param event
	 * @return
	 */
    private DialogBox crearVentanaSignUp(Object event) {
        final DialogBox dialogBox = new DialogBox();
        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setStyleName("gwt-BGPopup");
        dialogContents.setSpacing(4);
        dialogBox.setWidget(dialogContents);
        Signup signup = new Signup();
        dialogContents.add(signup);
        Button deleteButton = new Button("Volver", new ClickHandler() {

            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        dialogContents.add(deleteButton);
        return dialogBox;
    }
}
