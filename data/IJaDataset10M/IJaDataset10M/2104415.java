package pikes.html.xhtml.text.phrase;

import pikes.html.xhtml.Inline;
import pikes.html.xhtml.text.AbstractTextStyle;

public abstract class Phrase extends AbstractTextStyle {

    protected Phrase() {
        super();
    }

    protected Phrase(CharSequence text) {
        super(text);
    }

    protected Phrase(Inline inline) {
        super(inline);
    }
}
