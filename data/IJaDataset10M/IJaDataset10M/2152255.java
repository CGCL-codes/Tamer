package org.eclipse.jface.preference;

import java.util.HashMap;
import java.util.Map;
import nill.NullInterface;

public interface IPreferenceStore extends NullInterface {

    void setDefault(String key, boolean val);

    void setDefault(String key, int val);

    void setDefault(String key, String val);

    void addPropertyChangeListener(Object listener);

    String getString(String str);

    int getInt(String key);

    void setValue(String key, String value);

    void setValue(String key, int val);

    int getDefaultInt(String key);

    void setValue(String key, boolean val);

    boolean getBoolean(String key);

    public class MiniImplementation implements IPreferenceStore {

        private Map<String, String> stringPrefs = new HashMap<String, String>();

        public void addPropertyChangeListener(Object listener) {
        }

        public boolean getBoolean(String key) {
            return false;
        }

        public int getDefaultInt(String key) {
            return 0;
        }

        public int getInt(String key) {
            return 0;
        }

        public String getString(String str) {
            return stringPrefs.get(str);
        }

        public void setDefault(String key, boolean val) {
        }

        public void setDefault(String key, int val) {
        }

        public void setDefault(String key, String val) {
            stringPrefs.put(key, val);
        }

        public void setValue(String key, String val) {
            stringPrefs.put(key, val);
        }

        public void setValue(String key, int val) {
        }

        public void setValue(String key, boolean val) {
        }
    }
}
