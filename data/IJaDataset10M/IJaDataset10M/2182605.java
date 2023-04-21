package com.doxological.doxquery.types;

import java.lang.String;
import java.util.*;
import javax.xml.namespace.QName;

/**
 * <p>.</p>
 *
 * @author John Snelson
 */
public class AllType extends Type {

    private List<Type> children_ = new ArrayList<Type>(2);

    public AllType() {
        this(Occurrence.EXACTLY_ONE);
    }

    public AllType(Occurrence occurrence) {
        super(Kind.ALL, occurrence);
    }

    public AllType(Type l, Type r, Occurrence occurrence) {
        this(occurrence);
        addChild(l);
        addChild(r);
    }

    public AllType(Type l, Type r) {
        this(Occurrence.EXACTLY_ONE);
        addChild(l);
        addChild(r);
    }

    public void addChild(Type type) {
        if (type instanceof AllType && type.getOccurrence() == Occurrence.EXACTLY_ONE) {
            children_.addAll(((AllType) type).children_);
        } else {
            children_.add(type);
        }
    }

    public Collection<Type> getChildren() {
        return children_;
    }

    public String toQuery() {
        String result = "(";
        boolean add = false;
        for (Type t : children_) {
            if (add) result += " & "; else add = true;
            result += t.toQuery();
        }
        return result + ")" + occurrenceToSymbol();
    }
}
