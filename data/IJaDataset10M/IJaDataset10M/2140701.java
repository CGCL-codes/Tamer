package org.jmlspecs.jml6.core.ast.binding;

public interface IJmlBinding {

    public static enum Kind {

        VARIABLE, METHOD, TYPE
    }

    public Kind getKind();
}
