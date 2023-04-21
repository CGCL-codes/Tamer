package sdloader.internal;

import java.util.Map;
import java.util.Properties;
import sdloader.util.BooleanUtil;
import sdloader.util.CollectionsUtil;

/**
 * SDLoaderの設定データ.
 * 
 * <pre>
 * SDLoaderに関するすべての設定は、このインスタンスに入ります.
 * デフォルトの設定は、jar内のsdloader.propertiesに入っています.
 * デフォルト設定を変更したい場合は、同名のファイルをクラスパス配下に置くか、
 * SDLoaderインスタンス化後にgetSDLoaderConfigでインスタンスを取り、設定します。
 * </pre>
 * 
 * @author AKatayama
 * 
 */
public class SDLoaderConfig {

    private Map<String, Object> setting = CollectionsUtil.newHashMap();

    public void clear() {
        setting.clear();
    }

    public void addAllIfNotExist(Properties p) {
        for (Object objKey : p.keySet()) {
            String key = (String) objKey;
            String value = p.getProperty(key);
            setConfigIfNotExit(key, value);
        }
    }

    public void addAll(Properties p) {
        for (Object objKey : p.keySet()) {
            String key = (String) objKey;
            String value = p.getProperty(key);
            setConfig(key, value);
        }
    }

    public Object setConfig(String key, Object value) {
        return setConfig(key, value, null);
    }

    public Object setConfig(String key, Object value, Object defaultValue) {
        Object setValue = (value == null) ? defaultValue : value;
        setting.put(key, setValue);
        return setValue;
    }

    public Object setConfigIfNotExit(String key, Object value) {
        return setConfigIfNotExit(key, value, null);
    }

    public Object setConfigIfNotExit(String key, Object value, Object defaultValue) {
        Object exitValue = getConfigIgnoreExist(key);
        if (exitValue == null) {
            setConfig(key, value, defaultValue);
            exitValue = value;
        }
        return exitValue;
    }

    public void setConfigFromSystemIfNotExit(String key) {
        Object value = getConfigIgnoreExist(key);
        if (value == null) {
            value = System.getProperty(key);
            if (value != null) {
                setConfig(key, value);
            }
        }
    }

    public Object getConfig(String key) {
        return getConfig(key, null);
    }

    public Object getConfig(String key, Object defaultValue) {
        return getConfig(key, defaultValue, false);
    }

    public Object getConfigIgnoreExist(String key) {
        return getConfig(key, null, true);
    }

    public Object getConfigIgnoreExist(String key, Object defaultValue) {
        return getConfig(key, defaultValue, true);
    }

    protected Object getConfig(String key, Object defaultValue, boolean ignoreExist) {
        Object value = setting.get(key);
        if (value == null) {
            value = defaultValue;
        }
        if (value == null && !ignoreExist) {
            throw new RuntimeException("Config not found.key=" + key);
        }
        return value;
    }

    public String getConfigString(String key) {
        return getConfigString(key, null);
    }

    public String getConfigString(String key, String defaultValue) {
        Object value = getConfig(key, defaultValue);
        return (value instanceof String) ? (String) value : value.toString();
    }

    public String getConfigStringIgnoreExist(String key) {
        return getConfigStringIgnoreExist(key, null);
    }

    public String getConfigStringIgnoreExist(String key, String defaultValue) {
        Object value = getConfigIgnoreExist(key, defaultValue);
        if (value == null) {
            return null;
        }
        return (value instanceof String) ? (String) value : value.toString();
    }

    public Integer getConfigInteger(String key) {
        return getConfigInteger(key, null);
    }

    public Integer getConfigInteger(String key, Integer defaultValue) {
        Object value = getConfig(key, defaultValue);
        return (value instanceof Integer) ? (Integer) value : Integer.valueOf(value.toString());
    }

    public Integer getConfigIntegerIgnoreExist(String key) {
        return getConfigIntegerIgnoreExist(key, null);
    }

    public Integer getConfigIntegerIgnoreExist(String key, Integer defaultValue) {
        Object value = getConfigIgnoreExist(key, defaultValue);
        if (value == null) {
            return null;
        }
        return (value instanceof Integer) ? (Integer) value : Integer.valueOf(value.toString());
    }

    public Long getConfigLong(String key) {
        return getConfigLong(key, null);
    }

    public Long getConfigLong(String key, Long defaultValue) {
        Object value = getConfig(key, defaultValue);
        return (value instanceof Long) ? (Long) value : Long.valueOf(value.toString());
    }

    public Long getConfigLongIgnoreExist(String key) {
        return getConfigLongIgnoreExist(key, null);
    }

    public Long getConfigLongIgnoreExist(String key, Long defaultValue) {
        Object value = getConfigIgnoreExist(key, defaultValue);
        if (value == null) {
            return null;
        }
        return (value instanceof Long) ? (Long) value : Long.valueOf(value.toString());
    }

    public Boolean getConfigBoolean(String key) {
        return getConfigBoolean(key, null);
    }

    public Boolean getConfigBoolean(String key, Boolean defaultValue) {
        Object value = getConfig(key, defaultValue);
        return (value instanceof Boolean) ? (Boolean) value : BooleanUtil.toBoolean(value.toString());
    }

    public Boolean getConfigBooleanIgnoreExist(String key) {
        return getConfigBooleanIgnoreExist(key, null);
    }

    public Boolean getConfigBooleanIgnoreExist(String key, Boolean defaultValue) {
        Object value = getConfigIgnoreExist(key, defaultValue);
        if (value == null) {
            return null;
        }
        return (value instanceof Boolean) ? (Boolean) value : BooleanUtil.toBoolean(value.toString());
    }
}
