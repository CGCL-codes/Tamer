package alx.library.converter;

import java.io.*;

public class HtmlReader extends Reader {

    private int underlyingFileOffset = 0;

    private InputStreamReader isr;

    private final StringBuilder destinationBuffer = new StringBuilder();

    private ParserState state = ParserState.NORMAL;

    private boolean wasSpace = false;

    private enum ParserState {

        NORMAL, O_C_1, O_C_2, O_C_3, O_C_4, IN_COMMENT, C_C_1, C_C_2, C_C_3, IN_TAG, C_T
    }

    ;

    public HtmlReader(InputStream stream) {
        isr = new InputStreamReader(stream);
    }

    @Override
    public void close() throws IOException {
        isr.close();
    }

    @Override
    public synchronized int read(char[] cbuf, int off, int len) throws IOException {
        int i = initBuffer(len);
        if (i == 0) {
            return -1;
        } else {
            destinationBuffer.getChars(0, destinationBuffer.length(), cbuf, off);
            destinationBuffer.setLength(0);
            return i;
        }
    }

    private int initBuffer(int len) throws IOException {
        int i;
        char c;
        while (destinationBuffer.length() < len && (i = isr.read()) != -1) {
            underlyingFileOffset++;
            c = (char) i;
            state = stateMachine(c, state);
            if (state == ParserState.NORMAL) {
                if (isSpace(c)) {
                    if (wasSpace) {
                    } else {
                        destinationBuffer.append(" ");
                    }
                    wasSpace = true;
                } else {
                    destinationBuffer.append(c);
                    wasSpace = false;
                }
            }
        }
        return destinationBuffer.length();
    }

    private static ParserState stateMachine(final char c, final ParserState state) {
        ParserState newState = state;
        switch(c) {
            case '<':
                switch(state) {
                    case NORMAL:
                        newState = ParserState.O_C_1;
                        break;
                    case O_C_1:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_2:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_3:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_4:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case IN_COMMENT:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_1:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_2:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_3:
                        newState = ParserState.O_C_1;
                        break;
                    case IN_TAG:
                        newState = ParserState.IN_TAG;
                        break;
                    case C_T:
                        newState = ParserState.O_C_1;
                        break;
                }
                break;
            case '>':
                switch(state) {
                    case NORMAL:
                        newState = ParserState.NORMAL;
                        break;
                    case O_C_1:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_2:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_3:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_4:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case IN_COMMENT:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_1:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_2:
                        newState = ParserState.C_C_3;
                        break;
                    case C_C_3:
                        newState = ParserState.NORMAL;
                        break;
                    case IN_TAG:
                        newState = ParserState.C_T;
                        break;
                    case C_T:
                        newState = ParserState.C_T;
                        break;
                }
                break;
            case '!':
                switch(state) {
                    case NORMAL:
                        newState = ParserState.NORMAL;
                        break;
                    case O_C_1:
                        newState = ParserState.O_C_2;
                        break;
                    case O_C_2:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_3:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_4:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case IN_COMMENT:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_1:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_2:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_3:
                        newState = ParserState.NORMAL;
                        break;
                    case IN_TAG:
                        newState = ParserState.IN_TAG;
                        break;
                    case C_T:
                        newState = ParserState.NORMAL;
                        break;
                }
                break;
            case '-':
                switch(state) {
                    case NORMAL:
                        newState = ParserState.NORMAL;
                        break;
                    case O_C_1:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_2:
                        newState = ParserState.O_C_3;
                        break;
                    case O_C_3:
                        newState = ParserState.O_C_4;
                        break;
                    case O_C_4:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case IN_COMMENT:
                        newState = ParserState.C_C_1;
                        break;
                    case C_C_1:
                        newState = ParserState.C_C_2;
                        break;
                    case C_C_2:
                        newState = ParserState.C_C_2;
                        break;
                    case C_C_3:
                        newState = ParserState.NORMAL;
                        break;
                    case IN_TAG:
                        newState = ParserState.IN_TAG;
                        break;
                    case C_T:
                        newState = ParserState.NORMAL;
                        break;
                }
                break;
            default:
                switch(state) {
                    case NORMAL:
                        newState = ParserState.NORMAL;
                        break;
                    case O_C_1:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_2:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_3:
                        newState = ParserState.IN_TAG;
                        break;
                    case O_C_4:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case IN_COMMENT:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_1:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_2:
                        newState = ParserState.IN_COMMENT;
                        break;
                    case C_C_3:
                        newState = ParserState.NORMAL;
                        break;
                    case IN_TAG:
                        newState = ParserState.IN_TAG;
                        break;
                    case C_T:
                        newState = ParserState.NORMAL;
                        break;
                }
                break;
        }
        return newState;
    }

    private static boolean isSpace(final char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    public int getUnderlyingFileOffset() {
        return underlyingFileOffset;
    }

    @Override
    public boolean markSupported() {
        return false;
    }
}
