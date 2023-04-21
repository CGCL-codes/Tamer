package com.rapidminer.operator;

import com.rapidminer.tools.Tools;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class WordList extends ResultObjectAdapter {

    private static final long serialVersionUID = 540707324825965969L;

    private WVTWordList wordList;

    public WordList(WVTWordList wordList) {
        this.wordList = wordList;
    }

    public WVTWordList getWordList() {
        return wordList;
    }

    public String getExtension() {
        return "wordList";
    }

    public String getFileDescription() {
        return "Word List";
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < wordList.getNumWords(); i++) buffer.append(wordList.getWord(i) + Tools.getLineSeparator());
        return buffer.toString();
    }
}
