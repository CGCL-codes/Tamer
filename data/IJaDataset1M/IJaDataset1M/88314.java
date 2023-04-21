package net.sourceforge.toscanaj.model.burmeister;

import net.sourceforge.toscanaj.model.BinaryRelation;
import net.sourceforge.toscanaj.model.BinaryRelationImplementation;
import net.sourceforge.toscanaj.model.Context;
import java.util.Collection;
import java.util.HashSet;

public class BurmeisterContext implements Context {

    private Collection objects = new HashSet();

    private Collection attributes = new HashSet();

    private BinaryRelation relation = new BinaryRelationImplementation();

    private String name = "";

    public BurmeisterContext(String name) {
        this.name = name;
    }

    public Collection getObjects() {
        return objects;
    }

    public Collection getAttributes() {
        return attributes;
    }

    public BinaryRelation getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }
}
