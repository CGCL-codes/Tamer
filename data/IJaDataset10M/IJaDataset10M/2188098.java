package gps;

import bt747.sys.Convert;

/**Class to write a PLT file (OZI).
 * @author Mario De Weerd
 */
public class GPSPLTFile extends GPSFile {

    public void writeFileHeader(final String s) {
        super.writeFileHeader(s);
        writeTxt("BT747 Track Point File http://sf.net/projects/bt747 Version " + bt747.Version.VERSION_NUMBER + "\r\n" + "WGS 84\r\n" + "Altitude is in feet\r\n" + "Reserved 3\r\n" + "0,2,255,BT747 Track,0,0,2,8421376\r\n" + "50000\r\n");
    }

    /**
     * Returns true when the record is used by the format.
     * 
     * Override parent class because only the trackpoint filter is used.
     */
    protected boolean recordIsNeeded(GPSRecord s) {
        return m_Filters[GPSFilter.C_TRKPT_IDX].doFilter(s);
    }

    public void writeRecord(GPSRecord s) {
        super.writeRecord(s);
        boolean prevField = false;
        if (activeFields != null && m_Filters[GPSFilter.C_TRKPT_IDX].doFilter(s)) {
            String rec = "";
            if (activeFields.latitude != 0) {
                rec += Convert.toString(s.latitude, 6);
            }
            rec += ",";
            if (activeFields.longitude != 0) {
                rec += Convert.toString(s.longitude, 6);
            }
            rec += ",";
            rec += "0,";
            if (activeFields.height != 0) {
                rec += Convert.toString((int) (s.height * 3.2808398950131233595800524934383));
            } else {
                rec += "-777";
            }
            rec += ",";
            if (activeFields.utc != 0) {
                rec += Convert.toString((s.utc + (activeFields.milisecond != 0 ? (s.milisecond / 1000.0) : 0)) / 86400.0 + 25569, 7);
                rec += ",";
                rec += (t.getMonth() < 10 ? "0" : "") + Convert.toString(t.getMonth()) + "/" + (t.getDay() < 10 ? "0" : "") + Convert.toString(t.getDay()) + "/" + Convert.toString(t.getYear()) + "," + (t.getHour() < 10 ? "0" : "") + Convert.toString(t.getHour()) + ":" + (t.getMinute() < 10 ? "0" : "") + Convert.toString(t.getMinute()) + ":" + (t.getSecond() < 10 ? "0" : "") + Convert.toString(t.getSecond());
                if (activeFields.milisecond != 0) {
                    rec += ".";
                    rec += (s.milisecond < 100) ? "0" : "";
                    rec += (s.milisecond < 10) ? "0" : "";
                    rec += Convert.toString(s.milisecond);
                }
            } else {
                rec += ",,";
            }
            rec += "\r\n";
            writeTxt(rec);
        }
    }
}
