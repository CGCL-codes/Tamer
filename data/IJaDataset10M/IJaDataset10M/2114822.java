package info.repy.idlistserver;

import java.util.LinkedList;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class IDMember {

    @PrimaryKey
    @Persistent
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Persistent
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Persistent
    private LinkedList<String> keys;

    @Persistent
    private LinkedList<String> values;

    private void setKeys(LinkedList<String> keys) {
        this.keys = keys;
    }

    private LinkedList<String> getKeys() {
        return keys;
    }

    private void setValues(LinkedList<String> values) {
        this.values = values;
    }

    private LinkedList<String> getValues() {
        return values;
    }

    public void setProperty(String key, String value) {
        if (getKeys() == null) setKeys(new LinkedList<String>());
        if (getValues() == null) setValues(new LinkedList<String>());
        int keyindex = getKeys().indexOf(key);
        if (keyindex == -1) {
            getKeys().add(key);
            getValues().add(value);
        } else {
            getValues().set(keyindex, value);
        }
    }

    public String getProperty(String key) {
        if (getKeys() == null) return null;
        if (getValues() == null) return null;
        int keyindex = getKeys().indexOf(key);
        if (keyindex == -1) {
            return null;
        } else {
            return getValues().get(keyindex);
        }
    }
}
