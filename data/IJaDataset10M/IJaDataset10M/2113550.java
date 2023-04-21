package org.hl7.hpath;

class Parenthesis<R, T> extends Expression<R, T> {

    public static final Language.ExpressionForm FORM = new Language.ExpressionForm() {

        public Expression parse(StaticContext staticContext) throws SyntaxError {
            return Parenthesis.parse(staticContext);
        }
    };

    public static Expression parse(StaticContext staticContext) throws SyntaxError {
        Token token = staticContext.tokenizer().peek();
        switch(token.type()) {
            case LPAR:
                staticContext.tokenizer().next();
                Expression previousExpression = staticContext.expression();
                if (previousExpression == null) {
                    staticContext.expression(null);
                    Expression parentheticalExpression = Expression.parse(staticContext);
                    staticContext.tokenizer().next(TokenType.RPAR);
                    return parentheticalExpression;
                } else throw new SyntaxError("parenthesis in context in " + staticContext.expressionString() + " at '" + token.string() + "'");
            default:
                return null;
        }
    }

    EvaluationIterator<R, T> createEvaluationIterator(Evaluation<R, ?> evaluation, EvaluationIterator<R, R> sourceIterator) {
        throw new IllegalStateException("should never be here");
    }
}
