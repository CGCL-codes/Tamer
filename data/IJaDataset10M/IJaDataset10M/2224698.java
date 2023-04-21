package org.axsl.fontR;

import org.axsl.fontR.output.FontOutput;
import org.axsl.fontR.output.FontOutputFactory;
import org.axsl.psR.Encoding;

/**
 * The FontUse interface exposes a font resource to the client application.
 * It encapsulates a {@link Font} as it is used by a specific
 * {@link FontConsumer}, for a specific {@link Encoding} and for a specific
 * font family. It is possible for a {@link FontConsumer} to use the same
 * {@link Font} instance in more than one way, due to more than one encoding or
 * the font being configured in more than one font family.
 */
public interface FontUse extends Comparable {

    /**
     * Return the parent Font.
     * @return The parent Font.
     */
    Font getFont();

    /**
     * <p>Retrieve the font glyph index for a specified Unicode character. Note
     * that if the font is subsetted, the glyph index returned is that for the
     * subsetted font, not for the original font.
     * This is needed to properly build and retrieve font subset information
     * and to ensure that the right encoding is used.</p>
     *
     * <p>Design Note: This method returns an int (instead of a char) because
     * it is conceivable that some fonts will need more than 16 bits to express
     * their glyph indexes.</p>
     *
     * @param codePoint The Unicode character for which the glyph index is
     * desired.
     * @return The glyph index for <code>codePoint</code>. This is normally the
     * code that is written to an output document (such as PDF or PostScript).
     */
    int encodeCharacter(int codePoint);

    /**
     * Get the Encoding scheme that is used to encode characters in this Font.
     * Note that this may be different from the font's internal encoding.
     * @return The Encoding scheme that is used to encode characters in this
     * Font.
     * @see Font#getInternalEncoding()
     */
    Encoding getEncoding();

    /**
     * Returns the weight of the next bolder font in this font's font-family,
     * if one exists.
     * If no bolder font exists, returns the next bolder numerical weight value.
     * If the weight of this Font is already the boldest,
     * {@link Font#FONT_WEIGHT_900}, then returns that value.
     * @return One of {@link Font#FONT_WEIGHT_100},
     * {@link Font#FONT_WEIGHT_200},
     * {@link Font#FONT_WEIGHT_300}, {@link Font#FONT_WEIGHT_400},
     * {@link Font#FONT_WEIGHT_500}, {@link Font#FONT_WEIGHT_600},
     * {@link Font#FONT_WEIGHT_700}, {@link Font#FONT_WEIGHT_800},
     * {@link Font#FONT_WEIGHT_900}
     */
    short nextBolderWeight();

    /**
     * Returns the weight of the next lighter font in this font's font-family,
     * if one exists.
     * If no lighter font exists, returns the next lighter numerical weight
     * value.
     * If the weight of this Font is already the lightest,
     * {@link Font#FONT_WEIGHT_100}, then returns that value.
     * @return One of {@link Font#FONT_WEIGHT_100},
     * {@link Font#FONT_WEIGHT_200},
     * {@link Font#FONT_WEIGHT_300}, {@link Font#FONT_WEIGHT_400},
     * {@link Font#FONT_WEIGHT_500}, {@link Font#FONT_WEIGHT_600},
     * {@link Font#FONT_WEIGHT_700}, {@link Font#FONT_WEIGHT_800},
     * {@link Font#FONT_WEIGHT_900}
     */
    short nextLighterWeight();

    /**
     * Returns the next bolder font in this font's font-family, if one exists,
     * or null if this is the boldest font.
     * @return Either the next bolder font, or null if there is no such font.
     */
    FontUse nextBolderFont();

    /**
     * Returns the next lighter font in this font's font-family, if one exists,
     * or null if this is the lightest font.
     * @return Either the next lighter font, or null if there is no such font.
     */
    FontUse nextLighterFont();

