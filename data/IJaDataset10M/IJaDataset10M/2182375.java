package net.sourceforge.tnv.ui;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * TNVPreferenceData
 */
public class TNVPreferenceData {

    private transient ChangeEvent preferenceChangeEvent = null;

    private EventListenerList listenerList = new EventListenerList();

    public static final int SORT_ARRIVAL = 0;

    public static final int SORT_ARRIVAL_REVERSE = 1;

    public static final int SORT_FREQUENCY = 2;

    public static final int SORT_FREQUENCY_REVERSE = 3;

    public static final int SORT_ALPHA = 4;

    public static final int SORT_ALPHA_REVERSE = 5;

    private static final String PROPERTIES_FILE = "config/tnv.properties";

    private static final int DEFAULT_LOCAL_SORT = SORT_ARRIVAL;

    private static final int DEFAULT_REMOTE_SORT = SORT_ARRIVAL;

    private static final int DEFAULT_COLUMN_COUNT = 5;

    private static final int DEFAULT_ROW_HEIGHT = 75;

    private static final boolean DEFAULT_SHOW_TOOLTIPS = true;

    private static final boolean DEFAULT_CURVED_LINKS = false;

    private static final boolean DEFAULT_SHOW_PACKETS = true;

    private static final boolean DEFAULT_SHOW_FLAGS = false;

    private static final Color DEFAULT_TCP_COLOR = new Color(27, 158, 119);

    private static final Color DEFAULT_UDP_COLOR = new Color(217, 95, 2);

    private static final Color DEFAULT_ICMP_COLOR = new Color(107, 102, 169);

    private static final Color DEFAULT_SYN_COLOR = new Color(166, 206, 227);

    private static final Color DEFAULT_ACK_COLOR = new Color(31, 120, 180);

    private static final Color DEFAULT_FIN_COLOR = new Color(178, 223, 138);

    private static final Color DEFAULT_URG_COLOR = new Color(51, 160, 44);

    private static final Color DEFAULT_PSH_COLOR = new Color(251, 154, 153);

    private static final Color DEFAULT_RST_COLOR = new Color(227, 26, 28);

    private static final int DEFAULT_COLOR_MAP_INDEX = 0;

    private int localSort, remoteSort;

    private int columnCount, rowHeight;

    private boolean showTooltips, showPackets, showFlags, curvedLinks;

    private String homeNet;

    private int colorMapIndex;

    private Color tcpColor, udpColor, icmpColor, synColor, ackColor, finColor, urgColor, pshColor, rstColor;

    private Properties properties = new Properties();

    private static TNVPreferenceData instance = new TNVPreferenceData();

    /**
	 * Private Constructor
	 */
    private TNVPreferenceData() {
        loadProperties();
    }

    /**
	 * @return singleton instance
	 */
    public static TNVPreferenceData getInstance() {
        return instance;
    }

