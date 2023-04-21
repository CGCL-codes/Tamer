package com.rapidminer.operator.preprocessing.ie.tokenizer.tools;

import java.util.ArrayList;
import de.unidortmund.pg520.fileworker.app.simpleregex.SimpleRegexFinder;

/**
 * This class reads the contents of an ascii file and marks possible line
 * endings, by matching a triplet of words against a set of rules
 * 
 * @author Marius Kubatz
 * @version $Id: SentenceSplitter.java,v 1.2 2009-03-12 13:30:29 jungerma Exp $
 */
public class SentenceSplitter {

    private ArrayList<String> contents = new ArrayList<String>();

    private DictionaryLookup abbrevations = new DictionaryLookup("../data/dictionaries/abbreviations.txt");

    private DictionaryLookup months = new DictionaryLookup("../data/dictionaries/months.txt");

    private DictionaryLookup datepreq = new DictionaryLookup("../data/dictionaries/dateprequisites.txt");

    private SimpleRegexFinder finder = new SimpleRegexFinder();

    static String suspicious = "[-!.?:)]$";

    static String wrapped_end = "[-]$";

    static String allcaps_wrapped = "[A-Z??????][-]$";

    static String allcaps_begins = "^[A-Z??????][A-Z??????]+";

    static String ends_dot = "[.]$";

    static String begins_small = "^[a-z????????]";

    static String begins_large = "^[A-Z??????]";

    static String innerdots = "[a-z????????A-Z??????0-9_]+[.][a-z????????A-Z??????0-9_,]+";

    static String number = "[0-9]+[.]";

    static String begins_number = "^[0-9]";

    static String sentence_end = "[?!]$";

    static String direct_speech = "[:]$";

    static String begins_brace = "^[(]";

    static String ends_brace = "[)]$";

    public static String single_end_tag = "<end/>";

    public static String end_tag = single_end_tag + System.getProperty("line.separator");

    public String processString(String input) {
        this.createWordwiseList(input);
        this.contents.trimToSize();
        int size = this.contents.size() - 1;
        StringBuffer output = new StringBuffer();
        output.append(this.contents.get(0) + " ");
        for (int i = 1; i < size; i++) {
            String ending = this.checkEnding(i, size);
            output.append(ending);
        }
        output.append(this.contents.get(size) + end_tag);
        this.contents.clear();
        output = cleanOutput(output);
        return output.toString();
    }

    private StringBuffer cleanOutput(StringBuffer o) {
        String s = o.toString();
        StringBuffer sB = new StringBuffer("");
        String[] lines = s.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().length() != 0) {
                if (line.endsWith(single_end_tag)) {
                    line = line.substring(0, line.indexOf(single_end_tag));
                }
                sB.append(line.trim() + System.getProperty("line.separator"));
            }
        }
        return sB;
    }

    private void createWordwiseList(String input) {
        String[] linewise = input.split(System.getProperty("line.separator"));
        int lineCount = linewise.length;
        for (int x = 0; x < lineCount; x++) {
            String[] st = linewise[x].trim().split("\\s+");
            int wordCount = st.length;
            for (int y = 0; y < wordCount; y++) {
                if (!st[y].equals("") && !st[y].equals(".") && st[y] != null) {
                    this.contents.add(st[y]);
                }
            }
        }
    }

    private String checkEnding(int index, int size) {
        if (finder.findPattern("[??]", this.contents.get(index))) {
        }
        if (!finder.findPattern(suspicious, this.contents.get(index))) {
            return this.contents.get(index) + " ";
        }
        int next = index + 1;
        int previous = index - 1;
        if (finder.findPattern(allcaps_wrapped, this.contents.get(index))) {
            if (finder.findPattern(allcaps_begins, this.contents.get(next))) {
                return this.contents.get(index).replaceFirst("-", "");
            }
            return this.contents.get(index);
        }
        if (finder.findPattern(wrapped_end, this.contents.get(index))) {
            return this.contents.get(index);
        }
        if (finder.findPattern(ends_dot, this.contents.get(index)) && finder.findPattern(begins_small, this.contents.get(next))) {
            return this.contents.get(index) + " ";
        }
        if (finder.findPattern(innerdots, this.contents.get(index))) {
            return this.contents.get(index) + " ";
        }
        if (finder.findPattern(number, this.contents.get(index)) && this.datepreq.hitDict(this.contents.get(previous))) {
            return this.contents.get(index) + " ";
        }
        if (finder.findPattern(number, this.contents.get(index)) && this.months.hitDict(this.contents.get(next))) {
            return this.contents.get(index) + " ";
        }
        if (finder.findPattern(begins_small, this.contents.get(next))) {
            return this.contents.get(index) + " ";
        }
        if (finder.findPattern(number, this.contents.get(index))) {
            return this.contents.get(index) + " ";
        }
        if (this.abbrevations.hitDict(this.contents.get(index))) {
            return this.contents.get(index) + " ";
        }
        if (finder.findPattern(sentence_end, this.contents.get(index))) {
            return this.contents.get(index) + end_tag;
        }
        if (finder.findPattern(direct_speech, this.contents.get(index))) {
            if (finder.findPattern(begins_number, this.contents.get(next))) {
                return this.contents.get(index) + " ";
            }
            return this.contents.get(index) + end_tag;
        }
        if (finder.findPattern(begins_brace, this.contents.get(index))) {
            if (finder.findPattern(ends_brace, this.contents.get(index)) || finder.findPattern(ends_brace, this.contents.get(next))) {
                return this.contents.get(index) + " ";
            }
            return end_tag + System.getProperty("line.separator") + this.contents.get(index) + " ";
        }
        if (finder.findPattern(ends_brace, this.contents.get(index))) {
            if (finder.findPattern(begins_brace, this.contents.get(previous))) {
                return this.contents.get(index) + " ";
            }
            return this.contents.get(index) + end_tag;
        }
        if (finder.findPattern(ends_dot, this.contents.get(index)) && finder.findPattern(begins_large, this.contents.get(next))) {
            return this.contents.get(index) + end_tag;
        }
        return this.contents.get(index) + end_tag;
    }

    public void setSentenceEndTag(String input) {
        end_tag = input;
    }
}