    /**
     * <p>Examines each character in a specified portion of a String or other
     * CharSequence to determine whether a glyph can be encoded for that
     * character.
     * If a glyph cannot be encoded, either because the Font does not have
     * a glyph for the character, or because the FontUse encoding cannot
     * encode the character, the character's index in the CharSequence is
     * returned.</p>
     * @param chars The String, StringBuffer or other CharSequence to be
     * examined.
     * @param beginIndex The first index in <code>chars</code> to be examined.
     * @return The first index into <code>chars</code> at or after
     * <code>beginIndex</code> which contains an unencodable character.
     * If all characters in <code>chars</code> can be encoded, -1 is returned.
     */
    int unavailableChar(CharSequence chars, int beginIndex);

    /**
     * <p>Examines each character in a specified portion of a String or other
     * CharSequence to determine whether a glyph can be encoded for that
     * character.
     * If a glyph cannot be encoded, either because the Font does not have
     * a glyph for the character, or because the FontUse encoding cannot
     * encode the character, it is included in an array of indexes that is
     * returned.</p>
     * @param chars The String, StringBuffer or other CharSequence to be
     * examined.
     * @param beginIndex The first index in <code>chars</code> to be examined.
     * @return An int array, each element of which contains the index to one
     * char in <code>chars</code> for which no glyph can be encoded.
     * If all characters in <code>chars</code> can be encoded, null is returned.
     */
    int[] unavailableChars(CharSequence chars, int beginIndex);

    /**
     * <p>Reports whether a glyph can be encoded for a given character.</p>
     * <p>Note that, even if the Font has a glyph for the character, this method
     * will return false if the character cannot be encoded with this FontUse's
     * encoding.</p>
     * @param codePoint The Unicode character to be tested.
     * @return True iff a glyph can be encoded for this character.
     */
    boolean glyphAvailable(int codePoint);

    /**
     * <p>Computes the size, in millipoints, which should be used for the
     * intrinsically lowercase characters in this font.
     * If the font has true small-caps glyphs or is unable to simulate
     * small-caps, the value returned will be the same as the value passed.
     * In other words, no scaling is implied.
     * Only if the font is capable of simulating small-caps will the value
     * returned be different from the value passed.</p>
     *
     * <p>Not all font-families have a small-caps variant, but small-caps can be
     * simulated by the client application.
     * To do so, 1) convert all text to uppercase characters, and 2) switch
     * back and forth between a larger font size (for characters originally in
     * uppercase) and a smaller font size (for characters originally in
     * lowercase). If the requested font size is 12 points, and the lowercase
     * characters should be scaled at 80%, then the font size to use for the
     * lowercase characters is 9.6 points.</p>
     *
     * <p>Note that the simulation of small-caps is done entirely outside of
     * the font system. As far as the font system is concerned, the same font
     * is being used regardless of whether it is being used at the size for
     * uppercase characters or the size for lowercase characters. All that is
     * being reported in this method is the computation of what has been
     * configured for the font.</p>
     *
     * @param fontSize The size, in millipoints, of the font at normal size.
     * In other words, the size that will be used to create the uppercase
     * glyphs.
     * @return The size, in millipoints, that should be used for creating the
     * instrinsically lowercase glyphs in this font. For a font that is
     * capable of simulating small-caps, this value will be scaled to a
     * percentage configured for the font.
     */
    int smallCapsSize(int fontSize);

    /**
     * If this font has been configured to simulate the "oblique" font-style,
     * return the percentage of 90 degrees that should be used to compute
     * the angle at which the characters should be skewed to obtain that
     * effect.
     * A positive value skews the character clockwise, and a negative value
     * skews the character counter-clockwise.
     * Presumably values returned should be positive.
     * @return The percentage that characters from this font should be skewed
     * when the "oblique" font-style is requested, or zero if the font has not
     * been configured to simulate oblique.
     */
    float simulateOblique();

    /**
     * If this font has been configured to simulate the "backslant" font-style,
     * return the percentage of 90 degrees that should be used to compute
     * the angle at which the characters should be skewed to obtain that
     * effect.
     * A positive value skews the character clockwise, and a negative value
     * skews the character counter-clockwise.
     * Presumably values returned should be negative.
     * @return The percentage that characters from this font should be skewed
     * when the "backslant" font-style is requested, or zero if the font has not
     * been configured to simulate backslant.
     */
    float simulateBackslant();

