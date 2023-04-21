package org.nakedobjects.nos.client.cli;

import org.nakedobjects.noa.reflect.NakedObjectField;

public class FieldMatcher implements Matcher {

    private int index = 0;

    private final NakedObjectField[] members;

    public FieldMatcher(final NakedObjectField[] member) {
        this.members = member;
    }

    public Object getElement() {
        return members[index - 1];
    }

    public boolean hasMoreElements() {
        return index < members.length;
    }

    public String nextElement() {
        return members[index++].getName();
    }
}
