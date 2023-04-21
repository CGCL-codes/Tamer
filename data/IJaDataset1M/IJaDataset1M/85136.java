package org.bbop.expression;

/**
 *  A Resolver allows custom resolution of the expression, and can be
 *  added in front of the jexl engine, or after in the evaluation.
 *
 *  @todo This needs to be explained in detail.  Why do this?
 *  @since 1.0
 *  @author <a href="mailto:geirm@adeptra.com">Geir Magnusson Jr.</a>
 *  @version $Id: JexlExprResolver.java,v 1.2 2007/09/27 01:02:12 jmr39 Exp $
 */
public interface JexlExprResolver {

    /** represents an expression result of no value. */
    Object NO_VALUE = new Object();

    /**
     *  Evaluates an expression against the context.
     *
     *  @todo Must detail the expectations and effects of this resolver.
     *  @param context current data context
     *  @param expression expression to evauluate
     *  @return value (may be null) or the NO_VALUE object to
     *       indicate no resolution.
     */
    Object evaluate(JexlContext context, String expression);
}
