package fmx.bo;

public class Node {

    public Node(final String n, final String s) {
        setName(n);
        setShortName(s);
    }

    public Node(final String s) {
        this(s, s);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String s) {
        shortName = s;
    }

    public String getName() {
        return name;
    }

    public void setName(final String n) {
        name = n;
    }

    @Override
    public String toString() {
        return getShortName();
    }

    private String name;

    private String shortName;
}
