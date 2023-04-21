package qp.parser;

import java_cup.runtime.*;
import java.util.*;
import qp.utils.*;

/** CUP v0.10k generated parser.
  * @version Sat Oct 30 11:04:14 CST 2010
  */
public class parser extends java_cup.runtime.lr_parser {

    /** Default constructor. */
    public parser() {
        super();
    }

    /** Constructor which sets the default scanner. */
    public parser(java_cup.runtime.Scanner s) {
        super(s);
    }

    /** Production table. */
    protected static final short _production_table[][] = unpackFromStrings(new String[] { "\000\041\000\002\003\005\000\002\002\004\000\002\003" + "\011\000\002\003\010\000\002\003\011\000\002\003\010" + "\000\002\003\007\000\002\003\006\000\002\003\007\000" + "\002\003\006\000\002\004\005\000\002\004\003\000\002" + "\012\005\000\002\012\003\000\002\006\005\000\002\006" + "\003\000\002\007\005\000\002\007\003\000\002\010\005" + "\000\002\010\005\000\002\010\004\000\002\010\004\000" + "\002\005\005\000\002\005\004\000\002\013\005\000\002" + "\013\006\000\002\013\006\000\002\011\003\000\002\011" + "\003\000\002\011\003\000\002\011\003\000\002\011\003" + "\000\002\011\003" });

    /** Access to production table. */
    public short[][] production_table() {
        return _production_table;
    }

    /** Parse-action table. */
    protected static final short[][] _action_table = unpackFromStrings(new String[] { "\000\075\000\004\021\005\001\002\000\006\002\066\026" + "\065\001\002\000\010\004\006\010\010\025\007\001\002" + "\000\006\003\062\007\063\001\002\000\006\004\006\010" + "\050\001\002\000\004\022\044\001\002\000\006\005\013" + "\022\014\001\002\000\006\005￶\022￶\001\002\000" + "\004\004\006\001\002\000\004\004\015\001\002\000\012" + "\002￲\005￲\023￲\026￲\001\002\000\012\002" + "￺\005\017\023\020\026￺\001\002\000\004\004\042" + "\001\002\000\006\003\022\004\006\001\002\000\010\002" + "￾\005\040\026￾\001\002\000\004\031\037\001\002" + "\000\020\003\026\011\030\012\033\013\032\014\025\015" + "\027\016\031\001\002\000\010\002￰\005￰\026￰" + "\001\002\000\006\004￣\031￣\001\002\000\010\002" + "￭\005￭\026￭\001\002\000\006\004￡\031￡" + "\001\002\000\006\004￦\031￦\001\002\000\006\004" + "￢\031￢\001\002\000\006\004￤\031￤\001\002" + "\000\006\004￥\031￥\001\002\000\006\004\006\031" + "\036\001\002\000\010\002￮\005￮\026￮\001\002" + "\000\010\002￯\005￯\026￯\001\002\000\010\002" + "￬\005￬\026￬\001\002\000\006\003\022\004\006" + "\001\002\000\010\002￱\005￱\026￱\001\002\000" + "\012\002￳\005￳\023￳\026￳\001\002\000\006" + "\005￷\022￷\001\002\000\004\004\015\001\002\000" + "\012\002￸\005\017\023\046\026￸\001\002\000\006" + "\003\022\004\006\001\002\000\010\002￼\005\040\026" + "￼\001\002\000\004\022\056\001\002\000\006\005\013" + "\022\052\001\002\000\004\004\015\001\002\000\012\002" + "￻\005\017\023\054\026￻\001\002\000\006\003\022" + "\004\006\001\002\000\010\002￿\005\040\026￿\001" + "\002\000\004\004\015\001\002\000\012\002￹\005\017" + "\023\060\026￹\001\002\000\006\003\022\004\006\001" + "\002\000\010\002�\005\040\026�\001\002\000\030" + "\002￪\003￪\005￪\011￪\012￪\013￪\014" + "￪\015￪\016￪\022￪\026￪\001\002\000\004" + "\004\064\001\002\000\030\002￫\003￫\005￫\011" + "￫\012￫\013￫\014￫\015￫\016￫\022￫" + "\026￫\001\002\000\004\004\067\001\002\000\004\002" + "\000\001\002\000\004\007\074\001\002\000\010\002￴" + "\005￴\026￴\001\002\000\010\002\001\005\072\026" + "\001\001\002\000\004\004\067\001\002\000\010\002￵" + "\005￵\026￵\001\002\000\004\004\075\001\002\000" + "\014\002￩\005￩\026￩\027\076\030\077\001\002" + "\000\010\002￨\005￨\026￨\001\002\000\010\002" + "￧\005￧\026￧\001\002" });

