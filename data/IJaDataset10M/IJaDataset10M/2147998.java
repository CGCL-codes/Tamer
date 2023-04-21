package info.danshin.payperreply.client;

import info.danshin.payperreply.login.client.OptionalTextBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Payperreply implements EntryPoint {

    /**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
    private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network " + "connection and try again.";

    /**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        RootPanel.get().add(new OptionalTextBox("Da boom"));
        final Button sendButton = new Button("Don't Send");
        final TextBox nameField = new TextBox();
        nameField.setText("GWT User");
        sendButton.addStyleName("sendButton");
        RootPanel.get("nameFieldContainer").add(nameField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        nameField.setFocus(true);
        nameField.selectAll();
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Remote Procedure Call");
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button("Close");
        closeButton.getElement().setId("closeButton");
        final Label textToServerLabel = new Label();
        final HTML serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);
        closeButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                dialogBox.hide();
                sendButton.setEnabled(true);
                sendButton.setFocus(true);
            }
        });
        class MyHandler implements ClickHandler, KeyUpHandler {

            /**
			 * Fired when the user clicks on the sendButton.
			 */
            public void onClick(ClickEvent event) {
                sendNameToServer();
            }

            /**
			 * Fired when the user types in the nameField.
			 */
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sendNameToServer();
                }
            }

            /**
			 * Send the name from the nameField to the server and wait for a response.
			 */
            private void sendNameToServer() {
                sendButton.setEnabled(false);
                String textToServer = nameField.getText();
                textToServerLabel.setText(textToServer);
                serverResponseLabel.setText("");
                greetingService.greetServer(textToServer, new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        dialogBox.setText("Remote Procedure Call - Failure");
                        serverResponseLabel.addStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(SERVER_ERROR);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }

                    public void onSuccess(String result) {
                        dialogBox.setText("Remote Procedure Call");
                        serverResponseLabel.removeStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(result);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }
                });
            }
        }
        MyHandler handler = new MyHandler();
        sendButton.addClickHandler(handler);
        nameField.addKeyUpHandler(handler);
    }
}
