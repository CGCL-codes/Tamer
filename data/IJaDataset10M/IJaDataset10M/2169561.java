package org.ochnygosch.jIMAP.base.command.search;

import java.util.Iterator;
import java.util.Vector;
import org.ochnygosch.jIMAP.internal.Argument;
import org.ochnygosch.jIMAP.internal.ArgumentException;
import org.ochnygosch.jIMAP.internal.Atom;
import org.ochnygosch.jIMAP.internal.Number;

public class NumberSearchKey extends SearchKey {

    private long arg;

    public NumberSearchKey(String name, long arg) {
        super(name);
    }

    @Override
    public Iterator<Argument> getArguments() {
        Vector<Argument> v = new Vector<Argument>();
        try {
            v.add(new Atom(this.getName()));
            v.add(new Number(this.arg));
        } catch (ArgumentException e) {
        }
        return v.iterator();
    }
}
