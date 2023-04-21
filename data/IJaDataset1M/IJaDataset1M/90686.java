package org.jpox.samples.inheritance;

/**
 * Base class for inheritance tests - sample "E".
 * This sample has a container in the subclass.
 * @version $Revision: 1.1 $
 */
public class EBase {

    private long id;

    private String name;

    public EBase() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
