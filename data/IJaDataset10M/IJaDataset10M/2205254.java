package net.sapumal.jsinhalization.utilsoft.typingtutor.framework;

/**
 * @author sjayaratne
 */
public class SinhalaCharEvent {

    String textString;

    char[] textCharArray;

    int size;

    int committedIndex;

    boolean aged;

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public char[] gettextCharArray() {
        return textCharArray;
    }

    public void settextCharArray(char[] textCharArray) {
        this.textCharArray = textCharArray;
    }

    public int getsize() {
        return size;
    }

    public void setsize(int size) {
        this.size = size;
    }

    public int getcommittedIndex() {
        return committedIndex;
    }

    public void setcommittedIndex(int committedIndex) {
        this.committedIndex = committedIndex;
    }

    public void refresh() {
        aged = false;
    }

    public void aged() {
        aged = true;
    }

    public boolean isAged() {
        return aged;
    }
}
