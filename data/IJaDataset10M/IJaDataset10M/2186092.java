package org.activebpel.rt.bpel.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.activebpel.rt.bpel.IAeProcessInfoEvent;
import org.activebpel.rt.util.AeStaticConstantsMap;
import org.activebpel.rt.util.AeUtil;

/**
 * Implementation of bpel process information events.
 */
public class AeProcessInfoEvent extends AeBaseProcessEvent implements IAeProcessInfoEvent {

    /** Maps names and values of static constants declared in {@link IAeProcessInfoEvent}. **/
    private static final AeStaticConstantsMap mIAeProcessInfoEventConstantsMap = new AeStaticConstantsMap(IAeProcessInfoEvent.class);

    /**
    * Constructor with all members specified.
    * @param aPID The process ID of the event.
    * @param aPath The path of the object trigerring the event.
    * @param aEventID The event id of the event.
    * @param aFault The associated Fault, or empty.
    * @param aInfo Extra info to register with the event.
    */
    public AeProcessInfoEvent(long aPID, String aPath, int aEventID, String aFault, String aInfo) {
        super(aPID, aPath, aEventID, aFault, aInfo);
    }

    /**
    * Constructor with all members specified (including timestamp).
    * @param aPID The process ID of the event.
    * @param aPath The path of the object trigerring the event.
    * @param aEventID The event id of the event.
    * @param aFault The associated Fault, or empty.
    * @param aInfo Extra info to register with the event.
    * @param aTimestamp The event timestamp
    */
    public AeProcessInfoEvent(long aPID, String aPath, int aEventID, String aFault, String aInfo, Date aTimestamp) {
        super(aPID, aPath, aEventID, aFault, aInfo, aTimestamp);
    }

    /**
    * Constructor with no Fault or Ancillary Info.
    * @param aPID The process ID of the event.
    * @param aPath The path of the object trigerring the event.
    * @param aEventID The event id of the event.
    */
    public AeProcessInfoEvent(long aPID, String aPath, int aEventID) {
        this(aPID, aPath, aEventID, "", "");
    }

    /**
    * Returns the name of the specified event id.
    */
    protected static String getEventIdName(int aEventId) {
        String name = mIAeProcessInfoEventConstantsMap.getName(new Integer(aEventId));
        return (name != null) ? name : String.valueOf(aEventId);
    }

    /**
    * @see java.lang.Object#toString()
    */
    public String toString() {
        Map map = new LinkedHashMap();
        map.put("pid", String.valueOf(getPID()));
        map.put("eventid", getEventIdName(getEventID()));
        map.put("path", getNodePath());
        if (!AeUtil.isNullOrEmpty(getFaultName())) {
            map.put("fault", getFaultName());
        }
        if (!AeUtil.isNullOrEmpty(getAncillaryInfo())) {
            map.put("info", getAncillaryInfo());
        }
        return "AeProcessInfoEvent" + map.toString();
    }
}
