package au.org.uptick.mygwtpapp.client.view;

import au.org.uptick.mygwtpapp.client.presenter.MainPagePresenter;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

    private static String html = "<h1>My GWTP Starter Project</h1>\n" + "<table align=\"center\">\n" + "  <tr>\n" + "    <td colspan=\"2\" style=\"font-weight:bold;\">Please enter your name:</td>\n" + "  </tr>\n" + "  <tr>\n" + "    <td id=\"nameFieldContainer\"></td>\n" + "    <td id=\"sendButtonContainer\"></td>\n" + "  </tr>\n" + "  <tr>\n" + "    <td colspan=\"2\" style=\"color:red;\" id=\"errorLabelContainer\"></td>\n" + "  </tr>\n" + "</table>\n";

    HTMLPanel panel = new HTMLPanel(html);

    private final TextBox nameField;

    private final Button sendButton;

    private final Label errorLabel;

    @Inject
    public MainPageView() {
        nameField = new TextBox();
        sendButton = new Button("Send");
        errorLabel = new Label();
        nameField.setText("GWTP User");
        panel.add(nameField, "nameFieldContainer");
        panel.add(sendButton, "sendButtonContainer");
        panel.add(errorLabel, "errorLabelContainer");
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public Button getSendButton() {
        return sendButton;
    }

    @Override
    public void resetAndFocus() {
        nameField.setFocus(true);
        nameField.selectAll();
    }

    @Override
    public void setError(String errorText) {
        errorLabel.setText(errorText);
    }
}
