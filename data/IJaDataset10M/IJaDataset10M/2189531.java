package org.jpox.samples.serialised;

/**
 * Sample class having PC field that is serialised.
 * 
 * @version $Revision: 1.1 $
 */
public class SerialisedHolder {

    private String name;

    private SerialisedObject pc;

    public SerialisedHolder(String name, SerialisedObject pc) {
        this.name = name;
        this.pc = pc;
    }

    public String getName() {
        return name;
    }

    public SerialisedObject getSerialisedPC() {
        return pc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerialisedPC(SerialisedObject pc) {
        this.pc = pc;
    }
}
