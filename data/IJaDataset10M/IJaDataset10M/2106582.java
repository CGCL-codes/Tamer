package synthdrivers.TCElectronicGMajor;

public class TCModListItem {

    private int m_ofs;

    private String m_param;

    private int m_mod;

    private int m_min;

    private int m_mid;

    private int m_max;

    public TCModListItem(int ofs, String param, int mod, int min, int mid, int max) {
        m_ofs = ofs;
        m_param = param;
        m_mod = mod;
        m_min = min;
        m_mid = mid;
        m_max = max;
    }

    public String toString() {
        return m_param + "\t" + TCElectronicGMajorConst.modString[m_mod] + "\t" + m_min + "%" + "\t" + m_mid + "%" + "\t" + m_max + "%";
    }

    public int getOfs() {
        return m_ofs;
    }

    public String getParam() {
        return m_param;
    }

    public int getMod() {
        return m_mod;
    }

    public int getMin() {
        return m_min;
    }

    public int getMid() {
        return m_mid;
    }

    public int getMax() {
        return m_max;
    }
}
