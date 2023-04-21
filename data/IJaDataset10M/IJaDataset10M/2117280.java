package org.jpox.samples.ann_xml.override;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * User account for a person.
 */
@PersistenceCapable(detachable = "true")
public class Account implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.NATIVE)
    private long id;

    @Persistent(nullValue = NullValue.EXCEPTION)
    private String username;

    private boolean enabled;

    public Account() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String s) {
        username = s;
    }
}
