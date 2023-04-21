package com.weespers.ui.player.actions;

import com.weespers.Activator;

public class StopAction extends PlayerAction {

    public StopAction() {
        super("Stop", Activator.getImageDescriptor("icons/stop.png"));
    }

    @Override
    public void run() {
        getPlayer().stop();
    }
}
