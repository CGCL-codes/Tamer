package uk.co.q3c.deplan.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestPanel extends VerticalPanel {

    final Button sendButton = new Button("Send");

    final TextBox nameField = new TextBox();

    public TestPanel() {
        super();
        init();
    }

    protected void init() {
        nameField.setText("GWT User");
        sendButton.addStyleName("sendButton");
        add(nameField);
        add(sendButton);
    }
}
