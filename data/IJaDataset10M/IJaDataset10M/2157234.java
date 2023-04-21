package edu.collablab.brenthecht.wikapidia;

import java.util.Vector;
import edu.collablab.brenthecht.wikapidia.dbentity.Synonym;

/**
 * A convenience class for expressing a String along with the language in which the String is written.
 * @author brent
 *
 */
public class LanguagedString {

    public final String string;

    public final String langCode;

    private final int hash;

    public LanguagedString(String title, String langCode) {
        this.string = title;
        this.langCode = langCode;
        this.hash = (title + langCode).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LanguagedString) {
            LanguagedString s = (LanguagedString) o;
            boolean rVal = (this.string.equals(s.string) && this.langCode.equals(s.langCode));
            return rVal;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return hash;
    }

    public String toString() {
        return this.string + " (" + this.langCode + ")";
    }

    public LanguagedString resolveRedirects(WikapidiaDatabase db) throws WikapidiaException {
        if (db.getParsedLanguages().contains(this.langCode)) {
            Synonym mySyn = db.resolveSynonym(new Title(this.string, db.getLanguage(this.langCode)), this.langCode);
            if (mySyn == null) {
                return this;
            } else {
                LanguagedString newObj = new LanguagedString(mySyn.getSynsetTitle().getCanonical(), this.langCode);
                return newObj;
            }
        } else {
            return this;
        }
    }

    public Vector<LanguagedString> getAllSynonyms(WikapidiaDatabase db) throws WikapidiaException {
        Vector<LanguagedString> rVal = new Vector<LanguagedString>();
        if (db.getParsedLanguages().contains(this.langCode)) {
            Vector<Synonym> syns = db.getAllSynonyms(new Title(this.string, db.getLanguage(langCode)), this.langCode);
            for (Synonym s : syns) {
                rVal.add(new LanguagedString(s.getTitle().getCanonical(), s.getLanguageCode()));
            }
        }
        return rVal;
    }
}
