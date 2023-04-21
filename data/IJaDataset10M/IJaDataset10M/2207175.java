package com.newisys.behsim;

import com.newisys.verilog.ObjectType;
import com.newisys.verilog.VerilogCallback;
import com.newisys.verilog.VerilogObject;
import com.newisys.verilog.VerilogSchedEvent;

/**
 * Provides access to a scheduled event.
 * 
 * @author Scott Diesing
 */
public class BehavioralSchedEvent implements VerilogSchedEvent {

    private final VerilogCallback callback;

    private final VerilogObject object;

    private final boolean scheduled;

    BehavioralSchedEvent(VerilogObject object, VerilogCallback callback, boolean scheduled) {
        this.callback = callback;
        this.scheduled = scheduled;
        this.object = object;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void cancel() {
        if (callback != null) {
            callback.cancel();
        }
    }

    public ObjectType getType() {
        return object.getType();
    }
}
