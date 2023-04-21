package com.xenoage.zong.core.music.text;

import com.xenoage.zong.core.music.Attachable;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.text.Text;

/**
 * This class represents a single syllable of lyrics.
 * 
 * @author Andreas Wenger
 */
public final class Lyric implements TextElement, Attachable {

    public enum SyllableType {

        Single, Begin, Middle, End, Extend
    }

    ;

    private final Text text;

    private final SyllableType syllableType;

    private final int verse;

    /**
	 * Creates a new {@link Lyric} element.
	 */
    public Lyric(Text text, SyllableType syllableType, int verse) {
        this.text = text;
        this.syllableType = syllableType;
        this.verse = verse;
    }

    public static Lyric createExtend(int verse) {
        return new Lyric(null, SyllableType.Extend, verse);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Text getText() {
        return text;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Lyric withText(Text text) {
        return new Lyric(text, syllableType, verse);
    }

    public SyllableType getSyllableType() {
        return syllableType;
    }

    /**
	 * Gets the number of the verse this {@link Lyric} belongs to,
	 * starting with 0 for the first verse.
	 */
    public int getVerse() {
        return verse;
    }

    @Override
    public String toString() {
        return "Lyric (\"" + text + "\")";
    }
}
