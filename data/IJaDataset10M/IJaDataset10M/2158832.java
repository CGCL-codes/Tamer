package com.ikaad.mathnotepad.engine.visitor;

import com.ikaad.mathnotepad.engine.ast.ApplyExpr;
import com.ikaad.mathnotepad.engine.ast.Arg;
import com.ikaad.mathnotepad.engine.ast.AssignStmt;
import com.ikaad.mathnotepad.engine.ast.Command;
import com.ikaad.mathnotepad.engine.ast.CommandStmt;
import com.ikaad.mathnotepad.engine.ast.DivExpr;
import com.ikaad.mathnotepad.engine.ast.ExprStmt;
import com.ikaad.mathnotepad.engine.ast.FacExpr;
import com.ikaad.mathnotepad.engine.ast.FloatExpr;
import com.ikaad.mathnotepad.engine.ast.FunctionStmt;
import com.ikaad.mathnotepad.engine.ast.IdExpr;
import com.ikaad.mathnotepad.engine.ast.Input;
import com.ikaad.mathnotepad.engine.ast.Lvalue;
import com.ikaad.mathnotepad.engine.ast.MinusExpr;
import com.ikaad.mathnotepad.engine.ast.MulExpr;
import com.ikaad.mathnotepad.engine.ast.NegExpr;
import com.ikaad.mathnotepad.engine.ast.ParnExpr;
import com.ikaad.mathnotepad.engine.ast.PlusExpr;
import com.ikaad.mathnotepad.engine.ast.PowExpr;
import com.ikaad.mathnotepad.engine.ast.UserFunction;

public interface Visitor {

    Object visit(Input input, Object o);

    Object visit(AssignStmt v, Object o);

    Object visit(ExprStmt v, Object o);

    Object visit(CommandStmt v, Object o);

    Object visit(FunctionStmt v, Object o);

    Object visit(Command v, Object o);

    Object visit(UserFunction v, Object o);

    Object visit(Lvalue v, Object o);

    Object visit(PlusExpr v, Object o);

    Object visit(MinusExpr v, Object o);

    Object visit(MulExpr v, Object o);

    Object visit(DivExpr v, Object o);

    Object visit(FacExpr v, Object o);

    Object visit(NegExpr v, Object o);

    Object visit(PowExpr v, Object o);

    Object visit(IdExpr v, Object o);

    Object visit(ApplyExpr v, Object o);

    Object visit(FloatExpr v, Object o);

    Object visit(ParnExpr v, Object o);

    Object visit(Arg arg, Object o);
}
