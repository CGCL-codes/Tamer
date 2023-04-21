package compiler.absyn;

import java.util.*;

public class Print {

    java.io.PrintStream out;

    public Print(java.io.PrintStream o) {
        out = o;
    }

    void indent(int d) {
        for (int i = 0; i < d; i++) out.print(' ');
    }

    void say(String s) {
        out.print(s);
    }

    void say(int i) {
        Integer local = new Integer(i);
        out.print(local.toString());
    }

    void say(boolean b) {
        Boolean local = new Boolean(b);
        out.print(local.toString());
    }

    void sayln(String s) {
        say(s);
        say("\n");
    }

    void prExp(OpExp e, int d) {
        sayln("OpExp(");
        indent(d + 1);
        switch(e.oper) {
            case OpExp.PLUS:
                say("PLUS");
                break;
            case OpExp.MINUS:
                say("MINUS");
                break;
            case OpExp.MUL:
                say("MUL");
                break;
            case OpExp.DIV:
                say("DIV");
                break;
            case OpExp.EQ:
                say("EQ");
                break;
            case OpExp.NE:
                say("NE");
                break;
            case OpExp.LT:
                say("LT");
                break;
            case OpExp.LE:
                say("LE");
                break;
            case OpExp.GT:
                say("GT");
                break;
            case OpExp.GE:
                say("GE");
                break;
            case OpExp.AND:
                say("GE");
                break;
            case OpExp.OR:
                say("GE");
                break;
            default:
                throw new Error("Print.prExp.OpExp");
        }
        sayln(",");
        prExp(e.left, d + 1);
        sayln(",");
        prExp(e.right, d + 1);
        say(")");
    }

    void prExp(IntExp e, int d) {
        say("IntExp(");
        say(e.value);
        say(")");
    }

    void prExp(SelectExp e, int d) {
        say("SelectExp(");
        prExp(e.selectlist, d + 1);
        prExp(e.fromlist, d + 1);
        if (e.predicate != null) prExp(e.predicate, d + 1);
        sayln(")");
    }

    void prExp(SelectList e, int d) {
        say("SelectList(");
        say(e.field.idTok + ") ");
    }

    void prExp(FromList e, int d) {
        say("FromList{");
        say(e.name + ") ");
    }

    void prExp(WhereList e, int d) {
        say("wherelist(");
        prExp(e.term, d + 1);
        if (e.tail != null) {
            prExp(e.tail, d + 1);
        }
        say(") ");
    }

    void prExp(Term t, int d) {
        if (t.lexp.isFieldName()) {
            say(t.lexp.asFieldName() + " ");
        } else say(t.lexp.asConstant().toString() + " ");
        if (t.rexp.isFieldName()) {
            say(t.rexp.asFieldName() + " ");
        } else say(t.rexp.asConstant().toString() + " ");
    }

    public void prExp(Exp e, int d) {
        indent(d);
        if (e instanceof OpExp) prExp((OpExp) e, d); else if (e instanceof SelectExp) prExp((SelectExp) e, d); else if (e instanceof CreateExp) preExp((CreateExp) e, d); else throw new Error("Print.prExp");
    }

    public void preExp(CreateExp e, int d) {
        if (e instanceof CreateTable) preExp((CreateTable) e, d);
    }

    public void preExp(CreateTable e, int d) {
        indent(d);
        say("CreateTable " + e.tablename + "(");
        preExp(e.fielddefs, d);
        say(")");
    }

    public void preExp(FieldDefs fds, int d) {
        while (fds != null) {
            FieldDef fd = fds.fielddef;
            say(fd.idTok + " ");
            say(fd.typedef.toString() + " ");
            fds = fds.tail;
        }
    }

    void prExplist(ExpList e, int d) {
        indent(d);
        say("ExpList(");
        if (e != null) {
            sayln("");
            prExp(e.head, d + 1);
            if (e.tail != null) {
                sayln(",");
                prExplist(e.tail, d + 1);
            }
        }
        say(")");
    }

    void prFieldExpList(FieldExpList f, int d) {
        indent(d);
        say("FieldExpList(");
        if (f != null) {
            sayln("");
            say(f.name.toString());
            sayln(",");
            prExp(f.init, d + 1);
            sayln(",");
            prFieldExpList(f.tail, d + 1);
        }
        say(")");
    }
}
