package gov.nist.atlas.impl;

import gov.nist.atlas.Id;

/**
 * @version $Revision: 1.12 $
 * @author <a href='mailto:ubik@users.sf.net'>Christophe Laprun</a>
 */
public class IdImpl implements Id {

    /** Private constructor to prevent direct instantiation. */
    private IdImpl() {
        id = null;
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Id) {
            Id id = (Id) obj;
            return getAsString().equals(id.getAsString());
        }
        return false;
    }

    public String toString() {
        return getAsString();
    }

    protected IdImpl(String stringId) {
        this.id = stringId;
    }

    public String getAsString() {
        return id;
    }

    private final String id;
}
