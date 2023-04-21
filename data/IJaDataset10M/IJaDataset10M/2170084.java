package org.dinopolis.gpstool.gpsinput.garmin;

/**
 * D802 is an extension to D800 and has some additional (unknown) fields.
 *
 * @author Christof Dallermassl
 * @version $Revision: 694 $
 */
public class GarminPVTD802 extends GarminPVTD800 {

    public GarminPVTD802(int[] buffer) {
        super(buffer);
    }

    public GarminPVTD802(GarminPacket pack) {
        super(pack);
    }

    /**
 * Print PVT data in human readable form.
 * @return string representation of this object.
 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("GarminPVTD802[");
        buffer.append("alt=").append(alt_).append(", ");
        buffer.append("epe=").append(epe_).append(", ");
        buffer.append("eph=").append(eph_).append(", ");
        buffer.append("epv=").append(epv_).append(", ");
        buffer.append("fix=").append(fix_).append(", ");
        buffer.append("tow=").append(tow_).append(", ");
        buffer.append("lat=").append(lat_).append(", ");
        buffer.append("lon=").append(lon_).append(", ");
        buffer.append("east=").append(east_).append(", ");
        buffer.append("north=").append(north_).append(", ");
        buffer.append("up=").append(up_).append(", ");
        buffer.append("msl_height=").append(msl_height_).append(", ");
        buffer.append("leap_seconds=").append(leap_seconds_).append(", ");
        buffer.append("wn_days=").append(wn_days_);
        buffer.append("]");
        return (buffer.toString());
    }
}
