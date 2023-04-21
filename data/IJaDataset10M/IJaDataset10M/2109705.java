package net.sourceforge.transmogrify.symtab;

import java.util.*;

public abstract class ExternalDefinition implements IDefinition {

    public boolean isSourced() {
        return false;
    }

    public void addReference(Reference reference) {
    }

    public Iterator getReferences() {
        return new Vector().iterator();
    }

    public int getNumReferences() {
        return 0;
    }
}
