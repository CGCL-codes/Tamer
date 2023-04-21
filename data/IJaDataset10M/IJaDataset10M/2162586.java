package org.jmathematics.impl;

import org.jmathematics.sets.NumberSetVisitor;

public class SimpleNumberSetVisitor<R, D> implements NumberSetVisitor<R, D> {

    protected R visit(D data) {
        return null;
    }

    public R visitCOMPLEX(D data) {
        return visit(data);
    }

    public R visitINTEGER(D data) {
        return visitRATIONAL(data);
    }

    public R visitIRRATIONAL(D data) {
        return visitREAL(data);
    }

    public R visitNATURAL(D data) {
        return visitINTEGER(data);
    }

    public R visitRATIONAL(D data) {
        return visitREAL(data);
    }

    public R visitREAL(D data) {
        return visitCOMPLEX(data);
    }

    public R visitTRANSCENDENTAL(D data) {
        return visitIRRATIONAL(data);
    }

    public R visitZERO(D data) {
        return visitINTEGER(data);
    }

    public R visitUNDEF(D data) {
        return visit(data);
    }
}
