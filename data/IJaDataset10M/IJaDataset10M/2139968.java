package syntelos.uel.parser;

/**
 * <p>  </p>
 * 
 * @see EL 
 * @author jdp
 * @since 1.5
 */
public final class OpBinary extends Node {

    public static final int OPB_ARITH_PLUS = 1;

    public static final int OPB_ARITH_MINUS = 2;

    public static final int OPB_ARITH_MULTIPLY = 3;

    public static final int OPB_ARITH_DIVIDE = 4;

    public static final int OPB_ARITH_MOD = 5;

    public static final int OPB_ARITH_POW = 6;

    public static final int OPB_LOGIC_AND = 7;

    public static final int OPB_LOGIC_OR = 8;

    public static final int OPB_REL_EQ = 9;

    public static final int OPB_REL_NE = 10;

    public static final int OPB_REL_LT = 11;

    public static final int OPB_REL_LE = 12;

    public static final int OPB_REL_GT = 13;

    public static final int OPB_REL_GE = 14;

    /**
     * Scanning for separators between operators
     */
    public static final boolean IsSep(char ch) {
        switch(ch) {
            case ' ':
            case '\t':
            case '\r':
            case '\n':
            case '(':
            case '-':
            case '!':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }

    private int op;

    public OpBinary(Node p, char[] expr) {
        super(p);
        this.ofs = (p.ofs + p.len);
        this.ofs = ConsumeWhitespace(expr, this.ofs);
        this.parse(expr);
    }

    public OpBinary(char[] expr) {
        super();
        this.ofs = ConsumeWhitespace(expr, 0);
        this.parse(expr);
    }

    private void parse(char[] expr) {
        if (this.ofs < expr.length) {
            int cc = (this.ofs + 1);
            switch(expr[this.ofs]) {
                case '+':
                    if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                        this.len = 1;
                        this.op = OPB_ARITH_PLUS;
                    } else throw new Jump();
                    break;
                case '-':
                    if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                        this.len = 1;
                        this.op = OPB_ARITH_MINUS;
                    } else throw new Jump();
                    break;
                case '*':
                    if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                        this.len = 1;
                        this.op = OPB_ARITH_MULTIPLY;
                    } else throw new Jump();
                    break;
                case '/':
                    if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                        this.len = 1;
                        this.op = OPB_ARITH_DIVIDE;
                    } else throw new Jump();
                    break;
                case '%':
                    if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                        this.len = 1;
                        this.op = OPB_ARITH_MOD;
                    } else throw new Jump();
                    break;
                case '^':
                    if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                        this.len = 1;
                        this.op = OPB_ARITH_POW;
                    } else throw new Jump();
                    break;
                case '&':
                    if (cc < expr.length && '&' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_LOGIC_AND;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case '|':
                    if (cc < expr.length && '|' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_LOGIC_OR;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case '=':
                    if (cc < expr.length && '=' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_REL_EQ;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case '!':
                    if (cc < expr.length && '=' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_REL_NE;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case '<':
                    if (cc < expr.length && '=' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_REL_LE;
                        } else throw new Jump();
                    } else {
                        this.len = 1;
                        this.op = OPB_REL_LT;
                    }
                    break;
                case '>':
                    if (cc < expr.length) {
                        char t0 = expr[cc++];
                        if ('=' == t0) {
                            if (cc == expr.length || IsSep(expr[cc]) || java.lang.Character.isJavaIdentifierStart(expr[cc])) {
                                this.len = 2;
                                this.op = OPB_REL_GE;
                            } else throw new Jump();
                        } else if (cc == expr.length || IsSep(t0) || java.lang.Character.isJavaIdentifierStart(t0)) {
                            this.len = 1;
                            this.op = OPB_REL_GT;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'a':
                    if (cc < expr.length && 'n' == expr[cc++]) {
                        if (cc < expr.length && 'd' == expr[cc++]) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 3;
                                this.op = OPB_LOGIC_AND;
                            } else throw new Jump();
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'd':
                    if (cc < expr.length && 'i' == expr[cc++]) {
                        if (cc < expr.length && 'v' == expr[cc++]) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 3;
                                this.op = OPB_ARITH_DIVIDE;
                            } else throw new Jump();
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'e':
                    if (cc < expr.length && 'q' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_REL_EQ;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'g':
                    if (cc < expr.length) {
                        char t1 = expr[cc++];
                        if ('e' == t1) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 2;
                                this.op = OPB_REL_GE;
                            } else throw new Jump();
                        } else if ('t' == t1) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 2;
                                this.op = OPB_REL_GT;
                            } else throw new Jump();
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'l':
                    if (cc < expr.length) {
                        char t2 = expr[cc++];
                        if ('e' == t2) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 2;
                                this.op = OPB_REL_LE;
                            } else throw new Jump();
                        } else if ('t' == t2) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 2;
                                this.op = OPB_REL_LT;
                            } else throw new Jump();
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'm':
                    if (cc < expr.length && 'o' == expr[cc++]) {
                        if (cc < expr.length && 'd' == expr[cc++]) {
                            if (cc == expr.length || IsSep(expr[cc])) {
                                this.len = 3;
                                this.op = OPB_ARITH_MOD;
                            } else throw new Jump();
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'n':
                    if (cc < expr.length && 'e' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_REL_NE;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                case 'o':
                    if (cc < expr.length && 'r' == expr[cc++]) {
                        if (cc == expr.length || IsSep(expr[cc])) {
                            this.len = 2;
                            this.op = OPB_LOGIC_OR;
                        } else throw new Jump();
                    } else throw new Jump();
                    break;
                default:
                    throw new Jump();
            }
            this.term = new java.lang.String(expr, this.ofs, this.len);
            this.add(new Expression(this, expr));
        } else throw new Jump();
    }

    public java.lang.Object eval(syntelos.uel.Context cx) {
        java.lang.Object lhs = cx.getEvalStack().pop();
        java.lang.Object rhs = super.eval(cx);
        java.lang.Number lhs_num, rhs_num, re_num;
        java.lang.Boolean lhs_bool, rhs_bool, re_bool;
        switch(this.op) {
            case OPB_ARITH_PLUS:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_num = Add(lhs_num, rhs_num);
                return re_num;
            case OPB_ARITH_MINUS:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_num = Subtract(lhs_num, rhs_num);
                return re_num;
            case OPB_ARITH_MULTIPLY:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_num = Multiply(lhs_num, rhs_num);
                return re_num;
            case OPB_ARITH_DIVIDE:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_num = Divide(lhs_num, rhs_num);
                return re_num;
            case OPB_ARITH_MOD:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_num = Mod(lhs_num, rhs_num);
                return re_num;
            case OPB_ARITH_POW:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_num = Pow(lhs_num, rhs_num);
                return re_num;
            case OPB_LOGIC_AND:
                lhs_bool = ToBoolean(lhs);
                rhs_bool = ToBoolean(rhs);
                re_bool = And(lhs_bool, rhs_bool);
                return re_bool;
            case OPB_LOGIC_OR:
                lhs_bool = ToBoolean(lhs);
                rhs_bool = ToBoolean(rhs);
                re_bool = Or(lhs_bool, rhs_bool);
                return re_bool;
            case OPB_REL_EQ:
                lhs = ToObject(lhs);
                rhs = ToObject(rhs);
                return Eq(lhs, rhs);
            case OPB_REL_NE:
                lhs = ToObject(lhs);
                rhs = ToObject(rhs);
                return Ne(lhs, rhs);
            case OPB_REL_LT:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_bool = Lt(lhs_num, rhs_num);
                return re_bool;
            case OPB_REL_LE:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_bool = Le(lhs_num, rhs_num);
                return re_bool;
            case OPB_REL_GT:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_bool = Gt(lhs_num, rhs_num);
                return re_bool;
            case OPB_REL_GE:
                lhs_num = ToNumber(lhs);
                rhs_num = ToNumber(rhs);
                re_bool = Ge(lhs_num, rhs_num);
                return re_bool;
            default:
                throw new alto.sys.Error("Unrecognized term '" + this.term + "'.");
        }
    }
}
