package com.rapidminer.ntext.document.annotation.impl.token;

import com.rapidminer.ntext.document.Document;
import com.rapidminer.ntext.document.annotation.impl.SingleRangeAnnotation;

/**
 * This class of annotations simply have a start and an end. It will
 * return a String as value that is the substring of the document containing
 * the covered text.
 * 
 * @author Sebastian Land
 */
public class TokenAnnotation extends SingleRangeAnnotation<String> {

    private Document document;

    public TokenAnnotation(Document document) {
        this.document = document;
    }

    @Override
    public String getValue() {
        return document.getText().substring(startIndex, endIndex);
    }

    public void set(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}
