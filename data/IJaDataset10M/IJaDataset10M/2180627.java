package org.gocha.textbox;

import java.awt.event.ActionEvent;
import org.gocha.rbnf.RBNFLog;
import org.gocha.rbnf.RBNFParser;
import org.gocha.styles.Styles;
import org.gocha.styles.StylesParser;

/**
 * @author gocha
 */
public class RBNFSyntaxAction extends TextBoxFrame.TextBoxFrameAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        final TextBoxFrame f = getTextBoxFrame();
        if (f == null) return;
        TextDocument textDoc = f.getSelectedTextDocument();
        if (textDoc == null) return;
        String styleSource = "comment {\n" + "color: #800;\n" + "}\n" + "id { \n" + "font-weight : bold; \n" + "}\n";
        StylesParser sparser = new StylesParser(styleSource);
        Styles s = sparser.styles();
        if (s == null) return;
        textDoc.getHighlighter().setStyles(s);
        RBNFParser rbnf = new RBNFLog("");
        textDoc.getHighlighter().setLexer(rbnf.getLexer());
        textDoc.getHighlighter().start();
    }
}
