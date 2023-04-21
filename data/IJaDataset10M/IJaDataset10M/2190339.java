package adnotatio.client.annotator.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import adnotatio.client.annotator.util.PrintXMLParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;

/**
 * This widget can be used to annotate HTML formatted texts - it can change
 * styles of individual words, listen clicks on words, retrieve the total number
 * of words and retrieve word by word using their index in the text. This widget
 * splits the given HTML into individual words, spaces and special symbols and
 * wraps them with utility elements. These elements are used to specify styles
 * and behaviour of individual words.
 * 
 * @author kotelnikov
 */
public class ActiveHTMLPanel extends Composite implements HasHTML {

    /**
     * Listeners of this type are notified when user clicks on a word in the
     * managed text.
     * 
     * @author kotelnikov
     */
    public interface IWordClickListener {

        /**
         * This method is called to notify that user clicked on a word in the
         * widget.
         * 
         * @param widget the widget generating the event
         * @param wordPos the position of the word where the user clicked
         */
        void onWordClick(ActiveHTMLPanel widget, int wordPos);
    }

    /**
     * The HTML container used to visualize the content (with wrapped words and
     * spaces)
     */
    private HTML fHtml = new HTML();

    /**
     * This list contains identifiers of all wrapper elements managed by this
     * object
     */
    private List fIdList = new ArrayList();

