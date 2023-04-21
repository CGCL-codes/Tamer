package org.jfonia.musicxml.model;

import org.w3c.dom.Node;

/**
 * Lyric element (most often single syllable).
 * 
 * @author Wijnand
 *
 */
public class Lyric extends Element {

    private int number;

    private String text;

    private String syllabic;

    public Lyric(Node node) {
        super(node);
    }

    public Lyric(Node node, int number, String text, String syllabic) {
        super(node);
        this.number = number;
        this.text = text;
        this.syllabic = syllabic;
    }

    public String getSyllabic() {
        return syllabic;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSyllabic(String syllabic) {
        this.syllabic = syllabic;
    }

    public void setText(String text) {
        this.text = text;
    }
}
