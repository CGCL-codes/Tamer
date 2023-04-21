package com.platform.client.gui;

import com.platform.domain.User;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.layout.HLayout;

public class StudentGUI extends HLayout {

    User user;

    UserMenu menu;

    UserHome home;

    public StudentGUI(User authenticatedUser) {
        user = authenticatedUser;
        init();
    }

    public StudentGUI() {
        init();
    }

    private void init() {
        menu = new UserMenu();
        menu.setPosition(Positioning.ABSOLUTE);
        menu.setLeft(0);
        menu.setTop(0);
        home = new UserHome();
        home.setPosition(Positioning.ABSOLUTE);
        home.setLeft(300);
        home.setTop(35);
        drawItems();
    }

    private void drawItems() {
        addChild(menu);
        addChild(home);
    }
}
