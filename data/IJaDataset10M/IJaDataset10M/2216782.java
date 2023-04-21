package org.wingx;

import java.util.List;
import java.util.Map;

/**
 * @author Christian Schyma
 */
public interface XSuggestDataSource {

    /**
     * Generates and returns a list of suggestions. A suggestion consists of a map entry, mapping a string value to
     * a string representation. In most cases, both strings will be the same. Sometimes one might like to display
     * suggestions in a deviant manner. Then the key is what will be inserted into the textfield and the value is
     * what is displayed as suggestions.
     *
     * @param lookupText The query to filter the suggestions
     */
    List<Map.Entry<String, String>> generateSuggestions(String lookupText);

    static class Entry implements Map.Entry<String, String> {

        String key;

        String value;

        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public String setValue(String value) {
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
