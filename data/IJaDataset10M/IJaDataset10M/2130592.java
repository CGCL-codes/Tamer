package org.makagiga.editors.text;

import java.io.IOException;
import java.io.Writer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.makagiga.commons.HTMLViewFactory;

public final class TextEditorKit extends HTMLEditorKit {

    @Override
    public Document createDefaultDocument() {
        StyleSheet styles = new StyleSheet();
        styles.addRule("a {" + "color: blue;" + "text-decoration: underline;" + "}");
        TextEditorDocument doc = new TextEditorDocument(styles);
        doc.setParser(getParser());
        doc.setAsynchronousLoadPriority(-1);
        doc.setTokenThreshold(100);
        return doc;
    }

    @Override
    public ViewFactory getViewFactory() {
        return new TextEditorViewFactory(super.getViewFactory());
    }

    @Override
    public void write(final Writer out, final Document doc, final int pos, final int len) throws BadLocationException, IOException {
        TextEditorWriter writer = new TextEditorWriter(out, (HTMLDocument) doc, pos, len);
        writer.write();
    }

    private static final class TextEditorViewFactory extends HTMLViewFactory {

        private TextEditorViewFactory(final ViewFactory impl) {
            super(impl);
            setLoadsSynchronously(true);
        }
    }
}
