package org.ibex.js.parse;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.ibex.util.Basket;
import org.ibex.util.SourceException;
import org.ibex.util.Vec;

/**
 *  Parses a stream of lexed tokens into a tree of Function's.
 *
 *  There are three kinds of things we parse: blocks, statements, and
 *  expressions.
 *
 *  - Expressions are a special type of statement that evaluates to a
 *    value (for example, "break" is not an expression, * but "3+2"
 *    is).  Some tokens sequences start expressions (for * example,
 *    literal numbers) and others continue an expression which * has
 *    already been begun (for example, '+').  Finally, some *
 *    expressions are valid targets for an assignment operation; after
 *    * each of these expressions, continueExprAfterAssignable() is
 *    called * to check for an assignment operation.
 *
 *  - A statement ends with a semicolon and does not return a value.
 *
 *  - A block is a single statement or a sequence of statements
 *    surrounded by curly braces.
 *
 *  Each parsing method saves the parserLine before doing its actual
 *  work and restores it afterwards.  This ensures that parsing a
 *  subexpression does not modify the line number until a token
 *  *after* the subexpression has been consumed by the parent
 *  expression.
 *
 *  Technically it would be a better design for this class to build an
 *  intermediate parse tree and use that to emit bytecode.  Here's the
 *  tradeoff:
 *
 *  Advantages of building a parse tree:
 *  - easier to apply optimizations
 *  - would let us handle more sophisticated languages than JavaScript
 *
 *  Advantages of leaving out the parse tree
 *  - faster compilation
 *  - less load on the garbage collector
 *  - much simpler code, easier to understand
 *  - less error-prone
 *
 *  Fortunately JS is such a simple language that we can get away with
 *  the half-assed approach and still produce a working, complete
 *  compiler.
 *
 *  The bytecode language emitted doesn't really cause any appreciable
 *  semantic loss, and is itself a parseable language very similar to
 *  Forth or a postfix variant of LISP.  This means that the bytecode
 *  can be transformed into a parse tree, which can be manipulated.
 *  So if we ever want to add an optimizer, it could easily be done by
 *  producing a parse tree from the bytecode, optimizing that tree,
 *  and then re-emitting the bytecode.  The parse tree node class
 *  would also be much simpler since the bytecode language has so few
 *  operators.
 *
 *  Actually, the above paragraph is slightly inaccurate -- there are
 *  places where we push a value and then perform an arbitrary number
 *  of operations using it before popping it; this doesn't parse well.
 *  But these cases are clearly marked and easy to change if we do
 *  need to move to a parse tree format.
 */
public abstract class Parser extends Lexer implements ByteCodes {

    public static boolean newfor = true;

    /** Special slots for special variables */
    public static final int SLOT_ARGUMENTS = -1;

    public static final int SLOT_TRAPNAME = -2;

    public static final int SLOT_TRAPEE = -3;

    public static final int SLOT_CALLEE = -4;

    public static final int SLOT_THIS = -5;

    static byte[] precedence = new byte[MAX_TOKEN + 1];

    static boolean[] isRightAssociative = new boolean[MAX_TOKEN + 1];

    private static final int NO_COMMA = 2;

    static {
        isRightAssociative[ASSIGN] = isRightAssociative[ASSIGN_BITOR] = isRightAssociative[ASSIGN_BITXOR] = isRightAssociative[ASSIGN_BITAND] = isRightAssociative[ASSIGN_LSH] = isRightAssociative[ASSIGN_RSH] = isRightAssociative[ASSIGN_URSH] = isRightAssociative[ASSIGN_ADD] = isRightAssociative[ASSIGN_SUB] = isRightAssociative[ASSIGN_MUL] = isRightAssociative[ASSIGN_DIV] = isRightAssociative[ASSIGN_MOD] = isRightAssociative[ADD_TRAP] = isRightAssociative[DEL_TRAP] = true;
        precedence[COMMA] = 1;
        precedence[ASSIGN] = precedence[ASSIGN_BITOR] = precedence[ASSIGN_BITXOR] = precedence[ASSIGN_BITAND] = precedence[ASSIGN_LSH] = precedence[ASSIGN_RSH] = precedence[ASSIGN_URSH] = precedence[ASSIGN_ADD] = precedence[ASSIGN_SUB] = precedence[ASSIGN_MUL] = precedence[ASSIGN_DIV] = precedence[ADD_TRAP] = precedence[DEL_TRAP] = precedence[ASSIGN_MOD] = 3;
        precedence[HOOK] = precedence[HOOK_COLON] = 4;
        precedence[OR] = 5;
        precedence[AND] = 6;
        precedence[BITOR] = 7;
        precedence[BITXOR] = 8;
        precedence[BITAND] = 9;
        precedence[EQ] = precedence[NE] = precedence[SHEQ] = precedence[SHNE] = 10;
        precedence[LT] = precedence[LE] = precedence[GT] = precedence[GE] = 11;
        precedence[INSTANCEOF] = 11;
        precedence[LSH] = precedence[RSH] = precedence[URSH] = 12;
        precedence[ADD] = precedence[SUB] = 12;
        precedence[MUL] = precedence[DIV] = precedence[MOD] = 13;
        precedence[BITNOT] = precedence[BANG] = precedence[TYPEOF] = precedence[KEYSOF] = 14;
        precedence[HOOK_LB] = precedence[LB] = precedence[LP] = precedence[INC] = precedence[DEC] = 15;
        precedence[NEW] = 16;
        precedence[HOOK_DOT] = precedence[DOT] = 17;
    }

