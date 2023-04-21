package org.loonglab.segment.nshort;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Sentence {

    private String content;

    private boolean isSeg;

    public Sentence() {
    }

    public Sentence(String content) {
        this.content = content;
    }

    public Sentence(String content, boolean isSeg) {
        this.content = content;
        this.isSeg = isSeg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSeg() {
        return isSeg;
    }

    public void setSeg(boolean isSeg) {
        this.isSeg = isSeg;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
