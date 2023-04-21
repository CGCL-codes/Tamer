package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;
import org.fife.ui.rsyntaxtextarea.*;

/**
 * This class takes plain text and returns tokens representing Z80
 * assembler.<p>
 *
 * @author Markus Hohmann
 * @version 1.0
 *
 */
public class AssemblerZ80TokenMaker extends AbstractJFlexTokenMaker implements TokenMaker {

    /** This character denotes the end of file */
    public static final int YYEOF = -1;

    /** initial size of the lookahead buffer */
    private static final int ZZ_BUFFERSIZE = 16384;

    /** lexical states */
    public static final int YYINITIAL = 0;

    /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
    private static final int ZZ_LEXSTATE[] = { 0, 1 };

    /** 
   * Translates characters to character classes
   */
    private static final String ZZ_CMAP_PACKED = "\11\0\1\11\1\10\1\0\1\11\23\0\1\11\1\15\1\5\2\0" + "\1\14\1\15\1\6\1\42\1\43\1\13\1\13\1\4\1\13\1\4" + "\1\13\12\2\1\12\1\7\1\15\1\15\1\15\2\0\1\35\1\31" + "\1\3\1\24\1\16\1\25\1\23\1\41\1\33\1\47\1\34\1\36" + "\1\37\1\27\1\21\1\40\1\17\1\22\1\30\1\26\1\20\1\1" + "\1\32\1\44\1\45\1\46\1\4\1\0\1\4\1\15\1\1\1\0" + "\1\35\1\31\1\3\1\24\1\16\1\25\1\23\1\41\1\33\1\47" + "\1\34\1\36\1\37\1\27\1\21\1\40\1\17\1\22\1\30\1\26" + "\1\20\1\1\1\32\1\44\1\45\1\46\1\0\1\15\1\0\1\15" + "ﾁ\0";

    /** 
   * Translates characters to character classes
   */
    private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

    /** 
   * Translates DFA states to action switch labels.
   */
    private static final int[] ZZ_ACTION = zzUnpackAction();

    private static final String ZZ_ACTION_PACKED_0 = "\2\0\2\1\1\2\1\3\1\4\1\5\1\6\1\7" + "\1\10\1\11\1\3\1\1\3\3\3\1\1\3\1\1" + "\6\3\1\2\3\1\1\12\1\1\1\13\2\2\1\14" + "\1\15\1\16\1\1\2\14\1\1\1\14\3\1\1\14" + "\1\1\1\3\1\12\1\1\1\12\2\17\1\2\13\1" + "\1\14\1\3\1\2\1\3\1\1\1\14\3\1\2\2" + "\2\1\1\14\1\1\1\14\1\13\4\14\1\2\5\1" + "\1\3\1\1\1\2\6\1";

