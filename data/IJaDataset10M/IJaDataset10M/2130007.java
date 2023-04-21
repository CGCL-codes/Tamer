package com.flagstone.transform.util;

import com.flagstone.transform.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * The FSTextConstructor class greatly simplifies the use of fonts and text when 
 * creating Flash files. Font definitions can be created in three ways:
 * 
 * <ol>
 * <li>Using TrueType or OpenType font definition stored in a file.</li>
 * <li>Using a font definition stored in an existing Flash file.</li>
 * <li>Using a given Java AWT font as a template.</li>
 * </ol>
 * 
 * <P>Font definitions from TrueType, OpenType or Flash files are created by 
 * specifying the name of the file:</p>
 * 
 * <pre>
 * FSTextConstructor font = new FSTextConstructor(id, trueTypeFile.getPath());
 * FSTextConstructor font = new FSTextConstructor(id, swfFile.getPath());
 * </pre>
 * 
 * <P>The OpenType or TrueType file is parsed to construct the font definition. 
 * Files with the extensions ".otf" or ".ttf" may be used. Files containing 
 * collections of fonts ".otc" are not currently supported. For Flash files the 
 * first font definition encountered is used and all the text definition objects 
 * associated with the font are used to obtain the advance information for each 
 * character. An example Flash file can be found in the resources directory 
 * included in this release.</p>
 * 
 * <P>Font definitions from AWT templates are created by passing a font object:</p>
 * 
 * <pre>
 * FSTextConstructor font = new FSTextConstructor(id, new Font("Arial", Font.PLAIN, 1));
 * </pre>
 * 
 * <p>Only the font or file name needs to specified. Unlike Java fonts, font 
 * definitions in Flash are size independent, the size and colour in which a 
 * given string in rendered is specified in the object  that defines the text to 
 * be displayed avoiding the need to create multiple font definitions for  the 
 * same font name.</p>
 *
 * <P>An array of FSGlyph objects are created from the font definition. Each 
 * object contains the glyph for the font, the associated character code and 
 * information used to lay out each glyph when it is displayed.</P>
 * 
 * <P>Once a font has been created text strings can be generated:</p>
 * 
 * <pre>
 *    int fontSize = 280; // twips
 *    FSColor fontColor = FSColorTable.black();
 *    FSDefineText2 aString = font.defineText("This is a string", fontSize, fontColor);
 * </pre>
 * 
 * <P>The defineText method returns FSDefineText2 objects since they support 
 * transparent colours, while FSDefineText only supports opaque colours.</P>
 *
 * <P>Once all the strings to be displayed have been generated the font 
 * definition object containing the glyphs can be generated.</p>
 * 
 * <pre>
 *    FSDefineFont2 defineFont = font.defineFont();
 * </pre>
 * 
 * <P>Remember however that objects in a Flash file must be defined before they 
 * are used so the font object must be added to the file before the first 
 * FSDefineText object that references the glyphs in the font.</p>
 * 
 * <P>To reduce the size of the Flash file only the glyphs actually displayed 
 * can be included in a font definition object. When a FSDefineText object is 
 * created the glyphs used are marked. This is why the text definition objects 
 * are generated before the font definition.</P>
 *
 * <P>Glyphs are included in the font in the order they are used with each 
 * FSDefineText2 object referencing a glyph by its position in the array of 
 * glyphs contained in a font definition. Any unused glyphs can then easily be 
 * omitted.</P>
 * 
 * <P>When the font definition is created only the glyphs used (up to that 
 * point) are included. If subsequent FSDefineText objects are generated that 
 * include glyphs not previously used then the text will not be displayed 
 * correctly.</p>
 *
 * <b>Text Fields</b><br/>
 *
 * <P>When creating text fields using the FSDefineTextField class, there are two 
 * options when specifying the font used to display the characters entered into 
 * the field:
 *
 * <ol>
 * <li>The glyphs for the font may be loaded from the host platform.</li>
 * <li>The glyphs for the font are taken from a font definition object.</li>
 * </ol>
 *
 * <P>Using the glyphs loaded from the host platform is by far the easiest way 
 * of using text fields. First a font definition is created that specifies only 
 * the name of the font which will be loaded from the host platform. Then 
 * creating the text fields set the useFontGlyphs attribute to false.</P>
 *
 * <pre>
 *    FSDefineFont2 fontDefinition = new FSDefineFont2(movie.newIdentifier(), "Arial");
 *
 *    FSDefineTextField textField = new FSDefineTextField(movie.newIdentifier(), bounds);
 *
 *    textField.setUseFontGlyphs(false);
 * </pre>
 *
 * <P>This approach only works if the font is defined on the host platform 
 * otherwise the Flash Player will substitute a default font.</P>
 *
 * <P>When using a font definition contained in the Flash file obviously the 
 * glyphs for all possible characters must be defined otherwise the character 
 * will not be displayed correctly. When using text fields the characters must 
 * be defined in the font, sorted by ascending character code, otherwise the
 * text will not be displayed correctly. To do this the FSCharacterTable class
 * provides arrays of characters sorted in the correct order. The array is 
 * passed to the willDisplay() method and the FSTextConstructor will add the 
 * glyphs for the characters in the order they appear in the array:</p>
 * 
 * <pre>
 *     char[] characterSet = FSCharacterTable.ascii();
 *     
 *     textConstructor.willDisplay(characterSet);
 * </pre>
 * 
 * <P>This will add ALL of the characters in the array to the font definition.
 * Several character sets are provided in the FSCharacterTable class but any 
 * array of characters could be passed to the willDisplay() method - allowing
 * smaller font definitions to be created when only a few characters are 
 * displayed.</p>
 *
 * <b>Missing Characters</b><br/>
 *
 * <P>Characters that cannot be displayed using the font are handled by a 
 * displaying a default glyph which typically represents a space or an empty 
 * box. Both Java AWT and True/Open Type definitions explicitly define the 
 * missing glyph. For fonts parsed from Flash files the missing glyph is assumed 
 * (by default) to be the first glyph in the font definition.</p>
 * 
 * <P>When a font is loaded the missing glyph is added automatically to the font 
 * definition so there is no need to explicitly include it by creating a text 
 * object to force it to be included. The missing glyph will always be the first 
 * glyph in the font definition generated by the FSTextConstructor object so it 
 * may be substituted by another suitable shape if required.</P>
 * 
 * <P>Whether a string can be displayed using a font can be determined by the 
 * <em>canDisplay()</em> method which returns the index of the first character 
 * that cannot be displayed (the missing glyph will be displayed instead) or -1 
 * if all the characters are represented in the font.</P>
 */
public class FSTextConstructor {

    private static final int OS_2 = 0x4F532F32;

    private static final int HEAD = 0x68656164;

    private static final int HHEA = 0x68686561;

    private static final int MAXP = 0x6D617870;

    private static final int LOCA = 0x6C6F6361;

    private static final int CMAP = 0x636D6170;

    private static final int HMTX = 0x686D7478;

    private static final int NAME = 0x6E616D65;

    private static final int GLYF = 0x676C7966;

    private static final int ITLF_SHORT = 0;

    private static final int ITLF_LONG = 1;

    private static final int FONT_WEIGHT_THIN = 100;

    private static final int FONT_WEIGHT_EXTRALIGHT = 200;

    private static final int FONT_WEIGHT_LIGHT = 300;

    private static final int FONT_WEIGHT_NORMAL = 400;

    private static final int FONT_WEIGHT_MEDIUM = 500;

    private static final int FONT_WEIGHT_SEMIBOLD = 600;

    private static final int FONT_WEIGHT_BOLD = 700;

    private static final int FONT_WEIGHT_EXTRABOLD = 800;

    private static final int FONT_WEIGHT_BLACK = 900;

    private static final int ON_CURVE = 0x01;

    private static final int X_SHORT = 0x02;

