package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class LiteralExpr extends Expression {

    private final String _value;

    private final String _namespace;

    /**
     * Creates a new literal expression node.
     * @param value the literal expression content/value.
     */
    public LiteralExpr(String value) {
        _value = value;
        _namespace = null;
    }

    /**
     * Creates a new literal expression node.
     * @param value the literal expression content/value.
     * @param namespace the namespace in which the expression exists.
     */
    public LiteralExpr(String value, String namespace) {
        _value = value;
        _namespace = namespace.equals(Constants.EMPTYSTRING) ? null : namespace;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return _type = Type.String;
    }

    public String toString() {
        return "literal-expr(" + _value + ')';
    }

    protected boolean contextDependent() {
        return false;
    }

    protected String getValue() {
        return _value;
    }

    protected String getNamespace() {
        return _namespace;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new PUSH(cpg, _value));
    }
}
