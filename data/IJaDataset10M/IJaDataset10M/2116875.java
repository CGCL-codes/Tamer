package org.dicom4j.dicom.dictionary.item.support;

import org.dicom4j.dicom.dictionary.item.DictionaryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implements of implement {@link DictionaryItem}
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class AbstractDictionaryItem implements DictionaryItem {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDictionaryItem.class);

    private String key;

    private String name;

    private String fType;

    public AbstractDictionaryItem() {
        this("", "");
    }

    public AbstractDictionaryItem(String key) {
        this(key, "");
    }

    public AbstractDictionaryItem(String key, String name) {
        super();
        setKey(key);
        setName(name);
        setType("");
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return fType;
    }

    public void setKey(String aKey) {
        logger.warn("setKey: " + aKey);
        if (aKey != null) {
            this.key = aKey;
        }
    }

    public void setName(String aName) {
        if (aName != null) {
            name = aName;
        }
    }

    public void setType(String aType) {
        if (aType != null) {
            fType = aType;
        }
    }
}
