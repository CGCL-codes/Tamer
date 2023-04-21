package edu.uvm.cs.calendar.parser.visitor;

import java.io.PrintWriter;
import java.io.Writer;
import edu.uvm.cs.calendar.AbstractSyntaxContainer;
import edu.uvm.cs.calendar.parser.abstractsyntax.*;

/**
 * Use this visitor to dump an abstract syntax tree into the CAL interpreter
 * format. This is a relatively simple task since the abstract syntax is based
 * on this format.
 * 
 * @author Jeremy Gustie
 * @version 1.0
 */
public class CalendarFileVisitor implements AbstractSyntaxVisitor {

    protected PrintWriter out;

    public CalendarFileVisitor(Writer out) {
        this.out = new PrintWriter(out);
    }

    protected void emit(String str) {
        out.print(str);
    }

    protected void newline() {
        out.println();
        out.flush();
    }

    public void visit(AbstractSyntaxContainer a) {
        a.getAbstractSyntax().accept(this);
    }

    public void visit(AddGranularity a) {
        emit("add gran ");
        a.getGranId().accept(this);
        emit(" into ");
        a.getCalendarId().accept(this);
        emit(" with ");
        a.getExp().accept(this);
        emit(";");
        newline();
    }

    public void visit(Alter a) {
        emit("alter (");
        a.getStep().accept(this);
        emit(", ");
        a.getCycle().accept(this);
        emit(", ");
        a.getOffset().accept(this);
        emit(", ");
        a.getChange().accept(this);
        emit(", ");
        a.getRepeat().accept(this);
        emit(")");
    }

    public void visit(AnchoredGroup a) {
        emit("anchored_group (");
        a.getC1().accept(this);
        emit(", ");
        a.getC2().accept(this);
        emit(")");
    }

    public void visit(Combine c) {
        emit("combine (");
        c.getC1().accept(this);
        emit(", ");
        c.getC2().accept(this);
        emit(")");
    }

    public void visit(CommandList c) {
        for (int i = 0; i < c.size(); ++i) {
            c.get(i).accept(this);
        }
    }

    public void visit(CreateCalendar c) {
        emit("create calendar ");
        c.getCalendarId().accept(this);
        emit(" with ");
        c.getBaseGranId().accept(this);
        emit(";");
        newline();
    }

    public void visit(DefineLabel d) {
        emit("lab_scheme ");
        d.getLabelName().accept(this);
        emit(" in ");
        d.getCalendarId().accept(this);
        emit(" for ");
        d.getBase().accept(this);
        emit(" with ");
        d.getLabel().accept(this);
        emit(";");
        newline();
    }

    public void visit(Difference d) {
        emit("difference (");
        d.getC1().accept(this);
        emit(", ");
        d.getC2().accept(this);
        emit(")");
    }

    public void visit(Divide d) {
        d.getDividend().accept(this);
        emit("/");
        d.getDevisor().accept(this);
    }

    public void visit(DropCalendar d) {
        emit("drop calendar ");
        d.getCalendarId().accept(this);
        emit(";");
        newline();
    }

    public void visit(Granularity g) {
        emit(g.toString());
    }

    public void visit(Group g) {
        emit("group (");
        g.getBase().accept(this);
        emit(", ");
        g.getSize().accept(this);
        emit(")");
    }

    public void visit(Identifier i) {
        emit(i.toString());
    }

    public void visit(Intersect i) {
        emit("intersect (");
        i.getC1().accept(this);
        emit(", ");
        i.getC2().accept(this);
        emit(")");
    }

    public void visit(IntValue i) {
        emit(i.toString());
    }

    public void visit(Label l) {
        for (int i = 0; i < l.size() - 1; ++i) {
            emit(l.getGranularity(i).toString());
            emit(", ");
        }
        emit(l.getGranularity(l.size() - 1).toString());
    }

    public void visit(Minus m) {
        m.getMinuend().accept(this);
        emit("-");
        m.getSubtrahend().accept(this);
    }

    public void visit(Plus p) {
        p.getAddendA().accept(this);
        emit("+");
        p.getAddendB().accept(this);
    }

    public void visit(SelectDown s) {
        emit("select_down (");
        s.getC1().accept(this);
        emit(", ");
        s.getC2().accept(this);
        emit(", ");
        s.getOffset().accept(this);
        emit(", ");
        s.getSize().accept(this);
        emit(")");
    }

    public void visit(SelectIntersect s) {
        emit("select_by_overlap (");
        s.getC1().accept(this);
        emit(", ");
        s.getC2().accept(this);
        emit(", ");
        s.getSize().accept(this);
        emit(", ");
        s.getOffset().accept(this);
        emit(")");
    }

    public void visit(SelectUp s) {
        emit("select_up (");
        s.getC1().accept(this);
        emit(", ");
        s.getC2().accept(this);
        emit(")");
    }

    public void visit(Shift s) {
        emit("shift (");
        s.getBase().accept(this);
        emit(", ");
        s.getOffset().accept(this);
        emit(")");
    }

    public void visit(Subset s) {
        emit("subset (");
        s.getBase().accept(this);
        emit(", ");
        s.getLower().accept(this);
        emit(", ");
        s.getUpper().accept(this);
        emit(")");
    }

    public void visit(Times t) {
        t.getFactorA().accept(this);
        emit("*");
        t.getFactorB().accept(this);
    }

    public void visit(Union u) {
        emit("union (");
        u.getC1().accept(this);
        emit(", ");
        u.getC2().accept(this);
        emit(")");
    }
}
