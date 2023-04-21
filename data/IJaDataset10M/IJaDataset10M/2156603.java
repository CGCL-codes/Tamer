package de.matthiasmann.twl.utils;

import de.matthiasmann.twl.renderer.AnimationState;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * A class to handle animation state expression
 * 
 * @author Matthias Mann
 */
public abstract class StateExpression {

    public abstract boolean evaluate(AnimationState as);

    public static StateExpression parse(String exp, boolean negate) throws ParseException {
        StringIterator si = new StringIterator(exp);
        StateExpression expr = parse(si);
        if (si.hasMore()) {
            si.unexpected();
        }
        expr.negate ^= negate;
        return expr;
    }

    private static StateExpression parse(StringIterator si) throws ParseException {
        ArrayList<StateExpression> children = new ArrayList<StateExpression>();
        char kind = ' ';
        for (; ; ) {
            if (!si.skipSpaces()) {
                si.unexpected();
            }
            char ch = si.peek();
            boolean negate = ch == '!';
            if (negate) {
                si.pos++;
                if (!si.skipSpaces()) {
                    si.unexpected();
                }
                ch = si.peek();
            }
            StateExpression child = null;
            if (Character.isJavaIdentifierStart(ch)) {
                child = new Check(si.getIdent());
            } else if (ch == '(') {
                si.pos++;
                child = parse(si);
                si.expect(')');
            } else if (ch == ')') {
                break;
            } else {
                si.unexpected();
            }
            child.negate = negate;
            children.add(child);
            if (!si.skipSpaces()) {
                break;
            }
            ch = si.peek();
            if ("|+^".indexOf(ch) < 0) {
                break;
            }
            if (children.size() == 1) {
                kind = ch;
            } else if (kind != ch) {
                si.expect(kind);
            }
            si.pos++;
        }
        if (children.isEmpty()) {
            si.unexpected();
        }
        assert kind != ' ' || children.size() == 1;
        if (children.size() == 1) {
            return children.get(0);
        }
        StateExpression[] childArray = children.toArray(new StateExpression[children.size()]);
        if (kind == '^') {
            return new Xor(childArray);
        } else {
            return new AndOr(kind, childArray);
        }
    }

    static class StringIterator {

        final String str;

        int pos;

        StringIterator(String str) {
            this.str = str;
        }

        boolean hasMore() {
            return pos < str.length();
        }

        char peek() {
            return str.charAt(pos);
        }

        void expect(char what) throws ParseException {
            if (!hasMore() || peek() != what) {
                throw new ParseException("Expected '" + what + "' got " + describePosition(), pos);
            }
            pos++;
        }

        void unexpected() throws ParseException {
            throw new ParseException("Unexpected " + describePosition(), pos);
        }

        String describePosition() {
            if (pos >= str.length()) {
                return "Unexpected end of expression";
            }
            return "'" + peek() + "' at " + (pos + 1);
        }

        boolean skipSpaces() {
            while (hasMore() && Character.isWhitespace(peek())) {
                pos++;
            }
            return hasMore();
        }

        String getIdent() {
            int start = pos;
            while (hasMore() && Character.isJavaIdentifierPart(peek())) {
                pos++;
            }
            return str.substring(start, pos).intern();
        }
    }

    protected boolean negate;

    static class AndOr extends StateExpression {

        private final StateExpression[] children;

        private final boolean kind;

        public AndOr(char kind, StateExpression... children) {
            assert kind == '|' || kind == '+';
            this.children = children;
            this.kind = kind == '|';
        }

        @Override
        public boolean evaluate(AnimationState as) {
            for (StateExpression e : children) {
                if (kind == e.evaluate(as)) {
                    return kind ^ negate;
                }
            }
            return !kind ^ negate;
        }
    }

    static class Xor extends StateExpression {

        private final StateExpression[] children;

        public Xor(StateExpression... children) {
            this.children = children;
        }

        @Override
        public boolean evaluate(AnimationState as) {
            boolean result = negate;
            for (StateExpression e : children) {
                result ^= e.evaluate(as);
            }
            return result;
        }
    }

    static class Check extends StateExpression {

        private final String state;

        public Check(String state) {
            this.state = state;
        }

        @Override
        public boolean evaluate(AnimationState as) {
            return negate ^ (as != null && as.getAnimationState(state));
        }
    }
}
