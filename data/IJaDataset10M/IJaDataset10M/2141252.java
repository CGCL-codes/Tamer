package org.jmlspecs.jml6.core.ast.binding;

public abstract class JmlBinding implements IJmlBinding {

    protected String name;

    protected Kind kind;

    protected JmlBinding(Kind kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Kind getKind() {
        return kind;
    }
}
