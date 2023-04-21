package au.gov.naa.digipres.xena.util;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import au.gov.naa.digipres.xena.kernel.XenaException;

/**
 * Base class for views of text. Many views have plain text, and by putting this
 * in a common place, it makes it easier for all sorts of other views to have
 * common code. The class inherits ChunkedView, so that if the text is particularly
 * large, it won't blow up memory.
 * @created    1 July 2002
 */
public abstract class TextView extends ChunkedView {

    protected JPopupMenu popup = new JPopupMenu();

    protected PrintableTextArea textArea = new PrintableTextArea();

    protected int DEFAULT_TAB_SIZE = 2;

    protected JScrollPane scrollPane = new JScrollPane();

    protected final Font monoFont = new java.awt.Font("Monospaced", 0, 12);

    protected final Font unicodeFont = new Font("Arial Unicode MS", Font.PLAIN, 12);

    protected Font myFont = monoFont;

    public TextView() {
        try {
            tjbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendLine(String line) {
        textArea.append(line);
        textArea.append("\n");
        if (myFont == monoFont && 0 <= monoFont.canDisplayUpTo(line)) {
            myFont = unicodeFont;
        }
    }

    @Override
    public void PrintView() {
        textArea.doPrintActions();
    }

    @Override
    public void initListeners() {
        addPopupListener(popup, textArea);
    }

    protected void tjbInit() throws Exception {
        textArea.setTabSize(DEFAULT_TAB_SIZE);
        textArea.setEditable(false);
        Font font = new java.awt.Font("Monospaced", 0, 12);
        textArea.setFont(font);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(scrollPane);
        scrollPane.getViewport().add(textArea);
    }

    public ChunkedContentHandler getTextHandler() throws XenaException {
        ChunkedContentHandler ch = new ChunkedContentHandler() {

            StringBuffer buf = null;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (buf != null) {
                    buf.append(ch, start, length);
                }
            }

            @Override
            public void doStart(String namespaceURI, String localName, String qName, Attributes atts) {
                buf = new StringBuffer();
            }

            @Override
            public void doEnd(String namespaceURI, String localName, String qName) {
                appendLine(buf.toString());
                buf = null;
                textArea.setCaretPosition(0);
            }
        };
        return ch;
    }

    @Override
    public ContentHandler getContentHandler() throws XenaException {
        textArea.setText("");
        XmlContentHandlerSplitter splitter = new XmlContentHandlerSplitter();
        if (getTmpFile() == null) {
            splitter.addContentHandler(getTmpFileContentHandler());
        }
        ContentHandler ch = getTextHandler();
        splitter.addContentHandler(ch);
        return splitter;
    }
}
