package parser;

public abstract class BufferObject {

    public BufferObject() {
        next = null;
    }

    protected BufferObject next;

    public BufferObject getNextObj() {
        return next;
    }
}