    private static final int Y_SHORT = 0x04;

    private static final int REPEAT_FLAG = 0x08;

    private static final int X_SAME = 0x10;

    private static final int Y_SAME = 0x20;

    private static final int X_POSITIVE = 0x10;

    private static final int Y_POSITIVE = 0x20;

    private static final int ARG_1_AND_2_ARE_WORDS = 0x01;

    private static final int ARGS_ARE_XY_VALUES = 0x02;

    private static final int WE_HAVE_A_SCALE = 0x08;

    private static final int WE_HAVE_AN_X_AND_Y_SCALE = 0x40;

    private static final int WE_HAVE_A_TWO_BY_TWO = 0x80;

    private static final int MORE_COMPONENTS = 0x10;

    private static final int NUMBER_OF_METRICS = 0;

    private static final int SCALE = 1;

    private static final int GLYPH_OFFSET_SIZE = 2;

    private class FSGlyph {

        FSShape shape = null;

        FSBounds bounds = new FSBounds(0, 0, 0, 0);

        int advance = 0;

        int[] xCoordinates = null;

        int[] yCoordinates = null;

        boolean[] onCurve = null;

        int[] endPoints = null;

        FSGlyph() {
            shape = new FSShape();
            bounds = new FSBounds(0, 0, 0, 0);
            advance = 0;
            xCoordinates = new int[] {};
            yCoordinates = new int[] {};
            onCurve = new boolean[] {};
            endPoints = new int[] {};
        }

        FSGlyph(FSShape aShape, FSBounds aBounds) {
            shape = aShape;
            bounds = aBounds;
            advance = 0;
            xCoordinates = new int[] {};
            yCoordinates = new int[] {};
            onCurve = new boolean[] {};
            endPoints = new int[] {};
        }
    }

    ;

    private int identifier = 0;

    private String name = "";

    private int encoding = 0;

    private float size = 0.0f;

    private boolean isBold = false;

    private boolean isItalic = false;

    private int baseline = 0;

    private float ascent = 0;

    private float descent = 0;

    private float leading = 0;

    private short[] orderTable = new short[65536];

    private short[] characterTable = new short[65536];

    private FSGlyph[] glyphTable = null;

    private int numberOfGlyphs = 0;

    private int missingGlyph = 0;

    private ArrayList kernings = new ArrayList();

    private int[] attributes = new int[8];

    /**
     * Creates a new FSTextConstructor object using the specified font.
     * 
     * <P>The fontName can be used to identify a particular font in two ways, 
     * either specifying:
     * 
     * <ol>
     * <li>the name of a file containing a TrueType or OpenType font definition.</li>
     * <li>the name of a Flash file containing font definition.</li>
     * </ol>
     *
     * IMPORTANT: This method previously allowed the name of a AWT Font to be 
     * specified as an argument. This will no longer be supported in future 
     * releases, use the FSTextCOnstructor(int, Font) method instead.
     * 
     * @param anIdentifier a unique identifier that will be assigned to the 
     * font definition object generated and referenced by all the text object
     * generated.
     * 
     * @param filename either the name of a Flash, TrueType or OpenType file 
     * containing an existing font definition.
     * 
     * @throws FileNotFoundException if the fontName specifies a file and the 
     * file cannot be found or opened.
     * 
     * @throws IOException if the fontName specifies a file and an error occurs while
     * reading the file from disk.
     * 
     * @throws DataFormatException if the fontName specifies a file and an error occurs while 
     * parsing the font definition.
     */
    public FSTextConstructor(int anIdentifier, String filename) throws IOException, DataFormatException {
        identifier = anIdentifier;
        for (int i = 0; i < 65536; i++) orderTable[i] = -1;
        if (filename.toLowerCase().endsWith(".swf")) decodeSWFFont(filename); else if (filename.toLowerCase().endsWith(".otf")) decodeOpenTypeFont(filename); else if (filename.toLowerCase().endsWith(".ttf")) decodeOpenTypeFont(filename); else decodeAWTFont(filename);
    }

    /**
     * Creates a new FSTextConstructor object using the specified font.
     * 
     * @param anIdentifier a unique identifier that will be assigned to the 
     * font definition object generated and referenced by all the text object
     * generated.
     * 
     * @param font an AWT Font object.
     */
    public FSTextConstructor(int anIdentifier, Font font) {
        identifier = anIdentifier;
        for (int i = 0; i < 65536; i++) orderTable[i] = -1;
        decodeAWTFont(font);
    }

    /**
     * Resets the FSTextConstructor to generate a new set of font and text 
     * objects. 
     * 
     * The character sets defined for the font are cleared so the characters
     * that will be used to generate the next font definition should be set with
     * the willDisplay() method.
     * 
     * This method is useful when generating objects for more than one Flash
     * file as it avoids the penalty of reloading the font definition which 
     * can be very expensive.
     * 
     * @param anIdentifier a unique identifier that will be assigned to the 
     * font definition object generated and referenced by all the text object
     * generated.
     */
    public void reset(int anIdentifier) {
        identifier = anIdentifier;
        for (int i = 0; i < 65536; i++) orderTable[i] = -1;
    }

    /**
     * Indicates whether or not this FSTextConstructor can display all the 
     * characters specified in the array. This method returns the index of the 
     * first character that cannot be displayed using this font. If the Font can 
     * display all characters, -1 is returned.
     *
     * @param chars an array containing all the characters to be displayed.
     *
     * @return the index of the first character that cannot be displayed, -1 
     * otherwise.
     */
    public int canDisplay(char[] chars) {
        int firstMissingChar = -1;
        for (int i = 0; i < chars.length; i++) {
            if (canDisplay(chars[i]) == false) {
                firstMissingChar = i;
                break;
            }
        }
        return firstMissingChar;
    }

    /**
     * Indicates whether or not this FSTextConstructor can display a specified 
     * Unicode String. This method returns the index of the first character that 
     * cannot be displayed using this font. If the Font can display all 
     * characters, -1 is returned.
     *
     * @param aString the String to be displayed.
     *
     * @return the index of the first character that cannot be displayed, -1 
     * otherwise.
     */
    public int canDisplay(String aString) {
        int firstMissingChar = -1;
        for (int i = 0; i < aString.length(); i++) {
            if (canDisplay(aString.charAt(i)) == false) {
                firstMissingChar = i;
                break;
            }
        }
        return firstMissingChar;
    }

