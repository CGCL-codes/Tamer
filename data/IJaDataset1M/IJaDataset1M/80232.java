package net.larsbehnke.petclinicplus.util.namedvocabulary;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Manages a named vocabulary.
 * 
 * @author lbehnke
 * 
 */
public class DefaultNamedVocabularyManager implements NamedVocabularyManager {

    private Map<String, List<NVEntry>> listMap;

    private String resourceBaseName;

    public DefaultNamedVocabularyManager() {
        this("namedvocabulary");
    }

    public DefaultNamedVocabularyManager(String resourceBaseName) {
        super();
        setResourceBaseName(resourceBaseName);
        init();
    }

    private void init() {
        listMap = new HashMap<String, List<NVEntry>>();
        ResourceBundle rb = ResourceBundle.getBundle(getResourceBaseName());
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = rb.getString(key);
            String[] keyPair = key.split("\\.");
            if (keyPair.length != 2) {
                throw new NamedVocabularyException("Invalid configuration: " + key);
            }
            String listName = keyPair[0];
            String entryName = keyPair[1];
            List<NVEntry> list = listMap.get(listName);
            if (list == null) {
                list = new ArrayList<NVEntry>();
                listMap.put(listName, list);
            }
            NVEntry entry = new NVEntry(listName, entryName, value);
            list.add(entry);
        }
    }

    public String getResourceBaseName() {
        return resourceBaseName;
    }

    public void setResourceBaseName(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }

    public List<NVEntry> getList(String listName) {
        return listMap.get(listName);
    }

    public String getListValue(String listName, String entryName) {
        return getListEntry(listName, entryName).getValue();
    }

    public String getEntryNameByValue(String listName, String value) {
        String result = null;
        if (value == null || listName == null) {
            throw new NamedVocabularyException("Undefined listName or value: " + listName + "/" + value);
        }
        List<NVEntry> list = listMap.get(listName);
        if (list == null) {
            throw new NamedVocabularyException("Invalid list name: " + listName);
        }
        for (NVEntry entry : list) {
            if (value.equalsIgnoreCase(entry.getValue())) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }

    public NVEntry getListEntry(String listName, String entryName) {
        NVEntry result = null;
        if (entryName == null || listName == null) {
            throw new NamedVocabularyException("Undefined listName or entryName: " + listName + "/" + entryName);
        }
        List<NVEntry> list = listMap.get(listName);
        if (list == null) {
            throw new NamedVocabularyException("Invalid list name: " + listName);
        }
        for (NVEntry entry : list) {
            if (entryName.equalsIgnoreCase(entry.getKey())) {
                result = entry;
                break;
            }
        }
        return result;
    }
}
