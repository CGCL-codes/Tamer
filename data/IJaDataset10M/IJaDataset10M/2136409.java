package com.enoram.training1.register.client;

import com.enoram.training1.register.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Register implements EntryPoint {

    private FlexTable fTable = new FlexTable();

    private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network " + "connection and try again.";

    private final RegistrationServiceAsync service = GWT.create(RegistrationService.class);

    public void onModuleLoad() {
        final Label firstNameLabel = new Label("First Name: ");
        final TextBox firstNameField = new TextBox();
        final Label middleNameLabel = new Label("Middle Name: ");
        final TextBox middleNameField = new TextBox();
        final Label lastNameLabel = new Label("Last Name: ");
        final TextBox lastNameField = new TextBox();
        final Label attorneyEmailLable = new Label("Attorney Email Address: ");
        final TextBox attorneyEmailField = new TextBox();
        final Label barCodeLabel = new Label("Bar Code # ");
        final TextBox barCodeField = new TextBox();
        final Label idLabel = new Label("External ID # ");
        final TextBox idField = new TextBox();
        final Label firmNameLabel = new Label("Firm Name: ");
        final TextBox firmNameField = new TextBox();
        final Label firmAddressLabel = new Label("Firm Address: ");
        final TextBox firmAddressField = new TextBox();
        final Label cityLabel = new Label("City: ");
        final TextBox cityField = new TextBox();
        final Label stateLabel = new Label("State: ");
        final TextBox stateField = new TextBox();
        final Label zipLabel = new Label("Zipcode: ");
        final TextBox zipField = new TextBox();
        final Label contactNameLabel = new Label("Contact Person Name: ");
        final TextBox contactNameField = new TextBox();
        final Label contactEmailLabel = new Label("Contact Person Email Address:");
        final TextBox contactEmailField = new TextBox();
        final Label telephoneLabel = new Label("Contact Person Telephone Number: ");
        final TextBox telephoneField = new TextBox();
        final Button buttonRegister = new Button("Register");
        final DialogBox dialogBox = new DialogBox();
        final Button closeButton = new Button("Close");
        final Label errorLabel = new Label();
        final HTML serverResponseLabel = new HTML();
        dialogBox.setAnimationEnabled(true);
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<br><b>Thank you! Your login username and password will be sent to your email.</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);
        fTable.setText(0, 0, "Attorney Information");
        fTable.setWidget(1, 0, firstNameLabel);
        fTable.setWidget(1, 1, firstNameField);
        fTable.setWidget(2, 0, middleNameLabel);
        fTable.setWidget(2, 1, middleNameField);
        fTable.setWidget(3, 0, lastNameLabel);
        fTable.setWidget(3, 1, lastNameField);
        fTable.setWidget(4, 0, attorneyEmailLable);
        fTable.setWidget(4, 1, attorneyEmailField);
        fTable.setWidget(5, 0, barCodeLabel);
        fTable.setWidget(5, 1, barCodeField);
        fTable.setWidget(5, 2, idLabel);
        fTable.setWidget(5, 3, idField);
        fTable.setText(6, 0, "Firm Information");
        fTable.setWidget(7, 0, firmNameLabel);
        fTable.setWidget(7, 1, firmNameField);
        fTable.setWidget(8, 0, firmAddressLabel);
        fTable.setWidget(8, 1, firmAddressField);
        fTable.setWidget(9, 0, cityLabel);
        fTable.setWidget(9, 1, cityField);
        fTable.setWidget(9, 2, stateLabel);
        fTable.setWidget(9, 3, stateField);
        fTable.setWidget(9, 4, zipLabel);
        fTable.setWidget(9, 5, zipField);
        fTable.setWidget(10, 0, contactNameLabel);
        fTable.setWidget(10, 1, contactNameField);
        fTable.setWidget(11, 0, contactEmailLabel);
        fTable.setWidget(11, 1, contactEmailField);
        fTable.setWidget(12, 0, telephoneLabel);
        fTable.setWidget(12, 1, telephoneField);
        fTable.setText(13, 0, "Is the information for the above Attorney Up To Date in his/her above Online Docketaccount?");
        RadioButton rb0 = new RadioButton("myRadioGroup", "Yes");
        RadioButton rb1 = new RadioButton("myRadioGroup", "No");
        FlowPanel panel = new FlowPanel();
        panel.add(rb0);
        panel.add(rb1);
        fTable.setWidget(14, 0, panel);
        fTable.setWidget(16, 0, buttonRegister);
        buttonRegister.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                errorLabel.setText("");
                if (!FieldVerifier.checkZip(zipField.getText())) {
                    errorLabel.setText("Please enter a valid zip code!");
                    zipField.setFocus(true);
                    return;
                }
                if (!FieldVerifier.checkPhone(telephoneField.getText())) {
                    errorLabel.setText("Please enter a valid phone number!");
                    telephoneField.setFocus(true);
                    return;
                }
                Registration reg = new Registration();
                reg.setAttorneyBarCode(barCodeField.getText());
                reg.setFirstName(firstNameField.getText());
                reg.setLastName(lastNameField.getText());
                reg.setAttorneyEmail(attorneyEmailField.getText());
                reg.setLawFirmName(firmNameField.getText());
                reg.setCity(cityField.getText());
                reg.setContactEmail(contactEmailField.getText());
                reg.setContactName(contactNameField.getText());
                reg.setContactPhone(telephoneField.getText());
                reg.setFirmAddress(firmAddressField.getText());
                reg.setID(idField.getText());
                reg.setState(stateField.getText());
                reg.setZip(zipField.getText());
                serverResponseLabel.setText("");
                service.register(reg, new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        Window.alert("Error " + caught);
                        serverResponseLabel.addStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(SERVER_ERROR);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }

                    public void onSuccess(String result) {
                        Window.alert(result);
                        dialogBox.setText("E-Filing Registration");
                        dialogBox.center();
                        closeButton.setFocus(true);
                        firstNameField.setText("");
                        middleNameField.setText("");
                        lastNameField.setText("");
                        attorneyEmailField.setText("");
                        barCodeField.setText("");
                        idField.setText("");
                        firmNameField.setText("");
                        firmAddressField.setText("");
                        cityField.setText("");
                        stateField.setText("");
                        zipField.setText("");
                        contactNameField.setText("");
                        contactEmailField.setText("");
                        telephoneField.setText("");
                    }
                });
            }
        });
        closeButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        RootPanel.get().add(fTable);
        RootPanel.get("errorLabelContainer").add(errorLabel);
    }
}