    /**
     * If this font has been configured to simulate the "ultra-condensed"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be less than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "ultra-condensed" font-stretch is requested,
     * or 100% if the font has not been configured to simulate ultra-condensed.
     */
    float simulateUltraCondensed();

    /**
     * If this font has been configured to simulate the "extra-condensed"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be less than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "extra-condensed" font-stretch is requested,
     * or 100% if the font has not been configured to simulate extra-condensed.
     */
    float simulateExtraCondensed();

    /**
     * If this font has been configured to simulate the "condensed"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be less than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "condensed" font-stretch is requested,
     * or 100% if the font has not been configured to simulate condensed.
     */
    float simulateCondensed();

    /**
     * If this font has been configured to simulate the "semi-condensed"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be less than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "semi-condensed" font-stretch is requested,
     * or 100% if the font has not been configured to simulate semi-condensed.
     */
    float simulateSemiCondensed();

    /**
     * If this font has been configured to simulate the "semi-expanded"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be greater than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "semi-expanded" font-stretch is requested,
     * or 100% if the font has not been configured to simulate semi-expanded.
     */
    float simulateSemiExpanded();

    /**
     * If this font has been configured to simulate the "expanded"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be greater than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "expanded" font-stretch is requested,
     * or 100% if the font has not been configured to simulate expanded.
     */
    float simulateExpanded();

    /**
     * If this font has been configured to simulate the "extra-expanded"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be greater than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "extra-expanded" font-stretch is requested,
     * or 100% if the font has not been configured to simulate extra-expanded.
     */
    float simulateExtraExpanded();

    /**
     * If this font has been configured to simulate the "ultra-expanded"
     * font-stretch, return the percentage that should be applied to the width
     * of characters in this font to achieve that effect.
     * Values over 100% will result in wider characters, and a values less than
     * 100% will result in narrower characters.
     * Presumably values returned should be greater than 100%.
     * @return The percentage that should be applied to the width of characters
     * from this font when the "ultra-expanded" font-stretch is requested,
     * or 100% if the font has not been configured to simulate ultra-expanded.
     */
    float simulateUltraExpanded();

    /**
     * Returns the PostScript name of the font.
     * If this FontUse uses the Font's internal encoding, this name should be
     * the same as the Font's PostScript name.
     * If not, this name should be a combination of the Font's PostScript name
     * and the name of the encoding.
     * @return The PostScript name of the font.
     */
    String getPostscriptName();

    /**
     * Returns the FontConsumer for whom this FontUse exists.
     * @return The FontConsumer instance attached to this FontUse.
     */
    FontConsumer getFontConsumer();

    /**
     * Records the fact that a FontConsumer is using a particular character.
     * This is important for subsetting.
     * @param codePoint The Unicode character to be registered.
     */
    void registerCharUsed(int codePoint);

    /**
     * Convenience method that provides the equivalent of running
     * {@link #registerCharUsed(int)} for each char in a given
     * String, StringBuffer or other {@link CharSequence}.
     * @param charSequence The {@link CharSequence} whose contents should be
     * registered.
     */
    void registerCharsUsed(CharSequence charSequence);

    /**
     * Provide a {@link FontOutput} implementation for a given mime type.
     * @param mimeType The mime type for which the FontOutput instance should
     * be obtained.
     * @return A FontOutput implementation, or null if the mimeType is not
     * registered.
     * @see FontServer#registerFontOutputFactory(FontOutputFactory) for details
     * on registering {@link FontOutputFactory} instances.
     */
    FontOutput getFontOutput(String mimeType);

    /**
     * Indicates whether any characters registered use.
     * Character use is registered at {@link #registerCharUsed(int)} and
     * {@link #registerCharsUsed(CharSequence)}.
     * @return True if any characters were actually registered as used.
     */
    boolean wasUsed();
}
