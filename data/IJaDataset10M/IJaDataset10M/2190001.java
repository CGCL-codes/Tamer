package com.arsenal.user.client;

import javax.swing.*;
import com.arsenal.user.client.CreateEditUserWindow;
import com.arsenal.client.MenuCommand;

public class EditUserItem extends JMenuItem implements MenuCommand {

    public EditUserItem() {
        super("Edit Users");
    }

    public void execute() {
        CreateEditUserWindow.getInstance().prepareWindowForUserEdit();
        CreateEditUserWindow.getInstance().show();
    }
}
