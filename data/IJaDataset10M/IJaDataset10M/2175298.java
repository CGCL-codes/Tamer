package net.sourceforge.fraglets.mtgo.trader;

import javax.swing.text.html.HTMLEditorKit;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Properties;
import javax.swing.text.html.HTMLDocument;
import java.net.MalformedURLException;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import java.util.Enumeration;
import java.util.HashMap;
import java.net.URLConnection;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.io.FilterInputStream;

/**
 * The card detective tries to find an image URL for a given card name.
 * The default settings only support 7th edition cards.
 *
 * @author marion@users.sourceforge.net
 */
public class CardDetective {

    /** The editor kit we use to read HTML. */
    protected HTMLEditorKit kit = new HTMLEditorKit();

    /** The document with the set index. */
    protected HTMLDocument document;

    /** The URL to the index document. */
    protected URL index;

    /** The map of name to image URL. */
    protected HashMap imageMap;

    /** The map of set to spoiler. */
    protected HashMap setMap;

    /** Ordered set list. */
    protected String[] setList;

    /** Creates a new instance of CardDetective
     * @param index the URL to the card index
     */
    public CardDetective(URL index) {
        this.index = index;
    }

    /** Get the document from index.
     * @throws IOException propagated from I/O operations
     * @throws BadLocationException propagated from document inspection
     * @return the document created from the index
     */
    protected HTMLDocument getDocument() throws IOException, BadLocationException {
        if (document == null) {
            document = (HTMLDocument) kit.createDefaultDocument();
            document.setBase(getIndex());
            document.getDocumentProperties().put("IgnoreCharsetDirective", Boolean.TRUE);
            try {
                kit.read(openURL(getIndex()), document, 0);
            } catch (EmptyStackException ex) {
            }
        }
        return document;
    }

    public Set getSets() {
        if (setMap == null) {
            loadSetMap();
        }
        return setMap.keySet();
    }

    public String[] getSetList() {
        if (setList == null) {
            setList = new String[getSets().size()];
            getSets().toArray(setList);
            Arrays.sort(setList);
        }
        return setList;
    }

    public URL getSet(String name) {
        if (setMap == null) {
            loadSetMap();
        }
        return (URL) setMap.get(name);
    }

    protected synchronized void loadSetMap() {
        if (setMap != null) return;
        setMap = new HashMap();
        try {
            HTMLDocument document = getDocument();
            HTMLDocument.Iterator i = document.getIterator(HTML.Tag.A);
            while (i.isValid()) {
                Object value = i.getAttributes().getAttribute(HTML.Attribute.HREF);
                if (value != null) {
                    try {
                        String file = value.toString();
                        String text = getIteratorText(document, i);
                        if (text == null || !text.equals("Spoiler List") || file.endsWith("/.txt")) {
                            i.next();
                            continue;
                        }
                        Element element = document.getCharacterElement(i.getStartOffset());
                        while (element != null && !"tr".equals(element.getName())) {
                            element = element.getParentElement();
                        }
                        Element ni = element.getElement(0).getElement(0);
                        text = getElementText(ni);
                        setMap.put(text.trim(), new URL(document.getBase(), value.toString()));
                    } catch (BadLocationException ex) {
                    } catch (MalformedURLException ex) {
                    }
                }
                i.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static String getElementText(Element element) throws BadLocationException {
        int start = element.getStartOffset();
        int end = element.getEndOffset();
        return element.getDocument().getText(start, end - start);
    }

    protected static String getIteratorText(HTMLDocument document, HTMLDocument.Iterator i) throws BadLocationException {
        int start = i.getStartOffset();
        int end = i.getEndOffset();
        return document.getText(start, end - start);
    }

    /** Get the image URL for the card with the given name.
     * @param name name of the card
     * @return the URL to the card image, or null
     */
    public URL getImageURL(String name) {
        if (imageMap == null) {
            imageMap = new HashMap();
        }
        URL result = (URL) imageMap.get(name);
        if (result == null) {
            try {
                result = new URL(ResourceLoader.getProperty("defaultImageLocation") + simpleName(name) + ".jpg");
                imageMap.put(name, result);
            } catch (Exception ex) {
                result = null;
            }
        }
        return result;
    }

    /** Get the URL for the index document.
     * @return the URL to the card image index document
     */
    protected URL getIndex() {
        if (index == null) {
            try {
                index = new URL(ResourceLoader.getProperty("defaultSetIndex"));
            } catch (MalformedURLException ex) {
                throw new RuntimeException("invalid default card index: " + ex);
            }
        }
        return index;
    }

    protected ProgressListener progressListener;

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public Reader openURL(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        String encoding = uc.getContentEncoding();
        InputStream in = uc.getInputStream();
        if (progressListener != null) {
            progressListener.setObjective("Loading: " + url);
            in = new ProgressInputStream(in, progressListener, uc.getContentLength());
        }
        Reader reader;
        try {
            if (encoding != null) {
                reader = new InputStreamReader(in, encoding);
            } else {
                reader = new InputStreamReader(in);
            }
        } catch (UnsupportedEncodingException ex) {
            reader = new InputStreamReader(in);
        }
        return reader;
    }

    public static String simpleName(String name) {
        int end = name.length();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < end; i++) {
            char c = name.charAt(i);
            if (Character.isLetter(c)) {
                if (c == '�') {
                    buffer.append("ae");
                } else {
                    buffer.append(Character.toLowerCase(c));
                }
            } else if (c == ' ' || c == '-' || c == '_') {
                buffer.append('_');
            } else if (c == '(' && name.indexOf(')', i) > 0) {
                i = name.indexOf(')', i);
            }
        }
        if (buffer.charAt(buffer.length() - 1) == '_') {
            buffer.setLength(buffer.length() - 1);
        }
        return buffer.toString();
    }
}