    /** Access to parse-action table. */
    public short[][] action_table() {
        return _action_table;
    }

    /** <code>reduce_goto</code> table. */
    protected static final short[][] _reduce_table = unpackFromStrings(new String[] { "\000\075\000\004\003\003\001\001\000\002\001\001\000" + "\006\004\010\005\011\001\001\000\002\001\001\000\006" + "\004\050\005\011\001\001\000\002\001\001\000\002\001" + "\001\000\002\001\001\000\004\005\042\001\001\000\004" + "\006\015\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\010\005\022\007\020\010\023\001\001" + "\000\002\001\001\000\002\001\001\000\004\011\033\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\002\001\001\000\004\005\034\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\006\005\022\010\040\001\001\000\002\001\001\000\002" + "\001\001\000\002\001\001\000\004\006\044\001\001\000" + "\002\001\001\000\010\005\022\007\046\010\023\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\004\006\052\001\001\000\002\001\001\000\010\005\022" + "\007\054\010\023\001\001\000\002\001\001\000\004\006" + "\056\001\001\000\002\001\001\000\010\005\022\007\060" + "\010\023\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\002\001\001\000\006\012\070\013\067" + "\001\001\000\002\001\001\000\002\001\001\000\002\001" + "\001\000\002\001\001\000\004\013\072\001\001\000\002" + "\001\001\000\002\001\001\000\002\001\001\000\002\001" + "\001\000\002\001\001" });

    /** Access to <code>reduce_goto</code> table. */
    public short[][] reduce_table() {
        return _reduce_table;
    }

    /** Instance of action encapsulation class. */
    protected CUP$parser$actions action_obj;

    /** Action encapsulation object initializer. */
    protected void init_actions() {
        action_obj = new CUP$parser$actions(this);
    }

    /** Invoke a user supplied parse action. */
    public java_cup.runtime.Symbol do_action(int act_num, java_cup.runtime.lr_parser parser, java.util.Stack stack, int top) throws java.lang.Exception {
        return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
    }

    /** Indicates start state. */
    public int start_state() {
        return 0;
    }

    /** Indicates start production. */
    public int start_production() {
        return 1;
    }

    /** <code>EOF</code> Symbol index. */
    public int EOF_sym() {
        return 0;
    }

    /** <code>error</code> Symbol index. */
    public int error_sym() {
        return 1;
    }

    public SQLQuery query;

    public SQLQuery getSQLQuery() {
        return query;
    }

    public void report_fatal_error(String message, Object info) throws java.lang.Exception {
        done_parsing();
        report_error("Fatal error occurred, stop parsing.", info);
    }

