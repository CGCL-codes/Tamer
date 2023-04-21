package com.hba.web.lib.client.ui.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.hba.web.app.client.Application;
import com.hba.web.app.server.business.User;
import com.hba.web.lib.client.utils.ActionHandler;
import com.hba.web.lib.client.utils.Parameter;

public class JMenuBar extends MenuBar {

    private MenuItem homeMenuItem = new MenuItem(Application.constants.homeMenuItem(), new Command() {

        @Override
        public void execute() {
            Parameter parameter = new Parameter();
            parameter.setCurrentModule(Application.get().DEFAULT_MODULE);
            Application.get().gotoPanel(Application.get().DEFAULT_MODULE, parameter);
        }
    });

    private MenuItem userMenuItem = new MenuItem(Application.constants.userMenuItem(), new Command() {

        @Override
        public void execute() {
            Parameter parameter = new Parameter();
            parameter.setCurrentModule(User.MODULE_NAME);
            parameter.setAction(ActionHandler.SEARCH_ACTION);
            parameter.setTargetModule(User.MODULE_NAME);
            Application.get().gotoPanel(User.MODULE_NAME, parameter);
        }
    });

    public JMenuBar() {
        this.addItem(homeMenuItem);
        this.addItem(userMenuItem);
    }

    public void initMenuItem(MenuItem aMenuItem) {
        if (aMenuItem == null) return;
        this.addItem(aMenuItem);
    }
}
