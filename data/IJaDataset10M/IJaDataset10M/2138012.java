package org.zkoss.zkmax.xel.el;

import org.zkoss.xel.Expression;
import org.zkoss.xel.XelContext;
import org.zkoss.xel.FunctionMapper;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;
import javax.servlet.jsp.el.ELException;

/**
 * A XEL expression that is based on a EL expression.
 *
 * @author tomyeh
 * @since 3.0.0
 */
public class ELXelExpression implements Expression {

    private final javax.servlet.jsp.el.Expression _expr;

    private final String _rawexpr;

    private final FunctionMapper _mapper;

    private final Class _expected;

    /**
	 * @param expr the expression. It cannot be null.
	 */
    public ELXelExpression(javax.servlet.jsp.el.Expression expr, String rawexpr, FunctionMapper mapper, Class expectedType) {
        if (expr == null) throw new IllegalArgumentException();
        _rawexpr = rawexpr;
        _expr = expr;
        _mapper = mapper;
        _expected = expectedType;
    }

    public Object evaluate(XelContext ctx) {
        try {
            final FunctionMapper nfm = ctx.getFunctionMapper();
            if (nfm != null && _mapper != nfm) return new ApacheELFactory().evaluate(ctx, _rawexpr, _expected);
            final VariableResolver resolver = ctx.getVariableResolver();
            return _expr.evaluate(resolver != null ? new XelELResolver(resolver) : null);
        } catch (ELException ex) {
            throw new XelException(ex);
        }
    }

    public boolean equals(Object o) {
        return o instanceof ELXelExpression && ((ELXelExpression) o)._expr.equals(_expr);
    }

    public int hashCode() {
        return _expr.hashCode();
    }

    public String toString() {
        return _expr.toString();
    }
}