    /**
	 * Load preferences from file or use the defaults
	 */
    public void loadProperties() {
        try {
            FileInputStream in = new FileInputStream(PROPERTIES_FILE);
            this.properties.load(in);
            in.close();
        } catch (Exception e) {
            System.out.println("Could not load preference file: " + e.getMessage());
        }
        setHomeNet(getStringProperty("HomeNet", ""));
        setLocalSort(getIntProperty("LocalSort", DEFAULT_LOCAL_SORT));
        setRemoteSort(getIntProperty("RemoteSort", DEFAULT_REMOTE_SORT));
        setColumnCount(getIntProperty("ColumnCount", DEFAULT_COLUMN_COUNT));
        setRowHeight(getIntProperty("RowHeight", DEFAULT_ROW_HEIGHT));
        setCurvedLinks(getBooleanProperty("CurvedLinks", DEFAULT_CURVED_LINKS));
        setShowTooltips(getBooleanProperty("ShowTooltips", DEFAULT_SHOW_TOOLTIPS));
        setShowPackets(getBooleanProperty("ShowPackets", DEFAULT_SHOW_PACKETS));
        setShowFlags(getBooleanProperty("ShowFlags", DEFAULT_SHOW_FLAGS));
        setTcpColor(getColorProperty("TCPColor", DEFAULT_TCP_COLOR));
        setUdpColor(getColorProperty("UDPColor", DEFAULT_UDP_COLOR));
        setIcmpColor(getColorProperty("ICMPColor", DEFAULT_ICMP_COLOR));
        setSynColor(getColorProperty("SYNColor", DEFAULT_SYN_COLOR));
        setAckColor(getColorProperty("ACKColor", DEFAULT_ACK_COLOR));
        setFinColor(getColorProperty("FINColor", DEFAULT_FIN_COLOR));
        setPshColor(getColorProperty("PSHColor", DEFAULT_PSH_COLOR));
        setUrgColor(getColorProperty("URGColor", DEFAULT_URG_COLOR));
        setRstColor(getColorProperty("RSTColor", DEFAULT_RST_COLOR));
        setColorMapIndex(getIntProperty("MachineColorMap", DEFAULT_COLOR_MAP_INDEX));
    }

    /**
	 * Set all preferences and save the preferences to disk
	 */
    public void saveProperties() {
        this.properties.setProperty("HomeNet", getHomeNet());
        this.properties.setProperty("LocalSort", getLocalSort() + "");
        this.properties.setProperty("RemoteSort", getRemoteSort() + "");
        this.properties.setProperty("ColumnCount", getColumnCount() + "");
        this.properties.setProperty("RowHeight", getRowHeight() + "");
        if (isShowTooltips()) this.properties.setProperty("ShowTooltips", "true"); else this.properties.setProperty("ShowTooltips", "false");
        if (isShowPackets()) this.properties.setProperty("ShowPackets", "true"); else this.properties.setProperty("ShowPackets", "false");
        if (isShowFlags()) this.properties.setProperty("ShowFlags", "true"); else this.properties.setProperty("ShowFlags", "false");
        if (isCurvedLinks()) this.properties.setProperty("CurvedLinks", "true"); else this.properties.setProperty("CurvedLinks", "false");
        this.properties.setProperty("TCPColor", getTcpColor().getRed() + " " + getTcpColor().getGreen() + " " + getTcpColor().getBlue());
        this.properties.setProperty("UDPColor", getUdpColor().getRed() + " " + getUdpColor().getGreen() + " " + getUdpColor().getBlue());
        this.properties.setProperty("ICMPColor", getIcmpColor().getRed() + " " + getIcmpColor().getGreen() + " " + getIcmpColor().getBlue());
        this.properties.setProperty("SYNColor", getSynColor().getRed() + " " + getSynColor().getGreen() + " " + getSynColor().getBlue());
        this.properties.setProperty("ACKColor", getAckColor().getRed() + " " + getAckColor().getGreen() + " " + getAckColor().getBlue());
        this.properties.setProperty("FINColor", getFinColor().getRed() + " " + getFinColor().getGreen() + " " + getFinColor().getBlue());
        this.properties.setProperty("PSHColor", getPshColor().getRed() + " " + getPshColor().getGreen() + " " + getPshColor().getBlue());
        this.properties.setProperty("URGColor", getUrgColor().getRed() + " " + getUrgColor().getGreen() + " " + getUrgColor().getBlue());
        this.properties.setProperty("RSTColor", getRstColor().getRed() + " " + getRstColor().getGreen() + " " + getRstColor().getBlue());
        this.properties.setProperty("MachineColorMap", getColorMapIndex() + "");
        this.firePreferenceChanged();
        try {
            FileOutputStream out = new FileOutputStream(PROPERTIES_FILE);
            this.properties.store(out, "Saved by TNV ");
            out.close();
        } catch (Exception e) {
            System.err.println("Could not save preference file: " + e.getMessage());
        }
    }

