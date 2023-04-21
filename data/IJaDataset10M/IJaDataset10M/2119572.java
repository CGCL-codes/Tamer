package net.sourceforge.acts20_2;

/**
 * A specific verse.
 * 
 * @author Matt
 */
public class Verse implements GeneralVerse {

    private String verseLocation;

    private String verseText;

    private String verseTranslation;

    @Override
    public String getVerseLocation() {
        return verseLocation;
    }

    @Override
    public String getVerseText() {
        return verseText;
    }

    /**
	 * Creates a verse with the given location, text, and translation.
	 * 
	 * @param verseLocation
	 * @param verseText
	 * @param verseTranslation
	 */
    public Verse(String verseLocation, String verseText, String verseTranslation) {
        super();
        this.verseLocation = verseLocation;
        this.verseText = verseText;
        this.verseTranslation = verseTranslation;
    }

    @Override
    public String getVerseTranslation() {
        return verseTranslation;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Verse)) return false;
        Verse ov = (Verse) o;
        if (!(ov.verseLocation.equals(this.verseLocation))) return false;
        if (!(ov.verseText.equals(this.verseText))) return false;
        if (!(ov.verseTranslation.equals(this.verseTranslation))) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return verseLocation.hashCode() ^ verseText.hashCode() ^ verseTranslation.hashCode();
    }
}
