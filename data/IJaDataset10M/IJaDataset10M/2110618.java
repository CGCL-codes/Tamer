package net.disy.ogc.wps.v_1_0_0.model;

public interface DataTypeTypeVisitor {

    void visitLiteral();

    void visitBBox();

    void visitComplex();
}