    protected Parser(Reader r, String sourceName, int line) throws IOException {
        super(r, sourceName, line);
    }

    public abstract Object string(String s);

    public abstract Object integer(int i);

    public abstract Object number(Number n);

    public abstract Object bool(boolean b);

    public abstract int toInt(Object o);

    public static class ScopeEntry {

        public Object slot;

        final boolean isConst;

        public ScopeEntry(Object slot, boolean isConst) {
            this.slot = slot;
            this.isConst = isConst;
        }
    }

    Basket.Array scopeStack = new Basket.Array();

    public static class ScopeInfo {

        int base;

        int end;

        int newScopeInsn;

        public Map mapping = new HashMap();

        ScopeInfo() {
        }

        public Function jsfunc;

        public int pc;
    }

    Map globalCache = new HashMap();

    ScopeEntry newScopeEntry(int slot, boolean isConst) {
        return new ScopeEntry(integer(slot), isConst);
    }

    Object scopeKey(String name) throws ParserException {
        ScopeEntry r = scopeEntry(name);
        if (r != null) return r.slot;
        return null;
    }

    ScopeEntry scopeEntry(String name) throws ParserException {
        Object glob = globalCache.get(name);
        if (glob != null) return null;
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            ScopeEntry entry = (ScopeEntry) ((ScopeInfo) scopeStack.get(i)).mapping.get(name);
            if (entry != null) return entry;
        }
        globalCache.put(name, Boolean.TRUE);
        return null;
    }

    void scopeDeclare(String name, boolean isConst) throws IOException {
        ScopeInfo si = (ScopeInfo) scopeStack.peek();
        if (si.mapping.get(name) != null) throw pe("'" + name + "' already declared in this scope");
        si.mapping.put(name, newScopeEntry(si.end++, isConst));
        globalCache.put(name, null);
    }

    /** Scope declare for special variables which use negative integers for their scope slot
     * @param scopeSlot - must be a negative integer (otherwise will conflict)*/
    void scopeDeclare(String name, int scopeSlot, boolean isConst) throws IOException {
        ScopeInfo si = (ScopeInfo) scopeStack.peek();
        if (si.mapping.get(name) != null) throw pe("'" + name + "' already declared in this scope");
        si.mapping.put(name, newScopeEntry(scopeSlot, isConst));
        globalCache.put(name, null);
    }

    void scopePush(Function b) {
        ScopeInfo prev = (ScopeInfo) scopeStack.peek();
        ScopeInfo si = new ScopeInfo();
        si.base = prev.end;
        si.end = si.base;
        si.newScopeInsn = b.size;
        scopeStack.push(si);
        b.add(parserLine, NEWSCOPE);
        si.jsfunc = b;
        si.pc = b.size - 1;
    }

    void scopePop(Function b) {
        ScopeInfo si = (ScopeInfo) scopeStack.pop();
        b.add(parserLine, OLDSCOPE);
        b.set(si.newScopeInsn, integer((si.base << 16) | ((si.end - si.base) << 0)));
    }

    Object scopeAccess(Function b, String name) throws IOException {
        ScopeEntry entry = scopeEntry(name);
        Object varKey = null;
        if (entry == null) {
            b.add(parserLine, GLOBALSCOPE);
            b.add(parserLine, LITERAL, string(name));
        } else {
            if (entry.isConst) {
                String varname = name;
                int peek = peekToken();
                switch(peek) {
                    case ASSIGN_BITOR:
                    case ASSIGN_BITXOR:
                    case ASSIGN_BITAND:
                    case ASSIGN_LSH:
                    case ASSIGN_RSH:
                    case ASSIGN_URSH:
                    case ASSIGN_MUL:
                    case ASSIGN_DIV:
                    case ASSIGN_MOD:
                    case ASSIGN_ADD:
                    case ASSIGN_SUB:
                    case INC:
                    case DEC:
                    case ASSIGN:
                        throw pe("const assignment after declaration: " + varname);
                    default:
                }
            }
            varKey = entry.slot;
        }
        return varKey;
    }

    private void outputAssign(Function b, Object varKey) {
        if (varKey == null) {
            b.add(parserLine, PUT);
            b.add(parserLine, SWAP);
            b.add(parserLine, POP);
        } else {
            b.add(parserLine, SCOPEPUT, varKey);
        }
    }

    public Function parseScript() throws IOException {
        Function ret = new Function(sourceName, line);
        scopeStack.clear();
        ScopeInfo si = new ScopeInfo();
        si.jsfunc = ret;
        scopeStack.push(si);
        scopePush(ret);
        scopeDeclare("arguments", true);
        ret.add(line, SCOPEPUT, scopeKey("arguments"));
        while (true) {
            if (peekToken() == -1) break;
            parseStatement(ret, null);
        }
        scopePop(ret);
        if (scopeStack.size() != 1) throw new Error("scopeStack height mismatch");
        ret.add(-1, LITERAL, null);
        ret.add(-1, RETURN);
        return ret;
    }

    /** gets a token and throws an exception if it is not <tt>code</tt> */
    private void consume(int code) throws IOException {
        if (getToken() != code) {
            if (code == NAME) switch(op) {
                case RETURN:
                case TYPEOF:
                case KEYSOF:
                case BREAK:
                case CONTINUE:
                case TRY:
                case THROW:
                case ASSERT:
                case NULL:
                case TRUE:
                case FALSE:
                case IN:
                case IF:
                case ELSE:
                case SWITCH:
                case CASE:
                case DEFAULT:
                case WHILE:
                case VAR:
                case WITH:
                case CATCH:
                case FINALLY:
                    throw pe("Bad variable name; '" + codeToString[op].toLowerCase() + "' is a javascript keyword");
            }
            throw pe("expected " + codeToString[code] + ", got " + (op == -1 ? "EOF" : codeToString[op]));
        }
    }

    private void parseObjectInit(Function b) throws IOException {
        while (true) {
            int tok = getToken();
            if (tok == LP) {
                startExpr(b, -1);
                consume(RP);
            } else {
                if (tok != NAME && tok != STRING) {
                    throw pe("expected NAME, STRING or (");
                }
                b.add(parserLine, LITERAL, string(string));
            }
            consume(COLON);
            startExpr(b, NO_COMMA);
            b.add(parserLine, PUT);
            b.add(parserLine, POP);
            if (peekToken() == RC) break;
            consume(COMMA);
            if (peekToken() == RC) break;
        }
    }

    private int parseArrayInit(Function b) throws IOException {
        int i = 0;
        while (true) {
            b.add(parserLine, LITERAL, integer(i++));
            if (peekToken() == COMMA || peekToken() == RB) b.add(parserLine, LITERAL, null); else startExpr(b, NO_COMMA);
            b.add(parserLine, PUT);
            b.add(parserLine, POP);
            if (peekToken() == RB) break;
            consume(COMMA);
        }
        return i;
    }

    /**
     *  Parse the largest possible expression containing no operators
     *  of precedence below <tt>minPrecedence</tt> and append the
     *  bytecodes for that expression to <tt>appendTo</tt>; the
     *  appended bytecodes MUST grow the stack by exactly one element.
     */
    private void startExpr(Function appendTo, int minPrecedence) throws IOException {
        int saveParserLine = parserLine;
        _startExpr(appendTo, minPrecedence);
        parserLine = saveParserLine;
    }

    private void _startExpr(Function appendTo, int minPrecedence) throws IOException {
        int tok = getToken();
        Function b = appendTo;
        switch(tok) {
            case -1:
                throw pe("expected expression");
            case NUMBER:
                b.add(parserLine, LITERAL, number(number));
                break;
            case STRING:
                b.add(parserLine, LITERAL, string(string));
                break;
            case NULL:
                b.add(parserLine, LITERAL, null);
                break;
            case TRUE:
            case FALSE:
                b.add(parserLine, LITERAL, bool(tok == TRUE));
                break;
            case DOT:
                {
                    consume(NAME);
                    b.add(parserLine, GLOBALSCOPE);
                    b.add(parserLine, GET, string(""));
                    b.add(parserLine, LITERAL, string(string));
                    continueExprAfterAssignable(b, minPrecedence, null);
                    break;
                }
            case LB:
                {
                    b.add(parserLine, ARRAY, integer(0));
                    int size0 = b.size;
                    int i = 0;
                    if (peekToken() != RB) i = parseArrayInit(b);
                    b.set(size0 - 1, integer(i));
                    consume(RB);
                    break;
                }
            case SUB:
            case ADD:
                {
                    if (peekToken() == NUMBER) {
                        consume(NUMBER);
                        Number n = number;
                        if (tok == SUB) n = negateNumber(n);
                        b.add(parserLine, LITERAL, number(n));
                    } else {
                        if (tok == SUB) b.add(parserLine, LITERAL, integer(0));
                        startExpr(b, precedence[BITNOT]);
                        if (tok == ADD) b.add(parserLine, LITERAL, integer(0));
                        b.add(parserLine, SUB);
                    }
                    break;
                }
            case LP:
                {
                    startExpr(b, -1);
                    consume(RP);
                    break;
                }
            case INC:
            case DEC:
                {
                    startExpr(b, precedence[tok]);
                    int prev = b.size - 1;
                    boolean sg = b.get(prev) == SCOPEGET;
                    if (b.get(prev) == GET && b.getArg(prev) != null) b.set(prev, LITERAL, b.getArg(prev)); else if (b.get(prev) == GET) b.pop(); else if (!sg) throw pe("prefixed increment/decrement can only be performed on a valid assignment target");
                    if (!sg) b.add(parserLine, GET_PRESERVE);
                    b.add(parserLine, LITERAL, integer(1));
                    b.add(parserLine, tok == INC ? ADD : SUB, integer(2));
                    Object varKey = sg ? b.getArg(prev) : null;
                    outputAssign(b, varKey);
                    break;
                }
            case BANG:
            case BITNOT:
            case TYPEOF:
                {
                    startExpr(b, precedence[tok]);
                    b.add(parserLine, tok);
                    break;
                }
            case KEYSOF:
                {
                    startExpr(b, precedence[tok]);
                    b.add(parserLine, PUSHKEYS);
                    b.add(parserLine, SWAP);
                    b.add(parserLine, POP);
                    break;
                }
            case LC:
                {
                    b.add(parserLine, OBJECT, null);
                    if (peekToken() != RC) parseObjectInit(b);
                    consume(RC);
                    break;
                }
            case NAME:
                {
                    Object varKey = scopeAccess(b, string);
                    continueExprAfterAssignable(b, minPrecedence, varKey);
                    break;
                }
            case CASCADE:
                {
                    if (peekToken() == ASSIGN) {
                        consume(ASSIGN);
                        startExpr(b, precedence[ASSIGN]);
                        b.add(parserLine, CASCADE, bool(true));
                    } else {
                        b.add(parserLine, CASCADE, bool(false));
                    }
                    break;
                }
            case NEW:
                {
                    startExpr(b, precedence[NEW]);
                    consume(LP);
                    int n = parseArgs(b);
                    b.add(parserLine, NEW, integer(n));
                    break;
                }
            case FUNCTION:
                {
                    consume(LP);
                    int numArgs = 0;
                    Function b2 = new Function(sourceName, parserLine);
                    b.add(parserLine, NEWFUNCTION, b2);
                    scopePush(b2);
                    scopeDeclare("arguments", true);
                    scopeDeclare("callee", SLOT_CALLEE, true);
                    scopeDeclare("this", SLOT_THIS, true);
                    scopeDeclare("trapname", SLOT_TRAPNAME, false);
                    scopeDeclare("trapee", SLOT_TRAPEE, true);
                    b2.add(parserLine, SCOPEPUT, scopeKey("arguments"));
                    while (peekToken() != RP) {
                        numArgs++;
                        if (peekToken() == NAME) {
                            consume(NAME);
                            b2.add(parserLine, DUP);
                            b2.add(parserLine, GET, integer(numArgs - 1));
                            scopeDeclare(string, false);
                            b2.add(parserLine, SCOPEPUT, scopeKey(string));
                            b2.add(parserLine, POP);
                        }
                        if (peekToken() == RP) break;
                        consume(COMMA);
                    }
                    consume(RP);
                    b2.formalArgs = new String[numArgs];
                    b2.add(parserLine, POP);
                    if (peekToken() != LC) throw pe("JSFunctions must have a block surrounded by curly brackets");
                    parseBlock(b2, null);
                    scopePop(b2);
                    b2.add(parserLine, LITERAL, null);
                    b2.add(parserLine, RETURN);
                    break;
                }
            default:
                throw pe("expected expression, found " + codeToString[tok] + ", which cannot start an expression");
        }
        continueExpr(b, minPrecedence);
    }

    /**
     *  Assuming that a complete assignable (lvalue) has just been
     *  parsed and the object and key are on the stack,
     *  <tt>continueExprAfterAssignable</tt> will attempt to parse an
     *  expression that modifies the assignable.  This method always
     *  decreases the stack depth by exactly one element.
     */
    private void continueExprAfterAssignable(Function b, int minPrecedence, Object varKey) throws IOException {
        int saveParserLine = parserLine;
        _continueExprAfterAssignable(b, minPrecedence, varKey);
        parserLine = saveParserLine;
    }

    private void _continueExprAfterAssignable(Function b, int minPrecedence, Object varKey) throws IOException {
        if (b == null) throw new Error("got null b; this should never happen");
        int tok = getToken();
        if (minPrecedence != -1 && tok != -1 && (precedence[tok] < minPrecedence || (precedence[tok] == minPrecedence && !isRightAssociative[tok]))) tok = -1;
        switch(tok) {
            case ASSIGN_BITOR:
            case ASSIGN_BITXOR:
            case ASSIGN_BITAND:
            case ASSIGN_LSH:
            case ASSIGN_RSH:
            case ASSIGN_URSH:
            case ASSIGN_MUL:
            case ASSIGN_DIV:
            case ASSIGN_MOD:
            case ASSIGN_ADD:
            case ASSIGN_SUB:
            case ADD_TRAP:
            case DEL_TRAP:
                {
                    if (tok != ADD_TRAP && tok != DEL_TRAP) b.add(parserLine, varKey == null ? GET_PRESERVE : SCOPEGET, varKey);
                    startExpr(b, precedence[tok]);
                    if (tok != ADD_TRAP && tok != DEL_TRAP) {
                        b.add(parserLine, tok - 1, tok - 1 == ADD ? integer(2) : null);
                        if (varKey == null) {
                            b.add(parserLine, PUT);
                            b.add(parserLine, SWAP);
                            b.add(parserLine, POP);
                        } else {
                            b.add(parserLine, SCOPEPUT, varKey);
                        }
                    } else {
                        if (varKey != null) throw pe("cannot place traps on local variables");
                        b.add(parserLine, tok);
                    }
                    break;
                }
            case INC:
            case DEC:
                {
                    if (varKey == null) {
                        b.add(parserLine, GET_PRESERVE);
                        b.add(parserLine, LITERAL, integer(1));
                        b.add(parserLine, tok == INC ? ADD : SUB, integer(2));
                        b.add(parserLine, PUT, null);
                        b.add(parserLine, SWAP, null);
                        b.add(parserLine, POP, null);
                        b.add(parserLine, LITERAL, integer(1));
                        b.add(parserLine, tok == INC ? SUB : ADD, integer(2));
                    } else {
                        b.add(parserLine, SCOPEGET, varKey);
                        b.add(parserLine, DUP);
                        b.add(parserLine, LITERAL, integer(1));
                        b.add(parserLine, tok == INC ? ADD : SUB, integer(2));
                        b.add(parserLine, SCOPEPUT, varKey);
                        b.add(parserLine, POP, null);
                    }
                    break;
                }
            case ASSIGN:
                {
                    startExpr(b, precedence[tok]);
                    outputAssign(b, varKey);
                    break;
                }
            case LP:
                {
                    b.add(parserLine, varKey == null ? GET_PRESERVE : SCOPEGET, varKey);
                    int n = parseArgs(b);
                    b.add(parserLine, varKey == null ? CALLMETHOD : CALL, integer(n));
                    break;
                }
            default:
                {
                    pushBackToken();
                    if (varKey != null) b.add(parserLine, SCOPEGET, varKey); else if (b.get(b.size - 1) == LITERAL && b.getArg(b.size - 1) != null) b.set(b.size - 1, GET, b.getArg(b.size - 1)); else b.add(parserLine, GET);
                    return;
                }
        }
    }

    /**
     *  Assuming that a complete expression has just been parsed,
     *  <tt>continueExpr</tt> will attempt to extend this expression by
     *  parsing additional tokens and appending additional bytecodes.
     *
     *  No operators with precedence less than <tt>minPrecedence</tt>
     *  will be parsed.
     *
     *  If any bytecodes are appended, they will not alter the stack
     *  depth.
     */
    private void continueExpr(Function b, int minPrecedence) throws IOException {
        int saveParserLine = parserLine;
        _continueExpr(b, minPrecedence);
        parserLine = saveParserLine;
    }

    private void _continueExpr(Function b, int minPrecedence) throws IOException {
        if (b == null) throw new Error("got null b; this should never happen");
        int tok = getToken();
        if (tok == -1) return;
        if (minPrecedence != -1 && (precedence[tok] < minPrecedence || (precedence[tok] == minPrecedence && !isRightAssociative[tok]))) {
            pushBackToken();
            return;
        }
        switch(tok) {
            case LP:
                {
                    int n = parseArgs(b);
                    b.add(parserLine, CALL, integer(n));
                    break;
                }
            case BITOR:
            case BITXOR:
            case BITAND:
            case SHEQ:
            case SHNE:
            case LSH:
            case RSH:
            case URSH:
            case MUL:
            case DIV:
            case MOD:
            case GT:
            case GE:
            case EQ:
            case NE:
            case LT:
            case LE:
            case SUB:
                {
                    startExpr(b, precedence[tok]);
                    b.add(parserLine, tok);
                    break;
                }
            case ADD:
                {
                    int count = 1;
                    int nextTok;
                    do {
                        startExpr(b, precedence[tok]);
                        count++;
                        nextTok = getToken();
                    } while (nextTok == tok);
                    pushBackToken();
                    b.add(parserLine, tok, integer(count));
                    break;
                }
            case OR:
            case AND:
                {
                    b.add(parserLine, tok == AND ? Function.JF : Function.JT, integer(0));
                    int size = b.size;
                    startExpr(b, precedence[tok]);
                    b.add(parserLine, JMP, integer(2));
                    b.add(parserLine, LITERAL, bool(tok != AND));
                    b.set(size - 1, integer(b.size - size));
                    break;
                }
            case HOOK_LB:
            case HOOK_DOT:
                {
                    b.add(parserLine, DUP);
                    b.add(parserLine, LITERAL, null);
                    b.add(parserLine, SHNE);
                    b.add(parserLine, JT, integer(0));
                    int jump1 = b.size;
                    b.add(parserLine, POP);
                    b.add(parserLine, LITERAL, null);
                    b.add(parserLine, JMP, integer(0));
                    b.set(jump1 - 1, integer(b.size - jump1 + 1));
                    int jump2 = b.size;
                    if (tok == HOOK_DOT) {
                        consume(NAME);
                        b.add(parserLine, LITERAL, string(string));
                    } else {
                        startExpr(b, -1);
                        consume(RB);
                    }
                    continueExprAfterAssignable(b, minPrecedence, null);
                    b.set(jump2 - 1, integer(b.size - jump2 + 1));
                    break;
                }
            case DOT:
                {
                    if (peekToken() == DOT) {
                        string = "";
                    } else {
                        consume(NAME);
                    }
                    b.add(parserLine, LITERAL, string(string));
                    continueExprAfterAssignable(b, minPrecedence, null);
                    break;
                }
            case LB:
                {
                    startExpr(b, -1);
                    consume(RB);
                    continueExprAfterAssignable(b, minPrecedence, null);
                    break;
                }
            case HOOK:
                {
                    b.add(parserLine, JF, integer(0));
                    int size = b.size;
                    startExpr(b, minPrecedence);
                    b.add(parserLine, JMP, integer(0));
                    b.set(size - 1, integer(b.size - size + 1));
                    consume(COLON);
                    size = b.size;
                    startExpr(b, minPrecedence);
                    b.set(size - 1, integer(b.size - size + 1));
                    break;
                }
            case HOOK_COLON:
                {
                    b.add(parserLine, DUP);
                    b.add(parserLine, JT, integer(0));
                    int size = b.size;
                    startExpr(b, minPrecedence);
                    b.add(parserLine, SWAP);
                    b.add(parserLine, POP);
                    b.set(size - 1, integer(b.size - size + 1));
                    break;
                }
            case COMMA:
                {
                    b.add(parserLine, POP);
                    startExpr(b, -1);
                    break;
                }
            case INSTANCEOF:
                {
                    startExpr(b, precedence[INSTANCEOF]);
                    b.add(parserLine, INSTANCEOF);
                    break;
                }
            default:
                {
                    pushBackToken();
                    return;
                }
        }
        continueExpr(b, minPrecedence);
    }

    private int parseArgs(Function b) throws IOException {
        int i = 0;
        while (peekToken() != RP) {
            i++;
            if (peekToken() != COMMA) {
                startExpr(b, NO_COMMA);
                if (peekToken() == RP) break;
            }
            consume(COMMA);
        }
        consume(RP);
        return i;
    }

    /** Parse a block of statements which must be surrounded by LC..RC. */
    void parseBlock(Function b, String label) throws IOException {
        int saveParserLine = parserLine;
        _parseBlock(b, label);
        parserLine = saveParserLine;
    }

    void _parseBlock(Function b, String label) throws IOException {
        if (peekToken() == -1) return; else if (peekToken() != LC) parseStatement(b, null); else {
            consume(LC);
            while (peekToken() != RC && peekToken() != -1) parseStatement(b, null);
            consume(RC);
        }
    }

    /** Parse a single statement, consuming the RC or SEMI which terminates it. */
    void parseStatement(Function b, String label) throws IOException {
        int saveParserLine = parserLine;
        _parseStatement(b, label);
        parserLine = saveParserLine;
    }

    void _parseStatement(Function b, String label) throws IOException {
        int tok = peekToken();
        if (tok == -1) return;
        switch(tok = getToken()) {
            case THROW:
            case ASSERT:
            case RETURN:
                {
                    if (tok == RETURN && peekToken() == SEMI) b.add(parserLine, LITERAL, null); else startExpr(b, -1);
                    b.add(parserLine, tok);
                    consume(SEMI);
                    break;
                }
            case BREAK:
            case CONTINUE:
                {
                    if (peekToken() == NAME) consume(NAME);
                    b.add(parserLine, tok, string);
                    consume(SEMI);
                    break;
                }
            case CONST:
            case VAR:
                {
                    while (true) {
                        consume(NAME);
                        String var = string;
                        scopeDeclare(var, tok == CONST);
                        if (peekToken() == ASSIGN) {
                            consume(ASSIGN);
                            startExpr(b, NO_COMMA);
                            b.add(parserLine, SCOPEPUT, scopeKey(var));
                            b.add(parserLine, POP);
                        } else if (tok == CONST) {
                            throw pe("const not assigned to");
                        }
                        if (peekToken() != COMMA) break;
                        consume(COMMA);
                    }
                    if ((mostRecentlyReadToken != RC || peekToken() == SEMI) && peekToken() != -1 && mostRecentlyReadToken != SEMI) consume(SEMI);
                    break;
                }
            case IF:
                {
                    consume(LP);
                    startExpr(b, -1);
                    consume(RP);
                    b.add(parserLine, JF, integer(0));
                    int size = b.size;
                    parseStatement(b, null);
                    if (peekToken() == ELSE) {
                        consume(ELSE);
                        b.add(parserLine, JMP, integer(0));
                        b.set(size - 1, integer(b.size - size + 1));
                        size = b.size;
                        parseStatement(b, null);
                    }
                    b.set(size - 1, integer(b.size - size + 1));
                    break;
                }
            case WHILE:
                {
                    consume(LP);
                    if (label != null) b.add(parserLine, LABEL, label);
                    b.add(parserLine, LOOP);
                    int size = b.size;
                    b.add(parserLine, POP);
                    startExpr(b, -1);
                    b.add(parserLine, JT, integer(2));
                    b.add(parserLine, BREAK);
                    consume(RP);
                    parseStatement(b, null);
                    b.add(parserLine, CONTINUE);
                    b.set(size - 1, integer(b.size - size + 1));
                    break;
                }
            case SWITCH:
                {
                    consume(LP);
                    if (label != null) b.add(parserLine, LABEL, label);
                    int switchPC = b.size;
                    b.add(parserLine, SWITCH);
                    startExpr(b, -1);
                    consume(RP);
                    consume(LC);
                    Function b2 = new Function(sourceName, parserLine);
                    Vec jumps = new Vec();
                    OUTER: for (; ; ) {
                        tok = getToken();
                        switch(tok) {
                            case CASE:
                                b.add(parserLine, DUP);
                                startExpr(b, -1);
                                consume(COLON);
                                b.add(parserLine, EQ);
                                jumps.addElement(integer(b.size));
                                b.add(parserLine, JT, integer(b2.size));
                                while (peekToken() != CASE && peekToken() != DEFAULT && peekToken() != RC) parseStatement(b2, null);
                                break;
                            case DEFAULT:
                                consume(COLON);
                                jumps.addElement(integer(b.size));
                                b.add(parserLine, JMP, integer(b2.size));
                                while (peekToken() != RC) parseStatement(b2, null);
                                consume(RC);
                                break OUTER;
                            case RC:
                                b.add(parserLine, BREAK);
                                break OUTER;
                            default:
                                throw pe("expected CASE, DEFAULT, or RC; got " + codeToString[peekToken()]);
                        }
                    }
                    b.add(parserLine, POP);
                    int testEnd = b.size;
                    b.paste(b2);
                    b.add(parserLine, BREAK);
                    b.set(switchPC, integer(b.size - switchPC));
                    Object n;
                    while ((n = jumps.pop()) != null) {
                        int i = toInt(n);
                        int offset = toInt(b.getArg(i));
                        b.set(i, integer(testEnd - i + offset));
                    }
                    break;
                }
            case DO:
                {
                    if (label != null) b.add(parserLine, LABEL, label);
                    b.add(parserLine, LOOP);
                    int size = b.size;
                    parseStatement(b, null);
                    consume(WHILE);
                    consume(LP);
                    startExpr(b, -1);
                    b.add(parserLine, JT, integer(2));
                    b.add(parserLine, BREAK);
                    b.add(parserLine, CONTINUE);
                    consume(RP);
                    consume(SEMI);
                    b.set(size - 1, integer(b.size - size + 1));
                    break;
                }
            case TRY:
                {
                    b.add(parserLine, TRY);
                    int tryInsn = b.size - 1;
                    parseStatement(b, null);
                    b.add(parserLine, POP);
                    b.add(parserLine, JMP);
                    int successJMPInsn = b.size - 1;
                    if (peekToken() != CATCH && peekToken() != FINALLY) throw pe("try without catch or finally");
                    int catchJMPDistance = -1;
                    if (peekToken() == CATCH) {
                        Basket.List catchEnds = new Basket.Array();
                        boolean catchAll = false;
                        catchJMPDistance = b.size - tryInsn;
                        while (peekToken() == CATCH && !catchAll) {
                            String exceptionVar;
                            getToken();
                            consume(LP);
                            consume(NAME);
                            exceptionVar = string;
                            int[] writebacks = new int[] { -1, -1, -1 };
                            if (peekToken() != RP) {
                                consume(NAME);
                                b.add(parserLine, DUP);
                                b.add(parserLine, LITERAL, string(string));
                                b.add(parserLine, GET);
                                b.add(parserLine, DUP);
                                b.add(parserLine, LITERAL, null);
                                b.add(parserLine, EQ);
                                b.add(parserLine, JT);
                                writebacks[0] = b.size - 1;
                                if (peekToken() == STRING) {
                                    consume(STRING);
                                    b.add(parserLine, DUP);
                                    b.add(parserLine, LITERAL, string);
                                    b.add(parserLine, LT);
                                    b.add(parserLine, JT);
                                    writebacks[1] = b.size - 1;
                                    b.add(parserLine, DUP);
                                    b.add(parserLine, LITERAL, string + "/");
                                    b.add(parserLine, GE);
                                    b.add(parserLine, JT);
                                    writebacks[2] = b.size - 1;
                                } else {
                                    consume(NUMBER);
                                    b.add(parserLine, DUP);
                                    b.add(parserLine, LITERAL, number);
                                    b.add(parserLine, EQ);
                                    b.add(parserLine, JF);
                                    writebacks[1] = b.size - 1;
                                }
                                b.add(parserLine, POP);
                            } else {
                                catchAll = true;
                            }
                            consume(RP);
                            scopePush(b);
                            scopeDeclare(exceptionVar, false);
                            b.add(parserLine, SCOPEPUT, scopeKey(exceptionVar));
                            b.add(parserLine, POP);
                            parseBlock(b, null);
                            scopePop(b);
                            b.add(parserLine, JMP);
                            catchEnds.add(new Integer(b.size - 1));
                            for (int i = 0; i < 3; i++) if (writebacks[i] != -1) b.set(writebacks[i], integer(b.size - writebacks[i]));
                            b.add(parserLine, POP);
                        }
                        if (!catchAll) b.add(parserLine, THROW);
                        for (int i = 0; i < catchEnds.size(); i++) {
                            int n = ((Integer) catchEnds.get(i)).intValue();
                            b.set(n, integer(b.size - n));
                        }
                        b.add(parserLine, POP);
                        b.add(parserLine, POP);
                    }
                    b.set(successJMPInsn, integer(b.size - successJMPInsn));
                    int finallyJMPDistance = -1;
                    if (peekToken() == FINALLY) {
                        b.add(parserLine, LITERAL, null);
                        finallyJMPDistance = b.size - tryInsn;
                        consume(FINALLY);
                        parseStatement(b, null);
                        b.add(parserLine, FINALLY_DONE);
                    }
                    b.set(tryInsn, new int[] { catchJMPDistance, finallyJMPDistance });
                    break;
                }
            case FOR:
                {
                    consume(LP);
                    boolean hadVar = false;
                    String name1 = null;
                    String name2 = null;
                    if (peekToken() == VAR) {
                        consume(VAR);
                        hadVar = true;
                    }
                    if (peekToken() == NAME) {
                        consume(NAME);
                        name1 = string;
                    }
                    if (peekToken() == COMMA) {
                        consume(COMMA);
                        consume(NAME);
                        name2 = string;
                    }
                    if (peekToken() == IN) {
                        consume(IN);
                        startExpr(b, -1);
                        consume(RP);
                        b.add(parserLine, PUSHKEYS);
                        b.add(parserLine, LITERAL, string("iterator"));
                        b.add(parserLine, GET_PRESERVE);
                        b.add(parserLine, CALLMETHOD, integer(0));
                        int size = b.size;
                        b.add(parserLine, LOOP);
                        b.add(parserLine, POP);
                        b.add(parserLine, SWAP);
                        b.add(parserLine, DUP);
                        b.add(parserLine, GET, string("hasNext"));
                        int size2 = b.size;
                        b.add(parserLine, JT);
                        b.add(parserLine, SWAP);
                        b.add(parserLine, BREAK);
                        b.set(size2, integer(b.size - size2));
                        b.add(parserLine, DUP);
                        b.add(parserLine, GET, string("next"));
                        scopePush(b);
                        if (hadVar) scopeDeclare(name1, false);
                        Object varKey1 = scopeKey(name1);
                        if (varKey1 == null) {
                            b.add(parserLine, GLOBALSCOPE);
                            b.add(parserLine, SWAP);
                            b.add(parserLine, LITERAL, string(name1));
                            b.add(parserLine, SWAP);
                            b.add(parserLine, PUT);
                            b.add(parserLine, POP);
                        } else {
                            b.add(parserLine, SCOPEPUT, varKey1);
                        }
                        if (name2 != null) {
                            b.add(parserLine, DUP, integer(3));
                            b.add(parserLine, SWAP);
                            b.add(parserLine, GET);
                            if (hadVar) scopeDeclare(name2, false);
                            Object varKey2 = scopeKey(name2);
                            if (varKey2 == null) {
                                b.add(parserLine, GLOBALSCOPE);
                                b.add(parserLine, SWAP);
                                b.add(parserLine, LITERAL, string(name2));
                                b.add(parserLine, SWAP);
                                b.add(parserLine, PUT);
                                b.add(parserLine, POP);
                            } else {
                                b.add(parserLine, SCOPEPUT, varKey2);
                            }
                        }
                        b.add(parserLine, POP);
                        b.add(parserLine, SWAP);
                        parseStatement(b, null);
                        scopePop(b);
                        b.add(parserLine, CONTINUE);
                        b.set(size, integer(b.size - size));
                        b.add(parserLine, POP);
                    } else {
                        if (name2 != null) {
                            pushBackToken(NAME, name2);
                            pushBackToken(COMMA, null);
                        }
                        if (name1 != null) {
                            pushBackToken(NAME, name1);
                        }
                        if (hadVar) {
                            pushBackToken(VAR, null);
                        }
                        scopePush(b);
                        parseStatement(b, null);
                        Function e2 = new Function(sourceName, parserLine);
                        if (peekToken() != SEMI) startExpr(e2, -1); else e2.add(parserLine, Function.LITERAL, bool(true));
                        consume(SEMI);
                        if (label != null) b.add(parserLine, LABEL, label);
                        b.add(parserLine, LOOP);
                        int size2 = b.size;
                        b.add(parserLine, JT, integer(0));
                        int size = b.size;
                        if (peekToken() != RP) {
                            startExpr(b, -1);
                            b.add(parserLine, POP);
                        }
                        b.set(size - 1, integer(b.size - size + 1));
                        consume(RP);
                        b.paste(e2);
                        b.add(parserLine, JT, integer(2));
                        b.add(parserLine, BREAK);
                        parseStatement(b, null);
                        b.add(parserLine, CONTINUE);
                        b.set(size2 - 1, integer(b.size - size2 + 1));
                        scopePop(b);
                    }
                    break;
                }
            case NAME:
                {
                    String possiblyTheLabel = string;
                    if (peekToken() == COLON) {
                        consume(COLON);
                        parseStatement(b, possiblyTheLabel);
                        break;
                    } else {
                        pushBackToken(NAME, possiblyTheLabel);
                        startExpr(b, -1);
                        b.add(parserLine, POP);
                        if ((mostRecentlyReadToken != RC || peekToken() == SEMI) && peekToken() != -1 && mostRecentlyReadToken != SEMI) consume(SEMI);
                        break;
                    }
                }
            case SEMI:
                return;
            case LC:
                {
                    pushBackToken();
                    scopePush(b);
                    parseBlock(b, label);
                    scopePop(b);
                    break;
                }
            case LB:
                {
                    final Basket.Array list = new Basket.Array();
                    consume(NAME);
                    list.push(string);
                    while (true) {
                        if (peekToken() == RB) break;
                        consume(COMMA);
                        consume(NAME);
                        list.push(string);
                    }
                    consume(RB);
                    consume(ASSIGN);
                    startExpr(b, NO_COMMA);
                    for (int i = list.size() - 1; i >= 0; i--) {
                        b.add(parserLine, GET_PRESERVE, integer(i));
                        b.add(parserLine, SWAP);
                    }
                    b.add(parserLine, POP);
                    for (int i = 0; i < list.size(); i++) {
                        String varname = (String) list.get(i);
                        Object varKey = scopeAccess(b, varname);
                        outputAssign(b, varKey);
                        b.add(parserLine, POP);
                    }
                    break;
                }
            default:
                {
                    pushBackToken();
                    startExpr(b, -1);
                    b.add(parserLine, POP);
                    if ((mostRecentlyReadToken != RC || peekToken() == SEMI) && peekToken() != -1 && mostRecentlyReadToken != SEMI) consume(SEMI);
                    break;
                }
        }
    }

    private IOException pe(String s) {
        return new ParserException(s, sourceName, line, col);
    }

    static class ParserException extends SourceException {

        public ParserException(String message, String sourceName, int line, int col) {
            super(message, sourceName, line, col);
        }

        public String getMessageSig() {
            return "[Parsing Error]";
        }
    }
}
