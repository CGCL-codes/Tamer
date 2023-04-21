package org.dinopolis.gpstool.event;

import org.dinopolis.gpstool.map.MapInfo;

/**
 * 
 *
 * @author Christof Dallermassl
 * @version $Revision: 750 $
 */
public class MapsChangedEvent {

    public static int MAP_REMOVED = 0;

    public static int MAP_ADDED = 1;

    protected int action_;

    protected MapInfo map_info_;

    protected Object source_;

    /**
 * Constructor
 */
    public MapsChangedEvent(Object source, MapInfo info, int action) {
        source_ = source;
        map_info_ = info;
        action_ = action;
    }

    /**
 * Returns information about the map.
 *
 * @return information about the map added or removed.
 */
    public MapInfo getMapInfo() {
        return (map_info_);
    }

    /**
 * Returns the action.
 *
 * @return the action
 */
    public int getAction() {
        return (action_);
    }

    /**
 * Returns the source of the event.
 *
 * @return the source
 */
    public Object getSource() {
        return (source_);
    }
}
