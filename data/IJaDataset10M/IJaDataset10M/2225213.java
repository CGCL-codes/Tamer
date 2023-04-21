package ctagsinterface.context;

import org.gjt.sp.jedit.Buffer;
import ctagsinterface.main.Tag;

public abstract class AbstractContextFinder {

    protected Buffer buffer;

    protected int pos;

    protected int line;

    AbstractContextFinder(Buffer buffer, int pos) {
        this.buffer = buffer;
        this.pos = pos;
        line = buffer.getLineOfOffset(pos);
    }

    public abstract String findExpressionBeforePos();

    public abstract String findExpressionAtPos();

    public abstract Object[] parseExpression(String expression);

    public abstract Object findFirstContext(Object firstExpressionPart);

    public abstract Object findNextContext(Object prevContext, Object expressionPart);

    public abstract Tag getCurrentContext();

    public abstract Tag getContextTag(Object context);

    private Tag resolveExpression(String expr) {
        if (expr == null) return null;
        Object[] parts = parseExpression(expr);
        if (parts == null) return null;
        Object current = findFirstContext(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            current = findNextContext(current, parts[i]);
            if (current == null) return null;
        }
        return getContextTag(current);
    }

    public Tag getContext() {
        String expr = findExpressionBeforePos();
        if (expr == null) return getCurrentContext();
        return resolveExpression(expr);
    }

    public Tag getTag() {
        String expr = findExpressionAtPos();
        if (expr == null) return null;
        return resolveExpression(expr);
    }
}
