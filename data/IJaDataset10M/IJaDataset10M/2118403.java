package nmazurs.sample_gae.client.word_connections.beans.impl;

import nmazurs.sample_gae.client.word_connections.beans.WordCount;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class WordCountListGridRecord extends ListGridRecord {

    public WordCountListGridRecord() {
        setAttribute("details", "<i>click for connections</i>");
    }

    public WordCountListGridRecord(final WordCount wordCountToWrap) {
        this();
        setWord(wordCountToWrap.getWord());
        setCount(wordCountToWrap.getCount());
    }

    public static WordCountListGridRecord[] createArray(final WordCount[] array) {
        if (array == null) {
            return null;
        }
        final WordCountListGridRecord[] newArray = new WordCountListGridRecord[array.length];
        int i = 0;
        for (final WordCount wordCount : array) {
            newArray[i++] = new WordCountListGridRecord(wordCount);
        }
        return newArray;
    }

    public String getWord() {
        return getAttributeAsString("word");
    }

    public void setWord(String word) {
        setAttribute("word", word);
    }

    public int getCount() {
        return getAttributeAsInt("count");
    }

    public void setCount(int count) {
        setAttribute("count", count);
    }

    public String getFieldValue(String field) {
        return getAttributeAsString(field);
    }
}
