package org.armedbear.j;

public final class StringPosition implements Constants {

    private final String text;

    private final int length;

    private int offset;

    public StringPosition(String s) {
        text = s;
        length = s.length();
    }

    public StringPosition(String s, int offset) {
        this(s);
        this.offset = offset;
    }

    public final String getText() {
        return text;
    }

    public final int getOffset() {
        return offset;
    }

    public final void setOffset(int n) {
        Debug.assertTrue(n <= length);
        this.offset = n;
    }

    public final char getChar() {
        Debug.assertTrue(offset <= length);
        if (offset == length) return EOL;
        return text.charAt(offset);
    }

    public final boolean lookingAt(String s) {
        return s.regionMatches(0, text, offset, s.length());
    }

    public final boolean atEnd() {
        return offset >= length;
    }

    public final boolean charIsWhitespace() {
        return Character.isWhitespace(text.charAt(offset));
    }

    public final boolean next() {
        if (offset < length) {
            ++offset;
            return true;
        }
        return false;
    }

    public final void skip(int count) {
        offset += count;
    }
}
