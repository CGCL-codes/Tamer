package olr.statementpool;

import olr.rdf.Statement;

public class ExtStatement extends Statement {

    public ExtStatement(String subject, String predicate, String object) {
        super(subject, predicate, object);
        isPublic = true;
    }

    public ExtStatement(String subject, String predicate, String object, int id) {
        super(subject, predicate, object, id);
        isPublic = true;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean mine) {
        own = mine;
    }

    protected boolean isPublic;

    protected boolean own;
}
