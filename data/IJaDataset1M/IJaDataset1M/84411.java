package de.offis.semanticmm4u.generators.html_and_time;

import de.offis.semanticmm4u.global.Constants;

public class XHTMLContentGenerator extends AbstractXHTMLContentGenerator {

    @Override
    protected String prepareContentText(String s) {
        return s;
    }

    @Override
    public String getFilenameSuffix() {
        return ".xhtml";
    }

    @Override
    protected String getMimeType() {
        return Constants.MIME_TYPE_XHTML;
    }
}
