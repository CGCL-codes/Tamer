package org.jpox.samples.dependentfield;

/**
 * Related object for dependent field testing.
 * @version $Revision: 1.1 $
 */
public class DependentElement9 {

    private int id;

    String name;

    DependentHolder owner;

    DependentElement9 key;

    public DependentElement9() {
        super();
    }

    public DependentElement9(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public final DependentHolder getOwner() {
        return owner;
    }

    public final void setOwner(DependentHolder owner) {
        this.owner = owner;
    }

    public DependentElement9 getKey() {
        return key;
    }

    public void setKey(DependentElement9 key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null || !(arg0 instanceof DependentElement9)) {
            return false;
        }
        DependentElement9 df = (DependentElement9) arg0;
        return this.id == df.id;
    }
}