    /**
	 * @return Returns the localSort.
	 */
    public final int getLocalSort() {
        return this.localSort;
    }

    /**
	 * @param localSort The localSort to set.
	 */
    public final void setLocalSort(int o) {
        this.localSort = o;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the remoteSort.
	 */
    public final int getRemoteSort() {
        return this.remoteSort;
    }

    /**
	 * @param remoteSort The remoteSort to set.
	 */
    public final void setRemoteSort(int o) {
        this.remoteSort = o;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the columnWidth.
	 */
    public final int getColumnCount() {
        return this.columnCount;
    }

    /**
	 * @param columnCount The columnWidth to set.
	 */
    public final void setColumnCount(int w) {
        this.columnCount = w;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the rowHeight.
	 */
    public final int getRowHeight() {
        return this.rowHeight;
    }

    /**
	 * @param rowHeight The rowHeight to set.
	 */
    public final void setRowHeight(int h) {
        this.rowHeight = h;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the colorMapIndex.
	 */
    public final int getColorMapIndex() {
        return this.colorMapIndex;
    }

    /**
	 * @param colorMapIndex The colorMapIndex to set.
	 */
    public final void setColorMapIndex(int m) {
        this.colorMapIndex = m;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the homeNet.
	 */
    public final String getHomeNet() {
        return this.homeNet;
    }

    /**
	 * @param homeNet The homeNet to set.
	 */
    public final void setHomeNet(String net) {
        this.homeNet = net;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the curvedLinks.
	 */
    public final boolean isCurvedLinks() {
        return this.curvedLinks;
    }

    /**
	 * @param showTooltips The showTooltips to set.
	 */
    public final void setCurvedLinks(boolean l) {
        this.curvedLinks = l;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the showTooltips.
	 */
    public final boolean isShowTooltips() {
        return this.showTooltips;
    }

    /**
	 * @param showTooltips The showTooltips to set.
	 */
    public final void setShowTooltips(boolean tt) {
        this.showTooltips = tt;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the boolean TNVPreferenceData.java
	 */
    public final boolean isShowFlags() {
        return this.showFlags;
    }

    /**
	 *
	 * @param showFlags the boolean showFlags to set
	 */
    public final void setShowFlags(boolean showFlags) {
        this.showFlags = showFlags;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the boolean TNVPreferenceData.java
	 */
    public final boolean isShowPackets() {
        return this.showPackets;
    }

    /**
	 *
	 * @param showPackets the boolean showPackets to set
	 */
    public final void setShowPackets(boolean showPackets) {
        this.showPackets = showPackets;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the tcpColor.
	 */
    public final Color getTcpColor() {
        return this.tcpColor;
    }

    /**
	 * @param tcpColor The tcpColor to set.
	 */
    public final void setTcpColor(Color c) {
        this.tcpColor = c;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the udpColor.
	 */
    public final Color getUdpColor() {
        return this.udpColor;
    }

    /**
	 * @param udpColor The udpColor to set.
	 */
    public final void setUdpColor(Color c) {
        this.udpColor = c;
        this.firePreferenceChanged();
    }

    /**
	 * @return Returns the icmpColor.
	 */
    public final Color getIcmpColor() {
        return this.icmpColor;
    }

    /**
	 * @param icmpColor The icmpColor to set.
	 */
    public final void setIcmpColor(Color c) {
        this.icmpColor = c;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the Color TNVPreferenceData.java
	 */
    public final Color getAckColor() {
        return this.ackColor;
    }

    /**
	 *
	 * @param ackColor the Color ackColor to set
	 */
    public final void setAckColor(Color ackColor) {
        this.ackColor = ackColor;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the Color TNVPreferenceData.java
	 */
    public final Color getFinColor() {
        return this.finColor;
    }

    /**
	 *
	 * @param finColor the Color finColor to set
	 */
    public final void setFinColor(Color finColor) {
        this.finColor = finColor;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the Color TNVPreferenceData.java
	 */
    public final Color getPshColor() {
        return this.pshColor;
    }

    /**
	 *
	 * @param pshColor the Color pshColor to set
	 */
    public final void setPshColor(Color pshColor) {
        this.pshColor = pshColor;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the Color TNVPreferenceData.java
	 */
    public final Color getRstColor() {
        return this.rstColor;
    }

    /**
	 *
	 * @param rstColor the Color rstColor to set
	 */
    public final void setRstColor(Color rstColor) {
        this.rstColor = rstColor;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the Color TNVPreferenceData.java
	 */
    public final Color getSynColor() {
        return this.synColor;
    }

    /**
	 *
	 * @param synColor the Color synColor to set
	 */
    public final void setSynColor(Color synColor) {
        this.synColor = synColor;
        this.firePreferenceChanged();
    }

    /**
	 *
	 * @return the Color TNVPreferenceData.java
	 */
    public final Color getUrgColor() {
        return this.urgColor;
    }

    /**
	 *
	 * @param urgColor the Color urgColor to set
	 */
    public final void setUrgColor(Color urgColor) {
        this.urgColor = urgColor;
        this.firePreferenceChanged();
    }

    /**
	 * @param l
	 */
    public void addPreferenceChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
    }

    /**
	 * @param l
	 */
    public void removePreferenceChangeListener(ChangeListener l) {
        this.listenerList.remove(ChangeListener.class, l);
    }

    /**
	 * @return listeners
	 */
    public ChangeListener[] getPreferenceChangeListener() {
        return this.listenerList.getListeners(ChangeListener.class);
    }

    /**
	 * fire preference changed event
	 */
    public void firePreferenceChanged() {
        Object[] listeners = this.listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (this.preferenceChangeEvent == null) this.preferenceChangeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(this.preferenceChangeEvent);
            }
        }
    }

    /**
	 * @param key
	 * @param defaultValue
	 * @return string property
	 */
    private String getStringProperty(String key, String defaultValue) {
        String string = this.properties.getProperty(key, defaultValue);
        return string;
    }

    /**
	 * @param key
	 * @param defaultValue
	 * @return integer property
	 */
    private int getIntProperty(String key, int defaultValue) {
        String string = this.properties.getProperty(key, Integer.toString(defaultValue));
        if (string == null) {
            System.err.println("WARN: couldn't find integer value under '" + key + "'");
            return 0;
        }
        return Integer.parseInt(string);
    }

    /**
	 * @param key
	 * @param defaultValue
	 * @return boolean property
	 */
    private boolean getBooleanProperty(String key, boolean defaultValue) {
        String string = this.properties.getProperty(key, Boolean.toString(defaultValue));
        if (string == null) {
            System.err.println("WARN: couldn't find boolean value under '" + key + "'");
            return false;
        }
        if (string.toLowerCase().equals("true") || string.toLowerCase().equals("on") || string.toLowerCase().equals("yes") || string.toLowerCase().equals("1")) return true;
        return false;
    }

    /**
	 * @param key
	 * @param defaultValue
	 * @return color property
	 */
    private Color getColorProperty(String key, Color defaultValue) {
        String r = Integer.toString(defaultValue.getRed());
        String g = Integer.toString(defaultValue.getGreen());
        String b = Integer.toString(defaultValue.getBlue());
        String colorString = r + " " + g + " " + b;
        String string = this.properties.getProperty(key, colorString);
        if (string == null) {
            System.err.println("WARN: couldn't find color tuplet under '" + key + "'");
            return Color.BLACK;
        }
        return parseColorString(string);
    }

    /**
	 * @param string
	 * @return color
	 */
    private Color parseColorString(String string) {
        StringTokenizer st = new StringTokenizer(string, " ");
        Color c;
        try {
            c = new Color(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        } catch (NoSuchElementException e) {
            c = Color.BLACK;
            System.err.println("WARN: invalid color spec '" + string + "' in property file");
        }
        return c;
    }
}
