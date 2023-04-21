package de.kugihan.dictionaryformids.translation.normation;

import de.kugihan.dictionaryformids.general.Util;

public class NormationUkrC extends Normation {

    public StringBuffer normateWord(StringBuffer nonNormatedWord, boolean fromUserInput) {
        StringBuffer defaultNormatedWord = NormationLib.defaultNormation(nonNormatedWord, fromUserInput);
        StringBuffer normatedWord = new StringBuffer();
        for (int charPos = 0; charPos < defaultNormatedWord.length(); ++charPos) {
            if (defaultNormatedWord.charAt(charPos) == 'а') {
                normatedWord.append("a");
            } else if (defaultNormatedWord.charAt(charPos) == 'б') {
                normatedWord.append("b");
            } else if (defaultNormatedWord.charAt(charPos) == 'в') {
                normatedWord.append("v");
            } else if (defaultNormatedWord.charAt(charPos) == 'г') {
                normatedWord.append("h");
            } else if (defaultNormatedWord.charAt(charPos) == 'д') {
                normatedWord.append("d");
            } else if (defaultNormatedWord.charAt(charPos) == 'е') {
                normatedWord.append("e");
            } else if (defaultNormatedWord.charAt(charPos) == 'ё') {
                normatedWord.append("jo");
            } else if (defaultNormatedWord.charAt(charPos) == 'ж') {
                normatedWord.append("z");
            } else if (defaultNormatedWord.charAt(charPos) == 'з') {
                normatedWord.append("z");
            } else if (defaultNormatedWord.charAt(charPos) == 'и') {
                normatedWord.append("i");
            } else if (defaultNormatedWord.charAt(charPos) == 'ї') {
                normatedWord.append("ji");
            } else if (defaultNormatedWord.charAt(charPos) == 'й') {
                normatedWord.append("j");
            } else if (defaultNormatedWord.charAt(charPos) == 'к') {
                normatedWord.append("k");
            } else if (defaultNormatedWord.charAt(charPos) == 'л') {
                normatedWord.append("l");
            } else if (defaultNormatedWord.charAt(charPos) == 'м') {
                normatedWord.append("m");
            } else if (defaultNormatedWord.charAt(charPos) == 'н') {
                normatedWord.append("n");
            } else if (defaultNormatedWord.charAt(charPos) == 'о') {
                normatedWord.append("o");
            } else if (defaultNormatedWord.charAt(charPos) == 'п') {
                normatedWord.append("p");
            } else if (defaultNormatedWord.charAt(charPos) == 'р') {
                normatedWord.append("r");
            } else if (defaultNormatedWord.charAt(charPos) == 'с') {
                normatedWord.append("s");
            } else if (defaultNormatedWord.charAt(charPos) == 'т') {
                normatedWord.append("t");
            } else if (defaultNormatedWord.charAt(charPos) == 'у') {
                normatedWord.append("u");
            } else if (defaultNormatedWord.charAt(charPos) == 'ф') {
                normatedWord.append("f");
            } else if (defaultNormatedWord.charAt(charPos) == 'х') {
                normatedWord.append("ch");
            } else if (defaultNormatedWord.charAt(charPos) == 'ц') {
                normatedWord.append("c");
            } else if (defaultNormatedWord.charAt(charPos) == 'ч') {
                normatedWord.append("c");
            } else if (defaultNormatedWord.charAt(charPos) == 'ш') {
                normatedWord.append("s");
            } else if (defaultNormatedWord.charAt(charPos) == 'щ') {
                normatedWord.append("sc");
            } else if (defaultNormatedWord.charAt(charPos) == 'ъ') {
                normatedWord.append("x");
            } else if (defaultNormatedWord.charAt(charPos) == 'ы') {
                normatedWord.append("y");
            } else if (defaultNormatedWord.charAt(charPos) == 'ь') {
                normatedWord.append("x");
            } else if (defaultNormatedWord.charAt(charPos) == 'э') {
                normatedWord.append("e");
            } else if (defaultNormatedWord.charAt(charPos) == 'ю') {
                normatedWord.append("ju");
            } else if (defaultNormatedWord.charAt(charPos) == 'я') {
                normatedWord.append("ja");
            } else if (defaultNormatedWord.charAt(charPos) == 'ґ') {
                normatedWord.append("g");
            } else {
                normatedWord.append(defaultNormatedWord.charAt(charPos));
            }
        }
        return normatedWord;
    }
}
