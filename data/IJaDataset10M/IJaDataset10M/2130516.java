package sjp.hg;

import sjp.hg.simple.SimpleAttribute;
import sjp.hg.simple.SimpleComment;
import sjp.hg.simple.SimpleDocument;
import sjp.hg.simple.SimpleElement;
import sjp.hg.simple.SimplePI;
import sjp.hg.simple.SimpleText;

/**
 * A factory to create HTML documents.  Note that this factory is not
 * abstract, but is a singleton.  If you provide your own implementation,
 * override this and specify your instance with <code>setInstance</code>.
 * @author stacy
 * @version $Revision: 1.1.1.1 $
 */
public class DocumentFactory {

    /**
     * The document factory to use.
     */
    private static DocumentFactory instance = null;

    /**
     * Get the document factory to use.  If none has been specified,
     * a default factory instance is returned.
     * @return	The document factory to use.
     */
    public static final DocumentFactory getInstance() {
        if (instance == null) {
            instance = new DocumentFactory();
        }
        return instance;
    }

    /**
     * Specify the document factory instance to use.
     * @param factory	The document factory instance to use.
     * @return	The document factory instance to use.
     */
    public static final DocumentFactory setInstance(DocumentFactory factory) {
        if (factory == null) {
            throw new NullPointerException("Factory instance is null.");
        }
        instance = factory;
        return instance;
    }

    /**
     * Convenience method to make a new document using the current
     * document factory.
     * @return	The new document.
     */
    public static final Document makeDocument() {
        return DocumentFactory.getInstance().newDocument();
    }

    /**
     * Convenience method to make a new element using the current
     * document factory.
     * @param name	The name of the new element.
     * @return	The new element.
     */
    public static final Element makeElement(String name) {
        return DocumentFactory.getInstance().newElement(name);
    }

    /**
     * Convenience method to make a new text node using the current
     * document factory.
     * @param content	The content of the text node.
     * @return	The new node.
     */
    public static final Text makeText(String content) {
        return DocumentFactory.getInstance().newText(content);
    }

    /**
     * Convenience method to make a new processing instruction node using
     * the current document factory.
     * @param target	The target of the new processing instruction.
     * @param content	The content of the new processing instruction.
     * @return	The new node.
     */
    public static final PI makePI(String target, String content) {
        return DocumentFactory.getInstance().newPI(target, content);
    }

    /**
     * Convenience method to make a new comment node using the current
     * document factory.
     * @param content	The content of the new comment node.
     * @return	The new element.
     */
    public static final Comment makeComment(String content) {
        return DocumentFactory.getInstance().newComment(content);
    }

    /**
     * Convenience method to make a new attribute node using the current
     * document factory.
     * @param name	The name of the attribute.
     * @param value	The value of the attribute.
     * @return	The new attribute.
     */
    public static final Attribute makeAttribute(String name, String value) {
        return DocumentFactory.getInstance().newAttribute(name, value);
    }

    /**
     * Make and return a new document instance.
     * @return	The new document.
     */
    public Document newDocument() {
        return new SimpleDocument();
    }

    /**
     * Make and return a new element.
     * @param name	The name of the element.
     * @return	The new element.
     */
    public Element newElement(String name) {
        return new SimpleElement(name);
    }

    /**
     * Make and return a new text node.
     * @param content	The text content of the node.
     * @return	The new node.
     */
    public Text newText(String content) {
        return new SimpleText(content);
    }

    /**
     * Make and return a new processing instruction node.
     * @param target	The target of the processing instruction.
     * @param content	The content of the processing instruction.
     * @return	The new node.
     */
    public PI newPI(String target, String content) {
        return new SimplePI(target, content);
    }

    /**
     * Make and return a new comment node.
     * @param content	The content of the comment node.
     * @return	The new node.
     */
    public Comment newComment(String content) {
        return new SimpleComment(content);
    }

    /**
     * Make and return a new attribute node.
     * @param name	The name of the attribute.
     * @param value	The value of the attribute.
     * @return	The new attribute instance.
     */
    public Attribute newAttribute(String name, String value) {
        return new SimpleAttribute(name, value);
    }
}
