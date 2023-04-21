package com.showdown.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import com.showdown.log.ShowDownLog;
import com.showdown.settings.ISettingsItem.SettingsItemType;
import com.showdown.util.FileUtil;

/**
 * Preferences for ShowDown
 * @author Mat DeLong
 */
public class ShowDownSettings implements IShowDownSettings {

    private SimpleDateFormat SDF = new SimpleDateFormat();

    private File propFile;

    private Properties properties;

    private List<ISettingsListener> listeners;

    private boolean notifying = false;

    /**
    * Constructor
    * @param propFile the properties file to save to and load from
    */
    public ShowDownSettings(File propFile) {
        this.propFile = propFile;
        properties = new Properties();
        this.listeners = new ArrayList<ISettingsListener>();
        loadProperties();
    }

    /**
    * Returns the current preferences
    * @return the current preferences
    */
    public Properties getProperties() {
        return properties;
    }

    /**
    * Loads the properties from disk
    */
    public void loadProperties() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(propFile);
            properties.load(in);
        } catch (Exception ex) {
        } finally {
            FileUtil.close(in);
            notifyListeners(null);
        }
    }

    /**
    * Stories the preferences to disk
    */
    public void storeProperties() {
        FileOutputStream out = null;
        try {
            if (!propFile.getParentFile().exists()) {
                propFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(propFile);
            properties.store(out, null);
        } catch (Exception ex) {
            ShowDownLog.getInstance().logError(ex.getLocalizedMessage(), ex);
        } finally {
            FileUtil.close(out);
        }
    }

    /**
    * {@inheritDoc}
    */
    public Boolean getBoolean(ISettingsItem item) {
        if (item != null && item.getType() == SettingsItemType.BOOLEAN) {
            String valString = (String) properties.get(item.getKey());
            try {
                if (valString == null && item.getDefaultValue() != null) {
                    return (Boolean) item.getDefaultValue();
                }
                if (valString != null) {
                    return Boolean.parseBoolean(valString);
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
    * {@inheritDoc}
    */
    public Date getDate(ISettingsItem item) {
        if (item != null && item.getType() == SettingsItemType.DATE) {
            String valString = (String) properties.get(item.getKey());
            if (valString != null) {
                try {
                    return SDF.parse(valString);
                } catch (Exception ex) {
                    properties.put(item.getKey(), null);
                }
            }
        }
        return null;
    }

    /**
    * {@inheritDoc}
    */
    public Integer getInteger(ISettingsItem item) {
        if (item != null && item.getType() == SettingsItemType.INT) {
            String valString = (String) properties.get(item.getKey());
            try {
                if (valString == null && item.getDefaultValue() != null) {
                    return (Integer) item.getDefaultValue();
                }
                if (valString != null) {
                    return Integer.parseInt(valString);
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
    * {@inheritDoc}
    */
    public Long getLong(ISettingsItem item) {
        if (item != null && item.getType() == SettingsItemType.INT) {
            String valString = (String) properties.get(item.getKey());
            try {
                if (valString == null && item.getDefaultValue() != null) {
                    return (Long) item.getDefaultValue();
                }
                if (valString != null) {
                    return Long.parseLong(valString);
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
    * {@inheritDoc}
    */
    public String getString(ISettingsItem item) {
        if (item != null) {
            String valString = (String) properties.get(item.getKey());
            if (valString == null && item.getDefaultValue() != null) {
                valString = item.getDefaultValue().toString();
            }
            return valString;
        }
        return null;
    }

    /**
    * {@inheritDoc}
    */
    public void setDate(ISettingsItem item, Date date) {
        if (item != null && item.getType() == SettingsItemType.DATE) {
            if (date != null) {
                properties.put(item.getKey(), SDF.format(date));
            } else {
                properties.remove(item.getKey());
            }
            storeProperties();
            notifyListeners(item);
        }
    }

    /**
    * {@inheritDoc}
    */
    public void setToDefault(ISettingsItem item) {
        if (item != null) {
            properties.remove(item.getKey());
            storeProperties();
            notifyListeners(item);
        }
    }

    /**
    * {@inheritDoc}
    */
    public void setValue(ISettingsItem item, String value) {
        if (item != null) {
            String oldVal = (String) properties.get(item.getKey());
            if (oldVal == null || !oldVal.equals(value)) {
                if (value != null) {
                    properties.put(item.getKey(), value);
                } else {
                    properties.remove(item.getKey());
                }
                storeProperties();
                notifyListeners(item);
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    public void setValue(ISettingsItem item, int value) {
        if (item != null && item.getType() == SettingsItemType.INT) {
            String oldVal = (String) properties.get(item.getKey());
            String newVal = Integer.toString(value);
            if (oldVal == null || !oldVal.equals(newVal)) {
                properties.put(item.getKey(), Integer.toString(value));
                storeProperties();
                notifyListeners(item);
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    public void setValue(ISettingsItem item, boolean value) {
        if (item != null && item.getType() == SettingsItemType.BOOLEAN) {
            String oldVal = (String) properties.get(item.getKey());
            String newVal = Boolean.toString(value);
            if (oldVal == null || !oldVal.equals(newVal)) {
                properties.put(item.getKey(), Boolean.toString(value));
                storeProperties();
                notifyListeners(item);
            }
        }
    }

    private void notifyListeners(ISettingsItem item) {
        if (!notifying) {
            notifying = true;
            for (ISettingsListener i : new ArrayList<ISettingsListener>(listeners)) {
                i.settingChanged(item);
            }
            notifying = false;
        }
    }

    /**
    * {@inheritDoc}
    */
    public void addSettingsListener(ISettingsListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
    * {@inheritDoc}
    */
    public void removeSettingsListener(ISettingsListener listener) {
        listeners.remove(listener);
    }
}
