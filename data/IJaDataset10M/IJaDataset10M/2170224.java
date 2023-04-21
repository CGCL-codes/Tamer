package net.sourceforge.btb.types;

public class Department extends Identifiable {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getId() + ": " + name;
    }
}
