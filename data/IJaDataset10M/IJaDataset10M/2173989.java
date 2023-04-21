package com.ibm.wala.cast.js.ssa;

import com.ibm.wala.cast.ir.ssa.AstAbstractInstructionVisitor;

public class JSAbstractInstructionVisitor extends AstAbstractInstructionVisitor implements JSInstructionVisitor {

    public void visitJavaScriptInvoke(JavaScriptInvoke instruction) {
    }

    public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction) {
    }

    public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction) {
    }

    public void visitTypeOf(JavaScriptTypeOfInstruction instruction) {
    }

    public void visitJavaScriptInstanceOf(JavaScriptInstanceOf instruction) {
    }

    public void visitCheckRef(JavaScriptCheckReference instruction) {
    }

    public void visitWithRegion(JavaScriptWithRegion instruction) {
    }

    @Override
    public void visitSetPrototype(SetPrototype instruction) {
    }

    @Override
    public void visitPrototypeLookup(PrototypeLookup instruction) {
    }
}
