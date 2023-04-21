package com.ynhenc.kml;

import java.io.Writer;

public class KmlText extends KmlSimpleObject {

    public String getText() {
        return text;
    }

    @Override
    public void writeTag(Writer buff) throws Exception {
        buff.append(this.getText());
    }

    @Override
    public String getTag() {
        return null;
    }

    public KmlText(String text) {
        super();
        this.text = text;
    }

    private String text;
}
