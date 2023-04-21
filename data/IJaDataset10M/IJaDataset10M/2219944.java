package com.platform.client.gui;

import com.platform.domain.User;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class UserHome extends HLayout {

    User user;

    TextItem lastLogin;

    TextItem firstName;

    TextItem lastName;

    TextItem dateOfBirth;

    TextItem username;

    TextItem userType;

    Window home;

    public UserHome(User authenticatedUser) {
        user = authenticatedUser;
        init();
    }

    public UserHome() {
        init();
    }

    private void init() {
        lastLogin = new TextItem("lastLogin");
        lastLogin.setTitle("Last visit: ");
        lastLogin.setAttribute("readOnly", true);
        firstName = new TextItem("firstName");
        firstName.setTitle("First name: ");
        firstName.setAttribute("readOnly", true);
        lastName = new TextItem("lastName");
        lastName.setTitle("Last name:");
        lastName.setAttribute("readOnly", true);
        dateOfBirth = new TextItem("dateOfBirth");
        dateOfBirth.setTitle("Date of birth: ");
        dateOfBirth.setAttribute("readOnly", true);
        username = new TextItem("username");
        username.setTitle("Welcome ");
        username.setAttribute("readOnly", true);
        userType = new TextItem("userType");
        userType.setTitle("Type of user: ");
        userType.setAttribute("readOnly", true);
        home = new Window();
        home.setTitle("Home - user details");
        home.setWidth(700);
        home.setHeight(300);
        home.setCanDragReposition(false);
        home.setCanDrag(false);
        home.setCanDragResize(false);
        home.setShowCloseButton(false);
        home.setShowMaximizeButton(false);
        home.setShowMinimizeButton(false);
        DynamicForm profileDetails = new DynamicForm();
        profileDetails.setNumCols(2);
        profileDetails.setColWidths(100, 700);
        profileDetails.setFields(username, lastLogin, userType, firstName, lastName, dateOfBirth);
        profileDetails.setPosition(Positioning.ABSOLUTE);
        profileDetails.setLeft(100);
        profileDetails.setTop(35);
        home.addChild(profileDetails);
        this.addChild(home);
    }
}
