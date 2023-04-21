package net.googlecode.exigenlab.task4.formatting;

import java.util.StringTokenizer;
import java.lang.String;
import java.lang.StringBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.googlecode.exigenlab.task4.logging.Logger;

/**
 * Class that realize task4 requirements.
 * 1) Provide a given sentence to the normal form
 * (beginning sentences with capital letters, the normal case, remove the extra spaces, tabs, and transfers to a new line).
 * 2) Each first letter in the word do in upper case.
 * 3) Rearrange the letters in each word in reverse order.
 * 4) All the letters 'A' or 'a' in the text to replace '!'.
 * 5) At the end of the sentence, after a point,
 * add the date (20 days earlier than current date) in the format "dd-MM-yyyy hh: mm".
 *
 * @author Sokolovskiy Mike
 * @version 1.0
 */
public class StringFormatter {

    private static final String startsentenсe = "heLLo aLl, i    aM \t\t tALkihG \ntO YoU.";

    private static final String sDelimiter = new String("\n\t,. ");

    private String sentenceAfterNormalize;

    private String sentenceAfterFirstToUpperCase;

    private String sentenceAfterReverse;

    private String sentenceAfterReplacement;

    private String sentenceAfterAddingDate;

    /**
     * Delete extra dividers from sentence.
     *
     * @param finalSentence sentence that under construction
     * @param word          current token
     * @return part that will be added to finalSentence
     */
    private StringBuffer getDelimiters(String finalSentence, StringBuffer word) {
        if (finalSentence.length() == 0) {
            finalSentence += " ";
        }
        for (int i = 0; i < sDelimiter.length(); i++) {
            if (finalSentence.charAt(finalSentence.length() - 1) == sDelimiter.charAt(i)) {
                for (int j = 0; j < sDelimiter.length(); j++) {
                    if (word.charAt(0) == sDelimiter.charAt(j)) {
                        return new StringBuffer();
                    }
                }
            }
        }
        return new StringBuffer(word);
    }

    /**
     * Shows start sentence
     */
    public void showStartSentence() {
        Logger logger = new Logger();
        logger.log("showStartMessage", startsentenсe);
    }

    /**
     * Provide a given sentence to the normal form
     * (beginning sentences with capital letters, the normal case, remove the extra spaces, tabs, and transfers to a new line).
     * Result written in <code>sentenceAfterNormalize</code>
     */
    public void normalize() {
        String finalSentence = new String();
        StringTokenizer stringTokenizer = new StringTokenizer(startsentenсe, sDelimiter, true);
        while (true) {
            StringBuffer word = new StringBuffer(stringTokenizer.nextToken());
            if (word.toString().equals(".")) {
                finalSentence += word.toString();
                break;
            }
            word = this.getDelimiters(finalSentence, word);
            String words = new String(word.toString());
            if (finalSentence.equals("")) {
                words = words.substring(0, 1).toUpperCase() + words.substring(1, words.length()).toLowerCase();
                finalSentence += words;
            } else {
                if (words.equals("")) {
                } else {
                    words = words.toLowerCase();
                    finalSentence += words;
                }
            }
        }
        sentenceAfterNormalize = new String(finalSentence);
        Logger logger = new Logger();
        logger.log("normalize", sentenceAfterNormalize);
    }

    /**
     * Makes each first letter in the word in upper case.
     * Result written in <code>sentenceAfterFirstToUpperCase</code>
     */
    public void firstToUpperCase() {
        String finalSentence = new String();
        String sDelimiter = new String("\n\t,. ");
        StringTokenizer stringTokenizer = new StringTokenizer(sentenceAfterNormalize, sDelimiter, true);
        while (true) {
            StringBuffer word = new StringBuffer(stringTokenizer.nextToken());
            if (word.toString().equals(".")) {
                finalSentence += word.toString();
                break;
            }
            String words = new String(word.toString());
            if (words.equals("")) {
            } else {
                words = words.substring(0, 1).toUpperCase() + words.substring(1, words.length()).toLowerCase();
                finalSentence += words;
            }
        }
        sentenceAfterFirstToUpperCase = new String(finalSentence);
        Logger logger = new Logger();
        logger.log("firstToUpperCase", sentenceAfterFirstToUpperCase);
    }

    /**
     * Rearrange the letters in each word in reverse order.
     * Result written in <code>sentenceAfterReverse</code>
     */
    public void reverseWords() {
        String finalSentence = new String();
        String sDelimiter = new String("\n\t,. ");
        StringTokenizer stringTokenizer = new StringTokenizer(sentenceAfterFirstToUpperCase, sDelimiter, true);
        while (true) {
            StringBuffer word = new StringBuffer(stringTokenizer.nextToken());
            if (word.toString().equals(".")) {
                finalSentence += word.toString();
                break;
            }
            word.reverse();
            finalSentence += word.toString();
        }
        sentenceAfterReverse = new String(finalSentence);
        Logger logger = new Logger();
        logger.log("reverseWords", sentenceAfterReverse);
    }

    /**
     * Replaces all the letters 'A' or 'a' in the text to '!'.
     * Result written in <code>sentenceAfterReplacement</code>
     */
    public void replaceA() {
        String finalSentence = new String(sentenceAfterReverse);
        finalSentence = finalSentence.replace('A', '!');
        finalSentence = finalSentence.replace('a', '!');
        sentenceAfterReplacement = new String(finalSentence);
        Logger logger = new Logger();
        logger.log("replaceA", sentenceAfterReplacement);
    }

    /**
     * Adding at the end of the sentence, after a point,
     * the date (20 days earlier than current date) in the format "dd-MM-yyyy hh: mm".
     * Result written in <code>sentenceAfterAddingDate</code>
     */
    public void addDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -20);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        sentenceAfterAddingDate = sentenceAfterReplacement + simpleDateFormat.format(calendar.getTime());
        Logger logger = new Logger();
        logger.log("addDate", sentenceAfterAddingDate);
    }
}
