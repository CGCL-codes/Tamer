package org.light.portal.portlet.core.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PreferencesValidator;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.light.portal.context.Context;
import org.light.portal.core.model.PortletObjectPreferences;
import org.light.portal.core.service.PortalService;
import org.light.portal.portlet.definition.Preference;
import org.light.portal.portlet.definition.types.ReadOnlyType;
import org.light.portal.util.Enumerator;
import org.light.portal.util.StringUtil;

/**
 * 
 * @author Jianmin Liu
 **/
public class PortletPreferencesImpl implements PortletPreferences {

    private HashMap<String, PortletPreference> preferences = new HashMap<String, PortletPreference>();

    private HashMap<String, PortletPreference> defaultPreferences = new HashMap<String, PortletPreference>();

    private HashMap changedPreferences = new HashMap();

    private HashSet removedPreferences = new HashSet();

    private Integer methodId = null;

    private PortletRequest request;

    private PortletWindow portletWindow = null;

    private PortalService portalService;

    public PortletPreferencesImpl(Integer methodId, PortletRequest request, PortletWindow portletWindow) {
        this.methodId = methodId;
        this.request = request;
        this.portletWindow = portletWindow;
        this.portalService = Context.getInstance().getPortalService();
        if (portletWindow.getDefition().getPortletPreferences() != null && portletWindow.getDefition().getPortletPreferences().getPreference() != null) {
            for (int i = 0; i < portletWindow.getDefition().getPortletPreferences().getPreference().length; i++) {
                Preference p = portletWindow.getDefition().getPortletPreferences().getPreference()[i];
                boolean readOnly = false;
                if (p.getReadOnly() != null && p.getReadOnly().equals(ReadOnlyType.TRUE)) {
                    readOnly = true;
                }
                defaultPreferences.put(p.getName().getContent(), new PortletPreference(p.getName().getContent(), p.getValue(), readOnly));
            }
        }
        if (portletWindow.getPortletObject() != null) {
            List<PortletObjectPreferences> list = this.portalService.getPortletPreferences(this.portletWindow.getPortletObject().getId());
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    PortletObjectPreferences ps = (PortletObjectPreferences) it.next();
                    if (!ps.isRemoved()) {
                        if (preferences.containsKey(ps.getName())) {
                            PortletPreference pp = preferences.get(ps.getName());
                            String[] values = pp.getValues();
                            String[] newValues = new String[values.length + 1];
                            for (int i = 0; i < values.length; i++) {
                                newValues[i] = values[i];
                            }
                            newValues[newValues.length - 1] = ps.getValue();
                            preferences.put(ps.getName(), new PortletPreference(ps.getName(), newValues));
                        } else {
                            preferences.put(ps.getName(), new PortletPreference(ps.getName(), ps.getValue()));
                        }
                    } else {
                        defaultPreferences.remove(ps.getName());
                    }
                }
            }
        }
    }

    public boolean isReadOnly(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        boolean isReadOnly = false;
        if (defaultPreferences.get(key) != null) {
            isReadOnly = defaultPreferences.get(key).isReadOnly();
        }
        return isReadOnly;
    }

    public String getValue(String key, String def) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        String[] defStr = new String[1];
        defStr[0] = def;
        String[] values = this.getValues(key, defStr);
        if ((values == null) || (values.length == 0)) {
            return null;
        }
        return values[0];
    }

    public String[] getValues(String key, String[] def) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        if (removedPreferences.contains(key)) {
            return null;
        }
        if (changedPreferences.containsKey(key)) {
            return StringUtil.copy((String[]) changedPreferences.get(key));
        }
        if (preferences.containsKey(key)) {
            return StringUtil.copy((String[]) preferences.get(key).getValues());
        }
        if (defaultPreferences.containsKey(key)) {
            return StringUtil.copy((String[]) defaultPreferences.get(key).getValues());
        }
        return def;
    }

    public void setValue(String key, String value) throws ReadOnlyException {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        String[] values = new String[1];
        values[0] = value;
        setValues(key, values);
    }

    public void setValues(String key, String[] values) throws ReadOnlyException {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        if (isReadOnly(key)) {
            throw new ReadOnlyException("Preference attribute called " + key + " may not be modified");
        }
        changedPreferences.put(key, StringUtil.copy(values));
        removedPreferences.remove(key);
    }

    public Enumeration getNames() {
        HashSet keyset = new HashSet();
        Iterator listIter = preferences.keySet().iterator();
        Iterator defaultListIter = defaultPreferences.keySet().iterator();
        Iterator changedIter = changedPreferences.keySet().iterator();
        Iterator removedIter = removedPreferences.iterator();
        while (listIter.hasNext()) {
            keyset.add(listIter.next());
        }
        while (defaultListIter.hasNext()) {
            keyset.add(defaultListIter.next());
        }
        while (changedIter.hasNext()) {
            keyset.add(changedIter.next());
        }
        while (removedIter.hasNext()) {
            keyset.remove(removedIter.next());
        }
        return new Enumerator(keyset.iterator());
    }

    public Map getMap() {
        HashMap map = new HashMap();
        Enumeration enumerator = this.getNames();
        while (enumerator.hasMoreElements()) {
            String name = (String) enumerator.nextElement();
            map.put(name, getValues(name, null));
        }
        return map;
    }

    public void reset(String key) throws ReadOnlyException {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        if (isReadOnly(key)) {
            throw new ReadOnlyException("preference attribute called " + key + " may not be modified");
        }
        changedPreferences.remove(key);
        removedPreferences.add(key);
    }

    public void store() throws java.io.IOException, ValidatorException {
        if (portletWindow.getDefition().getPortletPreferences() != null) {
            String validatorStr = portletWindow.getDefition().getPortletPreferences().getPreferencesValidator();
            if (validatorStr != null) {
                try {
                    PreferencesValidator validator = (PreferencesValidator) Class.forName(validatorStr).newInstance();
                    validator.validate(this);
                } catch (Exception e) {
                }
            }
        }
        Iterator iterator = changedPreferences.keySet().iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            String[] values = (String[]) changedPreferences.get(name);
            if (values != null && values.length > 0) {
                List<PortletObjectPreferences> list = this.portalService.getPortletPreferences(this.portletWindow.getPortletObject().getId());
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        PortletObjectPreferences pop = (PortletObjectPreferences) it.next();
                        if (pop.getName().equals(name)) {
                            this.portalService.delete(pop);
                        }
                    }
                }
                for (int i = 0; i < values.length; i++) {
                    this.portalService.save(new PortletObjectPreferences(name, values[i], this.portletWindow.getPortletObject().getId()));
                }
            }
        }
        Iterator iterator2 = removedPreferences.iterator();
        while (iterator2.hasNext()) {
            String name = (String) iterator2.next();
            if (preferences.containsKey(name)) {
                List<PortletObjectPreferences> list = this.portalService.getPortletPreferences(this.portletWindow.getPortletObject().getId());
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        PortletObjectPreferences pop = (PortletObjectPreferences) it.next();
                        if (pop.getName().equals(name)) {
                            this.portalService.delete(pop);
                        }
                    }
                }
            }
            if (defaultPreferences.containsKey(name)) {
                List<PortletObjectPreferences> list = this.portalService.getPortletPreferences(this.portletWindow.getPortletObject().getId());
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        PortletObjectPreferences pop = (PortletObjectPreferences) it.next();
                        if (pop.getName().equals(name)) {
                            this.portalService.delete(pop);
                        }
                    }
                }
                this.portalService.save(new PortletObjectPreferences(name, "", this.portletWindow.getPortletObject().getId(), 1));
            }
        }
    }

    protected HttpServletRequest getServletRequest(PortletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getAttribute("httpServletRequest");
        return httpServletRequest;
    }
}
