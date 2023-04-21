package com.timk.goserver.client.ui.screens;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.timk.goserver.client.model.ClientUserInfo;
import com.timk.goserver.client.services.NoSessionException;
import com.timk.goserver.client.ui.widgets.CenteredDialog;
import com.timk.goserver.client.ui.widgets.StatusLabel;

/**
 * Dialog for editing user profile
 * @author TKington
 *
 */
public class UserProfileDialog extends CenteredDialog {

    final StatusLabel status;

    private final TextBox nameText;

    private final TextBox emailText;

    private final TextBox rankText;

    private final ListBox notifyList;

    private final ListBox imageSizeList;

    ClientUserInfo user;

    /**
	 * Creates a UserProfileDialog
	 */
    public UserProfileDialog() {
        super(null);
        setText("User Profile");
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setWidth("250px");
        mainPanel.setSpacing(2);
        status = new StatusLabel();
        mainPanel.add(status);
        HorizontalPanel namePanel = new HorizontalPanel();
        mainPanel.add(namePanel);
        Label nameLabel = new Label("Full Name:");
        namePanel.add(nameLabel);
        nameText = new TextBox();
        namePanel.add(nameText);
        namePanel.setCellVerticalAlignment(nameLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        namePanel.setWidth("100%");
        nameLabel.setWidth("100%");
        nameText.setWidth("100%");
        namePanel.setCellWidth(nameLabel, "33%");
        namePanel.setCellWidth(nameText, "67%");
        HorizontalPanel emailPanel = new HorizontalPanel();
        mainPanel.add(emailPanel);
        Label emailLabel = new Label("Email:");
        emailPanel.add(emailLabel);
        emailText = new TextBox();
        emailPanel.add(emailText);
        emailPanel.setCellVerticalAlignment(emailLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        emailPanel.setWidth("100%");
        emailLabel.setWidth("100%");
        emailText.setWidth("100%");
        emailPanel.setCellWidth(emailLabel, "33%");
        emailPanel.setCellWidth(emailText, "67%");
        HorizontalPanel rankPanel = new HorizontalPanel();
        mainPanel.add(rankPanel);
        Label rankLabel = new Label("Rank:");
        rankPanel.add(rankLabel);
        rankText = new TextBox();
        rankText.setEnabled(false);
        rankPanel.add(rankText);
        rankPanel.setCellVerticalAlignment(rankLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        rankPanel.setWidth("100%");
        rankLabel.setWidth("100%");
        rankText.setWidth("100%");
        rankPanel.setCellWidth(rankLabel, "33%");
        rankPanel.setCellWidth(rankText, "67%");
        HorizontalPanel notifyPanel = new HorizontalPanel();
        mainPanel.add(notifyPanel);
        Label notifyLabel = new Label("E-mail:");
        notifyPanel.add(notifyLabel);
        notifyList = new ListBox();
        notifyList.setVisibleItemCount(1);
        notifyList.addItem("None");
        notifyList.addItem("Notification");
        notifyList.addItem("Full Board");
        notifyPanel.add(notifyList);
        notifyPanel.setCellVerticalAlignment(notifyLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        notifyPanel.setWidth("100%");
        notifyLabel.setWidth("100%");
        notifyList.setWidth("100%");
        notifyPanel.setCellWidth(notifyLabel, "33%");
        notifyPanel.setCellWidth(notifyList, "67%");
        HorizontalPanel imageSizePanel = new HorizontalPanel();
        mainPanel.add(imageSizePanel);
        Label imageSizeLabel = new Label("Image size:");
        imageSizePanel.add(imageSizeLabel);
        imageSizeList = new ListBox();
        imageSizeList.setVisibleItemCount(1);
        imageSizeList.addItem("17");
        imageSizeList.addItem("21");
        imageSizeList.addItem("25");
        imageSizePanel.add(imageSizeList);
        imageSizePanel.setCellVerticalAlignment(imageSizeLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        imageSizePanel.setWidth("100%");
        imageSizeLabel.setWidth("100%");
        imageSizeList.setWidth("100%");
        imageSizePanel.setCellWidth(imageSizeLabel, "33%");
        imageSizePanel.setCellWidth(imageSizeList, "67%");
        HorizontalPanel buttonPanel = new HorizontalPanel();
        mainPanel.add(buttonPanel);
        mainPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
        Button submitButton = new Button("Submit");
        submitButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                onSubmit();
            }
        });
        submitButton.setWidth("60px");
        buttonPanel.add(submitButton);
        Button closeButton = new Button("Close");
        closeButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                hide();
            }
        });
        closeButton.setWidth("60px");
        buttonPanel.add(closeButton);
        setWidget(mainPanel);
        MainPanel.userService.getUser(MainPanel.loggedInUser.getUsername(), new AsyncCallback() {

            public void onSuccess(Object result) {
                user = (ClientUserInfo) result;
                fillDialog();
            }

            public void onFailure(Throwable caught) {
                if (caught instanceof NoSessionException) {
                    MainPanel.sessionExpired(caught.getMessage());
                    return;
                }
                status.setError("Error getting current user: " + caught.getMessage());
            }
        });
    }

    void fillDialog() {
        nameText.setText(user.getFullname());
        emailText.setText(user.getEmail());
        rankText.setText(user.getRank());
        switch(user.getNotification()) {
            case ClientUserInfo.NOTIFICATION_NONE:
                notifyList.setSelectedIndex(0);
                break;
            case ClientUserInfo.NOTIFICATION_NOTIFY:
                notifyList.setSelectedIndex(1);
                break;
            case ClientUserInfo.NOTIFICATION_FULL_BOARD:
                notifyList.setSelectedIndex(2);
                break;
            default:
                status.setError("Unknown notification setting: " + user.getNotification());
                break;
        }
        switch(user.getImageSize()) {
            case 17:
                imageSizeList.setSelectedIndex(0);
                break;
            case 21:
                imageSizeList.setSelectedIndex(1);
                break;
            case 25:
                imageSizeList.setSelectedIndex(2);
                break;
            default:
                status.setError("Unknown image size: " + user.getImageSize());
                break;
        }
    }

    void onSubmit() {
        user.setFullname(nameText.getText());
        user.setEmail(emailText.getText());
        switch(notifyList.getSelectedIndex()) {
            case 0:
                user.setNotification(ClientUserInfo.NOTIFICATION_NONE);
                break;
            case 1:
                user.setNotification(ClientUserInfo.NOTIFICATION_NOTIFY);
                break;
            case 2:
                user.setNotification(ClientUserInfo.NOTIFICATION_FULL_BOARD);
                break;
            default:
                status.setError("Unknown notification selection: " + user.getNotification());
                break;
        }
        switch(imageSizeList.getSelectedIndex()) {
            case 0:
                user.setImageSize(17);
                break;
            case 1:
                user.setImageSize(21);
                break;
            case 2:
                user.setImageSize(25);
                break;
            default:
                status.setError("Unknown image size selection: " + imageSizeList.getSelectedIndex());
                break;
        }
        MainPanel.userService.setUserInfo(user, new AsyncCallback() {

            public void onSuccess(Object result) {
                status.clear();
                BoardPanel.setImageSize(user.getImageSize());
                hide();
            }

            public void onFailure(Throwable caught) {
                if (caught instanceof NoSessionException) {
                    MainPanel.sessionExpired(caught.getMessage());
                    return;
                }
                status.setError("Error saving settings: " + caught.getMessage());
            }
        });
    }
}
