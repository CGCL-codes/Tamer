package de.kugihan.dictionaryformids.translation.normation;

/**
 * Tiu ĉi klaso simple ignoras ĉiujn ĉapelojn kaj x-ojn. 
 * Tiel uzanto povas tajpi 'cxar' aux 'car' por serĉi 'ĉar'.
 *
 * @author Jacob Nordfalk
 */
public class NormationEpo extends Normation {

    public StringBuffer normateWord(StringBuffer nonNormatedWord, boolean fromUserInput) {
        StringBuffer defaultNormatedWord = NormationLib.defaultNormation(nonNormatedWord, fromUserInput);
        StringBuffer normatedWord = new StringBuffer();
        for (int charPos = 0; charPos < defaultNormatedWord.length(); ++charPos) {
            char character = defaultNormatedWord.charAt(charPos);
            switch(character) {
                case 'ĉ':
                case 'Ĉ':
                    normatedWord.append("c");
                    break;
                case 'ĝ':
                case 'Ĝ':
                    normatedWord.append("g");
                    break;
                case 'ĥ':
                case 'Ĥ':
                    normatedWord.append("h");
                    break;
                case 'ĵ':
                case 'Ĵ':
                    normatedWord.append("j");
                    break;
                case 'ŝ':
                case 'Ŝ':
                    normatedWord.append("s");
                    break;
                case 'ŭ':
                case 'Ŭ':
                    normatedWord.append("u");
                    break;
                case 'x':
                case 'X':
                    break;
                default:
                    normatedWord.append(character);
            }
        }
        return normatedWord;
    }
}
