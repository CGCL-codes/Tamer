package com.bbn.feupdater.gui;

import jfireeagle.FireEagleClient;
import jfireeagle.User;

public class UserLocationForm extends AbstractForm {

    public UserLocationForm(FireEagleClient c) {
        super(c, 0);
        this.clear.setVisible(false);
        this.submit.setText("send user request");
    }

    @Override
    protected Object onSubmit() {
        User u = this.client.getUserLocation();
        return u;
    }
}
