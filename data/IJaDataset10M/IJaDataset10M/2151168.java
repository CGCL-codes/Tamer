package gatchan.jedit.lucene;

public class LineResult extends Result {

    public int getLine() {
        return Integer.valueOf(getDocument().getFieldable("line").stringValue());
    }

    public String getText() {
        return getDocument().getFieldable("content").stringValue();
    }

    public String toString() {
        return super.toString() + ':' + getLine() + " - " + getText();
    }
}
