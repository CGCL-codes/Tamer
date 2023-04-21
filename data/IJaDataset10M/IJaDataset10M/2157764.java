package com.quikj.ace.web.client.presenter;

import com.google.gwt.user.client.ui.Widget;
import com.quikj.ace.web.client.ApplicationController;
import com.quikj.ace.web.client.view.LogoutPanel;
import com.quikj.ace.web.client.view.desktop.DesktopLogoutPanel;

/**
 * @author beckie
 * 
 */
public class LogoutPresenter {

    private LogoutPanel view;

    private static LogoutPresenter instance;

    public LogoutPresenter() {
        instance = this;
    }

    public static LogoutPresenter getCurrentInstance() {
        return instance;
    }

    public void processLogout() {
        view.logOut();
    }

    public void loggedOut() {
        ApplicationController.getInstance().cleanupSessionsAndCommunications("user logged out");
    }

    public void show() {
        view = createView();
        view.setPresenter(this);
        String html = "<span style=' text-decoration:underline;'>" + ApplicationController.getMessages().LogoutPresenter_logout() + "</span>";
        UserPanelPresenter.getCurrentInstance().addNewPanel(html, (Widget) view);
    }

    private LogoutPanel createView() {
        return new DesktopLogoutPanel();
    }

    public void dispose() {
        instance = null;
    }
}
