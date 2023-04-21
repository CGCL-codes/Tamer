package com.go.tea.io;

import java.io.*;

/******************************************************************************
 * The PushbackPositionReader is a kind of pushback reader that tracks the 
 * postion in the stream of the next character to be read.
 * The java.io.PushbackReader allows arbitrary characters
 * to be pushed back. Since this Reader may need to keep track of how many
 * characters were scanned from the underlying Reader to actually produce
 * a character, the unread operation cannot accept any arbitrary character.
 * 
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 6 $-->, <!--$$JustDate:--> 12/11/00 <!-- $-->
 * @deprecated Moved to com.go.trove.io package.
 * @see java.io.PushbackReader
 */
public class PushbackPositionReader extends PositionReader {

    /** Maximum pushback allowed. */
    private int mMaxPushback;

    /** Most recently read characters with escapes already processed. */
    private int[] mCharacters;

    /** The scan lengths of the most recently read characters. */
    private int[] mPositions;

    /** The cursor marks the position in the arrays of the last character. */
    private int mCursor;

    /** Amount of characters currently pushed back. */
    private int mPushback;

    public PushbackPositionReader(Reader reader) {
        this(reader, 0);
    }

    public PushbackPositionReader(Reader reader, int pushback) {
        super(reader);
        pushback += 2;
        mMaxPushback = pushback;
        mCharacters = new int[pushback];
        mPositions = new int[pushback];
        mCursor = 0;
        mPushback = 0;
    }

    /**
     * @return the start position of the last read character.
     */
    public int getStartPosition() {
        int back = mCursor - 2;
        if (back < 0) back += mMaxPushback;
        return mPositions[back];
    }

    public int read() throws IOException {
        int c;
        if (mPushback > 0) {
            mPushback--;
            mPosition = mPositions[mCursor];
            c = mCharacters[mCursor++];
            if (mCursor >= mMaxPushback) mCursor = 0;
            return c;
        }
        c = super.read();
        mPositions[mCursor] = mPosition;
        mCharacters[mCursor++] = c;
        if (mCursor >= mMaxPushback) mCursor = 0;
        return c;
    }

    public int peek() throws IOException {
        int c = read();
        unread();
        return c;
    }

    /**
     * Unread the last several characters read.
     *
     * <p>Unlike PushbackReader, unread does not allow arbitrary characters to
     * to be unread. Rather, it functions like an undo operation.
     *
     * @param amount Amount of characters to unread.
     * @see java.io.PushbackReader#unread(int)
     */
    public void unread(int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            unread();
        }
    }

    /**
     * Unread the last character read.
     * 
     * <p>Unlike PushbackReader, unread does not allow arbitrary characters to
     * to be unread. Rather, it functions like an undo operation.
     *
     * @see java.io.PushbackReader#unread(int)
     */
    public void unread() throws IOException {
        mPushback++;
        if (mPushback > mMaxPushback - 2) {
            throw new IOException(this.getClass().getName() + ": pushback exceeded " + (mMaxPushback - 2));
        }
        if ((--mCursor) < 0) mCursor += mMaxPushback;
        if (mCursor > 0) {
            mPosition = mPositions[mCursor - 1];
        } else {
            mPosition = mPositions[mMaxPushback - 1];
        }
        unreadHook(mCharacters[mCursor]);
    }

    /**
     * A hook call from the unread method(s). Every unread character is
     * passed to this method.
     */
    protected void unreadHook(int c) {
    }
}
