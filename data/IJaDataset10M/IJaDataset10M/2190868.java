package net.sf.signs.intermediate;

import net.sf.signs.SourceLocation;

@SuppressWarnings("serial")
class LibraryClause extends IntermediateObject {

    private String id;

    public LibraryClause(String id_, IntermediateObject parent_, SourceLocation location_) {
        super(parent_, location_);
        id = id_;
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        return null;
    }

    @Override
    public int getNumChildren() {
        return 0;
    }

    public String getId() {
        return id;
    }
}
