package net.sf.nanji.dataaccess.japanese;

import java.util.Iterator;

/** 
 * The Kanji Class is used to be able to build up
 * a kanji binary tree. In this tree you always have
 * a first element which is simply a String value of the Kanji.
 * The second element is another Kanji class which can either be null, which
 * means that the whole Kanji just consits of the first element.
 * If the secend element is not null and this second element Kanji
 * has another first element the outter Kanji and the inner Kanji
 * are handeld as one Kanji constisting of more than one "character".
 * <p>
 * Since there are Kanji with more than one Kanji-characters that need
 * to be written together since they need to be conjugated.
 * <p>
 * For example "八百" which is written "happyaku".<br>
 * This form is irregular and needs to be conjugated.
 * Therefore we would make this Kanji look like this: <br>
 * <code>
 * Kanji myKanji = new Kanji();<br>
 * myKanji.setFirst("八");<br>
 *    Kanji innerKanji = new Kanji();<br>
 *    innerKanji.setFirst("百");<br>
 * myKanji.setSecond(innerKanji);<br>
 * myKanji.setConjugate(true);
 * </code>
 * <p>
 * As we see we can put these Kanji together indefinitely until 
 * the second Element is null. The conjugate boolean controls 
 * wether the first element and the second element get conjugated.
 * The spaceAfter boolean controls wether a space is placed after the 
 * whole Kanji sequence, meaning after the last secondElement of every inner Kanji.
 * <p>
 * "Why doing this?" you might ask.<br>
 * The reason is that for every Kanji there is one or more hiragana/katakana reading.
 * Furthermore there is a romanji reading for every hiragana/katakana reading.<br>
 * That means I need to find a way to produce a Kanji which can be given to
 * a "HiraganaSpeller" that can - without knowing every kanji - produce a hiragana
 * reading for the given Kanji object. But that is not topic of this class. 
 *   
 * @author Bernhard Grünewaldt
 *
 */
public class Kanji implements Iterable<Kanji>, Kana {

    /** FIRST ELEMENT: String of Kanji (should be one char if possible) */
    private String firstKanji;

    /** SECOND ELEMENT: Kanji with another firstKanji String. Can be null */
    private Kanji secondKanji;

    /** If true, Kanji gets conjugated */
    private boolean conjugate;

    /** Space between romanji first and second? */
    private boolean spaceBetween;

    /** 
	 * Normally a Kanji has more than one reading, but
	 * since this will be a prototype we will stick to one
	 * reading for every kanji until we need more than one reading.
	 * <p>
	 * The Hiragana Object also contains the Romanji reading!
	 */
    private Hiragana hiraganaReading = new Hiragana();

    public boolean isSpaceBetween() {
        return spaceBetween;
    }

    public void setSpaceBetween(boolean spaceBetween) {
        this.spaceBetween = spaceBetween;
    }

    public Hiragana getHiraganaReading() {
        return hiraganaReading;
    }

    /**
	 * Append a Kanji to this Kanji.
	 * Puts the Kanji object to the end of the "list".
	 * @param k
	 */
    public void append(Kanji k) {
        Kanji last = (Kanji) getLast();
        if (last.getSecondKanji() == null) {
            last.setSecondKanji(k);
        } else {
            last.getSecondKanji().setSecondKanji(k);
        }
    }

    /**
	 * Assuming the list has 1 to n elements, 
	 * this method <b>returns the n-th element</b>.
	 * <p>
	 * Returns the last Kanji in the list.
	 * The Last Kanji in the List is the one,
	 * that doesn't have a second element.
	 * @return n-th Kanji
	 */
    public Kana getLast() {
        for (Kanji k : this) {
            if (k.secondKanji == null) {
                return k;
            }
        }
        return null;
    }

    /**
	 * Assuming the list has 1 to n elements, 
	 * this method <b>returns the (n-1)-th element</b>.
	 * <p>
	 * Returns the last Kanji in the list that has a
	 * second element which is not null.
	 * @return (n-1)-th Kanji
	 */
    public Kanji getLastKanjiWithSecondKanjiNotNull() {
        Kanji kTemp = null;
        for (Kanji k : this) {
            if (k.secondKanji == null) {
                break;
            }
            kTemp = k;
        }
        return kTemp;
    }

    /**
	 * Default constructor, sets boolean values to false
	 */
    public Kanji() {
        conjugate = false;
        spaceBetween = false;
    }

    /**
	 * convenience constructor which fills all properties.
	 * 
	 * @param first
	 * @param conjugate
	 * @param space
	 */
    public Kanji(String first, String hiragana, String romanji, boolean space, boolean conjugate) {
        this.setFirstKanji(first);
        this.setConjugate(conjugate);
        this.setSpaceBetween(space);
        hiraganaReading.setHiragana(hiragana);
        hiraganaReading.setRomanji(romanji);
    }

    public void setRomanjiForFirstKanji(String r) {
        this.hiraganaReading.setRomanji(r);
    }

    public void setHiraganaForFirstKanji(String h) {
        this.hiraganaReading.setHiragana(h);
    }

    private Kanji getKanji() {
        return this;
    }

    public String getFirstKanji() {
        return firstKanji;
    }

    public void setFirstKanji(String firstKanji) {
        this.firstKanji = firstKanji;
    }

    public Kanji getSecondKanji() {
        return secondKanji;
    }

    public void setSecondKanji(Kanji secondKanji) {
        this.secondKanji = secondKanji;
    }

    public boolean getConjugate() {
        return conjugate;
    }

    public void setConjugate(boolean conjugate) {
        this.conjugate = conjugate;
    }

    public Iterator<Kanji> iterator() {
        return new KanjiIterator();
    }

    /** 
	 * toString Method for better debugging,
	 * also called from other methods!
	 * Don't mess it up :)
	 */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (Kanji k : this) {
            strb.append(k.getFirstKanji());
        }
        return strb.toString();
    }

    /**
	 * See Interface JavaDoc
	 * @see net.sf.nanji.dataaccess.japanese.Kana#toStringJapaneseKanjiWithHiraganaOrKatakana() 
	 */
    public String toStringJapaneseKanjiWithHiraganaOrKatakana() {
        return toString();
    }

    /**
	 * See Interface JavaDoc
	 * @see net.sf.nanji.dataaccess.japanese.Kana#toStringJapaneseOnlyHirganaAndKatakana() 
	 */
    public String toStringJapaneseOnlyHirganaAndKatakana() {
        String ret = "";
        for (Kanji k : this) {
            ret += k.getHiraganaReading().toStringJapaneseOnlyHirganaAndKatakana();
            ;
        }
        return ret;
    }

    /**
	 * See Interface JavaDoc
	 * @see net.sf.nanji.dataaccess.japanese.Kana#toStringJapaneseOnlyRomanji() 
	 */
    public String toStringJapaneseOnlyRomanji() {
        String ret = "";
        for (Kanji k : this) {
            ret += k.getHiraganaReading().toStringJapaneseOnlyRomanji();
            ;
            if (k.isSpaceBetween() && k.getSecondKanji() != null) ret += (" ");
        }
        return ret;
    }

    /**
	 * Iterator for the Kanji class
	 * @author Bernhard Grünewaldt
	 */
    private class KanjiIterator implements Iterator<Kanji> {

        private Kanji current;

        public KanjiIterator() {
            current = new Kanji();
            current.setSecondKanji(getKanji());
        }

        public boolean hasNext() {
            if (current.getSecondKanji() == null) return false;
            return true;
        }

        public Kanji next() {
            current = current.getSecondKanji();
            return current;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
