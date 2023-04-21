package Tree;

import java.util.Iterator;
import java.util.List;

public class Print implements IntVisitor {

    java.io.PrintWriter out;

    Temp.TempMap tmap;

    public Print(java.io.PrintWriter o, Temp.TempMap t, Stm s, int i) {
        out = o;
        tmap = t;
        s.accept(this, i);
    }

    public Print(java.io.PrintWriter o, Stm s, int i) {
        this(o, new Temp.DefaultMap(), s, i);
    }

    public Print(java.io.PrintWriter o, Temp.TempMap t, Stm s) {
        this(o, new Temp.DefaultMap(), s, 0);
        o.println();
    }

    public Print(java.io.PrintWriter o, Stm s) {
        this(o, new Temp.DefaultMap(), s);
    }

    public Print(java.io.PrintWriter o, Temp.TempMap t, List<Stm> s) {
        out = o;
        tmap = t;
        for (Iterator<Stm> stms = s.iterator(); stms.hasNext(); ) {
            stms.next().accept(this, 0);
            o.println();
        }
        o.flush();
    }

    private void indent(int d) {
        for (int i = 0; i < d; i++) out.print(' ');
    }

    private void sayln(String s) {
        out.println(s);
    }

    private void say(String s) {
        out.print(s);
    }

    public void visit(SEQ s, int d) {
        indent(d);
        sayln("SEQ(");
        s.left.accept(this, d + 1);
        sayln(",");
        s.right.accept(this, d + 1);
        say(")");
    }

    public void visit(LABEL s, int d) {
        indent(d);
        say("LABEL ");
        say(s.label.toString());
    }

    public void visit(JUMP s, int d) {
        indent(d);
        sayln("JUMP(");
        s.exp.accept(this, d + 1);
        say(")");
    }

    public void visit(CJUMP s, int d) {
        indent(d);
        say("CJUMP(");
        switch(s.relop) {
            case CJUMP.EQ:
                say("EQ");
                break;
            case CJUMP.NE:
                say("NE");
                break;
            case CJUMP.LT:
                say("LT");
                break;
            case CJUMP.GT:
                say("GT");
                break;
            case CJUMP.LE:
                say("LE");
                break;
            case CJUMP.GE:
                say("GE");
                break;
            case CJUMP.ULT:
                say("ULT");
                break;
            case CJUMP.ULE:
                say("ULE");
                break;
            case CJUMP.UGT:
                say("UGT");
                break;
            case CJUMP.UGE:
                say("UGE");
                break;
            default:
                throw new Error("Print.prStm.CJUMP");
        }
        sayln(",");
        s.left.accept(this, d + 1);
        sayln(",");
        s.right.accept(this, d + 1);
        sayln(",");
        indent(d + 1);
        say(s.iftrue.toString());
        say(",");
        say(s.iffalse.toString());
        say(")");
    }

    public void visit(MOVE s, int d) {
        indent(d);
        sayln("MOVE(");
        s.dst.accept(this, d + 1);
        sayln(",");
        s.src.accept(this, d + 1);
        say(")");
    }

    public void visit(EXP s, int d) {
        indent(d);
        sayln("EXP(");
        s.exp.accept(this, d + 1);
        say(")");
    }

    public void visit(BINOP e, int d) {
        indent(d);
        say("BINOP(");
        switch(e.binop) {
            case BINOP.PLUS:
                say("PLUS");
                break;
            case BINOP.MINUS:
                say("MINUS");
                break;
            case BINOP.MUL:
                say("MUL");
                break;
            case BINOP.DIV:
                say("DIV");
                break;
            case BINOP.AND:
                say("AND");
                break;
            case BINOP.OR:
                say("OR");
                break;
            case BINOP.LSHIFT:
                say("LSHIFT");
                break;
            case BINOP.RSHIFT:
                say("RSHIFT");
                break;
            case BINOP.ARSHIFT:
                say("ARSHIFT");
                break;
            case BINOP.XOR:
                say("XOR");
                break;
            default:
                throw new Error("Print.prExp.BINOP");
        }
        sayln(",");
        e.left.accept(this, d + 1);
        sayln(",");
        e.right.accept(this, d + 1);
        say(")");
    }

    public void visit(MEM e, int d) {
        indent(d);
        sayln("MEM(");
        e.exp.accept(this, d + 1);
        say(")");
    }

    public void visit(TEMP e, int d) {
        indent(d);
        say("TEMP ");
        say(tmap.tempMap(e.temp));
    }

    public void visit(ESEQ e, int d) {
        indent(d);
        sayln("ESEQ(");
        e.stm.accept(this, d + 1);
        sayln(",");
        e.exp.accept(this, d + 1);
        say(")");
    }

    public void visit(NAME e, int d) {
        indent(d);
        say("NAME ");
        say(e.label.toString());
    }

    public void visit(CONST e, int d) {
        indent(d);
        say("CONST ");
        say(String.valueOf(e.value));
    }

    public void visit(CALL e, int d) {
        indent(d);
        sayln("CALL(");
        e.func.accept(this, d + 1);
        for (Iterator<Exp> args = e.args.iterator(); args.hasNext(); ) {
            sayln(",");
            args.next().accept(this, d + 1);
        }
        say(")");
    }
}
