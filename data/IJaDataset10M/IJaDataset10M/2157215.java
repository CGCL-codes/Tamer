package org.jmlspecs.jir.visitor.result;

public final class NotSpecified implements IExp {

    @Override
    public String toString() {
        return JMLKeyword.NOT_SPECIFIED.getToken();
    }
}
