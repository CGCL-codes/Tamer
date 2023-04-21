package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class ValueOf extends Instruction {

    private Expression _select;

    private boolean _escaping = true;

    private boolean _isString = false;

    public void display(int indent) {
        indent(indent);
        Util.println("ValueOf");
        indent(indent + IndentIncrement);
        Util.println("select " + _select.toString());
    }

    public void parseContents(Parser parser) {
        _select = parser.parseExpression(this, "select", null);
        if (_select.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "select");
            return;
        }
        final String str = getAttribute("disable-output-escaping");
        if ((str != null) && (str.equals("yes"))) _escaping = false;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type type = _select.typeCheck(stable);
        if (type != null && !type.identicalTo(Type.Node)) {
            if (type.identicalTo(Type.NodeSet)) {
                _select = new CastExpr(_select, Type.Node);
            } else {
                _isString = true;
                if (!type.identicalTo(Type.String)) {
                    _select = new CastExpr(_select, Type.String);
                }
                _isString = true;
            }
        }
        return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        final int setEscaping = cpg.addInterfaceMethodref(OUTPUT_HANDLER, "setEscaping", "(Z)Z");
        if (!_escaping) {
            il.append(methodGen.loadHandler());
            il.append(new PUSH(cpg, false));
            il.append(new INVOKEINTERFACE(setEscaping, 2));
        }
        if (_isString) {
            final int characters = cpg.addMethodref(TRANSLET_CLASS, CHARACTERSW, CHARACTERSW_SIG);
            il.append(classGen.loadTranslet());
            _select.translate(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(new INVOKEVIRTUAL(characters));
        } else {
            final int characters = cpg.addInterfaceMethodref(DOM_INTF, CHARACTERS, CHARACTERS_SIG);
            il.append(methodGen.loadDOM());
            _select.translate(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(new INVOKEINTERFACE(characters, 3));
        }
        if (!_escaping) {
            il.append(methodGen.loadHandler());
            il.append(SWAP);
            il.append(new INVOKEINTERFACE(setEscaping, 2));
            il.append(POP);
        }
    }
}
