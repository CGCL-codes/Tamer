package com.dukesoftware.ongakumusou.command;

import com.dukesoftware.ongakumusou.gui.main.IntegratedController;
import com.dukesoftware.utils.pattern.command.Command;

/**
 * 
 * 
 * @author 
 * @since 2007/11/21
 * @version last update 2007/11/21
 */
public class SnapshotClearCommand implements Command {

    private final IntegratedController controller;

    public SnapshotClearCommand(IntegratedController controller) {
        this.controller = controller;
    }

    public void execute() {
        controller.clearSnapshots();
    }
}
