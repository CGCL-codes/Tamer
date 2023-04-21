package de.hartmut.gwt.client;

import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author hartmut
 */
public class StatusWidget extends Composite {

    private Label statusLabel;

    private HTML lupoLink;

    public StatusWidget() {
        VerticalPanel panel = new VerticalPanel();
        HorizontalPanel statusPanel = new HorizontalPanel();
        statusLabel = new Label();
        statusLabel.setStyleName("statusText");
        statusPanel.add(statusLabel);
        HorizontalPanel versionPanel = new HorizontalPanel();
        lupoLink = new HTML("<a href=\"http://kenai.com/projects/lupo\">Lupo</a>: v" + lupoEntryPoint.LUPO_VERSION);
        lupoLink.setStyleName("statusVersionText");
        versionPanel.add(lupoLink);
        panel.add(statusPanel);
        panel.add(versionPanel);
        initWidget(panel);
    }

    public void clearStatus() {
        statusLabel.setText("");
        statusLabel.setVisible(false);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
        statusLabel.setStyleName("statusText");
        statusLabel.setVisible(true);
    }

    public void setWarningStatus(String status) {
        statusLabel.setText(status);
        statusLabel.setStyleName("statusWarningText");
        statusLabel.setVisible(true);
    }

    public void setErrorStatus(String status) {
        statusLabel.setText(status);
        statusLabel.setStyleName("statusErrorText");
        statusLabel.setVisible(true);
    }
}
