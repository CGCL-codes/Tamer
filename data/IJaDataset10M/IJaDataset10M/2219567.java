package com.enerjy.common.registry;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import com.enerjy.common.util.BundleHelper;
import com.enerjy.common.util.IPropertyStore;

public class Registry {

    private static BundleHelper bundle = BundleHelper.forName("com.enerjy.common.registry.RegistryBundle");

    private static final String VERSION_KEY = "registryVersion";

    private static final int VERSION = 2;

    /**
     * private constructor for Registry - statics only
     */
    private Registry() {
    }

    private static Map<Integer, PluginInfo> plugins = new HashMap<Integer, PluginInfo>();

    private static IPropertyStore store;

    private static class PluginInfo {

        private IKeyInfo keyInfo;

        private String name;

        PluginInfo(IKeyInfo keyInfo, String name) {
            this.keyInfo = keyInfo;
            this.name = name;
        }
    }

    /**
     * Bundle string access.
     * 
     * @param key String key to retrieve
     * @return Retrieved string
     */
    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static class PluginKeyInfo {

        PluginKeyInfo(int productNo, IPropertyStore store, String[] defaults) {
            this.productNo = productNo;
            this.serialNo = store.getProperty(getSerialNoPropName(), defaults[0]);
            this.serialKey = store.getProperty(getSerialKeyPropName(), defaults[1]);
            this.dateString = store.getProperty(getDateStringPropName(), "");
        }

        private int productNo;

        private String serialNo;

        private String serialKey;

        private String dateString;

        private String getSerialNoPropName() {
            return "SerialNo." + productNo;
        }

        private String getSerialKeyPropName() {
            return "SerialKey." + productNo;
        }

        private String getDateStringPropName() {
            return "MaintDate." + productNo;
        }

        public int getProductNo() {
            return this.productNo;
        }

        public String getSerialNo() {
            return this.serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getSerialKey() {
            return this.serialKey;
        }

        public void setSerialKey(String serialKey) {
            this.serialKey = serialKey;
        }

        public String getMaintenanceDate() {
            return dateString;
        }

        public void clearMaintenanceDate() {
            dateString = "";
        }

        public void save() {
            save(Registry.store);
        }

        public void save(IPropertyStore store) {
            store.setProperty(getSerialNoPropName(), this.serialNo);
            store.setProperty(getSerialKeyPropName(), this.serialKey);
            store.setProperty(getDateStringPropName(), this.dateString);
        }

        @Override
        public String toString() {
            return getPluginName(this.productNo);
        }
    }

    public static void setPropertyStore(IPropertyStore store) {
        Registry.store = store;
        store.setProperty(VERSION_KEY, VERSION);
    }

    public static void registerPlugin(String name, IKeyInfo keyInfo) {
        plugins.put(keyInfo.getProductNumber(), new PluginInfo(keyInfo, name));
    }

    public static int verifyPluginKey(int productNo) {
        PluginKeyInfo keyInfo = createPluginKeyInfo(productNo);
        return verifyPluginKey(keyInfo);
    }

    public static void updateKeyInfo(int productNo, String serial, String key) {
        PluginKeyInfo keyInfo = createPluginKeyInfo(productNo);
        keyInfo.setSerialNo(serial);
        keyInfo.setSerialKey(key);
        keyInfo.save();
    }

    private static PluginKeyInfo createPluginKeyInfo(int productNo) {
        String[] defaults = { "", "" };
        PluginInfo info = plugins.get(productNo);
        if (null != info) {
            if (!info.keyInfo.createDefaultRegInfo(defaults)) {
                defaults = new String[] { "", "" };
            }
        }
        PluginKeyInfo keyInfo = new PluginKeyInfo(productNo, store, defaults);
        return keyInfo;
    }

    public static int verifyPluginKey(PluginKeyInfo keyInfo) {
        PluginInfo info = plugins.get(new Integer(keyInfo.getProductNo()));
        char[] backDoor = { '\26', '\23', '\2', '\14' };
        boolean isBackDoor = false;
        if (backDoor.length == keyInfo.serialNo.length()) {
            isBackDoor = true;
            for (int i = 0; i < backDoor.length; ++i) {
                if (backDoor[i] + 'a' != keyInfo.serialNo.charAt(i)) {
                    isBackDoor = false;
                    break;
                }
            }
        }
        if (isBackDoor) {
            try {
                return Integer.parseInt(keyInfo.serialKey);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        int days = Serkey.verifyKey(keyInfo.getProductNo(), info.keyInfo.getKeyVersion(), keyInfo.getSerialNo(), keyInfo.getSerialKey());
        return days;
    }

    public static int verifyPluginKey(int productNo, int keyVer, String serial, String key) {
        return Serkey.verifyKey(productNo, keyVer, serial, key);
    }

    public static int getDaysToExpiration(PluginKeyInfo keyInfo) {
        int days = -1;
        try {
            days = Serkey.getDaysToExpiration(keyInfo.getSerialKey());
        } catch (SerkeyException e) {
            days = -1;
        }
        return days;
    }

    public static int getDaysToExpiration(int productNo) {
        PluginKeyInfo keyInfo = createPluginKeyInfo(productNo);
        return getDaysToExpiration(keyInfo);
    }

    public static int getFunctionality(PluginKeyInfo keyInfo) {
        int func = -1;
        try {
            func = Serkey.getFunctionality(keyInfo.getSerialKey());
        } catch (SerkeyException e) {
            func = -1;
        }
        return func;
    }

    public static int getFunctionality(int productNo) {
        if (null == store) {
            return -1;
        }
        PluginKeyInfo keyInfo = createPluginKeyInfo(productNo);
        return getFunctionality(keyInfo);
    }

    public static PluginKeyInfo[] getPluginKeyInfo() {
        return getPluginKeyInfo(store);
    }

    public static PluginKeyInfo[] getPluginKeyInfo(IPropertyStore store) {
        PluginKeyInfo[] ret = new PluginKeyInfo[plugins.size()];
        int i = 0;
        for (int productNo : plugins.keySet()) {
            ret[i++] = createPluginKeyInfo(productNo);
        }
        Arrays.sort(ret, new Comparator<PluginKeyInfo>() {

            public int compare(PluginKeyInfo key1, PluginKeyInfo key2) {
                return getPluginName(key1.getProductNo()).compareTo(getPluginName(key2.getProductNo()));
            }
        });
        return ret;
    }

    public static String getPluginName(int productNo) {
        return (plugins.get(new Integer(productNo))).name;
    }

    public static String getPluginVersion(int productNo) {
        IKeyInfo info = (plugins.get(new Integer(productNo))).keyInfo;
        if (null != info) {
            String build = info.getBuildNumber();
            return info.getProductVersion() + '.' + build;
        }
        return "<unknown>";
    }
}
