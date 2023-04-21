package seco.notebook.syntax.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 *  HTML documentation view. 
 *  Javadoc content is displayed in JEditorPane pane using HTMLEditorKit.
 *
 *  @author  Martin Roskanin
 *  @since   03/2002
 */
public class HTMLDocView extends JEditorPane {

    private HTMLEditorKit htmlKit;

    private boolean preserve_css;

    /** Creates a new instance of HTMLJavaDocView */
    public HTMLDocView(Color bgColor) {
        this(bgColor, true);
    }

    public HTMLDocView(Color bgColor, boolean preserve_css) {
        this.preserve_css = preserve_css;
        setEditable(false);
        setBackground(bgColor);
        setMargin(new Insets(0, 3, 3, 3));
    }

    /** Sets the javadoc content as HTML document */
    public void setContent(final String content) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Reader in = new StringReader("<HTML><BODY>" + content + "</BODY></HTML>");
                try {
                    Document doc = getDocument();
                    doc.remove(0, doc.getLength());
                    getEditorKit().read(in, getDocument(), 0);
                    setCaretPosition(0);
                    scrollRectToVisible(new Rectangle(0, 0, 0, 0));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
            }
        });
    }

    /** Sets javadoc background color */
    public void setBGColor(Color bgColor) {
        setBackground(bgColor);
    }

    protected EditorKit createDefaultEditorKit() {
        if (htmlKit == null) {
            htmlKit = new HTMLEditorKit();
            setEditorKit(htmlKit);
            if (htmlKit.getStyleSheet().getStyleSheets() != null) return htmlKit;
            if (preserve_css) {
                StyleSheet css = new StyleSheet();
                Font f = getFont();
                css.addRule(new StringBuffer("body { font-size: ").append(f.getSize()).append("; font-family: ").append(f.getName()).append("; }").toString());
                css.addStyleSheet(htmlKit.getStyleSheet());
                htmlKit.setStyleSheet(css);
            }
        }
        return htmlKit;
    }
}