    public void syntax_error(Symbol cur_token) {
    }
}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$parser$actions {

    private final parser parser;

    /** Constructor */
    CUP$parser$actions(parser parser) {
        this.parser = parser;
    }

    /** Method with the actual generated action code. */
    public final java_cup.runtime.Symbol CUP$parser$do_action(int CUP$parser$act_num, java_cup.runtime.lr_parser CUP$parser$parser, java.util.Stack CUP$parser$stack, int CUP$parser$top) throws java.lang.Exception {
        java_cup.runtime.Symbol CUP$parser$result;
        switch(CUP$parser$act_num) {
            case 32:
                {
                    Condition RESULT = null;
                    RESULT = new Condition(Condition.EQUAL);
                    CUP$parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 31:
                {
                    Condition RESULT = null;
                    RESULT = new Condition(Condition.NOTEQUAL);
                    CUP$parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 30:
                {
                    Condition RESULT = null;
                    RESULT = new Condition(Condition.GTOE);
                    CUP$parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 29:
                {
                    Condition RESULT = null;
                    RESULT = new Condition(Condition.LTOE);
                    CUP$parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 28:
                {
                    Condition RESULT = null;
                    RESULT = new Condition(Condition.GREATERTHAN);
                    CUP$parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 27:
                {
                    Condition RESULT = null;
                    RESULT = new Condition(Condition.LESSTHAN);
                    CUP$parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 26:
                {
                    OrderByAttribute RESULT = null;
                    int i1left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                    int i1right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                    TokenValue i1 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                    int i2left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int i2right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    TokenValue i2 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    Attribute a = new Attribute(i1.text(), i2.text());
                    OrderByAttribute o = new OrderByAttribute(a);
                    o.setDesc();
                    RESULT = o;
                    CUP$parser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 25:
                {
                    OrderByAttribute RESULT = null;
                    int i1left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
                    int i1right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
                    TokenValue i1 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
                    int i2left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int i2right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    TokenValue i2 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    Attribute a = new Attribute(i1.text(), i2.text());
                    OrderByAttribute o = new OrderByAttribute(a);
                    RESULT = o;
                    CUP$parser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 24:
                {
                    OrderByAttribute RESULT = null;
                    int i1left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int i1right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    TokenValue i1 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int i2left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int i2right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    TokenValue i2 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Attribute a = new Attribute(i1.text(), i2.text());
                    OrderByAttribute o = new OrderByAttribute(a);
                    RESULT = o;
                    CUP$parser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 23:
                {
                    Attribute RESULT = null;
                    int ileft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int iright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    TokenValue i = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    int pleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int pright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Object p = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    System.out.println("syntax error: incorrect attribute:" + i.text());
                    System.exit(0);
                    CUP$parser$result = new java_cup.runtime.Symbol(3, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 22:
                {
                    Attribute RESULT = null;
                    int i1left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int i1right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    TokenValue i1 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int i2left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int i2right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    TokenValue i2 = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    RESULT = new Attribute(i1.text(), i2.text());
                    CUP$parser$result = new java_cup.runtime.Symbol(3, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 21:
                {
                    Condition RESULT = null;
                    int pleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int pright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    Object p = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    int sleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    TokenValue s = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    System.out.println("syntax error: incorrect condition:" + s.text());
                    System.exit(0);
                    CUP$parser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 20:
                {
                    Condition RESULT = null;
                    int atleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int atright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    Attribute at = (Attribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    int pleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int pright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Object p = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    System.out.println("syntax error: incorrect condition");
                    System.exit(0);
                    CUP$parser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 19:
                {
                    Condition RESULT = null;
                    int a1left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int a1right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Attribute a1 = (Attribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int oleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int oright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    Condition o = (Condition) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    int a2left = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int a2right = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Attribute a2 = (Attribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Condition c = new Condition(a1, o.getExprType(), a2);
                    c.setOpType(Condition.JOIN);
                    RESULT = c;
                    CUP$parser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 18:
                {
                    Condition RESULT = null;
                    int atleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int atright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Attribute at = (Attribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int oleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int oright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    Condition o = (Condition) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    int sleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    TokenValue s = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Condition c = new Condition(at, o.getExprType(), s.text());
                    c.setOpType(Condition.SELECT);
                    RESULT = c;
                    CUP$parser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 17:
                {
                    Vector RESULT = null;
                    int cleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int cright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Condition c = (Condition) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector clist = new Vector();
                    clist.add(c);
                    RESULT = clist;
                    CUP$parser$result = new java_cup.runtime.Symbol(5, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 16:
                {
                    Vector RESULT = null;
                    int clistleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int clistright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector clist = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int cleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int cright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Condition c = (Condition) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    clist.add(c);
                    RESULT = clist;
                    CUP$parser$result = new java_cup.runtime.Symbol(5, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 15:
                {
                    Vector RESULT = null;
                    int ileft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int iright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    TokenValue i = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector tlist = new Vector();
                    tlist.add(i.text());
                    RESULT = tlist;
                    CUP$parser$result = new java_cup.runtime.Symbol(4, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 14:
                {
                    Vector RESULT = null;
                    int tlistleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int tlistright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector tlist = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int ileft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int iright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    TokenValue i = (TokenValue) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    tlist.add(i.text());
                    RESULT = tlist;
                    CUP$parser$result = new java_cup.runtime.Symbol(4, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 13:
                {
                    Vector RESULT = null;
                    int atleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int atright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    OrderByAttribute at = (OrderByAttribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector v = new Vector();
                    v.add(at);
                    RESULT = v;
                    CUP$parser$result = new java_cup.runtime.Symbol(8, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 12:
                {
                    Vector RESULT = null;
                    int asleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int asright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector as = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int aleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int aright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    OrderByAttribute a = (OrderByAttribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    as.add(a);
                    RESULT = as;
                    CUP$parser$result = new java_cup.runtime.Symbol(8, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 11:
                {
                    Vector RESULT = null;
                    int atleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int atright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Attribute at = (Attribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector v = new Vector();
                    v.add(at);
                    RESULT = v;
                    CUP$parser$result = new java_cup.runtime.Symbol(2, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 10:
                {
                    Vector RESULT = null;
                    int asleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int asright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector as = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int aleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int aright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Attribute a = (Attribute) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    as.add(a);
                    RESULT = as;
                    CUP$parser$result = new java_cup.runtime.Symbol(2, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 9:
                {
                    SQLQuery RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector a = new Vector();
                    SQLQuery sq = new SQLQuery(a, t);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 8:
                {
                    SQLQuery RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector a = new Vector();
                    SQLQuery sq = new SQLQuery(a, t);
                    sq.setIsDistinct(true);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 7:
                {
                    SQLQuery RESULT = null;
                    int aleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int aright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector a = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector v1 = new Vector();
                    SQLQuery sq = new SQLQuery(a, t);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 6:
                {
                    SQLQuery RESULT = null;
                    int aleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int aright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector a = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector v1 = new Vector();
                    SQLQuery sq = new SQLQuery(a, t);
                    sq.setIsDistinct(true);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 5:
                {
                    SQLQuery RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int cleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int cright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector c = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector a = new Vector();
                    SQLQuery sq = new SQLQuery(a, t, c);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 4:
                {
                    SQLQuery RESULT = null;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int cleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int cright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector c = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector a = new Vector();
                    SQLQuery sq = new SQLQuery(a, t, c);
                    sq.setIsDistinct(true);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 3:
                {
                    SQLQuery RESULT = null;
                    int aleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                    int aright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                    Vector a = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int cleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int cright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector c = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector v1 = new Vector();
                    SQLQuery sq = new SQLQuery(a, t, c);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 5)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 2:
                {
                    SQLQuery RESULT = null;
                    int aleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
                    int aright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
                    Vector a = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
                    int tleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int tright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    Vector t = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int cleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int cright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector c = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    Vector v1 = new Vector();
                    SQLQuery sq = new SQLQuery(a, t, c);
                    sq.setIsDistinct(true);
                    parser.query = sq;
                    RESULT = sq;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 6)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            case 1:
                {
                    Object RESULT = null;
                    int start_valleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                    int start_valright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                    SQLQuery start_val = (SQLQuery) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                    RESULT = start_val;
                    CUP$parser$result = new java_cup.runtime.Symbol(0, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                CUP$parser$parser.done_parsing();
                return CUP$parser$result;
            case 0:
                {
                    SQLQuery RESULT = null;
                    int sleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
                    int sright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
                    SQLQuery s = (SQLQuery) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
                    int bleft = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                    int bright = ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                    Vector b = (Vector) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                    s.setOrderbyList(b);
                    parser.query = s;
                    CUP$parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
                }
                return CUP$parser$result;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
