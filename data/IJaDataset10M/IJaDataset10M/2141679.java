package ufolib.convert;

import java_cup.runtime.*;

/** CUP v0.10j generated parser.
  * @version Tue Sep 30 15:01:35 CEST 2003
  */
public class BUParser extends java_cup.runtime.lr_parser {

    /** Default constructor. */
    public BUParser() {
        super();
    }

    /** Constructor which sets the default scanner. */
    public BUParser(java_cup.runtime.Scanner s) {
        super(s);
    }

    /** Production table. */
    protected static final short _production_table[][] = unpackFromStrings(new String[] { "\000\044\000\002\002\004\000\002\003\007\000\002\003" + "\006\000\002\004\003\000\002\010\003\000\002\005\005" + "\000\002\006\005\000\002\020\004\000\002\020\003\000" + "\002\021\004\000\002\007\004\000\002\023\004\000\002" + "\023\003\000\002\024\011\000\002\032\004\000\002\032" + "\003\000\002\034\004\000\002\034\003\000\002\035\003" + "\000\002\011\004\000\002\012\004\000\002\013\006\000" + "\002\014\007\000\002\015\003\000\002\016\004\000\002" + "\017\003\000\002\022\004\000\002\025\004\000\002\025" + "\004\000\002\025\004\000\002\026\004\000\002\027\005" + "\000\002\030\005\000\002\031\007\000\002\033\003\000" + "\002\036\003" });

    /** Access to production table. */
    public short[][] production_table() {
        return _production_table;
    }

    /** Parse-action table. */
    protected static final short[][] _action_table = unpackFromStrings(new String[] { "\000\114\000\004\004\005\001\002\000\004\002\116\001" + "\002\000\004\026\115\001\002\000\004\005￾\001\002" + "\000\004\005\011\001\002\000\004\006\103\001\002\000" + "\004\024\101\001\002\000\006\011\020\013\016\001\002" + "\000\004\014\037\001\002\000\004\025\032\001\002\000" + "\004\013\016\001\002\000\004\023\025\001\002\000\004" + "\010\024\001\002\000\004\023\021\001\002\000\004\025" + "￩\001\002\000\004\002�\001\002\000\004\002￿" + "\001\002\000\004\002￪\001\002\000\004\014￧\001" + "\002\000\004\010\024\001\002\000\004\002\000\001\002" + "\000\006\012\035\025\032\001\002\000\006\012￹\025" + "￹\001\002\000\004\024\033\001\002\000\006\012￸" + "\025￸\001\002\000\006\012￺\025￺\001\002\000" + "\004\013￨\001\002\000\004\013￻\001\002\000\010" + "\023\077\025\076\026\100\001\002\000\006\010￷\014" + "\037\001\002\000\006\010￵\014￵\001\002\000\004" + "\015\044\001\002\000\004\016\047\001\002\000\004\023" + "\045\001\002\000\004\016￣\001\002\000\004\017\053" + "\001\002\000\004\023\050\001\002\000\004\023\051\001" + "\002\000\004\017￢\001\002\000\004\020\056\001\002" + "\000\004\023\054\001\002\000\004\023\055\001\002\000" + "\004\020￡\001\002\000\004\023\071\001\002\000\004" + "\021\062\001\002\000\004\022\067\001\002\000\006\022" + "￲\025\064\001\002\000\006\022￟\025￟\001\002" + "\000\006\022￰\025￰\001\002\000\006\022￯\025" + "￯\001\002\000\006\022￳\025\064\001\002\000\006" + "\022￱\025￱\001\002\000\006\010￞\014￞\001" + "\002\000\006\010￴\014￴\001\002\000\004\023\072" + "\001\002\000\004\023\073\001\002\000\004\023\074\001" + "\002\000\004\021￠\001\002\000\006\010￶\014￶" + "\001\002\000\004\015￦\001\002\000\004\015￥\001" + "\002\000\004\015￤\001\002\000\004\006￭\001\002" + "\000\004\007\107\001\002\000\004\023\104\001\002\000" + "\004\023\105\001\002\000\004\023\106\001\002\000\004" + "\007￬\001\002\000\004\023\111\001\002\000\006\011" + "￼\013￼\001\002\000\004\023\112\001\002\000\004" + "\023\113\001\002\000\004\023\114\001\002\000\006\011" + "￫\013￫\001\002\000\004\005￮\001\002\000\004" + "\002\001\001\002" });

    /** Access to parse-action table. */
    public short[][] action_table() {
        return _action_table;
    }

