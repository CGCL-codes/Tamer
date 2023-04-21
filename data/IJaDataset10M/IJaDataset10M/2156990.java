package javasean.DataModel;

import java.util.StringTokenizer;

public class DMContr {

    private String m_AccID;

    private String m_Version;

    private String m_Date;

    public DMContr(String the_AccID, String the_Version, String the_Date) {
        m_AccID = the_AccID;
        m_Version = the_Version;
        m_Date = the_Date;
    }

    /**
* Parser used by FileAccessGBXML.java
*/
    public DMContr(String the_wholeEnchilada) {
        StringTokenizer stok = new StringTokenizer(the_wholeEnchilada);
        if (stok.hasMoreTokens()) {
            String AccID = stok.nextToken();
            if (AccID != null) {
                setAccID(AccID);
            }
        }
        if (stok.hasMoreTokens()) {
            String Version = stok.nextToken();
            if (Version != null) {
                setVersion(Version);
            }
        }
        if (stok.hasMoreTokens()) {
            String Date = stok.nextToken();
            if (Date != null) {
                setDate(Date);
            }
        }
    }

    public void setAccID(String the_AccID) {
        m_AccID = the_AccID;
    }

    public String getAccID() {
        return m_AccID;
    }

    public void setVersion(String the_Version) {
        m_Version = the_Version;
    }

    public String getVersion() {
        return m_Version;
    }

    public void setDate(String the_Date) {
        m_Date = the_Date;
    }

    public String getDate() {
        return m_Date;
    }

    public String toString() {
        String ret = null;
        if (m_AccID != null) {
            ret = m_AccID;
        }
        if (m_Version != null) {
            ret += " " + m_Version;
        }
        if (m_Date != null) {
            ret += " " + m_Date;
        }
        return ret;
    }

    public void Display() {
        System.out.println("\tAccID   <" + m_AccID + ">");
        System.out.println("\tVersion <" + m_Version + ">");
        System.out.println("\tDate    <" + m_Date + ">");
    }
}
