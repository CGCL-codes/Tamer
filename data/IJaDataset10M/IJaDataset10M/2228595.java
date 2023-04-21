package net.sf.jsfcomp.hibernatetrace.domain;

/**
 * @author Mert Caliskan
 * 
 */
public class Person extends BasePersistentObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
