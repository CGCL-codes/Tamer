package org.andrewwinter.jalker.command;

import org.andrewwinter.jalker.ListFlag;

public class InviteCmd extends ListChangeStyleCmd {

    public InviteCmd() {
        super(new String[] { "invite" }, ListFlag.Invite, "You allow %1$s to enter your room(s).", "You no longer allow %1$s to enter your room(s).");
    }
}
