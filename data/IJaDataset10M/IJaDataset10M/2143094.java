package org.andrewwinter.jalker.command;

import org.andrewwinter.jalker.Privilege;

public class GrantCmd extends Cmd {

    public GrantCmd() {
        super(new String[] { "grant" }, new Privilege[] { Privilege.Admin });
    }
}
