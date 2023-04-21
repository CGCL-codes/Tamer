package scrabbletools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nick
 */
public class BruteAnagram {

    Dictionary d;

    List<String> words = new ArrayList<String>();

    char[] currentWord;

    /** Creates a new instance of BruteAnagram */
    public BruteAnagram(Dictionary d) {
        this.d = d;
    }

    public List<Word> getAnagramWords(String word) {
        List<Word> result = new ArrayList<Word>();
        List<String> preResult = getAnagrams(word);
        for (String s : preResult) result.add(new Word(s));
        Collections.sort(result);
        return result;
    }

    public List<String> getAnagrams(String word) {
        word = word.toUpperCase();
        currentWord = word.toCharArray();
        words = new ArrayList<String>(50);
        boolean[] trueArray = new boolean[currentWord.length];
        Arrays.fill(trueArray, true);
        recursiveAnagram(new char[0], trueArray);
        return words;
    }

    private void recursiveAnagram(char[] sequence, boolean[] okToUse) {
        if (d.isWord(sequence) && !words.contains(String.valueOf(sequence))) {
            words.add(String.valueOf(sequence));
        }
        if (sequence.length == currentWord.length) {
            return;
        }
        char[] newSequence = Arrays.copyOf(sequence, sequence.length + 1);
        boolean[] newOkToUse = Arrays.copyOf(okToUse, okToUse.length + 1);
        for (int letter = 0; letter < currentWord.length; letter++) {
            if (okToUse[letter]) {
                if (!Character.isLetter(currentWord[letter]) && !Character.isWhitespace(currentWord[letter])) {
                    newOkToUse[letter] = false;
                    for (int i = 0; i < LetterScores.alphabet.length; i++) {
                        newSequence[newSequence.length - 1] = LetterScores.getCharFromInt(i);
                        recursiveAnagram(newSequence, newOkToUse);
                    }
                    newOkToUse[letter] = true;
                } else {
                    newSequence[newSequence.length - 1] = currentWord[letter];
                    newOkToUse[letter] = false;
                    recursiveAnagram(newSequence, newOkToUse);
                    newOkToUse[letter] = true;
                }
            } else {
                continue;
            }
        }
    }
}
