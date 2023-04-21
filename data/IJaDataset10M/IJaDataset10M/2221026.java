package tralesld.struct.source;

public class TextWithMarking {

    public String text;

    public int beginIndex;

    public int endIndex;

    public int caretIndex;

    public TextWithMarking(String text, int beginIndex, int endIndex, int caretIndex) {
        this.text = text;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.caretIndex = caretIndex;
    }

    public String toString() {
        return "begin: " + beginIndex + " end: " + endIndex + " caret: " + caretIndex;
    }
}
