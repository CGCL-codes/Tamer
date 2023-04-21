package ru.syktsu.projects.oko.objects;

import javax.persistence.Embeddable;

/**
 *
 * @author Konst
 */
@Embeddable
public class ExtendedResult implements java.io.Serializable {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
