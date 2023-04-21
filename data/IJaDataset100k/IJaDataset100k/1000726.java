package edu.uvm.cs.calendar.parser.abstractsyntax;

import edu.uvm.cs.calendar.parser.visitor.AbstractSyntaxVisitor;

public abstract class CalendarCommand {

    protected Identifier calendarId;

    protected CalendarCommand(Identifier calendarId) {
        this.calendarId = calendarId;
    }

    public abstract void accept(AbstractSyntaxVisitor v);

    public Identifier getCalendarId() {
        return calendarId;
    }
}
