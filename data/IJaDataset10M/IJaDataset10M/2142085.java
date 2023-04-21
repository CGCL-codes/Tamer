package codeGenerator;

import java.util.ArrayList;
import java.util.List;
import type.ArrayType;
import type.TypeCode;
import type.checker.TypeVisitor;
import Javalette.Absyn.ArrDec;
import Javalette.Absyn.DArrDecIn;
import Javalette.Absyn.DDecl;
import Javalette.Absyn.DInit;
import Javalette.Absyn.Decl;
import Javalette.Absyn.DeclAmb;
import Javalette.Absyn.ELit;
import Javalette.Absyn.LInteger;
import env.Env;
import env.Value;

public class DeclVisitor<R, A> implements Decl.Visitor<Object, Env> {

    private final AbstractCodeGenerator codeGenerator;

    public DeclVisitor(AbstractCodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    public Object visit(DArrDecIn p, Env arg) {
        codeGenerator.printDebug("visit(DDecIn p, Env arg) ");
        TypeCode type = (TypeCode) p.type_.accept(new TypeVisitor(), arg);
        boolean isArray = 0 < p.listarrdec_.size();
        List<Integer> arrayDims = new ArrayList<Integer>();
        if (isArray) {
            for (ArrDec x : p.listarrdec_) {
                if (x.exp_ instanceof ELit) {
                    ELit eLit = (ELit) x.exp_;
                    if (eLit.literal_ instanceof LInteger) {
                        arrayDims.add(((LInteger) eLit.literal_).integer_);
                    }
                } else {
                }
            }
        }
        for (DeclAmb x : p.listdeclamb_) {
            if (x instanceof DDecl) {
                DDecl d = (DDecl) x;
                String ident = d.cppident_;
                if (isArray) {
                    arg.addVar(ident, new ArrayType(type, arrayDims));
                } else {
                    arg.addVar(ident, type);
                }
                int variableIndex = arg.getVariableIndex(ident);
                if (isArray) codeGenerator.generateAllocateArray(variableIndex, arrayDims, type); else codeGenerator.generateAllocateVariable(variableIndex, type);
            } else if (x instanceof DInit) {
                DInit d = (DInit) x;
                String ident = d.cppident_;
                Value v = (Value) (d.exp_.accept(new ExpEvaluator(codeGenerator), arg));
                if (isArray) {
                    arg.addVar(ident, new ArrayType(type, arrayDims));
                } else {
                    arg.addVar(ident, type);
                    arg.setVar(ident, v);
                }
                codeGenerator.printDebug("visit(DDecIn p, Env arg) variableName:" + ident + " variableType:" + type.toString());
                int variableIndex = arg.getVariableIndex(ident);
                if (isArray) {
                    codeGenerator.generateAllocateArray(variableIndex, arrayDims, type);
                    codeGenerator.generateStoreArray(variableIndex, type);
                } else {
                    codeGenerator.generateAllocateVariable(variableIndex, type);
                    codeGenerator.generateStoreInVariable(variableIndex, type);
                }
            }
        }
        return null;
    }
}
