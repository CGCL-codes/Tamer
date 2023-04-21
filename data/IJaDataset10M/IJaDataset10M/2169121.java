package edu.cmu.ece.nebula.directory.p2p;

import edu.cmu.ece.nebula.directory.Entry;

public abstract class EntryImpl implements Entry {

    private String parent;

    private String path;

    EntryImpl(String parent, String path) {
        this.parent = parent;
        this.path = path;
    }

    public final String getParentPath() {
        return parent;
    }

    public final String getPath() {
        return path;
    }
}
