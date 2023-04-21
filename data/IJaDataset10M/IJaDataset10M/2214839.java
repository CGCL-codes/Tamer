package net.sourceforge.jlatin.words;

import java.util.ArrayList;
import net.sourceforge.jlatin.constants.*;
import net.sourceforge.jlatin.parser.*;

class irregularNounCollection {

    private ArrayList irregularLatinNouns;

    private ArrayList irregularEnglishNouns;

    public irregularNounCollection() {
        System.out.println("Building IRC");
        irregularLatinNouns = new ArrayList();
        irregularEnglishNouns = new ArrayList();
        ParserIrregularEnglishNoun pin = new ParserIrregularEnglishNoun(utilities.readFileAsString(constantsFileLocations.irregularEnglishNounPluralization));
        ParserIrregularLatinNoun piln = new ParserIrregularLatinNoun(utilities.readFileAsString(constantsFileLocations.irregularLatinNoun));
        for (int i = 0; i < pin.count(); i++) {
            irregularEnglishNouns.add(pin.getIrregularEnglishNoun(i));
        }
        for (int i = 0; i < piln.count(); i++) {
            irregularLatinNouns.add(piln.getIrregularLatinNoun(i));
        }
    }

    public String getIrregularLatinForm(String nom, String gen, String gender, byte declension, int theCase, boolean isPlural) {
        for (int i = 0; i < irregularLatinNouns.size(); i++) {
            if (((irregularLatinNoun) irregularLatinNouns.get(i)).sameNoun(nom, gen, gender, declension)) {
                return ((irregularLatinNoun) irregularLatinNouns.get(i)).getForm(theCase, isPlural);
            }
        }
        return "";
    }

    public String pluralizeEnglish(String englishNoun) {
        for (int i = 0; i < irregularEnglishNouns.size(); i++) {
            if (((irregularEnglishNoun) irregularEnglishNouns.get(i)).sameNoun(englishNoun)) {
                return ((irregularEnglishNoun) irregularEnglishNouns.get(i)).getPlural();
            }
        }
        return "";
    }
}
