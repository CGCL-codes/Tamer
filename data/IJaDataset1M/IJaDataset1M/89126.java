package com.ikaad.mathnotepad.engine.visitor;

import java.util.Vector;
import com.ikaad.mathnotepad.engine.Real;
import com.ikaad.mathnotepad.engine.SymTb;
import com.ikaad.mathnotepad.engine.ast.ApplyExpr;
import com.ikaad.mathnotepad.engine.ast.Arg;
import com.ikaad.mathnotepad.engine.ast.AssignStmt;
import com.ikaad.mathnotepad.engine.ast.Command;
import com.ikaad.mathnotepad.engine.ast.CommandStmt;
import com.ikaad.mathnotepad.engine.ast.DivExpr;
import com.ikaad.mathnotepad.engine.ast.Expr;
import com.ikaad.mathnotepad.engine.ast.ExprStmt;
import com.ikaad.mathnotepad.engine.ast.FacExpr;
import com.ikaad.mathnotepad.engine.ast.FloatExpr;
import com.ikaad.mathnotepad.engine.ast.Function;
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
import com.ikaad.mathnotepad.engine.command.CommandManager;

public class TypCheckVisitor implements Visitor {

    public Object visit(Input v, Object o) {
        TypCheckResult r = (TypCheckResult) v.stmt.accept(this, o);
        String errmsg = "";
        return r;
    }

    public Object visit(AssignStmt v, Object o) {
        TypCheckResult a = (TypCheckResult) v.lvalue.accept(this, o);
        return a.isChecked() ? v.expr.accept(this, o) : a;
    }

    public Object visit(ExprStmt v, Object o) {
        return v.expr.accept(this, o);
    }

    public Object visit(CommandStmt v, Object o) {
        return v.command.accept(this, o);
    }

    public Object visit(FunctionStmt v, Object o) {
        return v.function.accept(this, o);
    }

    public Object visit(Command v, Object o) {
        return CommandManager.instance.isValid((SymTb) o, v.id, v.idlist);
    }

    public Object visit(UserFunction v, Object o) {
        SymTb tb = new SymTb(((SymTb) o).onlyFunc());
        Vector ids = v.getIdList();
        for (int i = 0; i < ids.size(); i++) {
            String id = (String) ids.elementAt(i);
            if (tb.hasReal(id)) {
                String errmsg = "duplicate variable " + id;
                return new TypCheckResult(TypCheckResult.ERR_FUNC_ARITY_MISMATCH, errmsg);
            }
            tb.setReal((String) ids.elementAt(i), Real.ZERO);
        }
        TypCheckResult r = (TypCheckResult) v.getExpr().accept(this, tb);
        return r;
    }

    public Object visit(Lvalue v, Object o) {
        return SymTb.BUILTIN_SYMBOL_TB.hasSymbol(v.id) ? new TypCheckResult(TypCheckResult.ERR_RESERV_WORD_REASSIGN, "can not reassign reserved word: " + v.id) : TypCheckResult.CHECKED;
    }

    public Object visit(PlusExpr v, Object o) {
        TypCheckResult a = (TypCheckResult) v.left.accept(this, o);
        return a.isChecked() ? v.right.accept(this, o) : a;
    }

    public Object visit(MinusExpr v, Object o) {
        TypCheckResult a = (TypCheckResult) v.left.accept(this, o);
        return a.isChecked() ? v.right.accept(this, o) : a;
    }

    public Object visit(MulExpr v, Object o) {
        TypCheckResult a = (TypCheckResult) v.left.accept(this, o);
        return a.isChecked() ? v.right.accept(this, o) : a;
    }

    public Object visit(DivExpr v, Object o) {
        TypCheckResult a = (TypCheckResult) v.left.accept(this, o);
        return a.isChecked() ? v.right.accept(this, o) : a;
    }

    public Object visit(FacExpr v, Object o) {
        return v.expr.accept(this, o);
    }

    public Object visit(NegExpr v, Object o) {
        return v.expr.accept(this, o);
    }

    public Object visit(PowExpr v, Object o) {
        TypCheckResult a = (TypCheckResult) v.left.accept(this, o);
        return a.isChecked() ? v.right.accept(this, o) : a;
    }

    public Object visit(IdExpr v, Object o) {
        SymTb tb = (SymTb) o;
        return tb.hasReal(v.id) ? TypCheckResult.CHECKED : new TypCheckResult(TypCheckResult.ERR_UNKNOWN_ID, "unknown variable " + v.id);
    }

    public Object visit(ApplyExpr v, Object o) {
        SymTb tb = (SymTb) o;
        String n = v.id;
        if (!tb.hasFunc(n)) return new TypCheckResult(TypCheckResult.ERR_UNKNOWN_FUNCTION, v.id);
        Function f = tb.getFunc(n);
        Vector ps = f.getIdList();
        Vector es = v.exprList;
        if (ps.size() != es.size()) return new TypCheckResult(TypCheckResult.ERR_FUNC_ARITY_MISMATCH, "function " + f.getName() + " expects " + ps.size() + " parameter(s)");
        TypCheckResult r = TypCheckResult.CHECKED;
        for (int i = 0; i < es.size(); i++) {
            r = (TypCheckResult) ((Expr) es.elementAt(i)).accept(this, o);
            if (!r.isChecked()) return r;
        }
        return r;
    }

    public Object visit(FloatExpr v, Object o) {
        return TypCheckResult.CHECKED;
    }

    public Object visit(ParnExpr v, Object o) {
        return v.expr.accept(this, o);
    }

    public Object visit(Arg arg, Object o) {
        throw new RuntimeException("should not get here");
    }
}
