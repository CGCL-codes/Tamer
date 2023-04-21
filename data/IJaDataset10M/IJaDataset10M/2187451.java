package org.monet.kernel.model;

import org.monet.kernel.constants.Strings;

public class Expression {

    private ThesaurusLink oDataLink;

    public Expression() {
        this.oDataLink = null;
    }

    public Boolean setDataLink(ThesaurusLink oDataLink) {
        this.oDataLink = oDataLink;
        return true;
    }

    public String evaluate(String sFormula) {
        if (this.oDataLink == null) return Strings.EMPTY;
        return Strings.EMPTY;
    }
}
