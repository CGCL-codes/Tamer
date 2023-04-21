package org.zkforge.amcharts;

import org.zkoss.lang.Objects;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.au.AuScript;
import org.zkoss.zk.au.Command;
import java.util.Iterator;

/**
 * The component used to represent
 * &lt;a href="http://www.google.com/apis/maps/"&gt;Google Maps&lt;/a&gt;
 *
 * @author henrichen
 * @version $Revision: 1.6 $ $Date: 2006/03/31 08:38:55 $
 */
public class Linechart extends HtmlBasedComponent {

    private String _swfPath = "~./js/ext/amcharts/";

    private String _settingsFile = _swfPath + "amline_settings.xml";

    private String _dataFile;

    private String _bgcolor = "#FFFFFF";

    private String _preloadercolor = "#999999";

    private String _data;

    public Linechart() {
        setWidth("520");
        setHeight("400");
    }

    public void setSwfPath(String path) {
        _swfPath = path;
    }

    public String getSwfPath() {
        return _swfPath;
    }

    public void setBgcolor(String color) {
        _bgcolor = color;
    }

    public String getBgcolor() {
        return _bgcolor;
    }

    public void setSettingsFile(String file) {
        _settingsFile = file;
    }

    public String getSettingsFile() {
        return _settingsFile;
    }

    public void setDataFile(String file) {
        _dataFile = file;
    }

    public String getDataFile() {
        return _dataFile;
    }

    public void setPreloadercolor(String color) {
        _preloadercolor = color;
    }

    public String getPreloadercolor() {
        return _preloadercolor;
    }

    public void setData(String data) {
        if (!Objects.equals(_data, data)) {
            _data = data;
            smartUpdate("z.data", data);
        }
    }

    public void reloadData() {
        smartUpdate("z.reloadData", "");
    }

    public void reloadSettings() {
        smartUpdate("z.reloadSettings", "");
    }

    public void reloadAll() {
        smartUpdate("z.reloadAll", "");
    }

    public void showAll() {
        smartUpdate("z.showAll", "");
    }

    public void setZoom(String from, String to) {
        smartUpdate("z.zoom", from + "," + to);
    }

    public void setParam(String key, String val) {
        smartUpdate("z.setParam", key + "," + val);
    }

    /** Returns the HTML attributes for this tag.
	 * <p>Used only for component development, not for application developers.
	 */
    public String getOuterAttrs() {
        final String attrs = super.getOuterAttrs();
        final StringBuffer sb = new StringBuffer(64);
        if (attrs != null) {
            sb.append(attrs);
        }
        if (Events.isListened(this, "onZoom", true)) {
            HTMLs.appendAttribute(sb, "z.onZoom", "true");
        }
        HTMLs.appendAttribute(sb, "z.swfPath", encodeURL(getSwfPath()));
        HTMLs.appendAttribute(sb, "z.bgcolor", getBgcolor());
        HTMLs.appendAttribute(sb, "z.settingsFile", encodeURL(getSettingsFile()));
        HTMLs.appendAttribute(sb, "z.dataFile", encodeURL(getDataFile()));
        HTMLs.appendAttribute(sb, "z.preloadercolor", getPreloadercolor());
        HTMLs.appendAttribute(sb, "z.width", getWidth());
        HTMLs.appendAttribute(sb, "z.height", getHeight());
        return sb.toString();
    }

    private String encodeURL(String url) {
        final Desktop dt = getDesktop();
        return dt != null ? dt.getExecution().encodeURL(url) : Executions.getCurrent().encodeURL(url);
    }

    /** Not childable. */
    public boolean isChildable() {
        return false;
    }

    static {
        new ZoomCommand("onZoom", Command.IGNORE_OLD_EQUIV);
    }
}
