package net.cattaka.swing.text;

public class TextLineInfo {

    private int startPos;

    private int endPos;

    private String line;

    public TextLineInfo() {
    }

    public TextLineInfo(int startPos, int endPos, String line) {
        super();
        this.startPos = startPos;
        this.endPos = endPos;
        this.line = line;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "[" + startPos + "," + endPos + "," + line + "]";
    }
}
