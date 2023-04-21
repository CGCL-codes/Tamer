package org.pas.fb2.tag;

public class ImageTag extends Textual {

    static final String NAME = "image";

    public ImageTag(Tag parent) {
        super(parent);
    }

    public String getName() {
        return NAME;
    }

    @Override
    protected void doClose() {
        out("[место илюстрации]");
    }
}
