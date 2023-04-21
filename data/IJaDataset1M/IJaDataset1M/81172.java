package jgloss.parser;

import java.util.List;
import jgloss.util.StringTools;

/**
 * Filter reading annotations for kanji from a text fragment. Reading annotations placed in a
 * text after a kanji string in parentheses are removed from the text and a reading annotation is
 * added to the list of annotations.
 *
 * @author Michael Koch
 */
public class ReadingAnnotationFilter {

    /**
     * Character which signals the beginning of a reading annotation for a kanji word.
     */
    protected char readingStart;

    /**
     * Character which signals the end of a reading annotation for a kanji word.
     */
    protected char readingEnd;

    /**
     * Character used to separate two adjacent kanji substrings.
     */
    protected char kanjiSeparator;

    /**
     * Creates a reading annotation filter for a text which uses the specified characters as
     * delimiters.
     *
     * @param readingStart Character at the beginning of a reading annotation.
     * @param readingEnd Character at the end of a reading annotation.
     * @param kanjiSeparator Character used to separate two adjacent kanji substrings with
     *                       separate readings.
     */
    public ReadingAnnotationFilter(char readingStart, char readingEnd, char kanjiSeparator) {
        this.readingStart = readingStart;
        this.readingEnd = readingEnd;
        this.kanjiSeparator = kanjiSeparator;
    }

    /**
     * Filter the reading annotations from a text array. The text without the annotation fragments
     * is returned in a new array, the original text array is not modified. A 
     * {@link ReadingAnnotation ReadingAnnotation} is generated for every reading in the text.
     *
     * @param text Text to filter.
     * @param readings List of reading annotations from the text, with start offsets in the returned array.
     * @return Text without the reading annotations.
     */
    public char[] filter(char[] text, int start, int length, List readings) {
        char[] outtext = new char[length];
        int outtextIndex = 0;
        int kanjiStart = -1;
        int kanjiOutStart = -1;
        int annotationStart = -1;
        boolean inReading = false;
        for (int textIndex = start; textIndex < start + length; textIndex++) {
            if (inReading) {
                if (text[textIndex] == readingEnd) {
                    final String reading = new String(text, annotationStart + 1, textIndex - annotationStart - 1);
                    if (kanjiStart > 0 && text[kanjiStart - 1] == kanjiSeparator) {
                        System.arraycopy(outtext, kanjiOutStart, outtext, kanjiOutStart - 1, annotationStart - kanjiStart);
                        outtextIndex--;
                        kanjiOutStart--;
                    }
                    readings.add(new ReadingAnnotation(kanjiOutStart, annotationStart - kanjiStart, reading));
                    inReading = false;
                    kanjiStart = -1;
                } else if (text[textIndex] == '\n') {
                    textIndex = annotationStart - 1;
                    kanjiStart = -1;
                    inReading = false;
                }
            } else {
                if (text[textIndex] == readingStart && kanjiStart != -1) {
                    inReading = true;
                    annotationStart = textIndex;
                    continue;
                }
                if (StringTools.isKanji(text[textIndex])) {
                    if (kanjiStart == -1) {
                        kanjiStart = textIndex;
                        kanjiOutStart = outtextIndex;
                    }
                } else kanjiStart = -1;
                outtext[outtextIndex++] = text[textIndex];
            }
        }
        text = new char[outtextIndex];
        System.arraycopy(outtext, 0, text, 0, outtextIndex);
        return text;
    }

    /**
     * Sets the character which signals the beginning of a reading annotation for a kanji word.
     */
    public void setReadingStart(char _readingStart) {
        readingStart = _readingStart;
    }

    /**
     * Sets the character which signals the end of a reading annotation for a kanji word.
     */
    public void setReadingEnd(char _readingEnd) {
        readingEnd = _readingEnd;
    }

    public void setKanjiSeparator(char _kanjiSeparator) {
        kanjiSeparator = _kanjiSeparator;
    }

    /**
     * Returns the character which signals the beginning of a reading annotation for a kanji word.
     */
    public char getReadingStart() {
        return readingStart;
    }

    /**
     * Returns the character which signals the end of a reading annotation for a kanji word.
     */
    public char getReadingEnd() {
        return readingEnd;
    }

    public char getKanjiSeparator() {
        return kanjiSeparator;
    }
}
