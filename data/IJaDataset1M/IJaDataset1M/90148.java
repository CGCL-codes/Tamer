package edu.sdsc.rtdsm.drivers.turbine.util;

import java.util.Vector;
import edu.sdsc.rtdsm.framework.util.Debugger;

public class TurbineSinkWrapper extends TurbineClientWrapper {

    public Vector<Integer> channelModeVec = new Vector<Integer>();

    public Vector<Integer> channelIntervalOrTimeoutVec = new Vector<Integer>();

    public TurbineSinkWrapper(TurbineServer server) {
        super(server);
    }

    public void addChannel(String channelName, Integer datatype, Integer mode, Integer intervalOrTimeout) {
        addChannel(channelName, datatype);
        channelModeVec.addElement(mode);
        channelIntervalOrTimeoutVec.addElement(intervalOrTimeout);
    }

    public int getRequestMode() {
        if (channelModeVec == null || channelModeVec.size() == 0) {
            throw new IllegalStateException("Trying to fetch the request mode " + "for a sink with no channels");
        }
        return channelModeVec.elementAt(0).intValue();
    }

    public int getTimeout() {
        if (channelIntervalOrTimeoutVec == null || channelIntervalOrTimeoutVec.size() == 0) {
            throw new IllegalStateException("Trying to fetch the request mode " + "for a sink with no channels");
        }
        return channelIntervalOrTimeoutVec.elementAt(0).intValue();
    }

    public Vector<Integer> getPollIntervals() {
        return channelIntervalOrTimeoutVec;
    }

    public void printSinkInfo(int level) {
        Debugger.debug(level, "\n\tChannel params:");
        for (int i = 0; i < channelNamesVec.size(); i++) {
            Debugger.debug(level, "\t\t" + (i + 1) + " Channel name:" + channelNamesVec.elementAt(i));
            Debugger.debug(level, "\t\t" + (i + 1) + " Channel type:" + channelDatatypesVec.elementAt(i));
            Debugger.debug(level, "\t\t" + (i + 1) + " Channel requestMode= " + channelModeVec.elementAt(i));
            Debugger.debug(level, "\t\t" + (i + 1) + " Channel timeout= " + channelIntervalOrTimeoutVec.elementAt(i));
        }
    }

    public void resetChannelVecs(Vector<String> channelVec, Vector<Integer> channelDatatypeVec, Vector<Integer> reqModeVec, Vector<Integer> intervalOrToutVec) {
        super.resetChannelVecs(channelVec, channelDatatypeVec);
        if (channelVec.size() != reqModeVec.size() || channelVec.size() != intervalOrToutVec.size()) {
            throw new IllegalArgumentException("The channel vector and its " + "request modes and timeouts/intervals should be of the same size");
        }
        this.channelModeVec = reqModeVec;
        this.channelIntervalOrTimeoutVec = intervalOrToutVec;
    }
}
