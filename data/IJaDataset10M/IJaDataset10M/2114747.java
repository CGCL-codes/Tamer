package com.ikaad.mathnotepad.engine.ast;

import java.util.Vector;
import com.ikaad.mathnotepad.engine.visitor.Visitable;
import com.ikaad.mathnotepad.engine.visitor.Visitor;

public class Command implements Visitable {

    public String id;

    public Vector idlist;

    public Command(String sval, Object obj) {
        id = (String) sval;
        idlist = (Vector) obj;
    }

    public Object accept(Visitor v, Object o) {
        return v.visit(this, o);
    }
}