    /**
     * willDisplay is used to predefine the set of characters that will be 
     * used when defining text objects.
     * 
     * @see FSCharacterTable for lists of predefined character sets that can be
     * used with different spoken languages.
     * 
     * @param chars an array of characters defining the character set that will
     * be used when defining text objects and fonts. The characters must be 
     * sorted by the code used to represent the character.
     */
    public void willDisplay(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            int glyphIndex = characterTable[chars[i]];
            for (int index = 0; index < 65536; index++) {
                if (orderTable[index] == glyphIndex) break; else if (orderTable[index] == -1) {
                    orderTable[index] = (short) glyphIndex;
                    break;
                }
            }
        }
    }

    /**
     * Generates a FSDefineFont2 object containing a complete definition of the font.
     *
     * <P>NOTE: Only the glyphs specified in the array of characters passed to
     * the willDisplay method will be shown.</P>
     *
     * @return a FSDefineFont2 object generated from the font definition.
     */
    public FSDefineFont2 defineFont() {
        FSDefineFont2 font = null;
        int count = 0;
        for (count = 0; orderTable[count] != -1 && count < orderTable.length; count++) ;
        ArrayList glyphsArray = new ArrayList(count);
        ArrayList codesArray = new ArrayList(count);
        ArrayList advancesArray = new ArrayList(count);
        ArrayList boundsArray = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            int glyphIndex = orderTable[i];
            int character = 0;
            while (characterTable[character] != glyphIndex) character++;
            glyphsArray.add(glyphTable[glyphIndex].shape);
            codesArray.add(new Integer(character));
            advancesArray.add(new Integer(glyphTable[glyphIndex].advance));
            boundsArray.add(glyphTable[glyphIndex].bounds);
        }
        font = new FSDefineFont2(identifier, name);
        font.setEncoding(encoding);
        font.setItalic(isItalic);
        font.setBold(isBold);
        font.setAscent((int) ascent);
        font.setDescent((int) descent);
        font.setLeading((int) leading);
        font.setShapes(glyphsArray);
        font.setCodes(codesArray);
        font.setAdvances(advancesArray);
        font.setBounds(boundsArray);
        font.setKernings(kernings);
        return font;
    }

    /**
     * Generates an FSDefineText2 object for the text rendered in the specified font size
     * and colour. This method is used to create FSDefineText2 objects for arbitrary
     * strings, using the characters defined for the FSTextConstructor object when it is
     * constructed.
     * 
     * NOTE: The font size is specified in twips not points or pixels.
     * 
     * <P>An FSDefineText2 object differs from an FSDefineText object in that it supports
     * transparent colours.</p>
     * 
     * <P>If any of the Unicode characters in the String cannot be displayed using this font
     * then the missing glyph is substituted.</p>
     * 
     * @param anIdentifier a unique identifier for the object.
     * @param text the String to be displayed.
     * @param fontSize the size of the font in twips.
     * @param aColor the colour of the text including transparency.
     *
     * @return an FSDefineText object representing the text.
     */
    public FSDefineText2 defineText(int anIdentifier, String text, int fontSize, FSColor aColor) {
        FSCoordTransform coordTransform = new FSCoordTransform(0, 0);
        float scaleFactor = ((float) fontSize) / 1024.0f;
        int[] glyphCodes = glyphIndicesForString(text);
        int[] glyphAdvances = advancesForGlyphIndices(glyphCodes, scaleFactor);
        FSText textRecord = new FSText(identifier, aColor, 0, scaleBaseline(scaleFactor), fontSize, charactersForGlyphs(glyphCodes, glyphAdvances));
        ArrayList textRecords = new ArrayList();
        textRecords.add(textRecord);
        return new FSDefineText2(anIdentifier, boundsForText(glyphCodes, glyphAdvances, fontSize), coordTransform, textRecords);
    }

    /**
     * Generates an FSDefineText2 object for a block of text.
     * 
     * <P>An FSDefineText2 object differs from an FSDefineText object in that it supports
     * transparent colours.</p>
     * 
     * <P>If any of the Unicode characters in the String cannot be displayed using this font
     * then the missing glyph is substituted.</p>
     * 
     * @param anIdentifier a unique identifier for the object.
     * @param lines an array containing the lines of text to be displayed.
     * @param fontSize the size of the font in twips.
     * @param aColor the colour of the text including transparency.
     * @param lineSpacing the distance, in twips, between successive lines.
     *
     * @return an FSDefineText object representing the text.
     */
    public FSDefineText2 defineTextBlock(int anIdentifier, ArrayList lines, int fontSize, FSColor aColor, int lineSpacing) {
        FSCoordTransform coordTransform = new FSCoordTransform(0, 0);
        float scaleFactor = ((float) fontSize) / 1024.0f;
        int xMin = 0;
        int yMin = 0;
        int xMax = 0;
        int yMax = 0;
        int xOffset = 0;
        int yOffset = scaleBaseline(scaleFactor);
        ArrayList textRecords = new ArrayList();
        int n = 0;
        for (Iterator i = lines.iterator(); i.hasNext(); yOffset += lineSpacing, n++) {
            String text = (String) i.next();
            int[] glyphCodes = glyphIndicesForString(text);
            int[] glyphAdvances = advancesForGlyphIndices(glyphCodes, scaleFactor);
            FSBounds bounds = boundsForText(glyphCodes, glyphAdvances, fontSize);
            if (n == 0) {
                yMin = bounds.getMinY();
                yMax = bounds.getMaxY();
            } else {
                yMax += lineSpacing;
            }
            if (n == lines.size() - 1) yMax += bounds.getHeight();
            xMin = (xMin < bounds.getMinX()) ? xMin : bounds.getMinX();
            xMax = (xMax > bounds.getMaxX()) ? xMax : bounds.getMaxX();
            FSText textRecord = new FSText(identifier, aColor, xOffset, yOffset, fontSize, charactersForGlyphs(glyphCodes, glyphAdvances));
            textRecords.add(textRecord);
        }
        return new FSDefineText2(anIdentifier, new FSBounds(xMin, yMin, xMax, yMax), coordTransform, textRecords);
    }

    /**
     * Generates an FSBounds object that defines the bounding box that encloses the text,
     * rendered in the specified font size.
     *
     * @param text the String to be displayed.
     * @param fontSize the size of the font in twips.
     *
     * @return an FSBounds object defining the bounding box enclosing the text.
     */
    public FSBounds boundsForText(String text, int fontSize) {
        float scaleFactor = ((float) fontSize) / 1024.0f;
        int[] glyphCodes = glyphIndicesForString(text);
        int[] glyphAdvances = advancesForGlyphIndices(glyphCodes, scaleFactor);
        return boundsForText(glyphCodes, glyphAdvances, fontSize);
    }

    /**
     * Returns the advance, in twips, to the next character. This method can be
     * used when laying out sequences of individual characters, rather than as 
     * as single string.
     * 
     * @param c the character that will be displayed.
     * @param fontSize the size of the font the character will be rendered in.
     * @return the distance to the next character.
     */
    public int advanceForChar(char c, int fontSize) {
        float scaleFactor = ((float) fontSize) / 1024.0f;
        int index = characterTable[c];
        int advance = (int) (glyphTable[index].advance * scaleFactor);
        return advance;
    }

    /**
     * defineShape converts a string into an equivalent shape representation. The
     * shape is constructed from glyphs used to represent each character in the 
     * string and filled with the specified colour.
     * 
     * @param anIdentifier an unique identifier for the shape.
     * @param text the string to convert into a shape.
     * @param fontSize the font size in twips used to render the shape 
     * @param aColor the colour which will be used to fill the shape.
     *
     * @return an FSDefineShape3 object4 which contains a shape that represents 
     * the characters contained in the string argument.
     */
    public FSDefineShape3 defineShape(int anIdentifier, String text, int fontSize, FSColor aColor) {
        FSShapeConstructor path = new FSShapeConstructor();
        path.add(new FSSolidFill(aColor));
        path.selectFillStyle(0);
        float scaleFactor = ((float) fontSize) / 1024.0f;
        int[] glyphCodes = glyphIndicesForString(text);
        int[] glyphAdvances = advancesForGlyphIndices(glyphCodes, scaleFactor);
        int xOffset = 0;
        for (int i = 0; i < text.length(); i++) {
            ArrayList array = glyphTable[orderTable[glyphCodes[i]]].shape.getObjects();
            for (Iterator j = array.iterator(); j.hasNext(); ) {
                FSTransformObject currentObject = (FSTransformObject) j.next();
                if (currentObject instanceof FSShapeStyle) {
                    FSShapeStyle style = (FSShapeStyle) currentObject;
                    int moveX = (int) (style.getMoveX() * scaleFactor + ((style.getMoveX() < 0) ? (-0.5) : 0.5));
                    int moveY = (int) (style.getMoveY() * scaleFactor + ((style.getMoveY() < 0) ? (-0.5) : 0.5));
                    path.closePath();
                    path.move(moveX + xOffset, moveY);
                } else if (currentObject instanceof FSLine) {
                    FSLine line = (FSLine) currentObject;
                    int x = (int) (line.getX() * scaleFactor + ((line.getX() < 0) ? (-0.5) : 0.5));
                    int y = (int) (line.getY() * scaleFactor + ((line.getY() < 0) ? (-0.5) : 0.5));
                    path.rline(x, y);
                } else if (currentObject instanceof FSCurve) {
                    FSCurve curve = (FSCurve) currentObject;
                    int cx = (int) (curve.getControlX() * scaleFactor + ((curve.getControlX() < 0) ? (-0.5) : 0.5));
                    int cy = (int) (curve.getControlY() * scaleFactor + ((curve.getControlY() < 0) ? (-0.5) : 0.5));
                    int ax = (int) (curve.getAnchorX() * scaleFactor + ((curve.getAnchorX() < 0) ? (-0.5) : 0.5));
                    int ay = (int) (curve.getAnchorY() * scaleFactor + ((curve.getAnchorY() < 0) ? (-0.5) : 0.5));
                    path.rcurve(cx, cy, ax, ay);
                }
            }
            path.closePath();
            xOffset += glyphAdvances[i];
        }
        return path.defineTransparentShape(anIdentifier);
    }

    private void decodeSWFFont(String filename) throws IOException, DataFormatException {
        FSMovie fontMovie = new FSMovie(filename);
        FSDefineFont font = null;
        FSFontInfo fontInfo = null;
        FSDefineText text = null;
        FSText textRecord = null;
        for (Iterator i = fontMovie.getObjects().iterator(); i.hasNext(); ) {
            FSMovieObject currentObject = (FSMovieObject) i.next();
            if (currentObject instanceof FSDefineFont) {
                font = (FSDefineFont) currentObject;
            } else if (currentObject instanceof FSFontInfo) {
                fontInfo = (FSFontInfo) currentObject;
            } else if (currentObject instanceof FSDefineText) {
                text = (FSDefineText) currentObject;
            }
        }
        textRecord = (FSText) text.getObjects().get(0);
        name = fontInfo.getName();
        encoding = fontInfo.getEncoding();
        size = (float) (textRecord.getHeight());
        isBold = fontInfo.isBold();
        isItalic = fontInfo.isItalic();
        if (encoding == FSText.ANSI) encoding = FSText.Unicode;
        glyphTable = new FSGlyph[font.getShapes().size()];
        int glyphIndex = 0;
        for (Iterator j = font.getShapes().iterator(); j.hasNext(); glyphIndex++) glyphTable[glyphIndex] = new FSGlyph((FSShape) j.next(), new FSBounds(0, 0, 0, 0));
        glyphIndex = 0;
        for (Iterator k = fontInfo.getCodes().iterator(); k.hasNext(); glyphIndex++) characterTable[((Integer) (k.next())).intValue()] = (short) glyphIndex;
        for (Iterator l = textRecord.getCharacters().iterator(); l.hasNext(); ) {
            FSCharacter character = (FSCharacter) l.next();
            glyphTable[character.getGlyphIndex()].advance = (int) (character.getAdvance() * (1024.0 / size));
        }
        orderTable[0] = 0;
    }

    private void decodeAWTFont(String fontName) {
        FontRenderContext fontContext = new FontRenderContext(new AffineTransform(), true, true);
        Font font = new Font(fontName, Font.PLAIN, 1);
        if (font == null) throw new IllegalArgumentException("No such font: " + fontName);
        name = fontName;
        encoding = FSText.Unicode;
        Rectangle2D transform = transformToEMSquare(font, fontContext);
        double scaleY = 1024.0;
        double scaleX = scaleY;
        double translateX = 1024.0 - (transform.getX() * 1024.0);
        double translateY = 1024.0 - (transform.getY() * 1024.0);
        size = (float) scaleY;
        font = font.deriveFont((float) scaleX);
        missingGlyph = font.getMissingGlyphCode();
        isBold = font.isBold();
        isItalic = font.isItalic();
        int numGlyphs = font.getNumGlyphs();
        int glyphIndex = 0;
        int characterCode = 0;
        glyphTable = new FSGlyph[numGlyphs];
        while ((glyphIndex < numGlyphs) && (characterCode < 65535)) {
            char currentChar = (char) characterCode;
            if (font.canDisplay(currentChar)) {
                GlyphVector glyphVector = font.createGlyphVector(fontContext, new char[] { currentChar });
                Shape outline = glyphVector.getGlyphOutline(0);
                int advance = (int) (glyphVector.getGlyphMetrics(0).getAdvance());
                characterTable[currentChar] = (short) glyphIndex;
                glyphTable[glyphIndex] = new FSGlyph(convertShape(outline), new FSBounds(0, 0, 0, 0));
                glyphTable[glyphIndex].advance = advance;
                if (font.hasUniformLineMetrics() == false) {
                    LineMetrics lineMetrics = font.getLineMetrics(new char[] { (char) currentChar }, 0, 1, fontContext);
                    ascent = 0;
                    descent = 0;
                    leading = 0;
                }
            } else {
                GlyphVector glyphVector = font.createGlyphVector(fontContext, new char[] { (char) missingGlyph });
                Shape outline = glyphVector.getGlyphOutline(0);
                int advance = (int) (glyphVector.getGlyphMetrics(0).getAdvance());
                characterTable[currentChar] = (short) glyphIndex;
                glyphTable[glyphIndex] = new FSGlyph(convertShape(outline), new FSBounds(0, 0, 0, 0));
                glyphTable[glyphIndex].advance = advance;
                if (font.hasUniformLineMetrics() == false) {
                    LineMetrics lineMetrics = font.getLineMetrics(new char[] { (char) currentChar }, 0, 1, fontContext);
                    ascent = 0;
                    descent = 0;
                    leading = 0;
                }
            }
            glyphIndex++;
            characterCode++;
        }
        orderTable[0] = (short) missingGlyph;
    }

    private void decodeAWTFont(Font font) {
        FontRenderContext fontContext = new FontRenderContext(new AffineTransform(), true, true);
        font = font.deriveFont(1.0f);
        name = font.getName();
        encoding = FSText.Unicode;
        Rectangle2D transform = transformToEMSquare(font, fontContext);
        double scaleY = 1024.0;
        double scaleX = scaleY;
        double translateX = 1024.0 - (transform.getX() * 1024.0);
        double translateY = 1024.0 - (transform.getY() * 1024.0);
        size = (float) scaleY;
        AffineTransform at = AffineTransform.getTranslateInstance(translateX, translateY);
        font = font.deriveFont(at);
        font = font.deriveFont((float) scaleX);
        missingGlyph = font.getMissingGlyphCode();
        isBold = font.isBold();
        isItalic = font.isItalic();
        int numGlyphs = font.getNumGlyphs();
        int glyphIndex = 0;
        int characterCode = 0;
        glyphTable = new FSGlyph[numGlyphs];
        while ((glyphIndex < numGlyphs) && (characterCode < 65535)) {
            char currentChar = (char) characterCode;
            if (font.canDisplay(currentChar)) {
                GlyphVector glyphVector = font.createGlyphVector(fontContext, new char[] { currentChar });
                Shape outline = glyphVector.getGlyphOutline(0);
                int advance = (int) (glyphVector.getGlyphMetrics(0).getAdvance());
                characterTable[currentChar] = (short) glyphIndex;
                glyphTable[glyphIndex] = new FSGlyph(convertShape(outline), new FSBounds(0, 0, 0, 0));
                glyphTable[glyphIndex].advance = advance;
                if (font.hasUniformLineMetrics() == false) {
                    LineMetrics lineMetrics = font.getLineMetrics(new char[] { (char) currentChar }, 0, 1, fontContext);
                    ascent = 0;
                    descent = 0;
                    leading = 0;
                }
            } else {
                GlyphVector glyphVector = font.createGlyphVector(fontContext, new char[] { (char) missingGlyph });
                Shape outline = glyphVector.getGlyphOutline(0);
                int advance = (int) (glyphVector.getGlyphMetrics(0).getAdvance());
                characterTable[currentChar] = (short) glyphIndex;
                glyphTable[glyphIndex] = new FSGlyph(convertShape(outline), new FSBounds(0, 0, 0, 0));
                glyphTable[glyphIndex].advance = advance;
                if (font.hasUniformLineMetrics() == false) {
                    LineMetrics lineMetrics = font.getLineMetrics(new char[] { (char) currentChar }, 0, 1, fontContext);
                    ascent = 0;
                    descent = 0;
                    leading = 0;
                }
            }
            glyphIndex++;
            characterCode++;
        }
        orderTable[0] = (short) missingGlyph;
    }

    private Rectangle2D transformToEMSquare(Font font, FontRenderContext fontContext) {
        int numGlyphs = font.getNumGlyphs();
        int characterCode = 0;
        int glyphIndex = 0;
        double x = 0.0;
        double y = 0.0;
        double w = 0.0;
        double h = 0.0;
        while ((glyphIndex < numGlyphs) && (characterCode < 65535)) {
            char currentChar = (char) characterCode;
            if (font.canDisplay(currentChar)) {
                GlyphVector glyphVector = font.createGlyphVector(fontContext, new char[] { currentChar });
                Rectangle2D bounds = glyphVector.getGlyphOutline(0).getBounds2D();
                x = Math.min(bounds.getX(), x);
                y = Math.min(bounds.getY(), y);
                w = Math.max(bounds.getWidth(), w);
                h = Math.max(bounds.getHeight(), h);
                glyphIndex++;
            }
            characterCode++;
        }
        return new Rectangle2D.Double(x, y, w, h);
    }

    private FSShape convertShape(Shape glyph) {
        PathIterator pathIter = glyph.getPathIterator(null);
        FSShapeConstructor path = new FSShapeConstructor();
        double[] coords = new double[6];
        int xOrigin = 0;
        int yOrigin = 0;
        int currentX = 0;
        int currentY = 0;
        while (!pathIter.isDone()) {
            int segmentType = pathIter.currentSegment(coords);
            int p1 = (int) (coords[0]);
            int p2 = (int) (coords[1]);
            int p3 = (int) (coords[2]);
            int p4 = (int) (coords[3]);
            int p5 = (int) (coords[4]);
            int p6 = (int) (coords[5]);
            switch(segmentType) {
                case PathIterator.SEG_MOVETO:
                    path.closePath();
                    path.move(p1, p2);
                    break;
                case PathIterator.SEG_LINETO:
                    path.line(p1, p2);
                    break;
                case PathIterator.SEG_QUADTO:
                    path.curve(p1, p2, p3, p4);
                    break;
                case PathIterator.SEG_CUBICTO:
                    path.curve(p1, p2, p3, p4, p5, p6);
                    break;
                case PathIterator.SEG_CLOSE:
                    path.closePath();
                    break;
            }
            pathIter.next();
        }
        if (path.objects.size() > 0) {
            FSShapeStyle style = (FSShapeStyle) path.objects.get(0);
            style.setLineStyle(0);
            style.setAltFillStyle(1);
        }
        return path.shape();
    }

    private FSBounds boundsForText(int[] glyphIndices, int[] advances, int fontSize) {
        float scaleEMSquare = fontSize / 1024.0f;
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        boolean any = false;
        int advance = 0;
        for (int i = 0; i < glyphIndices.length; i++) {
            ArrayList array = null;
            if (glyphTable[orderTable[glyphIndices[i]]] == null) array = glyphTable[missingGlyph].shape.getObjects(); else array = glyphTable[orderTable[glyphIndices[i]]].shape.getObjects();
            int x = advance;
            int y = 0;
            for (Iterator j = array.iterator(); j.hasNext(); ) {
                FSTransformObject currentObject = (FSTransformObject) j.next();
                if (currentObject instanceof FSShapeStyle) {
                    FSShapeStyle style = (FSShapeStyle) currentObject;
                    x = advance + style.getMoveX();
                    y = style.getMoveY();
                } else if (currentObject instanceof FSLine) {
                    FSLine line = (FSLine) currentObject;
                    x += line.getX();
                    y += line.getY();
                } else if (currentObject instanceof FSCurve) {
                    FSCurve curve = (FSCurve) currentObject;
                    x += (curve.getControlX() + curve.getAnchorX());
                    y += (curve.getControlY() + curve.getAnchorY());
                }
                if (any) {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                } else {
                    minX = maxX = x;
                    minY = maxY = y;
                    any = true;
                }
            }
            advance += advances[i] / scaleEMSquare;
        }
        minX = (int) (minX * scaleEMSquare);
        maxX = (int) (maxX * scaleEMSquare);
        minY = (int) (minY * scaleEMSquare) + scaleBaseline(scaleEMSquare);
        maxY = (int) (maxY * scaleEMSquare) + scaleBaseline(scaleEMSquare);
        return new FSBounds(minX, minY, maxX, maxY);
    }

    private ArrayList charactersForGlyphs(int[] codes, int[] advances) {
        ArrayList characters = new ArrayList(codes.length);
        for (int i = 0; i < codes.length; i++) characters.add(new FSCharacter(codes[i], advances[i]));
        return characters;
    }

    private boolean canDisplay(char c) {
        boolean canDisplay = false;
        if (c < characterTable.length && characterTable[c] != 0) canDisplay = true;
        return canDisplay;
    }

    private int[] glyphIndicesForString(String aString) {
        int[] glyphCodes = new int[aString.length()];
        for (int i = 0; i < aString.length(); i++) {
            char character = aString.charAt(i);
            int glyphIndex = characterTable[character];
            int index = 0;
            for (index = 0; index < 65536; index++) {
                if (orderTable[index] == glyphIndex) break; else if (orderTable[index] == -1) {
                    orderTable[index] = (short) glyphIndex;
                    break;
                }
            }
            glyphCodes[i] = index;
        }
        return glyphCodes;
    }

    private int[] advancesForGlyphIndices(int[] glyphCodes, float scaleFactor) {
        int[] glyphAdvances = new int[glyphCodes.length];
        for (int i = 0; i < glyphCodes.length; i++) {
            int index = orderTable[glyphCodes[i]];
            glyphAdvances[i] = (int) (glyphTable[index].advance * scaleFactor);
        }
        return glyphAdvances;
    }

    private int scaleBaseline(float scaleFactor) {
        int newBaseline = (int) (baseline * scaleFactor);
        newBaseline += (20 - (newBaseline % 20));
        return newBaseline;
    }

    private byte[] dataFromFile(String filename) throws FileNotFoundException, IOException {
        File aFile = new File(filename);
        FileInputStream imageContents = null;
        byte[] bytes = new byte[(int) aFile.length()];
        imageContents = new FileInputStream(aFile);
        imageContents.read(bytes);
        imageContents.close();
        return bytes;
    }

    private void decodeOpenTypeFont(String fontName) throws IOException, DataFormatException {
        FSCoder coder = new FSCoder(FSCoder.BIG_ENDIAN, dataFromFile(fontName));
        float version = coder.readFixedBits(32, 16);
        int tableCount = coder.readWord(2, false);
        int searchRange = coder.readWord(2, false);
        int entrySelector = coder.readWord(2, false);
        int rangeShift = coder.readWord(2, false);
        int os_2Offset = 0;
        int headOffset = 0;
        int hheaOffset = 0;
        int maxpOffset = 0;
        int locaOffset = 0;
        int cmapOffset = 0;
        int glyfOffset = 0;
        int hmtxOffset = 0;
        int nameOffset = 0;
        int os_2Length = 0;
        int headLength = 0;
        int hheaLength = 0;
        int maxpLength = 0;
        int locaLength = 0;
        int cmapLength = 0;
        int hmtxLength = 0;
        int nameLength = 0;
        int glyfLength = 0;
        int chunkType = 0;
        int checksum = 0;
        int offset = 0;
        int length = 0;
        for (int i = 0; i < tableCount; i++) {
            chunkType = coder.readWord(4, false);
            checksum = coder.readWord(4, false);
            offset = coder.readWord(4, false) << 3;
            length = coder.readWord(4, false);
            switch(chunkType) {
                case OS_2:
                    os_2Offset = offset;
                    os_2Length = length;
                    break;
                case CMAP:
                    cmapOffset = offset;
                    cmapLength = length;
                    break;
                case GLYF:
                    glyfOffset = offset;
                    glyfLength = length;
                    break;
                case HEAD:
                    headOffset = offset;
                    headLength = length;
                    break;
                case HHEA:
                    hheaOffset = offset;
                    hheaLength = length;
                    break;
                case HMTX:
                    hmtxOffset = offset;
                    hmtxLength = length;
                    break;
                case LOCA:
                    locaOffset = offset;
                    locaLength = length;
                    break;
                case MAXP:
                    maxpOffset = offset;
                    maxpLength = length;
                    break;
                case NAME:
                    nameOffset = offset;
                    nameLength = length;
                    break;
                default:
                    break;
            }
        }
        int bytesRead = 0;
        if (maxpOffset != 0) {
            coder.setPointer(maxpOffset);
            decodeMAXP(coder);
            bytesRead = (coder.getPointer() - maxpOffset) >> 3;
        }
        if (os_2Offset != 0) {
            coder.setPointer(os_2Offset);
            decodeOS_2(coder);
            bytesRead = (coder.getPointer() - os_2Offset) >> 3;
        }
        if (headOffset != 0) {
            coder.setPointer(headOffset);
            decodeHEAD(coder);
            bytesRead = (coder.getPointer() - headOffset) >> 3;
        }
        if (hheaOffset != 0) {
            coder.setPointer(hheaOffset);
            decodeHHEA(coder);
            bytesRead = (coder.getPointer() - hheaOffset) >> 3;
        }
        if (nameOffset != 0) {
            coder.setPointer(nameOffset);
            decodeNAME(coder);
            bytesRead = (coder.getPointer() - nameOffset) >> 3;
        }
        glyphTable = new FSGlyph[numberOfGlyphs];
        if (locaOffset != 0) {
            coder.setPointer(locaOffset);
            decodeGlyphs(coder, glyfOffset);
            bytesRead = (coder.getPointer() - locaOffset) >> 3;
        }
        if (hmtxOffset != 0) {
            coder.setPointer(hmtxOffset);
            decodeHMTX(coder);
            bytesRead = (coder.getPointer() - hmtxOffset) >> 3;
        }
        if (cmapOffset != 0) {
            coder.setPointer(cmapOffset);
            decodeCMAP(coder);
            bytesRead = (coder.getPointer() - cmapOffset) >> 3;
        }
        orderTable[0] = characterTable[(short) ' '];
        for (int i = 0; i < characterTable.length; i++) {
            if (characterTable[i] >= glyphTable.length) characterTable[i] = (short) missingGlyph;
        }
        int spaceIndex = characterTable[(short) ' '];
        glyphTable[spaceIndex].shape = new FSShape();
        glyphTable[spaceIndex].advance = 250;
    }

    private void decodeHEAD(FSCoder coder) {
        byte[] date = new byte[8];
        coder.readFixedBits(32, 16);
        coder.readFixedBits(32, 16);
        coder.readWord(4, false);
        coder.readWord(4, false);
        coder.readBits(1, false);
        coder.readBits(1, false);
        coder.readBits(1, false);
        coder.readBits(1, false);
        coder.readBits(1, false);
        coder.readBits(11, false);
        attributes[SCALE] = coder.readWord(2, false) / 1024;
        if (attributes[SCALE] == 0) attributes[SCALE] = 1;
        coder.readBytes(date);
        coder.readBytes(date);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        isBold = coder.readBits(1, false) != 0;
        isItalic = coder.readBits(1, false) != 0;
        coder.readBits(14, false);
        coder.readWord(2, false);
        coder.readWord(2, true);
        attributes[GLYPH_OFFSET_SIZE] = coder.readWord(2, true);
        coder.readWord(2, true);
    }

    private void decodeHHEA(FSCoder coder) {
        coder.readFixedBits(32, 16);
        ascent = coder.readWord(2, true);
        descent = coder.readWord(2, true);
        leading = coder.readWord(2, true);
        coder.readWord(2, false);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, false);
        coder.readWord(2, false);
        coder.readWord(2, false);
        coder.readWord(2, false);
        coder.readWord(2, true);
        attributes[NUMBER_OF_METRICS] = coder.readWord(2, false);
    }

    private void decodeOS_2(FSCoder coder) {
        byte[] panose = new byte[10];
        int[] unicodeRange = new int[4];
        byte[] vendor = new byte[4];
        int version = coder.readWord(2, false);
        coder.readWord(2, true);
        switch(coder.readWord(2, false)) {
            case FONT_WEIGHT_BOLD:
                isBold = true;
                break;
            default:
                break;
        }
        coder.readWord(2, false);
        coder.readWord(2, false);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readWord(2, true);
        coder.readBytes(panose);
        for (int i = 0; i < 4; i++) unicodeRange[i] = coder.readWord(4, false);
        coder.readBytes(vendor);
        isItalic = coder.readBits(1, false) != 0;
        coder.readBits(4, false);
        isBold = coder.readBits(1, false) != 0;
        coder.readBits(10, false);
        coder.readWord(2, false);
        coder.readWord(2, false);
        ascent = coder.readWord(2, false);
        descent = coder.readWord(2, false);
        leading = coder.readWord(2, false);
        coder.readWord(2, false);
        coder.readWord(2, false);
        if (version > 0) {
            coder.readWord(4, false);
            coder.readWord(4, false);
            if (version > 1) {
                coder.readWord(2, true);
                coder.readWord(2, true);
                missingGlyph = coder.readWord(2, false);
                coder.readWord(2, false);
                coder.readWord(2, false);
            }
        }
    }

    private void decodeNAME(FSCoder coder) {
        int stringTableBase = coder.getPointer() >>> 3;
        int format = coder.readWord(2, false);
        int numberOfNameRecords = coder.readWord(2, false);
        int stringTable = coder.readWord(2, false) + stringTableBase;
        for (int i = 0; i < numberOfNameRecords; i++) {
            int platformId = coder.readWord(2, false);
            int encodingId = coder.readWord(2, false);
            int languageId = coder.readWord(2, false);
            int nameId = coder.readWord(2, false);
            int stringLength = coder.readWord(2, false);
            int stringOffset = coder.readWord(2, false);
            int current = coder.getPointer();
            coder.setPointer((stringTable + stringOffset) << 3);
            byte[] b = new byte[stringLength];
            coder.readBytes(b);
            String nameEncoding = "UTF-8";
            if (platformId == 0) {
                nameEncoding = "UTF-16";
            } else if (platformId == 1) {
                if (encodingId == 0 && languageId == 0) nameEncoding = "ISO8859-1";
            } else if (platformId == 3) {
                switch(encodingId) {
                    case 1:
                        nameEncoding = "UTF-16";
                        break;
                    case 2:
                        nameEncoding = "SJIS";
                        break;
                    case 4:
                        nameEncoding = "Big5";
                        break;
                }
            }
            try {
                if (nameId == 1) name = new String(b, nameEncoding);
            } catch (UnsupportedEncodingException e) {
                name = new String(b);
            }
            coder.setPointer(current);
        }
    }

    private void decodeMAXP(FSCoder coder) {
        float version = coder.readFixedBits(32, 16);
        numberOfGlyphs = coder.readWord(2, false);
        if (version == 1.0) {
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
            coder.readWord(2, false);
        }
    }

    private void decodeHMTX(FSCoder coder) {
        int i = 0;
        for (i = 0; i < attributes[NUMBER_OF_METRICS]; i++) {
            glyphTable[i].advance = (coder.readWord(2, false) / attributes[SCALE]);
            coder.readWord(2, true);
        }
        int advance = glyphTable[i - 1].advance;
        for (; i < numberOfGlyphs; i++) glyphTable[i].advance = advance;
        for (; i < numberOfGlyphs; i++) coder.readWord(2, true);
    }

    private void decodeCMAP(FSCoder coder) {
        int tableStart = coder.getPointer();
        int version = coder.readWord(2, false);
        int numberOfTables = coder.readWord(2, false);
        int platformId = 0;
        int encodingId = 0;
        int offset = 0;
        int current = 0;
        int format = 0;
        int length = 0;
        int language = 0;
        int segmentCount = 0;
        int[] startCount = null;
        int[] endCount = null;
        int[] delta = null;
        int[] range = null;
        int[] rangeAdr = null;
        int i = 0;
        int n = 0;
        for (i = 0; i < numberOfTables; i++) {
            platformId = coder.readWord(2, false);
            encodingId = coder.readWord(2, false);
            offset = coder.readWord(4, false) << 3;
            current = coder.getPointer();
            if (platformId == 0) {
                encoding = FSText.Unicode;
            } else if (platformId == 1) {
                switch(encodingId) {
                    case 1:
                        encoding = FSText.SJIS;
                        break;
                    default:
                        encoding = FSText.ANSI;
                        break;
                }
            } else if (platformId == 3) {
                switch(encodingId) {
                    case 1:
                        encoding = FSText.Unicode;
                        break;
                    case 2:
                        encoding = FSText.SJIS;
                        break;
                    default:
                        encoding = FSText.ANSI;
                        break;
                }
            }
            coder.setPointer(tableStart + offset);
            format = coder.readWord(2, false);
            length = coder.readWord(2, false);
            language = coder.readWord(2, false);
            switch(format) {
                case 0:
                    for (n = 0; n < 256; n++) characterTable[n] = (short) coder.readWord(1, false);
                    break;
                case 4:
                    segmentCount = coder.readWord(2, false) / 2;
                    coder.readWord(2, false);
                    coder.readWord(2, false);
                    coder.readWord(2, false);
                    startCount = new int[segmentCount];
                    endCount = new int[segmentCount];
                    delta = new int[segmentCount];
                    range = new int[segmentCount];
                    rangeAdr = new int[segmentCount];
                    for (n = 0; n < segmentCount; n++) endCount[n] = coder.readWord(2, false);
                    coder.readWord(2, false);
                    for (n = 0; n < segmentCount; n++) startCount[n] = coder.readWord(2, false);
                    for (n = 0; n < segmentCount; n++) delta[n] = coder.readWord(2, true);
                    for (n = 0; n < segmentCount; n++) {
                        rangeAdr[n] = coder.getPointer() >> 3;
                        range[n] = coder.readWord(2, true);
                    }
                    int glyphIndex = 0;
                    int location = 0;
                    for (n = 0; n < segmentCount; n++) {
                        for (int code = startCount[n]; code <= endCount[n]; code++) {
                            if (range[n] != 0) {
                                location = rangeAdr[n] + range[n] + ((code - startCount[n]) << 1);
                                coder.setPointer(location << 3);
                                glyphIndex = coder.readWord(2, false);
                                if (glyphIndex != 0) glyphIndex = (glyphIndex + delta[n]) % 65536;
                            } else glyphIndex = (delta[n] + code) % 65536;
                            characterTable[code] = (short) glyphIndex;
                        }
                    }
                    break;
                case 2:
                case 6:
                    System.err.println("Unimplemented encoding table format: " + format);
                    break;
                default:
                    System.err.println("Illegal value for encoding table format: " + format);
                    break;
            }
            coder.setPointer(current);
        }
        encoding = FSText.SJIS;
    }

    private void decodeGlyphs(FSCoder coder, int glyfOffset) {
        int numberOfContours = 0;
        int glyphStart = 0;
        int start = coder.getPointer();
        int end = 0;
        int[] offsets = new int[numberOfGlyphs];
        if (attributes[GLYPH_OFFSET_SIZE] == ITLF_SHORT) offsets[0] = glyfOffset + (coder.readWord(2, false) * 2 << 3); else offsets[0] = glyfOffset + (coder.readWord(4, false) << 3);
        for (int i = 1; i < numberOfGlyphs; i++) {
            if (attributes[GLYPH_OFFSET_SIZE] == ITLF_SHORT) offsets[i] = glyfOffset + (coder.readWord(2, false) * 2 << 3); else offsets[i] = glyfOffset + (coder.readWord(4, false) << 3);
            if (offsets[i] == offsets[i - 1]) offsets[i - 1] = 0;
        }
        end = coder.getPointer();
        for (int i = 0; i < numberOfGlyphs; i++) {
            if (offsets[i] == 0) {
                glyphTable[i] = new FSGlyph(new FSShape(), new FSBounds(0, 0, 0, 0));
            } else {
                coder.setPointer(offsets[i]);
                numberOfContours = coder.readWord(2, true);
                if (numberOfContours >= 0) decodeSimpleGlyph(coder, i, numberOfContours);
            }
        }
        coder.setPointer(start);
        for (int i = 0; i < numberOfGlyphs; i++) {
            if (offsets[i] != 0) {
                coder.setPointer(offsets[i]);
                if (coder.readWord(2, true) == -1) decodeCompositeGlyph(coder, i);
            }
        }
        coder.setPointer(end);
    }

    private void decodeSimpleGlyph(FSCoder coder, int glyphIndex, int numberOfContours) {
        int xMin = coder.readWord(2, true) / attributes[SCALE];
        int yMin = coder.readWord(2, true) / attributes[SCALE];
        int xMax = coder.readWord(2, true) / attributes[SCALE];
        int yMax = coder.readWord(2, true) / attributes[SCALE];
        int[] endPtsOfContours = new int[numberOfContours];
        for (int i = 0; i < numberOfContours; i++) endPtsOfContours[i] = coder.readWord(2, false);
        int instructionCount = coder.readWord(2, false);
        int[] instructions = new int[instructionCount];
        for (int i = 0; i < instructionCount; i++) instructions[i] = coder.readWord(1, false);
        int numberOfPoints = (numberOfContours == 0) ? 0 : endPtsOfContours[endPtsOfContours.length - 1] + 1;
        int[] flags = new int[numberOfPoints];
        int[] xCoordinates = new int[numberOfPoints];
        int[] yCoordinates = new int[numberOfPoints];
        boolean[] onCurve = new boolean[numberOfPoints];
        int repeatCount = 0;
        int repeatFlag = 0;
        for (int i = 0; i < numberOfPoints; i++) {
            if (repeatCount > 0) {
                flags[i] = repeatFlag;
                repeatCount--;
            } else {
                flags[i] = coder.readWord(1, false);
                if ((flags[i] & REPEAT_FLAG) > 0) {
                    repeatCount = coder.readWord(1, false);
                    repeatFlag = flags[i];
                }
            }
            onCurve[i] = (flags[i] & ON_CURVE) > 0;
        }
        int last = 0;
        for (int i = 0; i < numberOfPoints; i++) {
            if ((flags[i] & X_SHORT) > 0) {
                if ((flags[i] & X_POSITIVE) > 0) last = xCoordinates[i] = last + coder.readWord(1, false); else last = xCoordinates[i] = last - coder.readWord(1, false);
            } else {
                if ((flags[i] & X_SAME) > 0) last = xCoordinates[i] = last; else last = xCoordinates[i] = last + coder.readWord(2, true);
            }
        }
        last = 0;
        for (int i = 0; i < numberOfPoints; i++) {
            if ((flags[i] & Y_SHORT) > 0) {
                if ((flags[i] & Y_POSITIVE) > 0) last = yCoordinates[i] = last + coder.readWord(1, false); else last = yCoordinates[i] = last - coder.readWord(1, false);
            } else {
                if ((flags[i] & Y_SAME) > 0) last = yCoordinates[i] = last; else last = yCoordinates[i] = last + coder.readWord(2, true);
            }
        }
        FSShapeConstructor path = new FSShapeConstructor();
        boolean contourStart = true;
        boolean offPoint = false;
        int contour = 0;
        int x = 0;
        int y = 0;
        int prevX = 0;
        int prevY = 0;
        int initX = 0;
        int initY = 0;
        for (int i = 0; i < numberOfPoints; i++) {
            x = xCoordinates[i] / attributes[SCALE];
            y = yCoordinates[i] / attributes[SCALE];
            if (onCurve[i]) {
                if (contourStart) {
                    path.moveForFont(x, -y);
                    contourStart = false;
                    initX = x;
                    initY = y;
                } else if (offPoint) {
                    path.curve(prevX, -prevY, x, -y);
                    offPoint = false;
                } else {
                    path.line(x, -y);
                }
            } else {
                if (offPoint == true) path.curve(prevX, -prevY, (x + prevX) / 2, -(y + prevY) / 2);
                prevX = x;
                prevY = y;
                offPoint = true;
            }
            if (i == endPtsOfContours[contour]) {
                if (offPoint) {
                    path.curve(x, -y, initX, -initY);
                } else {
                    path.closePath();
                }
                contourStart = true;
                offPoint = false;
                prevX = 0;
                prevY = 0;
                contour++;
            }
        }
        glyphTable[glyphIndex] = new FSGlyph(path.shape(), new FSBounds(xMin, -yMax, xMax, -yMin));
        glyphTable[glyphIndex].xCoordinates = xCoordinates;
        glyphTable[glyphIndex].yCoordinates = yCoordinates;
        glyphTable[glyphIndex].onCurve = onCurve;
        glyphTable[glyphIndex].endPoints = endPtsOfContours;
    }

    private void decodeCompositeGlyph(FSCoder coder, int glyphIndex) {
        FSShape shape = new FSShape();
        FSCoordTransform transform = null;
        int xMin = coder.readWord(2, true);
        int yMin = coder.readWord(2, true);
        int xMax = coder.readWord(2, true);
        int yMax = coder.readWord(2, true);
        FSGlyph points = null;
        int numberOfPoints = 0;
        int[] endPtsOfContours = null;
        int[] xCoordinates = null;
        int[] yCoordinates = null;
        boolean[] onCurve = null;
        int flags = 0;
        int sourceGlyph = 0;
        int xOffset = 0;
        int yOffset = 0;
        int sourceIndex = 0;
        int destIndex = 0;
        do {
            transform = new FSCoordTransform();
            flags = coder.readWord(2, false);
            sourceGlyph = coder.readWord(2, false);
            if (sourceGlyph >= glyphTable.length || glyphTable[sourceGlyph] == null) {
                glyphTable[glyphIndex] = new FSGlyph();
                glyphTable[glyphIndex].bounds = new FSBounds(xMin, yMin, xMax, yMax);
                return;
            }
            points = glyphTable[sourceGlyph];
            numberOfPoints = points.xCoordinates.length;
            endPtsOfContours = new int[points.endPoints.length];
            for (int i = 0; i < points.endPoints.length; i++) endPtsOfContours[i] = points.endPoints[i];
            xCoordinates = new int[numberOfPoints];
            for (int i = 0; i < numberOfPoints; i++) xCoordinates[i] = points.xCoordinates[i];
            yCoordinates = new int[numberOfPoints];
            for (int i = 0; i < numberOfPoints; i++) yCoordinates[i] = points.yCoordinates[i];
            onCurve = new boolean[numberOfPoints];
            for (int i = 0; i < numberOfPoints; i++) onCurve[i] = points.onCurve[i];
            if ((flags & ARG_1_AND_2_ARE_WORDS) == 0 && (flags & ARGS_ARE_XY_VALUES) == 0) {
                destIndex = coder.readWord(1, false);
                sourceIndex = coder.readWord(1, false);
                transform.translate(0, 0);
            } else if ((flags & ARG_1_AND_2_ARE_WORDS) == 0 && (flags & ARGS_ARE_XY_VALUES) > 0) {
                xOffset = (coder.readWord(1, false) << 24) >> 24;
                yOffset = (coder.readWord(1, false) << 24) >> 24;
                transform.translate(xOffset, yOffset);
            } else if ((flags & ARG_1_AND_2_ARE_WORDS) > 0 && (flags & ARGS_ARE_XY_VALUES) == 0) {
                destIndex = coder.readWord(2, false);
                sourceIndex = coder.readWord(2, false);
                transform.translate(0, 0);
            } else {
                xOffset = coder.readWord(2, true);
                yOffset = coder.readWord(2, true);
                transform.translate(xOffset, yOffset);
            }
            if ((flags & WE_HAVE_A_SCALE) > 0) {
                float scaleXY = coder.readFixedBits(16, 14);
                transform.scale(scaleXY, scaleXY);
            } else if ((flags & WE_HAVE_AN_X_AND_Y_SCALE) > 0) {
                float scaleX = coder.readFixedBits(16, 14);
                float scaleY = coder.readFixedBits(16, 14);
                transform.scale(scaleX, scaleY);
            } else if ((flags & WE_HAVE_A_TWO_BY_TWO) > 0) {
                float scaleX = coder.readFixedBits(16, 14);
                float scale01 = coder.readFixedBits(16, 14);
                float scale10 = coder.readFixedBits(16, 14);
                float scaleY = coder.readFixedBits(16, 14);
                float[][] matrix = new float[][] { { scaleX, scale01, 0.0f }, { scale10, scaleY, 0.0f }, { 0.0f, 0.0f, 1.0f } };
                transform.composite(new FSCoordTransform(matrix));
            }
            for (int i = 0; i < numberOfPoints; i++) {
                int[] xy = transform.transformPoint(xCoordinates[i], yCoordinates[i]);
                xCoordinates[i] = xy[0];
                yCoordinates[i] = xy[1];
            }
            FSShapeConstructor path = new FSShapeConstructor();
            boolean contourStart = true;
            boolean offPoint = false;
            int contour = 0;
            int x = 0;
            int y = 0;
            int prevX = 0;
            int prevY = 0;
            int initX = 0;
            int initY = 0;
            for (int i = 0; i < numberOfPoints; i++) {
                x = xCoordinates[i] / attributes[SCALE];
                y = yCoordinates[i] / attributes[SCALE];
                if (onCurve[i]) {
                    if (contourStart) {
                        path.moveForFont(x, -y);
                        contourStart = false;
                        initX = x;
                        initY = y;
                    } else if (offPoint) {
                        path.curve(prevX, -prevY, x, -y);
                        offPoint = false;
                    } else {
                        path.line(x, -y);
                    }
                } else {
                    if (offPoint == true) path.curve(prevX, -prevY, (x + prevX) / 2, -(y + prevY) / 2);
                    prevX = x;
                    prevY = y;
                    offPoint = true;
                }
                if (i == endPtsOfContours[contour]) {
                    if (offPoint) {
                        path.curve(x, -y, initX, -initY);
                    } else {
                        path.closePath();
                    }
                    contourStart = true;
                    offPoint = false;
                    prevX = 0;
                    prevY = 0;
                    contour++;
                }
            }
            shape.getObjects().addAll(path.shape().getObjects());
        } while ((flags & MORE_COMPONENTS) > 0);
        glyphTable[glyphIndex] = new FSGlyph(shape, new FSBounds(xMin, yMin, xMax, yMax));
        glyphTable[glyphIndex].xCoordinates = xCoordinates;
        glyphTable[glyphIndex].yCoordinates = yCoordinates;
        glyphTable[glyphIndex].onCurve = onCurve;
        glyphTable[glyphIndex].endPoints = endPtsOfContours;
    }
}
