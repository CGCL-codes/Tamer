package jmri.jmrix.secsi;

/**
 * Returns a list of valid lenz XpressNet Connection Types
 * <P>
 * @author      Bob Jacobsen   Copyright (C) 2010
 * @author      Kevin Dickerson    Copyright (C) 2010
 * @version	$Revision: 1.1 $
 *
 */
public class SerialConnectionTypeList implements jmri.jmrix.ConnectionTypeList {

    public String[] getAvailableProtocolClasses() {
        return new String[] { "jmri.jmrix.secsi.serialdriver.ConnectionConfig" };
    }
}
