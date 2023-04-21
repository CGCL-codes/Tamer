package org.base.apps.api.view.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.apps.api.Application;
import org.base.apps.api.ApplicationInfo;
import org.base.apps.api.InfoAttr;
import org.base.apps.beans.BaseBean;
import org.base.apps.beans.prefs.BasePreferences;
import org.base.apps.beans.prefs.PreferenceUtil;

/**
 * Default implementation of {@link ApplicationInfo}.
 * 
 * @author Kevan Simpson
 */
public class BaseInfo extends BaseBean implements ApplicationInfo {

    private static final long serialVersionUID = -3464520803199866586L;

    private BasePreferences mPreferences, mInfoChild;

    /**
     * 
     */
    public <D, C> BaseInfo(Application<D, C> app) {
        this(app, false);
    }

    /**
     * @param isBroadcaster
     */
    protected <D, C> BaseInfo(Application<D, C> app, boolean isBroadcaster) {
        this(PreferenceUtil.getUserPreferences(app.getClass()).node(app.getName()), isBroadcaster, app.getName());
    }

    protected BaseInfo(BasePreferences prefs, boolean isBroadcaster, String name) {
        super(isBroadcaster);
        setPreferences(prefs);
        setInfoChild(getPreferences().node(InfoAttr.info.getAttr()));
        setName(name);
    }

    /** @see org.base.apps.api.ApplicationInfo#getChild(java.lang.CharSequence[]) */
    @Override
    public ApplicationInfo getChild(CharSequence... steps) {
        if (ArrayUtils.isEmpty(steps) || getPreferences() == null) return null;
        BasePreferences pref = getPreferences();
        StringBuffer buff = new StringBuffer();
        for (CharSequence cseq : steps) {
            String str = cseq.toString();
            pref = pref.node(str);
            buff.append("/").append(str);
        }
        return new BaseInfo(pref, false, buff.toString());
    }

    /** @see org.base.apps.util.Nameable#setName(java.lang.String) */
    @Override
    public void setName(String name) {
        getInfoChild().put(InfoAttr.name.getAttr(), name);
    }

    /** @see org.base.apps.util.Named#getName() */
    @Override
    public String getName() {
        return getInfoChild().get(InfoAttr.name.getAttr(), "?");
    }

    /** @see org.base.apps.api.ApplicationInfo#getDescription() */
    @Override
    public String getDescription() {
        return getInfoChild().get(InfoAttr.description.getAttr(), "?");
    }

    /** @see org.base.apps.api.ApplicationInfo#getTitle() */
    @Override
    public String getTitle() {
        return getInfoChild().get(InfoAttr.title.getAttr(), "?");
    }

    /** @see org.base.apps.api.ApplicationInfo#setDescription(java.lang.String) */
    @Override
    public void setDescription(String desc) {
        getInfoChild().put(InfoAttr.description.getAttr(), desc);
    }

    /** @see org.base.apps.api.ApplicationInfo#setTitle(java.lang.String) */
    @Override
    public void setTitle(String title) {
        getInfoChild().put(InfoAttr.title.getAttr(), title);
    }

    /** @see org.base.apps.api.ApplicationInfo#getType(java.lang.CharSequence) */
    @Override
    public Class<?> getType(CharSequence key) {
        String impl = get(key);
        try {
            return StringUtils.isBlank(impl) ? null : ClassUtils.getClass(impl);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load type: " + impl, ex);
        }
    }

    /** @see org.base.apps.api.ApplicationInfo#offer(java.lang.CharSequence, java.lang.String) */
    @Override
    public String offer(CharSequence key, String value) {
        String currentValue = get(key);
        if (StringUtils.isBlank(currentValue)) {
            put(key, value);
            return value;
        }
        return currentValue;
    }

    /** @see java.util.Map#size() */
    @Override
    public int size() {
        return getPropertyKeys().length;
    }

    /** @see java.util.Map#isEmpty() */
    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    /** @see java.util.Map#containsKey(java.lang.Object) */
    @Override
    public boolean containsKey(Object key) {
        return ArrayUtils.contains(getPropertyKeys(), ObjectUtils.toString(key));
    }

    /** @see java.util.Map#containsValue(java.lang.Object) */
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    /** @see java.util.Map#get(java.lang.Object) */
    @Override
    public String get(Object key) {
        return getPreferences().get(ObjectUtils.toString(key), null);
    }

    /** @see java.util.Map#put(java.lang.Object, java.lang.Object) */
    @Override
    public String put(CharSequence key, String value) {
        String old = get(key);
        getPreferences().put(key.toString(), value);
        firePropertyChange(ObjectUtils.toString(key), old, value);
        return old;
    }

    /** @see java.util.Map#remove(java.lang.Object) */
    @Override
    public String remove(Object key) {
        String old = get(key);
        getPreferences().remove(ObjectUtils.toString(key));
        return old;
    }

    /** @see java.util.Map#putAll(java.util.Map) */
    @Override
    public void putAll(Map<? extends CharSequence, ? extends String> m) {
        if (m != null) {
            for (CharSequence cseq : m.keySet()) {
                put(cseq, m.get(cseq));
            }
        }
    }

    /** @see java.util.Map#clear() */
    @Override
    public void clear() {
        String[] keys = getPropertyKeys();
        for (String key : keys) {
            remove(key);
        }
    }

    /** @see java.util.Map#keySet() */
    @Override
    public Set<CharSequence> keySet() {
        Set<CharSequence> keySet = new TreeSet<CharSequence>();
        String[] keys = getPropertyKeys();
        for (String key : keys) {
            keySet.add(key);
        }
        return keySet;
    }

    /** @see java.util.Map#values() */
    @Override
    public Collection<String> values() {
        List<String> vals = new ArrayList<String>();
        String[] keys = getPropertyKeys();
        for (String key : keys) {
            vals.add(get(key));
        }
        return vals;
    }

    /** @see java.util.Map#entrySet() */
    @Override
    public Set<Map.Entry<CharSequence, String>> entrySet() {
        Set<Map.Entry<CharSequence, String>> entrySet = new TreeSet<Map.Entry<CharSequence, String>>();
        String[] keys = getPropertyKeys();
        for (final String key : keys) {
            entrySet.add(new Map.Entry<CharSequence, String>() {

                /** @see java.util.Map.Entry#getKey() */
                @Override
                public CharSequence getKey() {
                    return key;
                }

                /** @see java.util.Map.Entry#getValue() */
                @Override
                public String getValue() {
                    return get(key);
                }

                /** @see java.util.Map.Entry#setValue(java.lang.Object) */
                @Override
                public String setValue(String value) {
                    return put(key, value);
                }
            });
        }
        return entrySet;
    }

    protected String[] getPropertyKeys() {
        try {
            return getPreferences().keys();
        } catch (Exception ex) {
            log().error("Failed to read preference keys: " + ex.getMessage(), ex);
            return new String[0];
        }
    }

    /**
     * @return the preferences
     */
    protected BasePreferences getPreferences() {
        return mPreferences;
    }

    /**
     * @return the infoChild
     */
    protected BasePreferences getInfoChild() {
        return mInfoChild;
    }

    /**
     * @param infoChild the infoChild to set
     */
    protected void setInfoChild(BasePreferences infoChild) {
        mInfoChild = infoChild;
    }

    /**
     * @param preferences the preferences to set
     */
    protected void setPreferences(BasePreferences preferences) {
        mPreferences = preferences;
    }
}
