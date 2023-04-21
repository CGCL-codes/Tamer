package com.cosylab.vdct.events.commands;

import com.cosylab.vdct.events.Command;
import com.cosylab.vdct.graphics.DsManagerInterface;

/**
 * @author ssah
 *
 */
public class GetDsManager extends Command {

    private DsManagerInterface manager = null;

    public GetDsManager(DsManagerInterface manager) {
        this.manager = manager;
    }

    public void execute() {
    }

    public DsManagerInterface getManager() {
        return manager;
    }
}
