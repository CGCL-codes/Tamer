package com.schwidder.petrinet.simulator.runtime;

import java.util.HashMap;
import com.schwidder.nucleus.runtime.CommandImpl;
import com.schwidder.nucleus.runtime.CommandMode;

public class QuitNetCommand extends CommandImpl {

    public QuitNetCommand(Integer netGroupIndex, Integer netIndex) {
        ((HashMap) getAttributes()).put("netgroup_index", new Integer(netGroupIndex));
        ((HashMap) getAttributes()).put("net_index", new Integer(netIndex));
    }

    public int getType() {
        return CommandMode.QUIT;
    }
}
