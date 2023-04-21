package com.ibm.tuningfork.infra.stream.expression.syntax;

import com.ibm.tuningfork.infra.stream.expression.base.Expression;
import com.ibm.tuningfork.infra.stream.expression.base.StreamExpression;
import com.ibm.tuningfork.infra.stream.expression.operations.NextEventReference;
import com.ibm.tuningfork.infra.stream.expression.syntax.Ast.IExpression;
import com.ibm.tuningfork.infra.stream.expression.syntax.Ast.NextEvent;

/**
 * Translate a NextEvent from AST form to a NextEventReference operation
 */
class NextEventTranslator extends EventTranslator {

    Expression translate(IExpression from, ForkTalkExpressionParser parser) {
        NextEvent astItem = (NextEvent) from;
        StreamExpression stream = resolveStream(parser, astItem.getExpression());
        return new NextEventReference(stream);
    }
}