    /** <code>reduce_goto</code> table. */
    protected static final short[][] _reduce_table = unpackFromStrings(new String[] { "\000\114\000\010\003\003\004\006\011\005\001\001\000" + "\002\001\001\000\002\001\001\000\002\001\001\000\006" + "\005\011\012\007\001\001\000\004\013\101\001\001\000" + "\002\001\001\000\012\006\014\007\016\016\013\022\012" + "\001\001\000\010\023\037\024\040\025\041\001\001\000" + "\006\020\027\021\030\001\001\000\006\007\025\022\012" + "\001\001\000\002\001\001\000\006\010\022\015\021\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\006\010\026\015\021\001\001\000\002\001\001\000\006" + "\017\035\021\033\001\001\000\002\001\001\000\002\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\006\024\074\025" + "\041\001\001\000\002\001\001\000\004\026\042\001\001" + "\000\004\027\045\001\001\000\002\001\001\000\002\001" + "\001\000\004\030\051\001\001\000\002\001\001\000\002" + "\001\001\000\002\001\001\000\004\031\056\001\001\000" + "\002\001\001\000\002\001\001\000\002\001\001\000\002" + "\001\001\000\006\032\057\033\060\001\001\000\004\036" + "\067\001\001\000\006\034\064\035\062\001\001\000\002" + "\001\001\000\002\001\001\000\002\001\001\000\004\035" + "\065\001\001\000\002\001\001\000\002\001\001\000\002" + "\001\001\000\002\001\001\000\002\001\001\000\002\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\004\014\107\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\002\001\001\000\002\001\001\000\002" + "\001\001\000\002\001\001\000\002\001\001\000\002\001" + "\001" });

    /** Access to <code>reduce_goto</code> table. */
    public short[][] reduce_table() {
        return _reduce_table;
    }

    /** Instance of action encapsulation class. */
    protected CUP$BUParser$actions action_obj;

    /** Action encapsulation object initializer. */
    protected void init_actions() {
        action_obj = new CUP$BUParser$actions(this);
    }

    /** Invoke a user supplied parse action. */
    public java_cup.runtime.Symbol do_action(int act_num, java_cup.runtime.lr_parser parser, java.util.Stack stack, int top) throws java.lang.Exception {
        return action_obj.CUP$BUParser$do_action(act_num, parser, stack, top);
    }

    /** Indicates start state. */
    public int start_state() {
        return 0;
    }

    /** Indicates start production. */
    public int start_production() {
        return 0;
    }

    /** <code>EOF</code> Symbol index. */
    public int EOF_sym() {
        return 0;
    }

    /** <code>error</code> Symbol index. */
    public int error_sym() {
        return 1;
    }

    public static BdfFont m_bdffont;

