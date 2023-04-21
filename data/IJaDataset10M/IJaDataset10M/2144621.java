package org.dinopolis.gpstool.gpsinput.garmin;

import java.awt.Color;
import org.dinopolis.gpstool.gpsinput.GPSWaypoint;

/**
 * This class represents packets in Garmin data format D107.
 *
 * @author Christof Dallermassl,  Stefan Feitl
 * @version $Revision: 812 $
 */
public class GarminWaypointD107 extends GarminWaypointD103 {

    protected Color color_;

    protected short color_index_;

    protected float distance_;

    protected static final byte WAYPOINT_TYPE = 107;

    protected static final Color[] COLORS = new Color[] { Color.black, Color.red, Color.green, Color.blue };

    protected static final int DEFAULT_COLOR_INDEX = 0;

    /**
   * Default constructor 
   */
    public GarminWaypointD107() {
    }

    /**
   * Constructor from a byte buffer
   * @param buffer the buffer
   */
    public GarminWaypointD107(int[] buffer) {
        identification_ = GarminDataConverter.getGarminString(buffer, 2, 6).trim();
        latitude_ = GarminDataConverter.getGarminSemicircleDegrees(buffer, 8);
        longitude_ = GarminDataConverter.getGarminSemicircleDegrees(buffer, 12);
        comment_ = GarminDataConverter.getGarminString(buffer, 20, 40).trim();
        symbol_ = GarminDataConverter.getGarminByte(buffer, 60);
        symbol_type_ = SYMBOL_TYPE[symbol_];
        symbol_name_ = GarminWaypointSymbols.getSymbolName(symbol_type_);
        display_option_ = GarminDataConverter.getGarminByte(buffer, 61);
        distance_ = GarminDataConverter.getGarminFloat(buffer, 62);
        color_index_ = GarminDataConverter.getGarminByte(buffer, 63);
        if (color_index_ == 0xff) color_index_ = DEFAULT_COLOR_INDEX;
        color_ = COLORS[color_index_];
    }

    /**
   * Constructor from a garmin packet
   * @param pack the packet
   */
    public GarminWaypointD107(GarminPacket pack) {
        identification_ = pack.getNextAsString(6).trim();
        latitude_ = pack.getNextAsSemicircleDegrees();
        longitude_ = pack.getNextAsSemicircleDegrees();
        pack.getNextAsLongWord();
        comment_ = pack.getNextAsString(40).trim();
        symbol_ = pack.getNextAsByte();
        symbol_type_ = SYMBOL_TYPE[symbol_];
        symbol_name_ = GarminWaypointSymbols.getSymbolName(symbol_type_);
        display_option_ = pack.getNextAsByte();
        distance_ = pack.getNextAsFloat();
        color_index_ = pack.getNextAsByte();
        if (color_index_ == 0xff) color_index_ = DEFAULT_COLOR_INDEX;
        color_ = COLORS[color_index_];
    }

    /**
   * Copy Constructor
   * @param waypoint the other waypoint
   */
    public GarminWaypointD107(GPSWaypoint waypoint) {
        String tmp;
        int val = -1;
        tmp = waypoint.getIdentification();
        identification_ = tmp == null ? "" : tmp;
        latitude_ = waypoint.getLatitude();
        longitude_ = waypoint.getLongitude();
        tmp = waypoint.getComment();
        comment_ = tmp == null ? "" : tmp;
        symbol_type_ = (short) GarminWaypointSymbols.getSymbolId(waypoint.getSymbolName());
        if (symbol_type_ < 0) symbol_type_ = 18;
        for (int i = 0; i < SYMBOL_TYPE.length; i++) if (SYMBOL_TYPE[i] == symbol_type_) val = i;
        if (val == -1) val = 0;
        symbol_ = val;
        symbol_name_ = GarminWaypointSymbols.getSymbolName(symbol_type_);
        display_option_ = 0;
        distance_ = 0f;
        color_index_ = 0xff;
        if (color_index_ == 0xff) color_index_ = DEFAULT_COLOR_INDEX;
        color_ = COLORS[color_index_];
    }

    /**
 * Convert data type to {@link org.dinopolis.gpstool.gpsinput.garmin.GarminPacket}
 * @param packet_id the packet id
 * @return GarminPacket representing content of data type.
 */
    public GarminPacket toGarminPacket(int packet_id) {
        int data_length = 6 + 4 + 4 + 4 + 40 + 1 + 1 + 4 + 1;
        GarminPacket pack = new GarminPacket(packet_id, data_length);
        pack.setNextAsString(identification_, 6, false);
        pack.setNextAsSemicircleDegrees(latitude_);
        pack.setNextAsSemicircleDegrees(longitude_);
        pack.setNextAsLongWord(0);
        pack.setNextAsString(comment_, 40, false);
        pack.setNextAsByte(symbol_);
        pack.setNextAsByte(display_option_);
        pack.setNextAsFloat(distance_);
        pack.setNextAsByte(color_index_);
        return (pack);
    }

    /**
 * Get the Waypoint Color
 *
 * @return Waypoint Color
 */
    public Color getColor() {
        return (color_);
    }
}
