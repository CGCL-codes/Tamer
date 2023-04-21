package org.ujac.print;

import org.ujac.print.tag.CommonAttributes;
import org.ujac.print.tag.CommonStyleAttributes;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;

/**
 * Name: DynamicContentContainer<br>
 * Description: A container tag holding results of dynamic content evaluation.
 * 
 * @author lauerc
 */
public class DynamicContentContainer extends BaseElementContainer implements PhraseHolder, ElementContainer {

    /** The item's name. */
    public static final String TAG_NAME = "_DYNAMIC_CONTENT_";

    /** 
   * The absolute leading, to be added to the variable leading amount, 
   * resulting from the line spacing factor.
   */
    private float leading = DEFAULT_LEADING;

    /** The line spacing factor. */
    private float lineSpacing = DEFAULT_LINE_SPACING;

    /** The element container. */
    protected ElementContainer elementContainer = null;

    /** The phrase, holding the dynamically evaluated content. */
    private Phrase contents = null;

    /**
   * @see org.ujac.print.BaseDocumentTag#getName()
   */
    public String getName() {
        return TAG_NAME;
    }

    /**
   * @see org.ujac.print.BaseDocumentTag#getDescription()
   */
    public String getDescription() {
        return "Internal container holding the results of dynamic content evaluation.";
    }

    /**
   * @see org.ujac.print.BaseDocumentTag#isVirtualContainer()
   */
    public boolean isVirtualContainer() {
        return true;
    }

    /**
   * Tells whether or not to disable checks which test if this tag fits into its parent tag.
   * This method should be overwritten by custom classes which extent the default tag 
   * base of UJAC.
   * @return true if structure checks are disabled for this tag.
   */
    public boolean isStructureChecksDisabled() {
        return true;
    }

    /**
   * Initializes the item. 
   * @exception DocumentHandlerException If something went badly wrong.
   */
    public void initialize() throws DocumentHandlerException {
        super.initialize();
        this.elementContainer = documentHandler.latestElementContainer();
    }

    /**
   * @see org.ujac.print.BaseDocumentTag#openItem()
   */
    public void openItem() throws DocumentHandlerException {
        super.openItem();
        this.leading = DEFAULT_LEADING;
        if (isAttributeDefined(CommonAttributes.LINE_SPACING, CommonStyleAttributes.LINE_SPACING)) {
            this.lineSpacing = getDimensionAttribute(CommonAttributes.LINE_SPACING, true, CommonStyleAttributes.LINE_SPACING);
        } else {
            this.lineSpacing = elementContainer.getLineSpacing();
        }
    }

    /**
   * Flushes the content buffer.
   * @exception DocumentHandlerException Thrown in case something went wrong while flushing the content buffer.
   */
    public void flushContent() throws DocumentHandlerException {
        if (!isValid()) {
            return;
        }
        if (isIgnoreFlush()) {
            return;
        }
        if (isTrimNextHead()) {
            trimContentHead();
            setTrimNextHead(false);
        }
        if (isItemClosed() && isTrimBody()) {
            trimContentTail();
        }
        Phrase nestedPhrase = addChunk(contents, getLeading(), getLineSpacing());
        if (nestedPhrase != null) {
            contents = nestedPhrase;
        }
        resetContent();
    }

    /**
   * Processes the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while processing the document item.
   */
    public void closeItem() throws DocumentHandlerException {
        if (!isValid()) {
            return;
        }
        flushContent();
    }

    /**
   * @see org.ujac.print.PhraseHolder#addChunk(org.ujac.print.BaseDocumentTag, java.lang.String, com.lowagie.text.Font)
   */
    public void addChunk(BaseDocumentTag item, String chunk, DocumentFont font) throws DocumentHandlerException {
        super.addChunk(item, contents, chunk, font, leading, lineSpacing);
    }

    /**
   * @see org.ujac.print.PhraseHolder#addChunk(org.ujac.print.BaseDocumentTag, com.lowagie.text.Chunk)
   */
    public void addChunk(BaseDocumentTag item, Chunk chunk) throws DocumentHandlerException {
        super.addChunk(contents, chunk, leading, lineSpacing);
    }

    /**
   * @see org.ujac.print.PhraseHolder#getLeading()
   */
    public float getLeading() {
        return leading;
    }

    /**
   * @see org.ujac.print.PhraseHolder#getLineSpacing()
   */
    public float getLineSpacing() {
        return lineSpacing;
    }

    /**
   * @see org.ujac.print.ElementContainer#addElement(org.ujac.print.BaseDocumentTag, com.lowagie.text.Element)
   */
    public void addElement(BaseDocumentTag item, Element element) throws DocumentHandlerException {
        if (!(element instanceof Phrase)) {
            throw new DocumentHandlerException(item.locator(), "Only phrases are allowed as contents for dynamic content evaluation by now!");
        }
        Phrase phrase = (Phrase) element;
        extendLeading(contents, phrase);
        this.contents.add(phrase);
    }

    /**
   * @see org.ujac.print.ElementContainer#isTopLevel()
   */
    public boolean isTopLevel() {
        return false;
    }

    /**
   * Getter method for the the property contents.
   * @return The current value of property contents.
   */
    public Phrase getContents() {
        return contents;
    }

    /**
   * Setter method for the the property contents.
   * @param contents The value to set for the property contents.
   */
    public void setContents(Phrase contents) {
        this.contents = contents;
    }
}
