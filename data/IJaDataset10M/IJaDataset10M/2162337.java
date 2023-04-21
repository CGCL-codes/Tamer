package org.owasp.orizon.core;

public class Keyword extends AbstractCore {

    @Override
    public String toXML() {
        return "<keyword name=\"" + getName() + "\" type=\"" + getType() + "\" />";
    }
}
