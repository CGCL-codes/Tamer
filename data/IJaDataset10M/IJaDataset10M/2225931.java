package org.geometerplus.fbreader.formats.xhtml;

import org.geometerplus.zlibrary.core.xml.ZLStringMap;

class XHTMLTagRestartParagraphAction extends XHTMLTagAction {

    protected void doAtStart(XHTMLReader reader, ZLStringMap xmlattributes) {
        reader.getModelReader().beginParagraph();
        reader.getModelReader().endParagraph();
    }

    protected void doAtEnd(XHTMLReader reader) {
    }
}
