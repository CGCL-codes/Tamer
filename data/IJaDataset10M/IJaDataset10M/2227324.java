package com.liferay.portlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.GetterUtil;

/**
 * <a href="PortletPreferencesWrapper.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public class PortletPreferencesWrapper implements PortletPreferences, Serializable {

    public PortletPreferencesWrapper(PortletPreferences prefs, boolean action) {
        _prefs = prefs;
        _action = action;
    }

    public Map getMap() {
        return _prefs.getMap();
    }

    public Enumeration getNames() {
        return _prefs.getNames();
    }

    public String getValue(String key, String def) {
        return _prefs.getValue(key, def);
    }

    public void setValue(String key, String value) throws ReadOnlyException {
        _prefs.setValue(key, value);
    }

    public String[] getValues(String key, String[] def) {
        return _prefs.getValues(key, def);
    }

    public void setValues(String key, String[] values) throws ReadOnlyException {
        _prefs.setValues(key, values);
    }

    public boolean isReadOnly(String key) {
        return _prefs.isReadOnly(key);
    }

    public void reset(String key) throws ReadOnlyException {
        _prefs.reset(key);
    }

    public void store() throws IOException, ValidatorException {
        if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.TCK_URL))) {
            if (_action) {
                _prefs.store();
            } else {
                throw new IllegalStateException("Preferences cannot be stored inside a render call");
            }
        } else {
            _prefs.store();
        }
    }

    public PortletPreferencesImpl getPreferencesImpl() {
        return (PortletPreferencesImpl) _prefs;
    }

    private PortletPreferences _prefs = null;

    private boolean _action;
}
