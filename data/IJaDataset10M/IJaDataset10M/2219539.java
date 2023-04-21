package org.makagiga.commons.opml;

import static org.makagiga.commons.UI._;
import org.makagiga.commons.*;

public class OPMLWriter extends XMLBuilder {

    public OPMLWriter() {
    }

    public void addOutline(final String text, final Object... args) {
        Object[] a = new Object[args.length + 2];
        a[0] = "text";
        a[1] = (text == null) ? "" : escape(text);
        System.arraycopy(args, 0, a, 2, args.length);
        singleTag("outline", a);
    }

    public void beginBody() {
        beginTag("body");
    }

    public void beginOPML() {
        beginTag("opml", "version", "1.0");
    }

    public void beginOutline(final String text, final Object... args) {
        Object[] a = new Object[args.length + 2];
        a[0] = "text";
        a[1] = (text == null) ? "" : escape(text);
        System.arraycopy(args, 0, a, 2, args.length);
        beginTag("outline", a);
    }

    public void emptyHead() {
        singleTag("head");
    }

    public void emptyHead(final String title) {
        if (title == null) {
            singleTag("head");
        } else {
            beginTag("head");
            doubleTag("title", escape(title));
            endTag("head");
        }
    }

    public void endBody() {
        endTag("body");
    }

    public void endOPML() {
        endTag("opml");
    }

    public void endOutline() {
        endTag("outline");
    }
}
