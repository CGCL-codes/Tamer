package org.ujac.print.tag;

import org.ujac.print.AttributeDefinition;
import org.ujac.print.AttributeDefinitionMap;
import org.ujac.print.DocumentHandlerException;
import org.ujac.print.ElementContainer;
import com.lowagie.text.Annotation;

/**
 * Name: AnnotationTag<br>
 * Description: Handler class for the annotation tag.
 * 
 * @author lauerc
 */
public class AnnotationTag extends BaseElementTag {

    /** Definition of the 'title' attribute. */
    private static final AttributeDefinition TITLE = new AttributeDefinition(TagAttributes.ATTR_TITLE, AttributeDefinition.TYPE_STRING, true, "The title of the annotation.");

    /** The item's name. */
    public static final String TAG_NAME = "annotation";

    /**
   * Constructs a AnnotationTag instance with no specific attributes.
   */
    public AnnotationTag() {
        super(TAG_NAME);
    }

    /**
   * Gets a brief description for the item.
   * @return The item's description.
   */
    public String getDescription() {
        return "Adds aa annotation to the document.";
    }

    /**
   * Gets the list of supported attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedAttributes() {
        return super.buildSupportedAttributes().addDefinition(TITLE);
    }

    /**
   * Tells whether textual content is allowed for the tag or not.
   * @return true if the item is allowed to contain textual content, else false.
   */
    public boolean isTextBodyAllowed() {
        return true;
    }

    /**
   * Closes the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while processing the document item.
   */
    public void closeItem() throws DocumentHandlerException {
        if (!isValid()) {
            return;
        }
        Annotation annotation = new Annotation(getTextAttribute(TITLE, true, null), getContent());
        ElementContainer elementContainer = getElementContainer();
        elementContainer.addElement(this, annotation);
    }
}
