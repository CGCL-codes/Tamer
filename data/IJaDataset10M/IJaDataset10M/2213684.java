package de.kugihan.dictionaryformids.translation.normation;

import de.kugihan.dictionaryformids.general.Util;

public class NormationFil extends Normation {

    NormationLat normationLatObj = null;

    NormationLat getNormationLatObj() {
        if (normationLatObj == null) normationLatObj = new NormationLat();
        return normationLatObj;
    }

    public StringBuffer normateWord(StringBuffer nonNormatedWord, boolean fromUserInput) {
        StringBuffer defaultNormatedWord = getNormationLatObj().normateWord(nonNormatedWord, fromUserInput);
        StringBuffer normatedWord = new StringBuffer();
        for (int charPos = 0; charPos < defaultNormatedWord.length(); ++charPos) {
            if (defaultNormatedWord.charAt(charPos) == 'b') {
                normatedWord.append("v");
            } else if (defaultNormatedWord.charAt(charPos) == 'i') {
                normatedWord.append("e");
            } else if (defaultNormatedWord.charAt(charPos) == 'o') {
                normatedWord.append("u");
            } else if (defaultNormatedWord.charAt(charPos) == 'f') {
                normatedWord.append("p");
            } else {
                normatedWord.append(defaultNormatedWord.charAt(charPos));
            }
        }
        return normatedWord;
    }
}
