package org.moxy.util.config;

import java.util.Vector;

public interface ConfigurationSystem {

    GroupItem[] getFirstGroup();

    GroupItem[] getGroup(String className, int page, String id);

    void setPreference(String id, String value);

    void setListPreference(String id, String[] values);

    String getPreference(String id);

    String[] getListPreference(String id);

    String constructID(String base, String next);

    String getPreviousID(String id);
}
