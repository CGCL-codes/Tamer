package com.nw.dsl4j.grammar;

import com.nw.dsl4j.annotations.Token;

@Token(pattern = "('a'..'z'|'A'..'Z'|'_')+")
public class ID {

    String value;

    public ID() {
    }

    public ID(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
