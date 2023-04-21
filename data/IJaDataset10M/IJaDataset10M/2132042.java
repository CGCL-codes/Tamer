package inglar.autotc.client.ui;

import inglar.autotc.client.HostService;
import inglar.autotc.client.HostServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.CheckBox;

public class PageTestConsole extends Composite {

    private static final String RESPONSE_WIDTH = "600px";

    private static final String RESPONSE_HEIGHT = "450px";

    private static final String COMMAND_WIDTH = "360px";

    private static final String RESPONSE = "width: " + RESPONSE_WIDTH + "; height: " + RESPONSE_HEIGHT + "; margin-top: 2px;";

    private static final String RESPONSE_DISABLE = RESPONSE + " font-style: italic; color: gray;";

    private static final String RESPONSE_ENABLE = RESPONSE + " color: black";

    private static final String RESPONSE_ERROR = RESPONSE + " color: red;";

    private TextBox txtbxCommand;

    private Button btnSend;

    private TextArea txtrResponse;

    private CheckBox chckbxAsScript;

    private HostServiceAsync consoleSvc = null;

    private AsyncCallback<String> consoleSvcCallback = null;

    public PageTestConsole() {
        FlowPanel flowPanel = new FlowPanel();
        initWidget(flowPanel);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        flowPanel.add(horizontalPanel);
        Label lblCommand = new Label("Command:");
        horizontalPanel.add(lblCommand);
        txtbxCommand = new TextBox();
        txtbxCommand.setText("command");
        horizontalPanel.add(txtbxCommand);
        txtbxCommand.setWidth(COMMAND_WIDTH);
        txtbxCommand.getElement().setAttribute("style", "margin-left: 10px; margin-right: 10px; width: " + COMMAND_WIDTH + ";");
        txtbxCommand.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    sendData();
                }
            }
        });
        txtbxCommand.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                txtbxCommand.setSelectionRange(0, txtbxCommand.getText().length());
            }
        });
        chckbxAsScript = new CheckBox("As script");
        horizontalPanel.add(chckbxAsScript);
        chckbxAsScript.getElement().setAttribute("style", "margin-right: 10px");
        chckbxAsScript.setValue(true);
        chckbxAsScript.setFormValue("checked");
        btnSend = new Button("Send");
        horizontalPanel.add(btnSend);
        btnSend.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                sendData();
            }
        });
        VerticalPanel verticalPanel = new VerticalPanel();
        flowPanel.add(verticalPanel);
        Label lblResponse = new Label("Response:");
        verticalPanel.add(lblResponse);
        txtrResponse = new TextArea();
        txtrResponse.setReadOnly(true);
        txtrResponse.setText("response");
        verticalPanel.add(txtrResponse);
        txtrResponse.setSize(RESPONSE_WIDTH, RESPONSE_HEIGHT);
        txtrResponse.getElement().setAttribute("style", RESPONSE_DISABLE);
        consoleSvc = HostService.Util.getInstance();
        consoleSvcCallback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                String errorMessage = caught.getMessage();
                txtrResponse.setText(errorMessage);
                txtrResponse.setEnabled(true);
                txtrResponse.getElement().setAttribute("style", RESPONSE_ERROR);
                btnSend.setEnabled(true);
            }

            public void onSuccess(String result) {
                String _t = "";
                if (result == null) _t = "<consoleSvcCallback> returns null"; else if (result.isEmpty()) _t = "Remote console: there is nothing to show"; else _t = result;
                txtrResponse.setText(_t);
                txtrResponse.setEnabled(true);
                txtrResponse.getElement().setAttribute("style", RESPONSE_ENABLE);
                btnSend.setEnabled(true);
            }
        };
    }

    private void sendData() {
        txtrResponse.setEnabled(false);
        btnSend.setEnabled(false);
        String data = txtbxCommand.getText().trim();
        if (chckbxAsScript.getValue()) consoleSvc.scriptase(data, consoleSvcCallback); else consoleSvc.exec(data, consoleSvcCallback);
    }

    public void ready() {
        txtbxCommand.setFocus(true);
        txtbxCommand.setSelectionRange(0, txtbxCommand.getText().length());
    }

    public void reset() {
        txtbxCommand.setText("");
        txtrResponse.getElement().setAttribute("style", RESPONSE_DISABLE);
        txtrResponse.setText("response");
        ready();
    }
}
