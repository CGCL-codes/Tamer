package com.jaeksoft.searchlib.snippet;

import org.w3c.dom.NamedNodeMap;

public class SentenceFragmenter extends FragmenterAbstract {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5789364707381845312L;

    @Override
    public void setAttributes(NamedNodeMap attr) {
    }

    @Override
    public void check(String originalText) {
        int pos = 0;
        char[] chars = originalText.toCharArray();
        boolean nextSpaceIsSplit = false;
        for (char ch : chars) {
            if (nextSpaceIsSplit) if (Character.isWhitespace(ch)) addSplit(pos + 1);
            switch(ch) {
                case '.':
                case '?':
                case '!':
                    nextSpaceIsSplit = true;
                    break;
                default:
                    nextSpaceIsSplit = false;
                    break;
            }
            pos++;
        }
    }

    @Override
    protected FragmenterAbstract newInstance() {
        return new SentenceFragmenter();
    }
}
