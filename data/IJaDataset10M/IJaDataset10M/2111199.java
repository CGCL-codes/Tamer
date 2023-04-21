package be.vds.jtbdive.core.core;

import java.io.Serializable;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class LogBookMeta implements Serializable {

    private static final long serialVersionUID = 3994031131420884328L;

    private long id = -1;

    private String name;

    private Diver owner;

    public LogBookMeta() {
    }

    public LogBookMeta(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Diver getOwner() {
        return owner;
    }

    public void setOwner(Diver owner) {
        this.owner = owner;
    }
}