    /**
     * This event listener is used to notify client code when user clicks on a
     * word
     */
    private EventListener fListener = new EventListener() {

        public void onBrowserEvent(Event event) {
            switch(DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    Element e = DOM.eventGetTarget(event);
                    String id = getElementId(e);
                    int pos = getWordPosById(id);
                    if (fWordClickListeners != null) {
                        for (Iterator iterator = fWordClickListeners.iterator(); iterator.hasNext(); ) {
                            IWordClickListener listener = (IWordClickListener) iterator.next();
                            listener.onWordClick(ActiveHTMLPanel.this, pos);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * The original (non processed) HTML document
     */
    private String fSourceHTML;

    /**
     * The widget id prefix. All identifiers of wrappers elements starts with
     * this prefix. It is used to avoid identifier collisions between different
     * instances of this class.
     */
    private String fWidetIdPrefix;

    /**
     * A list of word click listeners
     * 
     * @see IWordClickListener
     */
    private List fWordClickListeners;

    /**
     * The total number of words. This variable is defined when clients sets a
     * new HTML content (see {@link #setHTML(String)}).
     */
    private int fWordNumber;

    /**
     * @param prefix the index prefix used for all wrapper elements; it is
     *        required to avoid identifier collisions when multiple widgets of
     *        this type are used
     */
    public ActiveHTMLPanel(String prefix) {
        fWidetIdPrefix = prefix;
        initWidget(fHtml);
        setHTML("");
    }

    /**
     * Adds a new word click listener
     * 
     * @param listener the listener to add
     */
    public void addWordClickListener(IWordClickListener listener) {
        if (fWordClickListeners == null) fWordClickListeners = new ArrayList();
        fWordClickListeners.add(listener);
    }

    /**
     * Adds the specified style to all words in the range of word positions
     * 
     * @param pos position of the word for which the style name should be added
     * @param wordStyle the name of the style for words to set
     * @param spaceStyle the name of the style for spaces and special symbols to
     *        set
     */
    public void addWordStyle(int pos, int len, String wordStyle, String spaceStyle) {
        setWordStyle(pos, len, wordStyle, spaceStyle, true);
    }

    /**
     * Adds the specified style to the word in the given position.
     * 
     * @param pos position of the word for which the style name should be added
     * @param wordStyle the name of the style to add
     */
    public void addWordStyle(int pos, String wordStyle, String spaceStyle) {
        setWordStyle(pos, 1, wordStyle, spaceStyle, true);
    }

    /**
     * Returns the identifier of the specified element
     * 
     * @param e the element for which the identifier should be returned
     * @return the identifier of the specified element
     */
    String getElementId(Element e) {
        return e != null ? DOM.getElementAttribute(e, "id") : "";
    }

    /**
     * Returns the original HTML
     * 
     * @return the original HTML
     * @see com.google.gwt.user.client.ui.HasHTML#getHTML()
     */
    public String getHTML() {
        return fSourceHTML;
    }

    /**
     * Creates a wrapper element identifier based on the word position and on
     * the position of the space element.
     * 
     * @param wordPos the position of the word
     * @param spacePos the position of the space elements; for words it should
     *        be 0.
     * @return an identifier corresponding to the specified word position and
     *         the position of the space element
     */
    protected String getId(int wordPos, int spacePos) {
        String id = fWidetIdPrefix + wordPos;
        if (spacePos > 0) {
            id += "-" + spacePos;
        }
        return id;
    }

    /**
     * Returns the text representation of the content of this widget
     * 
     * @return the text representation of the content
     * @see com.google.gwt.user.client.ui.HasText#getText()
     */
    public String getText() {
        return fHtml.getText();
    }

    /**
     * Returns the word from the specified position. Positions of words should
     * be in the range <code>0..wordCount-1</code> where the
     * <code>wordCount</code> is the number of words in the text. This number
     * can be loaded using the {@link #getWordNumber()} method.
     * 
     * @param pos the position of the word
     * @return the word from the specified position
     */
    public String getWord(int pos) {
        Element e = getWordElement(pos);
        return e != null ? DOM.getInnerText(e) : null;
    }

    /**
     * Returns the wrapper element for a word with the specified index. Indexes
     * of words should be in the range <code>0..wordCount-1</code> where the
     * <code>wordCount</code> is the number of words in the text. This number
     * can be loaded using the {@link #getWordNumber()} method
     * 
     * @param pos the position of the word
     * @return the wrapper element for a word with the specified index
     */
    public Element getWordElement(int pos) {
        String id = getId(pos, 0);
        Element e = DOM.getElementById(id);
        return e;
    }

    /**
     * Returns the total number of words in the managed HTML
     * 
     * @return the total number of words in the managed HTML
     */
    public int getWordNumber() {
        return fWordNumber;
    }

    /**
     * Returns the position of the word corresponding to the given wrapper
     * identifier.
     * 
     * @param id the identifier of a word wrapper
     * @return the word position by its identifier
     */
    int getWordPosById(String id) {
        if (!id.startsWith(fWidetIdPrefix)) return -1;
        String num = id.substring(fWidetIdPrefix.length());
        int idx = num.indexOf('-');
        if (idx > 0) {
            num = num.substring(0, idx);
        }
        int result = -1;
        try {
            result = Integer.parseInt(num);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * Returns a selection object giving access to the underlying selection
     * information (like selected text, beginning/end words, ...)
     * 
     * @return a selection object giving access to the underlying selection
     *         information (like selected text, beginning/end words, ...)
     */
    public WordSelection getWordSelection() {
        return new WordSelection(this);
    }

    /**
     * Returns <code>true</code> if the specified index is the index of a word
     * wrapper.
     * 
     * @param id the id to check
     * @return <code>true</code> if the specified index is the index of a word
     *         wrapper
     */
    boolean isWordId(String id) {
        return id != null && id.startsWith(fWidetIdPrefix);
    }

    /**
     * @see com.google.gwt.user.client.ui.Widget#onLoad()
     */
    protected void onLoad() {
        super.onLoad();
        sinkSpans();
    }

    /**
     * Removes the given word click listener from the internal list of listeners
     * 
     * @param listener the listener to remove
     */
    public void removeWordClickListener(IWordClickListener listener) {
        if (fWordClickListeners != null) fWordClickListeners.remove(listener);
    }

    /**
     * Removes the specified style from all words in the range of word positions
     * 
     * @param pos position of the word for which the style name should be
     *        removed
     * @param wordStyle the name of the style for words to set
     * @param spaceStyle the name of the style for spaces and special symbols to
     *        set
     */
    public void removeWordStyle(int pos, int len, String wordStyle, String spaceStyle) {
        setWordStyle(pos, len, wordStyle, spaceStyle, false);
    }

    /**
     * Removes the specified style from the word in the given position.
     * 
     * @param pos position of the word for which the style name should be
     *        removed
     * @param wordStyle the name of the style for words to set
     * @param spaceStyle the name of the style for spaces and special symbols to
     *        set
     */
    public void removeWordStyle(int pos, String wordStyle, String spaceStyle) {
        setWordStyle(pos, 1, wordStyle, spaceStyle, false);
    }

    /**
     * Sets the HTML in this widget
     * 
     * @param html the HTML to set
     * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
     */
    public void setHTML(String html) {
        fSourceHTML = html;
        fIdList.clear();
        fWordNumber = 0;
        final StringBuffer buf = new StringBuffer();
        PrintXMLParser parser = new PrintXMLParser() {

            int fSpaceCounter = -1;

            protected void onBeginTag(String beginTag, List params) {
                super.onBeginTag(beginTag, params);
            }

            protected void onEndTag(String endTag) {
                super.onEndTag(endTag);
            }

            protected void onSpaces(String str) {
                if (fWordNumber > 0 && fSpaceCounter >= 0) {
                    printWrapper(str, fWordNumber - 1, fSpaceCounter++);
                } else {
                    print(str);
                }
            }

            protected void onSpecialSymbols(String str) {
                if (fWordNumber > 0 && fSpaceCounter >= 0) {
                    printWrapper(str, fWordNumber - 1, fSpaceCounter++);
                } else {
                    print(str);
                }
            }

            protected void onWord(String word) {
                fSpaceCounter = 0;
                printWrapper(word, fWordNumber++, fSpaceCounter++);
            }

            protected void print(String str) {
                buf.append(str);
            }

            private void printWrapper(String str, int wordId, int spaceId) {
                String id = getId(wordId, spaceId);
                fIdList.add(id);
                print("<span id='" + id + "' __b='" + fBegin + "' __e='" + fEnd + "'>");
                print(str);
                print("</span>");
            }
        };
        parser.split(fSourceHTML);
        String result = buf.toString();
        fHtml.setHTML(result);
        sinkSpans();
    }

    /**
     * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
     */
    public void setText(String text) {
        setHTML(text);
    }

    /**
     * Sets the specified style for all words in the given range.
     * 
     * @param pos the initial position of the word for which the style should be
     *        defined
     * @param len the length of the range for which the given style should be
     *        applied
     * @param wordStyle the name of the style for words to set
     * @param spaceStyle the name of the style for spaces and special symbols to
     *        set
     */
    public void setWordStyle(int pos, int len, String wordStyle, String spaceStyle) {
        for (int i = 0; i < len; i++) {
            Element e = getWordElement(pos + i);
            if (e != null) {
                setStyleName(e, wordStyle);
                if (spaceStyle != null && i < len - 1) {
                    String id = getElementId(e);
                    e = DOM.getNextSibling(e);
                    while (e != null) {
                        String siblingId = getElementId(e);
                        if (siblingId == null || !siblingId.startsWith(id)) break;
                        setStyleName(e, wordStyle);
                        e = DOM.getNextSibling(e);
                    }
                }
            }
        }
    }

    /**
     * This is an utility method used to add/remove style names to the range of
     * words.
     * 
     * @param pos the initial position of the word for which the style should be
     *        defined
     * @param len the length of the range for which the given style should be
     *        applied
     * @param wordStyle the name of the style for words to set
     * @param spaceStyle the name of the style for spaces and special symbols to
     *        set
     * @param set if this flag is <code>true</code> then the style will be
     *        added; otherwise the name of the style will be removed from
     *        elements
     */
    private void setWordStyle(int pos, int len, String wordStyle, String spaceStyle, boolean set) {
        for (int i = 0; i < len; i++) {
            Element e = getWordElement(pos + i);
            if (e != null) {
                if (wordStyle != null) {
                    setStyleName(e, wordStyle, set);
                }
                if (spaceStyle != null && i < len - 1) {
                    String id = getElementId(e);
                    e = DOM.getNextSibling(e);
                    while (e != null) {
                        String siblingId = getElementId(e);
                        if (siblingId == null || !siblingId.startsWith(id)) break;
                        setStyleName(e, wordStyle, set);
                        e = DOM.getNextSibling(e);
                    }
                }
            }
        }
    }

    /**
     * Sets the specified style to the word in the given position
     * 
     * @param pos the position of the word for which the style should be defined
     * @param styleName the name of the style to set
     */
    public void setWordStyle(int pos, String styleName) {
        setWordStyle(pos, 1, styleName, null);
    }

    /**
     * Activates all wrapper elements by adding to them the click listener
     */
    private void sinkSpans() {
        if (isAttached()) {
            for (Iterator iterator = fIdList.iterator(); iterator.hasNext(); ) {
                String id = (String) iterator.next();
                Element e = DOM.getElementById(id);
                DOM.sinkEvents(e, Event.ONCLICK);
                DOM.setEventListener(e, fListener);
            }
        }
    }
}
