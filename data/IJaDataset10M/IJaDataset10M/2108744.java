package org.mobicents.slee.container.management.console.client.sleestate;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano Zappaterra
 * 
 */
public class SleeStateInfo implements IsSerializable {

    public SleeStateInfo() {
    }

    public static final String STOPPED = "STOPPED";

    public static final String STARTING = "STARTING";

    public static final String RUNNING = "RUNNING";

    public static final String STOPPING = "STOPPING";

    private String state;

    public SleeStateInfo(String state) {
        super();
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
