package org.zmpp.encoding;

/**
 * Default implementation of AccentTable.
 * @author Wei-ju Wu
 * @version 1.5
 */
public class DefaultAccentTable implements AccentTable {

    private static final char[] STANDARD_TRANSLATION_TABLE = { 'ä', 'ö', 'ü', 'Ä', 'Ö', 'Ü', 'ß', '»', '«', 'ë', 'ï', 'ÿ', 'Ë', 'Ï', 'á', 'é', 'í', 'ó', 'ú', 'ý', 'Á', 'É', 'Í', 'Ó', 'Ú', 'Ý', 'à', 'è', 'ì', 'ò', 'ù', 'À', 'È', 'Ì', 'Ò', 'Ù', 'â', 'ê', 'î', 'ô', 'û', 'Â', 'Ê', 'Î', 'Ô', 'Û', 'å', 'Å', 'ø', 'Ø', 'ã', 'ñ', 'õ', 'Ã', 'Ñ', 'Õ', 'æ', 'Æ', 'ç', 'Ç', 'þ', 'ý', 'ð', 'Ð', '£', 'œ', 'Œ', '¡', '¿' };

    /**
   * {@inheritDoc}
   */
    public int getLength() {
        return STANDARD_TRANSLATION_TABLE.length;
    }

    /**
   * {@inheritDoc}
   */
    public char getAccent(final int index) {
        return STANDARD_TRANSLATION_TABLE[index];
    }

    /**
   * {@inheritDoc}
   */
    public int getIndexOfLowerCase(final int index) {
        final char c = (char) getAccent(index);
        final char lower = Character.toLowerCase(c);
        final int length = getLength();
        for (int i = 0; i < length; i++) {
            if (getAccent(i) == lower) {
                return i;
            }
        }
        return index;
    }
}
