package org.pas.fb2.tag;

public class ParagraphTag extends Textual {

    static final String NAME = "p";

    ParagraphTag(Tag parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void doClose() {
        out(" " + removeSpaces(getText()));
    }
}
