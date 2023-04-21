package es.viewerfree.gwt.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import es.viewerfree.gwt.client.ViewerFreeMessages;

public class BarPanel extends HorizontalPanel {

    private HorizontalPanel labelsPanel;

    private Label logoutLink;

    private Label userName;

    private final ViewerFreeMessages messages = GWT.create(ViewerFreeMessages.class);

    public BarPanel() {
        setStyleName("barPanel");
        add(getLabelsPanel());
        setCellHorizontalAlignment(getLabelsPanel(), HorizontalPanel.ALIGN_RIGHT);
    }

    private HorizontalPanel getLabelsPanel() {
        if (this.labelsPanel == null) {
            this.labelsPanel = new HorizontalPanel();
            this.labelsPanel.add(getUserName());
            this.labelsPanel.add(getLogoutLink());
        }
        return this.labelsPanel;
    }

    private Label getLogoutLink() {
        if (this.logoutLink == null) {
            this.logoutLink = new Logout(messages.logout());
        }
        return this.logoutLink;
    }

    public Label getUserName() {
        if (this.userName == null) {
            this.userName = new Label();
            this.userName.setStyleName("barLabel");
        }
        return this.userName;
    }
}
