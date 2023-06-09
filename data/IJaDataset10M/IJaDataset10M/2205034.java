package weka.core.json;

import java_cup.runtime.SymbolFactory;
import java.io.*;

/**
 * A scanner for JSON data files.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 5786 $
 */
public class Scanner implements java_cup.runtime.Scanner {

    /** This character denotes the end of file */
    public static final int YYEOF = -1;

    /** initial size of the lookahead buffer */
    private static final int ZZ_BUFFERSIZE = 16384;

    /** lexical states */
    public static final int STRING = 2;

    public static final int YYINITIAL = 0;

    /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
    private static final int ZZ_LEXSTATE[] = { 0, 0, 1, 1 };

    /** 
   * Translates characters to character classes
   */
    private static final char[] ZZ_CMAP = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 24, 0, 20, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 18, 17, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 21, 4, 0, 0, 0, 14, 23, 0, 0, 12, 13, 0, 0, 0, 0, 0, 9, 0, 7, 0, 0, 0, 11, 15, 10, 8, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0 };

    /** 
   * Translates DFA states to action switch labels.
   */
    private static final int[] ZZ_ACTION = zzUnpackAction();

    private static final String ZZ_ACTION_PACKED_0 = "\2\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7" + "\3\1\1\10\1\1\1\11\1\12\1\13\1\14\1\15" + "\3\0\2\16\1\17\1\20\1\21\1\22\1\23\1\24" + "\3\0\1\25\1\26";

    private static int[] zzUnpackAction() {
        int[] result = new int[35];
        int offset = 0;
        offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAction(String packed, int offset, int[] result) {
        int i = 0;
        int j = offset;
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do result[j++] = value; while (--count > 0);
        }
        return j;
    }

    /** 
   * Translates a state to a row index in the transition table
   */
    private static final int[] ZZ_ROWMAP = zzUnpackRowMap();

    private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\31\0\62\0\62\0\62\0\62\0\62\0\62" + "\0\62\0\113\0\144\0\175\0\226\0\257\0\62\0\62" + "\0\310\0\62\0\341\0\372\0ē\0Ĭ\0Ņ\0Ş" + "\0\62\0\62\0\62\0\62\0\62\0\62\0ŷ\0Ɛ" + "\0Ʃ\0\62\0\62";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[35];
        int offset = 0;
        offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackRowMap(String packed, int offset, int[] result) {
        int i = 0;
        int j = offset;
        int l = packed.length();
        while (i < l) {
            int high = packed.charAt(i++) << 16;
            result[j++] = high | packed.charAt(i++);
        }
        return j;
    }

    /** 
   * The transition table of the DFA
   */
    private static final int[] ZZ_TRANS = zzUnpackTrans();

    private static final String ZZ_TRANS_PACKED_0 = "\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12" + "\2\3\1\13\2\3\1\14\2\3\1\15\1\3\1\16" + "\1\17\1\20\1\3\1\20\1\3\1\20\23\21\1\22" + "\1\21\1\23\1\3\1\21\42\0\1\24\33\0\1\25" + "\33\0\1\26\32\0\1\15\1\27\27\0\1\30\10\0" + "\23\21\1\0\1\21\2\0\1\21\10\0\1\31\2\0" + "\1\32\1\33\1\0\1\34\5\0\1\35\3\0\1\36" + "\12\0\1\37\27\0\1\40\31\0\1\41\37\0\1\27" + "\30\0\1\30\1\27\20\0\1\42\33\0\1\43\33\0" + "\1\40\11\0";

    private static int[] zzUnpackTrans() {
        int[] result = new int[450];
        int offset = 0;
        offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackTrans(String packed, int offset, int[] result) {
        int i = 0;
        int j = offset;
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            value--;
            do result[j++] = value; while (--count > 0);
        }
        return j;
    }

    private static final int ZZ_UNKNOWN_ERROR = 0;

    private static final int ZZ_NO_MATCH = 1;

    private static final int ZZ_PUSHBACK_2BIG = 2;

    private static final String ZZ_ERROR_MSG[] = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };

    /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
    private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\2\0\7\11\5\1\2\11\1\1\1\11\1\1\3\0" + "\2\1\6\11\3\0\2\11";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[35];
        int offset = 0;
        offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAttribute(String packed, int offset, int[] result) {
        int i = 0;
        int j = offset;
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do result[j++] = value; while (--count > 0);
        }
        return j;
    }

    /** the input device */
    private java.io.Reader zzReader;

    /** the current state of the DFA */
    private int zzState;

    /** the current lexical state */
    private int zzLexicalState = YYINITIAL;

    /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
    private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

    /** the textposition at the last accepting state */
    private int zzMarkedPos;

    /** the current text position in the buffer */
    private int zzCurrentPos;

    /** startRead marks the beginning of the yytext() string in the buffer */
    private int zzStartRead;

    /** endRead marks the last character in the buffer, that has been read
      from input */
    private int zzEndRead;

    /** number of newlines encountered up to the start of the matched text */
    private int yyline;

    /** the number of characters up to the start of the matched text */
    private int yychar;

    /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
    private int yycolumn;

    /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
    private boolean zzAtBOL = true;

    /** zzAtEOF == true <=> the scanner is at the EOF */
    private boolean zzAtEOF;

    protected SymbolFactory m_SF;

    protected StringBuffer m_String = new StringBuffer();

    public Scanner(InputStream r, SymbolFactory sf) {
        this(r);
        m_SF = sf;
    }

    public Scanner(Reader r, SymbolFactory sf) {
        this(r);
        m_SF = sf;
    }

    /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
    public Scanner(java.io.Reader in) {
        this.zzReader = in;
    }

    /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
    public Scanner(java.io.InputStream in) {
        this(new java.io.InputStreamReader(in));
    }

    /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
    private boolean zzRefill() throws java.io.IOException {
        if (zzStartRead > 0) {
            System.arraycopy(zzBuffer, zzStartRead, zzBuffer, 0, zzEndRead - zzStartRead);
            zzEndRead -= zzStartRead;
            zzCurrentPos -= zzStartRead;
            zzMarkedPos -= zzStartRead;
            zzStartRead = 0;
        }
        if (zzCurrentPos >= zzBuffer.length) {
            char newBuffer[] = new char[zzCurrentPos * 2];
            System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
            zzBuffer = newBuffer;
        }
        int numRead = zzReader.read(zzBuffer, zzEndRead, zzBuffer.length - zzEndRead);
        if (numRead > 0) {
            zzEndRead += numRead;
            return false;
        }
        if (numRead == 0) {
            int c = zzReader.read();
            if (c == -1) {
                return true;
            } else {
                zzBuffer[zzEndRead++] = (char) c;
                return false;
            }
        }
        return true;
    }

    /**
   * Closes the input stream.
   */
    public final void yyclose() throws java.io.IOException {
        zzAtEOF = true;
        zzEndRead = zzStartRead;
        if (zzReader != null) zzReader.close();
    }

    /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
    public final void yyreset(java.io.Reader reader) {
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
        zzEndRead = zzStartRead = 0;
        zzCurrentPos = zzMarkedPos = 0;
        yyline = yychar = yycolumn = 0;
        zzLexicalState = YYINITIAL;
    }

    /**
   * Returns the current lexical state.
   */
    public final int yystate() {
        return zzLexicalState;
    }

    /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
    public final void yybegin(int newState) {
        zzLexicalState = newState;
    }

    /**
   * Returns the text matched by the current regular expression.
   */
    public final String yytext() {
        return new String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
    }

    /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
    public final char yycharat(int pos) {
        return zzBuffer[zzStartRead + pos];
    }

    /**
   * Returns the length of the matched text region.
   */
    public final int yylength() {
        return zzMarkedPos - zzStartRead;
    }

    /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
    private void zzScanError(int errorCode) {
        String message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
        }
        throw new Error(message);
    }

    /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
    public void yypushback(int number) {
        if (number > yylength()) zzScanError(ZZ_PUSHBACK_2BIG);
        zzMarkedPos -= number;
    }

    /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
    public java_cup.runtime.Symbol next_token() throws java.io.IOException {
        int zzInput;
        int zzAction;
        int zzCurrentPosL;
        int zzMarkedPosL;
        int zzEndReadL = zzEndRead;
        char[] zzBufferL = zzBuffer;
        char[] zzCMapL = ZZ_CMAP;
        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;
        while (true) {
            zzMarkedPosL = zzMarkedPos;
            zzAction = -1;
            zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
            zzState = ZZ_LEXSTATE[zzLexicalState];
            zzForAction: {
                while (true) {
                    if (zzCurrentPosL < zzEndReadL) zzInput = zzBufferL[zzCurrentPosL++]; else if (zzAtEOF) {
                        zzInput = YYEOF;
                        break zzForAction;
                    } else {
                        zzCurrentPos = zzCurrentPosL;
                        zzMarkedPos = zzMarkedPosL;
                        boolean eof = zzRefill();
                        zzCurrentPosL = zzCurrentPos;
                        zzMarkedPosL = zzMarkedPos;
                        zzBufferL = zzBuffer;
                        zzEndReadL = zzEndRead;
                        if (eof) {
                            zzInput = YYEOF;
                            break zzForAction;
                        } else {
                            zzInput = zzBufferL[zzCurrentPosL++];
                        }
                    }
                    int zzNext = zzTransL[zzRowMapL[zzState] + zzCMapL[zzInput]];
                    if (zzNext == -1) break zzForAction;
                    zzState = zzNext;
                    int zzAttributes = zzAttrL[zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction = zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) == 8) break zzForAction;
                    }
                }
            }
            zzMarkedPos = zzMarkedPosL;
            switch(zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
                case 20:
                    {
                        m_String.append('\b');
                    }
                case 23:
                    break;
                case 7:
                    {
                        return m_SF.newSymbol("Colon", sym.COLON);
                    }
                case 24:
                    break;
                case 13:
                    {
                        m_String.append('\\');
                    }
                case 25:
                    break;
                case 22:
                    {
                        return m_SF.newSymbol("Boolean", sym.BOOLEAN, new Boolean(yytext()));
                    }
                case 26:
                    break;
                case 14:
                    {
                        return m_SF.newSymbol("Double", sym.DOUBLE, new Double(yytext()));
                    }
                case 27:
                    break;
                case 17:
                    {
                        m_String.append('\r');
                    }
                case 28:
                    break;
                case 3:
                    {
                        return m_SF.newSymbol("Right curly bracket", sym.RCURLY);
                    }
                case 29:
                    break;
                case 19:
                    {
                        m_String.append('\"');
                    }
                case 30:
                    break;
                case 1:
                    {
                        System.err.println("Illegal character: " + yytext());
                    }
                case 31:
                    break;
                case 18:
                    {
                        m_String.append('\f');
                    }
                case 32:
                    break;
                case 21:
                    {
                        return m_SF.newSymbol("Null", sym.NULL);
                    }
                case 33:
                    break;
                case 16:
                    {
                        m_String.append('\t');
                    }
                case 34:
                    break;
                case 4:
                    {
                        return m_SF.newSymbol("Left square bracket", sym.LSQUARE);
                    }
                case 35:
                    break;
                case 12:
                    {
                        yybegin(YYINITIAL);
                        return m_SF.newSymbol("String", sym.STRING, m_String.toString());
                    }
                case 36:
                    break;
                case 15:
                    {
                        m_String.append('\n');
                    }
                case 37:
                    break;
                case 2:
                    {
                        return m_SF.newSymbol("Left curly bracket", sym.LCURLY);
                    }
                case 38:
                    break;
                case 6:
                    {
                        return m_SF.newSymbol("Comma", sym.COMMA);
                    }
                case 39:
                    break;
                case 8:
                    {
                        return m_SF.newSymbol("Integer", sym.INTEGER, new Integer(yytext()));
                    }
                case 40:
                    break;
                case 9:
                    {
                        m_String.setLength(0);
                        yybegin(STRING);
                    }
                case 41:
                    break;
                case 11:
                    {
                        m_String.append(yytext());
                    }
                case 42:
                    break;
                case 10:
                    {
                    }
                case 43:
                    break;
                case 5:
                    {
                        return m_SF.newSymbol("Right square bracket", sym.RSQUARE);
                    }
                case 44:
                    break;
                default:
                    if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                        zzAtEOF = true;
                        {
                            return m_SF.newSymbol("EOF", sym.EOF);
                        }
                    } else {
                        zzScanError(ZZ_NO_MATCH);
                    }
            }
        }
    }
}
