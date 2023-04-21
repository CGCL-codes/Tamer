package net.sf.hdkp.data;

public class Toon implements Comparable<Toon> {

    private final String name;

    private final String type;

    private final int dkp;

    public Toon(String name, String type, int dkp) {
        this.name = name;
        this.type = type;
        this.dkp = dkp;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getDkp() {
        return this.dkp;
    }

    @Override
    public int compareTo(Toon o) {
        return getName().compareTo(o.getName());
    }
}
