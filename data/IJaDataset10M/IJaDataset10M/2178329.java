package org.ujac.print.tag;

import java.awt.Color;
import org.ujac.print.AttributeDefinitionMap;
import org.ujac.print.BaseDocumentTag;
import org.ujac.print.DocumentFont;
import org.ujac.print.DocumentHandlerException;
import com.lowagie.text.Font;

/**
 * Name: FontDefItem<br>
 * Description: A class which handles font definitions.<br>
 * 
 * @author lauerc
 */
public class FontDefTag extends BaseDocumentTag {

    /** The item's name. */
    public static final String TAG_NAME = "font-def";

    /** The font. */
    private DocumentFont font = null;

    /** The font's name. */
    private String name = null;

    /** The font's color. */
    private Color color = null;

    /** The font's family. */
    private String family = DEFAULT_FONT_FAMILY;

    /** The font encoding. */
    private String encoding = DEFAULT_FONT_ENCODING;

    /** The font's size. */
    private float size = DEFAULT_FONT_SIZE;

    /** The font's style. */
    int style = DEFAULT_FONT_STYLE;

    /**
   * The text displacement relative to the baseline. 
   *  Positive values rise the text, negative values lower the text.
   */
    private float textRise = 0.0F;

    /**
   * Constructs a FontDefTag instance with no specific attributes.
   */
    public FontDefTag() {
        super(TAG_NAME);
    }

    /**
   * Gets a brief description for the item.
   * @return The item's description.
   */
    public String getDescription() {
        return "Defines a logical font for the document. This defined font can be used by the font tag, " + " by setting its 'font-def' attribute or by serveral other tags by setting their 'font' attributes.";
    }

    /**
   * Gets the list of supported attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedAttributes() {
        return super.buildSupportedAttributes().addDefinition(CommonAttributes.FONT_NAME).addDefinition(CommonAttributes.FONT_COLOR).addDefinition(CommonAttributes.FONT_FAMILY).addDefinition(CommonAttributes.FONT_ENCODING).addDefinition(CommonAttributes.FONT_SIZE).addDefinition(CommonAttributes.FONT_STYLE).addDefinition(CommonAttributes.TEXT_RISE).addDefinition(CommonAttributes.STYLE);
    }

    /**
   * Gets the list of supported style attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedStyleAttributes() {
        return super.buildSupportedStyleAttributes().addDefinition(CommonStyleAttributes.FONT_NAME.cloneAttrDef()).addDefinition(CommonStyleAttributes.FONT_COLOR.cloneAttrDef().addAlias("color")).addDefinition(CommonStyleAttributes.FONT_FAMILY.cloneAttrDef().addAlias("family")).addDefinition(CommonStyleAttributes.FONT_ENCODING.cloneAttrDef().addAlias("encoding")).addDefinition(CommonStyleAttributes.FONT_SIZE.cloneAttrDef().addAlias("size")).addDefinition(CommonStyleAttributes.FONT_STYLE.cloneAttrDef().addAlias("style")).addDefinition(CommonStyleAttributes.TEXT_RISE);
    }

    /**
   * @see org.ujac.print.BaseDocumentTag#isStyleable()
   */
    public boolean isStyleable() {
        return true;
    }

    /**
   * Initializes the item. 
   * @exception DocumentHandlerException If something went badly wrong.
   */
    public void initialize() throws DocumentHandlerException {
        super.initialize();
    }

    /**
   * Opens the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while opening the document item.
   */
    public void openItem() throws DocumentHandlerException {
        super.openItem();
        if (!isValid()) {
            return;
        }
        name = getStringAttribute(CommonAttributes.FONT_NAME, false, null);
        color = getColorAttribute(CommonAttributes.FONT_COLOR, true, CommonStyleAttributes.FONT_COLOR);
        String familyAttribute = getStringAttribute(CommonAttributes.FONT_FAMILY, true, CommonStyleAttributes.FONT_FAMILY);
        if (familyAttribute != null) {
            family = familyAttribute;
        }
        String encodingAttribute = getStringAttribute(CommonAttributes.FONT_ENCODING, true, CommonStyleAttributes.FONT_FAMILY);
        if (encodingAttribute != null) {
            encoding = encodingAttribute;
        }
        if (isAttributeDefined(CommonAttributes.FONT_SIZE, CommonStyleAttributes.FONT_SIZE)) {
            size = getDimensionAttribute(CommonAttributes.FONT_SIZE, true, CommonStyleAttributes.FONT_SIZE);
        }
        String styleAttribute = getStringAttribute(CommonAttributes.FONT_STYLE, true, CommonStyleAttributes.FONT_STYLE);
        if (styleAttribute != null) {
            style = Font.getStyleValue(styleAttribute);
        } else {
            styleAttribute = getStringAttribute(CommonAttributes.STYLE, true, null);
            if (styleAttribute != null) {
                style = Font.getStyleValue(styleAttribute);
            }
        }
        if (isAttributeDefined(CommonAttributes.TEXT_RISE, CommonStyleAttributes.TEXT_RISE)) {
            textRise = getDimensionAttribute(CommonAttributes.TEXT_RISE, true, CommonStyleAttributes.TEXT_RISE);
        }
        font = createFont(family, encoding, size, style, textRise, color);
        documentHandler.registerFont(name, font);
    }

    /**
   * Handles the font attributes.
   * @throws DocumentHandlerException
   */
    protected void handleFontAttributes() throws DocumentHandlerException {
    }

    /**
   * Gets the font.
   * @return The item's font.
   */
    public DocumentFont getFont() {
        return font;
    }
}
