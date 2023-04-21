package de.gstpl.algo.itc;

import de.gstpl.data.DBFactory;
import de.gstpl.data.db4o.RoomDb4oImpl;

/**
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class ITCRoom extends RoomDb4oImpl {

    public ITCRoom() {
        this("defaultRoomName", DBFactory.getDefaultDB().getDBProperties().getDefaultRoomCapacity());
    }

    public ITCRoom(String s, int capacity) {
        setName(s);
        setCapacity(capacity);
    }
}
