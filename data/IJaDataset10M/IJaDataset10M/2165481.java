package cz.langteacher.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import cz.langteacher.LTSettingKeys;
import cz.langteacher.dao.iface.SettingsDAOIface;
import cz.langteacher.model.Setting;

/**
 * Represent POJO for keeping of all settings for LangTeacher
 * @author libor
 *
 */
public class LangTeacherSettings implements LangTeacherSettingsIface {

    /** Shortcuts for some Settings constants*/
    public static final String STUDIED_LANGUAGE_NAME = LTSettingKeys.STUDIED_LANGUAGE_NAME.getValue();

    public static final String BASE_LANGUAGE_NAME = LTSettingKeys.BASE_LANGUAGE_NAME.getValue();

    private static LangTeacherSettings instance;

    private Map<String, String> settingsMap = new HashMap<String, String>();

    private Map<String, String> settingsMapShadow = new HashMap<String, String>();

    @Autowired
    private SettingsDAOIface settingDAO;

    public static LangTeacherSettings getInstance() {
        if (instance == null) {
            instance = new LangTeacherSettings();
        }
        return instance;
    }

    private LangTeacherSettings() {
    }

    private void init() {
        List<Setting> settings = settingDAO.getAllSettings();
        for (Setting setting : settings) {
            settingsMap.put(setting.getAttribute(), setting.getValue());
            settingsMapShadow.put(setting.getAttribute(), setting.getValue());
        }
    }

    /**
	 * Return setting for given setting
	 * @param key
	 * @return
	 */
    public String getSetting(LTSettingKeys key) {
        return getSettings().get(key.getValue());
    }

    /**
	 * Insert new value of property
	 * @param key
	 * @param value
	 */
    public void setSetting(LTSettingKeys key, String value) {
        getSettings().put(key.getValue(), value);
    }

    public Map<String, String> getSettings() {
        if (settingsMap.isEmpty()) {
            init();
        }
        return settingsMap;
    }

    /**
	 * Store changed setting to DB
	 */
    public void saveToDB() {
        for (Map.Entry<String, String> entry : getSettings().entrySet()) {
            String oldValue = settingsMapShadow.get(entry.getKey());
            if (oldValue == null) {
                settingDAO.insertSetting(new Setting(entry.getKey(), entry.getValue()));
            } else if (!oldValue.equals(entry.getValue())) {
                settingDAO.update(new Setting(entry.getKey(), entry.getValue()));
            }
        }
        settingsMapShadow = new HashMap<String, String>(settingsMap);
    }
}
