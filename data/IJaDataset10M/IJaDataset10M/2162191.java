package org.enerj.apache.commons.beanutils;

public class AlphaBean extends AbstractParent implements Child {

    private String name;

    public AlphaBean() {
    }

    public AlphaBean(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Used for testing that correct exception is thrown.
     */
    public void bogus(String badParameter) {
    }
}
