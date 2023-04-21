package com.aelitis.azureus.core.vuzefile;

import java.util.*;

public interface VuzeFileComponent {

    public static final int COMP_TYPE_NONE = 0x00000000;

    public static final int COMP_TYPE_METASEARCH_TEMPLATE = 0x00000001;

    public static final int COMP_TYPE_V3_NAVIGATION = 0x00000002;

    public static final int COMP_TYPE_V3_CONDITION_CHECK = 0x00000004;

    public static final int COMP_TYPE_PLUGIN = 0x00000008;

    public static final int COMP_TYPE_SUBSCRIPTION = 0x00000010;

    public static final int COMP_TYPE_SUBSCRIPTION_SINGLETON = 0x00000020;

    public static final int COMP_TYPE_CUSTOMIZATION = 0x00000040;

    public static final int COMP_TYPE_CONTENT_NETWORK = 0x00000080;

    public static final int COMP_TYPE_METASEARCH_OPERATION = 0x00000100;

    public int getType();

    public Map getContent();

    public void setProcessed();

    public boolean isProcessed();

    public void setData(Object key, Object value);

    public Object getData(Object key);
}
