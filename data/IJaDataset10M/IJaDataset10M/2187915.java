package jastgen.nodes;

import java.util.List;

public class Import {

    private final boolean isStatic;

    private final List<String> body;

    public Import(final boolean isStatic, final List<String> body) {
        this.isStatic = isStatic;
        this.body = body;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public List<String> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Import(" + isStatic + ", " + body + ")";
    }
}
