package cen5501c.managers;

public class FilePiece {

    private byte[] content = null;

    private int index = 0;

    public FilePiece(byte[] content, int index) {
        this.index = index;
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }
}