    private static int[] zzUnpackAction() {
        int[] result = new int[104];
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

    private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\50\0\120\0\170\0\240\0\310\0\360\0Ę" + "\0ŀ\0\120\0Ũ\0\120\0Ɛ\0Ƹ\0Ǡ\0Ȉ" + "\0\240\0Ȱ\0ɘ\0ʀ\0ʨ\0ː\0˸\0̠" + "\0͈\0\170\0Ͱ\0Θ\0π\0Ϩ\0А\0и" + "\0Ѡ\0҈\0҈\0Ұ\0Ә\0Ԁ\0\120\0\120" + "\0Ԩ\0\170\0Ր\0ո\0֠\0׈\0װ\0ؘ" + "\0ـ\0٨\0ڐ\0\170\0ڸ\0\240\0\240\0\170" + "\0۠\0܈\0ܰ\0ݘ\0ހ\0ި\0ߐ\0߸" + "\0ࠠ\0ࡈ\0ࡰ\0࢘\0ࣀ\0А\0ࣨ\0ऐ" + "\0स\0ॠ\0ঈ\0র\0৘\0਀\0ਨ\0੐" + "\0੸\0\240\0ઠ\0੸\0\170\0ૈ\0૰\0ଘ" + "\0ࡈ\0ୀ\0୨\0ஐ\0ஸ\0௠\0ఈ\0҈" + "\0ర\0ౘ\0ಀ\0ನ\0೐\0೸\0ഠ\0ൈ";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[104];
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

    private static final String ZZ_TRANS_PACKED_0 = "\1\3\1\4\1\5\1\6\1\3\1\7\1\10\1\11" + "\1\12\1\13\1\3\3\14\1\15\2\4\1\16\1\17" + "\1\4\1\20\1\21\1\22\1\23\1\24\1\25\1\26" + "\1\27\1\4\1\30\1\31\1\32\1\33\1\34\1\35" + "\1\5\1\36\1\37\1\32\1\40\1\3\1\4\1\5" + "\1\6\1\3\1\7\1\10\1\11\1\12\1\13\1\3" + "\1\14\1\41\1\14\1\15\2\4\1\16\1\17\1\4" + "\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27" + "\1\4\1\30\1\31\1\32\1\33\1\34\1\35\1\5" + "\1\36\1\37\1\32\1\40\50\0\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\32\4\1\42\1\4" + "\2\5\1\0\3\42\2\0\1\43\2\0\1\42\1\5" + "\5\4\2\5\3\4\1\5\3\4\1\5\4\4\2\5" + "\4\4\1\42\1\4\1\5\1\44\1\0\3\42\2\0" + "\1\43\2\0\1\42\1\5\5\4\2\5\3\4\1\5" + "\3\4\1\45\2\4\1\46\1\4\2\5\4\4\5\7" + "\1\47\42\7\6\10\1\50\41\10\10\11\1\0\37\11" + "\11\0\1\13\36\0\1\42\1\4\2\5\1\0\3\42" + "\2\0\1\43\2\0\1\42\1\5\1\51\4\4\2\5" + "\3\4\1\5\1\4\1\52\1\4\1\5\4\4\2\5" + "\1\53\3\4\1\42\3\4\1\0\3\42\2\0\1\43" + "\2\0\1\42\2\4\1\54\1\4\1\55\3\4\1\56" + "\21\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\1\57\1\4\1\60\1\4\1\61\5\4\1\62" + "\5\4\1\61\11\4\1\42\1\4\2\5\1\0\3\42" + "\2\0\1\43\2\0\1\42\1\63\1\64\1\65\3\4" + "\1\5\1\66\1\64\2\4\1\67\1\70\1\52\1\4" + "\1\71\4\4\2\5\3\4\1\72\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\15\4\1\73\14\4" + "\1\42\2\4\1\32\1\0\3\42\2\0\1\43\2\0" + "\1\42\1\74\2\4\1\75\24\4\1\32\1\4\1\42" + "\2\4\1\76\1\0\3\42\2\0\1\43\2\0\1\42" + "\1\62\1\4\1\77\1\4\1\100\6\4\1\101\4\4" + "\1\102\1\4\1\32\7\4\1\42\1\4\1\5\1\21" + "\1\0\3\42\2\0\1\43\2\0\1\42\1\5\3\4" + "\1\103\1\4\2\5\3\4\1\5\1\4\1\62\1\4" + "\1\5\4\4\2\5\4\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\4\4\1\104\25\4\1\42" + "\3\4\1\0\3\42\2\0\1\43\2\0\1\42\11\4" + "\1\105\7\4\1\52\4\4\2\106\2\4\1\42\1\4" + "\2\5\1\0\3\42\2\0\1\43\2\0\1\42\1\5" + "\5\4\1\107\1\110\1\4\1\111\1\4\1\5\3\4" + "\1\5\4\4\2\5\4\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\6\4\1\112\17\4\1\32" + "\3\4\1\42\2\4\1\32\1\0\3\42\2\0\1\43" + "\2\0\1\42\2\4\1\113\1\114\26\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\17\4\1\115" + "\1\32\5\4\2\32\2\4\1\42\1\4\2\5\1\0" + "\3\42\2\0\1\43\2\0\1\42\1\5\5\4\1\116" + "\1\5\3\4\1\117\3\4\1\5\3\4\1\120\2\5" + "\4\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\3\4\1\121\14\4\1\32\2\4\1\32\6\4" + "\1\42\3\4\1\0\3\42\2\0\1\43\2\0\1\42" + "\20\4\1\32\2\4\1\32\6\4\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\4\4\1\52\15\4" + "\1\52\7\4\1\0\3\41\12\0\32\41\4\42\1\0" + "\3\42\2\0\1\42\2\0\34\42\1\4\2\5\1\0" + "\3\42\2\0\1\43\2\0\1\42\1\5\5\4\1\5" + "\1\122\3\4\1\5\3\4\1\5\4\4\2\5\4\4" + "\1\42\1\4\2\5\1\0\3\42\2\0\1\43\2\0" + "\1\42\1\5\5\4\2\5\3\4\1\5\3\4\1\5" + "\1\123\3\4\2\5\4\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\6\4\1\124\6\4\1\124" + "\2\4\1\52\11\4\1\42\3\4\1\0\3\42\2\0" + "\1\43\2\0\1\42\2\4\1\125\27\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\26\4\1\52" + "\3\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\10\4\1\126\21\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\5\4\1\125\24\4\1\42" + "\3\4\1\0\3\42\2\0\1\43\2\0\1\42\6\4" + "\1\121\6\4\1\121\14\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\10\4\1\127\1\4\1\130" + "\17\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\11\4\1\64\20\4\1\42\2\4\1\131\1\0" + "\3\42\2\0\1\43\2\0\1\42\6\4\1\52\10\4" + "\1\52\12\4\1\42\3\4\1\0\3\42\2\0\1\43" + "\2\0\1\42\10\4\1\52\21\4\1\42\1\4\1\5" + "\1\122\1\0\3\42\2\0\1\43\2\0\1\42\1\5" + "\5\4\1\5\1\132\3\4\1\5\3\4\1\5\4\4" + "\2\5\4\4\1\42\3\4\1\0\3\42\2\0\1\43" + "\2\0\1\42\22\4\1\64\7\4\1\42\1\4\2\5" + "\1\0\3\42\2\0\1\43\2\0\1\42\1\5\5\4" + "\2\5\3\4\1\5\3\4\1\122\4\4\2\5\4\4" + "\1\42\3\4\1\0\3\42\2\0\1\43\2\0\1\42" + "\11\4\1\133\20\4\1\42\3\4\1\0\3\42\2\0" + "\1\43\2\0\1\42\21\4\1\134\10\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\5\4\1\52" + "\24\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\20\4\1\135\1\4\1\52\7\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\7\4\1\52" + "\22\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\13\4\1\52\16\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\17\4\2\52\11\4\1\42" + "\2\4\1\52\1\0\3\42\2\0\1\43\2\0\1\42" + "\32\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\17\4\1\52\12\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\1\136\15\4\1\64\13\4" + "\1\42\3\4\1\0\3\42\2\0\1\43\2\0\1\42" + "\15\4\1\137\14\4\1\42\2\4\1\52\1\0\3\42" + "\2\0\1\43\2\0\1\42\6\4\1\124\6\4\1\124" + "\14\4\1\42\1\4\1\5\1\122\1\0\3\42\2\0" + "\1\43\2\0\1\42\1\5\5\4\1\122\1\5\3\4" + "\1\5\3\4\1\5\4\4\2\5\4\4\1\42\1\4" + "\2\5\1\0\1\42\1\140\1\42\2\0\1\43\2\0" + "\1\42\1\5\5\4\2\5\3\4\1\5\3\4\1\5" + "\4\4\2\5\4\4\1\42\3\4\1\0\3\42\2\0" + "\1\43\2\0\1\42\6\4\1\52\23\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\6\4\1\124" + "\6\4\1\124\14\4\1\42\3\4\1\0\3\42\2\0" + "\1\43\2\0\1\42\12\4\1\141\17\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\22\4\1\52" + "\7\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\20\4\1\62\11\4\1\42\1\4\2\5\1\0" + "\3\42\2\0\1\43\2\0\1\42\1\142\5\4\2\5" + "\3\4\1\5\3\4\1\5\4\4\2\5\4\4\1\42" + "\1\4\1\5\1\142\1\0\3\42\2\0\1\43\2\0" + "\1\42\1\5\5\4\2\5\3\4\1\5\3\4\1\5" + "\4\4\2\5\4\4\1\42\3\4\1\0\3\42\2\0" + "\1\43\2\0\1\42\20\4\1\143\11\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\4\4\1\52" + "\25\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\20\4\1\52\11\4\1\42\3\4\1\0\3\42" + "\2\0\1\43\2\0\1\42\6\4\1\52\6\4\1\52" + "\14\4\1\42\3\4\1\0\3\42\2\0\1\43\2\0" + "\1\42\11\4\1\52\3\4\1\52\14\4\1\42\3\4" + "\1\0\3\42\2\0\1\43\2\0\1\42\1\4\1\64" + "\4\4\1\64\1\4\1\64\2\4\2\64\15\4\1\42" + "\1\4\2\5\1\0\3\42\2\0\1\43\2\0\1\42" + "\1\5\5\4\2\5\3\4\1\67\3\4\1\5\4\4" + "\2\5\4\4\1\42\3\4\1\0\3\42\2\0\1\43" + "\2\0\1\42\30\4\1\52\1\4\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\1\144\31\4\1\42" + "\3\4\1\0\3\42\2\0\1\43\2\0\1\42\15\4" + "\1\145\14\4\1\42\3\4\1\0\3\42\2\0\1\43" + "\2\0\1\42\17\4\1\146\12\4\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\10\4\1\147\21\4" + "\1\42\3\4\1\0\3\42\2\0\1\43\2\0\1\42" + "\23\4\1\52\6\4\1\42\1\4\2\5\1\0\3\42" + "\2\0\1\43\2\0\1\42\1\5\5\4\2\5\3\4" + "\1\5\3\4\1\5\4\4\1\5\1\21\4\4\1\42" + "\3\4\1\0\3\42\2\0\1\43\2\0\1\42\25\4" + "\1\32\4\4\1\42\3\4\1\0\3\42\2\0\1\43" + "\2\0\1\42\12\4\1\64\17\4\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\12\4\1\150\17\4" + "\1\42\3\4\1\0\3\42\2\0\1\43\2\0\1\42" + "\16\4\1\64\13\4\1\42\3\4\1\0\3\42\2\0" + "\1\43\2\0\1\42\1\64\31\4\1\42\3\4\1\0" + "\3\42\2\0\1\43\2\0\1\42\10\4\1\64\21\4";

    private static int[] zzUnpackTrans() {
        int[] result = new int[3440];
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

    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\2\0\1\11\6\1\1\11\1\1\1\11\32\1\2\11" + "\100\1";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[104];
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

    /** denotes if the user-EOF-code has already been executed */
    private boolean zzEOFDone;

    /**
	 * Constructor.  We must have this here as JFLex does not generate a
	 * no parameter constructor.
	 */
    public AssemblerZ80TokenMaker() {
        super();
    }

    /**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
    private void addToken(int tokenType) {
        addToken(zzStartRead, zzMarkedPos - 1, tokenType);
    }

    /**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
    private void addToken(int start, int end, int tokenType) {
        int so = start + offsetShift;
        addToken(zzBuffer, start, end, tokenType, so);
    }

    /**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *                    occurs.
	 */
    public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
        super.addToken(array, start, end, tokenType, startOffset);
        zzStartRead = zzMarkedPos;
    }

    /**
	 * Returns the text to place at the beginning and end of a
	 * line to "comment" it in a this programming language.
	 *
	 * @return The start and end strings to add to a line to "comment"
	 *         it out.
	 */
    public String[] getLineCommentStartAndEnd() {
        return new String[] { ";", null };
    }

    /**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *                    <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();
        this.offsetShift = -text.offset + startOffset;
        int state = Token.NULL;
        switch(initialTokenType) {
            default:
                state = Token.NULL;
        }
        s = text;
        try {
            yyreset(zzReader);
            yybegin(state);
            return yylex();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new DefaultToken();
        }
    }

    /**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 * @exception   IOException  if any I/O-Error occurs.
	 */
    private boolean zzRefill() throws java.io.IOException {
        return zzCurrentPos >= s.offset + s.count;
    }

    /**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream 
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream 
	 */
    public final void yyreset(java.io.Reader reader) throws java.io.IOException {
        zzBuffer = s.array;
        zzStartRead = s.offset;
        zzEndRead = zzStartRead + s.count - 1;
        zzCurrentPos = zzMarkedPos = s.offset;
        zzLexicalState = YYINITIAL;
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
    }

    /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
    public AssemblerZ80TokenMaker(java.io.Reader in) {
        this.zzReader = in;
    }

    /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
    public AssemblerZ80TokenMaker(java.io.InputStream in) {
        this(new java.io.InputStreamReader(in));
    }

    /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
    private static char[] zzUnpackCMap(String packed) {
        char[] map = new char[0x10000];
        int i = 0;
        int j = 0;
        while (i < 182) {
            int count = packed.charAt(i++);
            char value = packed.charAt(i++);
            do map[j++] = value; while (--count > 0);
        }
        return map;
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
    public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
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
            if (zzMarkedPosL > zzStartRead) {
                switch(zzBufferL[zzMarkedPosL - 1]) {
                    case '\n':
                    case '':
                    case '':
                    case '':
                    case ' ':
                    case ' ':
                        zzAtBOL = true;
                        break;
                    case '\r':
                        if (zzMarkedPosL < zzEndReadL) zzAtBOL = zzBufferL[zzMarkedPosL] != '\n'; else if (zzAtEOF) zzAtBOL = false; else {
                            boolean eof = zzRefill();
                            zzMarkedPosL = zzMarkedPos;
                            zzEndReadL = zzEndRead;
                            zzBufferL = zzBuffer;
                            if (eof) zzAtBOL = false; else zzAtBOL = zzBufferL[zzMarkedPosL] != '\n';
                        }
                        break;
                    default:
                        zzAtBOL = false;
                }
            }
            zzAction = -1;
            zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
            if (zzAtBOL) zzState = ZZ_LEXSTATE[zzLexicalState + 1]; else zzState = ZZ_LEXSTATE[zzLexicalState];
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
                case 12:
                    {
                        addToken(Token.RESERVED_WORD);
                    }
                case 16:
                    break;
                case 1:
                    {
                        addToken(Token.IDENTIFIER);
                    }
                case 17:
                    break;
                case 13:
                    {
                        addToken(Token.LITERAL_STRING_DOUBLE_QUOTE);
                    }
                case 18:
                    break;
                case 10:
                    {
                        addToken(Token.FUNCTION);
                    }
                case 19:
                    break;
                case 6:
                    {
                        addToken(Token.COMMENT_EOL);
                        addNullToken();
                        return firstToken;
                    }
                case 20:
                    break;
                case 8:
                    {
                        addToken(Token.WHITESPACE);
                    }
                case 21:
                    break;
                case 3:
                    {
                        addToken(Token.VARIABLE);
                    }
                case 22:
                    break;
                case 11:
                    {
                        addToken(Token.PREPROCESSOR);
                    }
                case 23:
                    break;
                case 15:
                    {
                        addToken(Token.DATA_TYPE);
                    }
                case 24:
                    break;
                case 5:
                    {
                        addToken(Token.ERROR_CHAR);
                    }
                case 25:
                    break;
                case 9:
                    {
                        addToken(Token.OPERATOR);
                    }
                case 26:
                    break;
                case 2:
                    {
                        addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
                    }
                case 27:
                    break;
                case 14:
                    {
                        addToken(Token.LITERAL_CHAR);
                    }
                case 28:
                    break;
                case 4:
                    {
                        addToken(Token.ERROR_STRING_DOUBLE);
                        addNullToken();
                        return firstToken;
                    }
                case 29:
                    break;
                case 7:
                    {
                        addNullToken();
                        return firstToken;
                    }
                case 30:
                    break;
                default:
                    if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                        zzAtEOF = true;
                        switch(zzLexicalState) {
                            case YYINITIAL:
                                {
                                    addNullToken();
                                    return firstToken;
                                }
                            case 105:
                                break;
                            default:
                                return null;
                        }
                    } else {
                        zzScanError(ZZ_NO_MATCH);
                    }
            }
        }
    }
}
