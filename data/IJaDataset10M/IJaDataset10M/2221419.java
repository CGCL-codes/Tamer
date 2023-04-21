package net.cevn.font;

import net.cevn.texture.TextureMap;
import net.cevn.util.Position;
import net.cevn.util.Size;
import org.jdom.Element;

/**
 * The <code>FontCharacter</code> class describes the mapping of a character to a location in a
 * font texture/image. The coordinates are relative to the upper left hand corner. A font character
 * is generally considered read-only since its definitions are from the XML file with the ".font"
 * extension. There are not setter methods for a font character to further enforce the read
 * only nature.
 * <br>
 * The class is read-only beceause the member fields are filled based on the XML file
 * with a ".font" extension. A font character is defined by a charMap tag that has an x, y, and width
 * attributes which define the location of the character within a texture/image of a font. The
 * char tag, which is a child of the charMap tag and describes the character that is mapped. To create a
 * new font character use the <code>createFontCharacter(Element)</code> static method. Since the
 * class is read-only, there are no setter methods, and the factory design pattern is used to create
 * new Font characters based on XML definitions.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class FontCharacter extends TextureMap {

    /**
	 * The charMap tag, a child of the font tag.
	 */
    public static final String CHARMAP_TAG = "charMap";

    /**
	 * The char tag, a child of the charMap.
	 */
    public static final String CHAR_TAG = "char";

    /**
	 * The x attribute name for the charMap tag.
	 */
    public static final String X_ATTRIB = "x";

    /**
	 * The y attribute name for the charMap tag.
	 */
    public static final String Y_ATTRIB = "y";

    /**
	 * The width attribute name for the charMap tag.
	 */
    public static final String WIDTH_ATTRIB = "width";

    /**
	 * The character.
	 */
    private char c;

    /**
	 * Creates a new <code>FontCharacter</code> instance.
	 */
    private FontCharacter() {
        super();
    }

    /**
	 * Get the Unicode character.
	 * 
	 * @return The character.
	 */
    public char getCharacter() {
        return c;
    }

    @Override
    public String toString() {
        return "c: " + c + " " + super.toString();
    }

    /**
	 * Creates a font character. This is used to enforce the factory design pattern.
	 * 
	 * @param element The XML charMap tag element.
	 * @return A font character.
	 */
    public static FontCharacter createFontCharacter(final Element element) {
        FontCharacter character = new FontCharacter();
        Position position = new Position(Integer.parseInt(element.getAttributeValue(X_ATTRIB)), Integer.parseInt(element.getAttributeValue(Y_ATTRIB)));
        character.setPosition(position);
        Size size = new Size(Integer.parseInt(element.getAttributeValue(WIDTH_ATTRIB)), 0);
        character.setSize(size);
        character.c = element.getChildText(CHAR_TAG).charAt(0);
        return character;
    }
}
