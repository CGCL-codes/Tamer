package org.wfp.vam.intermap.kernel.map.mapServices.wmc.om;

/**
 * @author ETj
 */
public class WMCFormat {

    private String _format = null;

    private boolean _current = false;

    private WMCFormat() {
    }

    public static WMCFormat newInstance() {
        return new WMCFormat();
    }

    public boolean isCurrent() {
        return _current;
    }

    public void setCurrent(boolean current) {
        this._current = current;
    }

    public String getFormat() {
        return _format;
    }

    public void setFormat(String format) {
        this._format = format;
    }
}
