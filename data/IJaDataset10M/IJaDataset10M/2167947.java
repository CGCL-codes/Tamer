package jfun.yan.xml;

import java.util.List;

final class Tag extends Node {

    private final String name;

    private final Attributes attrs;

    private final List sub;

    Tag(Location loc, String name, Attributes attrs, List sub) {
        super(loc);
        this.attrs = attrs;
        this.name = name;
        this.sub = sub;
    }

    public List getSubNodes() {
        return sub;
    }

    public Attributes getAttributes() {
        return attrs;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "<" + name + ">";
    }

    public String getAttribute(String key) {
        return attrs.getVal(key);
    }
}
