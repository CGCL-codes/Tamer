package org.armedbear.j;

public class Expression {

    protected final String name;

    protected final int arity;

    public Expression(String name) {
        this(name, -1);
    }

    public Expression(String name, int arity) {
        this.name = name;
        this.arity = arity;
    }

    public final String getName() {
        return name;
    }

    public final int getArity() {
        return arity;
    }

    public boolean matches(LocalTag tag) {
        if (!name.equals(tag.getMethodName())) return false;
        if (arity >= 0) {
            int n = getArity(tag.getCanonicalSignature());
            if (n < 0 || n == arity) return true; else return false;
        }
        return true;
    }

    public static int getArity(String s) {
        if (s == null) return -1;
        int start = -1;
        int parenCount = 0;
        int arity = 0;
        char quoteChar = '\0';
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (start < 0) {
                if (c == '(') start = i + 1;
                continue;
            }
            if (inQuote) {
                if (c == quoteChar) inQuote = false;
                continue;
            }
            if (c == '"' || c == '\'') {
                inQuote = true;
                quoteChar = c;
                continue;
            }
            if (c == ',') {
                if (parenCount == 0) ++arity;
                continue;
            }
            if (c == '(') {
                ++parenCount;
                continue;
            }
            if (c == ')') {
                --parenCount;
                if (parenCount < 0) {
                    if (arity == 0) {
                        String enclosed = s.substring(start, i);
                        boolean isBlank = true;
                        for (int j = 0; j < enclosed.length(); j++) {
                            if (!Character.isWhitespace(enclosed.charAt(j))) {
                                isBlank = false;
                                break;
                            }
                        }
                        if (!isBlank) arity = 1;
                    } else ++arity;
                    return arity;
                }
                continue;
            }
        }
        return -1;
    }
}
