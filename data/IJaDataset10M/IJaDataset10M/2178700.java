package org.andrewwinter.jalker.interactive.login;

import org.andrewwinter.jalker.OnlinePlayer;
import org.andrewwinter.jalker.interactive.InteractiveTask;

class NewUserScreen extends State {

    NewUserScreen(InteractiveTask loginProcess, OnlinePlayer onlinePlayer) {
        super(loginProcess, onlinePlayer);
    }

    @Override
    protected void enterState() {
        output("[Message for new users in here.]\n");
        enterToContinueMsg();
    }

    @Override
    protected void input(String str) {
        changeState(new NewbieHelpScreen(getTask(), getOnlinePlayer()));
    }
}
