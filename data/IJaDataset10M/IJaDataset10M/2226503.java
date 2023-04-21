package com.openbravo.pos.base;

import com.openbravo.basic.LocaleResources;

public class DrivceLocal {

    private static LocaleResources m_resources;

    static {
        m_resources = new LocaleResources();
        m_resources.addBundleName("pos_messages");
    }

    /** Creates a new instance of AppLocal */
    private DrivceLocal() {
    }

    public static String getIntString(String sKey) {
        return m_resources.getString(sKey);
    }

    public static String getIntString(String sKey, Object... sValues) {
        return m_resources.getString(sKey, sValues);
    }
}