    public static void setBdfFont(BdfFont f) {
        BUParser.m_bdffont = f;
    }
}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$BUParser$actions {

    private final BUParser parser;

    /** Constructor */
    CUP$BUParser$actions(BUParser parser) {
        this.parser = parser;
    }

    /** Method with the actual generated action code. */
    public final java_cup.runtime.Symbol CUP$BUParser$do_action(int CUP$BUParser$act_num, java_cup.runtime.lr_parser CUP$BUParser$parser, java.util.Stack CUP$BUParser$stack, int CUP$BUParser$top) throws java.lang.Exception {
        java_cup.runtime.Symbol CUP$BUParser$result;
        switch(CUP$BUParser$act_num) {
            case 35:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(28, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 34:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(25, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 33:
                {
                    Object RESULT = null;
                    int xleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).left;
                    int xright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).right;
                    Integer x = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).value;
                    int yleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left;
                    int yright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).right;
                    Integer y = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).value;
                    int xoleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left;
                    int xoright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).right;
                    Integer xo = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).value;
                    int yoleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int yoright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer yo = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.currGlyph().m_bbW = x.intValue();
                    BUParser.m_bdffont.currGlyph().m_bbH = y.intValue();
                    BUParser.m_bdffont.currGlyph().m_bbXoff = xo.intValue();
                    BUParser.m_bdffont.currGlyph().m_bbYoff = yo.intValue();
                    BUParser.m_bdffont.currGlyph().initBitmap();
                    CUP$BUParser$result = new java_cup.runtime.Symbol(23, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 4)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 32:
                {
                    Object RESULT = null;
                    int xleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left;
                    int xright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).right;
                    Integer x = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).value;
                    int yleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int yright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer y = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.currGlyph().m_dwidthX = x.intValue();
                    BUParser.m_bdffont.currGlyph().m_dwidthY = y.intValue();
                    CUP$BUParser$result = new java_cup.runtime.Symbol(22, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 31:
                {
                    Object RESULT = null;
                    int xleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left;
                    int xright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).right;
                    Integer x = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).value;
                    int yleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int yright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer y = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.currGlyph().m_swidthX = x.intValue();
                    BUParser.m_bdffont.currGlyph().m_swidthY = y.intValue();
                    CUP$BUParser$result = new java_cup.runtime.Symbol(21, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 30:
                {
                    Object RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer t = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.currGlyph().m_encoding = t.intValue();
                    BUParser.m_bdffont.currGlyph().m_encodingRaw = t.intValue();
                    BUParser.m_bdffont.mess(3, "   Encoding: int=" + t.intValue());
                    CUP$BUParser$result = new java_cup.runtime.Symbol(20, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 29:
                {
                    Object RESULT = null;
                    int sleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Float s = (Float) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.startChar();
                    BUParser.m_bdffont.currGlyph().m_startchar = "" + s;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(19, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 28:
                {
                    Object RESULT = null;
                    int sleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer s = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.startChar();
                    BUParser.m_bdffont.currGlyph().m_startchar = "" + s;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(19, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 27:
                {
                    Object RESULT = null;
                    int sleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    String s = (String) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.startChar();
                    BUParser.m_bdffont.currGlyph().m_startchar = s;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(19, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 26:
                {
                    Object RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer t = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.initChars(t.intValue());
                    CUP$BUParser$result = new java_cup.runtime.Symbol(16, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 25:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(13, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 24:
                {
                    Object RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer t = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.initProperties(t.intValue());
                    CUP$BUParser$result = new java_cup.runtime.Symbol(12, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 23:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(11, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 22:
                {
                    Object RESULT = null;
                    int x1left = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).left;
                    int x1right = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).right;
                    Integer x1 = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).value;
                    int x2left = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left;
                    int x2right = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).right;
                    Integer x2 = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).value;
                    int x3left = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left;
                    int x3right = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).right;
                    Integer x3 = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).value;
                    int x4left = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int x4right = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Integer x4 = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.m_fontboundingboxFbbx = x1.intValue();
                    BUParser.m_bdffont.m_fontboundingboxFbby = x2.intValue();
                    BUParser.m_bdffont.m_fontboundingboxXoff = x3.intValue();
                    BUParser.m_bdffont.m_fontboundingboxYoff = x4.intValue();
                    CUP$BUParser$result = new java_cup.runtime.Symbol(10, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 4)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 21:
                {
                    Object RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).right;
                    Integer t = (Integer) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).value;
                    BUParser.m_bdffont.m_sizePointsize = t.intValue();
                    CUP$BUParser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 20:
                {
                    Object RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    String t = (String) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.m_font = t;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(8, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 19:
                {
                    Object RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    Float t = (Float) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.m_startfont = t.toString();
                    CUP$BUParser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 18:
                {
                    Object RESULT = null;
                    int sleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    String s = (String) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.currGlyph().insertHexline(s);
                    CUP$BUParser$result = new java_cup.runtime.Symbol(27, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 17:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(26, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 16:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(26, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 15:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(24, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 14:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(24, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 13:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(18, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 6)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 12:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(17, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 11:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(17, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 10:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(5, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 9:
                {
                    Object RESULT = null;
                    int p1left = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left;
                    int p1right = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).right;
                    String p1 = (String) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).value;
                    int p2left = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left;
                    int p2right = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right;
                    String p2 = (String) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).value;
                    BUParser.m_bdffont.oneProperty(p1, p2);
                    CUP$BUParser$result = new java_cup.runtime.Symbol(15, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 8:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(14, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 7:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(14, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 6:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(4, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 5:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(3, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 4:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 3:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(2, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 2:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 1:
                {
                    Object RESULT = null;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 4)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                return CUP$BUParser$result;
            case 0:
                {
                    Object RESULT = null;
                    int start_valleft = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left;
                    int start_valright = ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).right;
                    Object start_val = (Object) ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).value;
                    RESULT = start_val;
                    CUP$BUParser$result = new java_cup.runtime.Symbol(0, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$BUParser$stack.elementAt(CUP$BUParser$top - 0)).right, RESULT);
                }
                CUP$BUParser$parser.done_parsing();
                return CUP$BUParser$result;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
