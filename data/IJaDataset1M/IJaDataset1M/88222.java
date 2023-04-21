package org.xteam.cs.runtime;

import java.io.IOException;
import java.util.Stack;

public class LRParser extends BaseParser {

    private static class Symbol {

        public int parseState;

        public Object object;

        public Symbol(int state, Object obj) {
            this.parseState = state;
            this.object = obj;
        }

        public Symbol(int state) {
            this(state, null);
        }

        public String toString() {
            return "Symbol(" + parseState + ", " + object + ")";
        }
    }

    protected Stack<Symbol> stack = new Stack<Symbol>();

    protected IToken curToken;

    private boolean doneParsing;

    private int startState;

    private int acceptingRule;

    protected ILexer lexer;

    private IRuleReducer ruleReducer;

    private IErrorReporter reporter;

    private ISyntaxHelper helper;

    private IToken lastToken;

    public LRParser(IParseTables tables, ILexer lexer, IRuleReducer ruleReducer, ISyntaxHelper helper, IErrorReporter reporter) {
        super(tables);
        this.lexer = lexer;
        this.startState = tables.startState();
        this.acceptingRule = tables.acceptingProduction();
        this.ruleReducer = ruleReducer;
        this.reporter = reporter;
        this.helper = helper;
    }

    protected void doneParsing() {
        doneParsing = true;
    }

    public Object parse() throws IOException {
        stack.clear();
        stack.push(new Symbol(startState));
        curToken = lexer.nextToken();
        doneParsing = false;
        while (!doneParsing) {
            short act = getAction(stack.peek().parseState, curToken.type());
            if (isShift(act)) {
                stack.push(new Symbol(toShift(act), curToken));
                lastToken = curToken;
                curToken = lexer.nextToken();
                if (curToken == null) throw new IOException("error while scanning");
            } else if (isReduce(act)) {
                act = toReduce(act);
                short lhs_sym_num = productionTab[act][0];
                short handle_size = productionTab[act][1];
                Object[] values = new Object[handle_size];
                for (int i = 0; i < handle_size; i++) {
                    values[handle_size - i - 1] = stack.pop().object;
                }
                if (act == acceptingRule) {
                    doneParsing();
                    return values[0];
                } else {
                    Object semValue = ruleReducer.reduce(act, values);
                    act = getReduce(((Symbol) stack.peek()).parseState, lhs_sym_num);
                    stack.push(new Symbol(act, semValue));
                }
            } else if (act == 0) {
                if (!errorRecovery()) {
                    syntaxError(curToken);
                }
            }
        }
        return null;
    }

    private boolean errorRecovery() {
        return false;
    }

    private void panicMode() throws IOException {
        Stack<Symbol> saveStack = stack;
        stack = (Stack<Symbol>) stack.clone();
        int act;
        if (stack.size() > 1) {
            do {
                stack.pop();
                act = getAction(((Symbol) stack.peek()).parseState, curToken.type());
            } while (act == 0 && stack.size() > 1);
        }
        if (stack.size() == 1) {
            System.err.println("discard " + curToken);
            stack = saveStack;
            curToken = lexer.nextToken();
        } else {
            for (int i = saveStack.size() - 1; i >= stack.size(); --i) System.err.println("discard " + ((Symbol) saveStack.get(i)).object);
        }
    }

    private void syntaxError(IToken token) throws IOException {
        IToken t = token;
        StringBuffer buffer = new StringBuffer();
        if (t.length() == 0 && lastToken != null) {
            t = lastToken;
        } else {
            buffer.append("Bad token, ");
        }
        if (helper != null) {
            buffer.append("Expecting token ");
            short[] row = actionTab[stack.peek().parseState];
            for (int i = 0; i < row.length; i += 2) {
                if (i > 0) {
                    if (i < row.length - 2) buffer.append(", "); else {
                        buffer.append(" or ");
                    }
                }
                buffer.append(helper.getTokenString(row[i]));
            }
        } else {
            buffer.append("Syntax Error");
        }
        reporter.reportError(IErrorReporter.ERROR, new Span(t.start(), t.length()), buffer.toString());
        throw new IOException("Parse Error : " + buffer.toString());
    }
}
