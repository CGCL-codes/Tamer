package org.andrewwinter.jalker.command;

import org.andrewwinter.jalker.Privilege;

public class WallCmd extends Cmd {

    public WallCmd() {
        super(new String[] { "wall" }, new Privilege[] { Privilege.Superuser });
    }
}
