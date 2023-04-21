package org.mobicents.ssf.commons;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

public abstract class NlsBaseTaglet implements Taglet {

    public abstract String getName();

    public boolean inConstructor() {
        return true;
    }

    public boolean inField() {
        return true;
    }

    public boolean inMethod() {
        return true;
    }

    public boolean inOverview() {
        return true;
    }

    public boolean inPackage() {
        return true;
    }

    public boolean inType() {
        return true;
    }

    public boolean isInlineTag() {
        return true;
    }

    public abstract String toString(Tag tag);

    public String toString(Tag[] arg0) {
        return null;
    }
}
