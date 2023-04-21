package gov.sns.apps.slacs;

import gov.sns.ca.*;
import gov.sns.xal.smf.impl.SCLCavity;

public class AutoRunHandler extends ChannelHandler {

    public AutoRunHandler(String name, SCLCavity cav, Controller cont, CavityController cc) {
        super(name, cav, cont);
        cavityController = cc;
    }

    private CavityController cavityController;

    /** The Connection Listener interface */
    @Override
    public void connectionMade(Channel chan) {
        if (monitor == null) makeMonitor();
    }

    @Override
    public void eventValue(ChannelRecord newRecord, Channel chan) {
        int val = newRecord.intValue();
        if (val == 4) controller.cavitySetAction(cavity, controller.IS_POWERED);
    }

    protected void putVal(int value) throws ChannelException {
        channel.putVal(value);
    }

    protected int getVal() throws ChannelException {
        return channel.getValEnum();
    }
}
