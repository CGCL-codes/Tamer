package net.sf.elbe.core.model.ldif.container;

import net.sf.elbe.core.model.ldif.lines.LdifCommentLine;

public class LdifCommentContainer extends LdifContainer {

    private static final long serialVersionUID = 5193234573866495240L;

    protected LdifCommentContainer() {
    }

    public LdifCommentContainer(LdifCommentLine comment) {
        super(comment);
    }

    public void addComment(LdifCommentLine comment) {
        if (comment == null) throw new IllegalArgumentException("null argument");
        this.parts.add(comment);
    }

    public boolean isValid() {
        if (!super.isAbstractValid()) {
            return false;
        }
        return true;
    }
}
