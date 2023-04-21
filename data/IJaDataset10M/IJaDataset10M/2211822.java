package org.andrewwinter.jalker.command;

import org.andrewwinter.jalker.OnlinePlayer;
import org.andrewwinter.jalker.Privilege;

public class ClearCmd extends Cmd {

    public ClearCmd() {
        super(new String[] { "clear" }, new Privilege[] {});
    }

    @Override
    public void execute(OnlinePlayer p, String str) {
        p.tell("\033[2J");
    }
}
