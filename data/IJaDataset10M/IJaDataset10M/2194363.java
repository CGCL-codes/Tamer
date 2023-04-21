package org.andrewwinter.jalker.command;

import org.andrewwinter.jalker.Privilege;

public class PrivsCmd extends Cmd {

    public PrivsCmd() {
        super(new String[] { "privs" }, new Privilege[] {});
    }
}
