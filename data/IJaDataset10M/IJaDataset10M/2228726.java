package com.dukesoftware.ongakumusou.command;

import com.dukesoftware.ongakumusou.gui.main.IntegratedController;
import com.dukesoftware.utils.pattern.command.Command;

/**
 * 
 * @author 
 *
 */
public class PlayListClearCommand implements Command {

    private final IntegratedController controller;

    public PlayListClearCommand(IntegratedController controller) {
        this.controller = controller;
    }

    public void execute() {
        controller.clearPlayList();
    }
}
