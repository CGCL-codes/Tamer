package org.sodeja.il;

import static org.sodeja.parsec.combinator.ParsecCombinators.*;
import static org.sodeja.parsec.standart.StandartParsers.*;
import java.util.List;
import org.sodeja.collections.ConsList;
import org.sodeja.functional.Function1;
import org.sodeja.il.model.ApplyExpression;
import org.sodeja.il.model.BlockExpression;
import org.sodeja.il.model.ClassExpression;
import org.sodeja.il.model.Expression;
import org.sodeja.il.model.IfExpression;
import org.sodeja.il.model.ImportExpression;
import org.sodeja.il.model.LambdaExpression;
import org.sodeja.il.model.PrecedenceExpression;
import org.sodeja.il.model.SetExpression;
import org.sodeja.il.model.ValueExpression;
import org.sodeja.il.model.VariableExpression;
import org.sodeja.parsec.AbstractParser;
import org.sodeja.parsec.ParseError;
import org.sodeja.parsec.Parser;
import org.sodeja.parsec.ParsingResult;
import org.sodeja.parsec.combinator.DelegateParser;
import org.sodeja.parsec.semantic.AbstractSemanticParser;

public class ILParser extends AbstractSemanticParser<String, List<Expression>> {

    private static final String[] KEYWORDS_ARRAY = { "class", "fun", "import", "if", "else" };

    private static boolean isKeyword(String str) {
        for (String keword : KEYWORDS_ARRAY) {
            if (keword.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private final DelegateParser<String, List<Expression>> ROOT_EXPRESSIONS_DEL = new DelegateParser<String, List<Expression>>("ROOT_EXPRESSIONS_DEL");

    private final DelegateParser<String, Expression> EXPRESSION_DEL = new DelegateParser<String, Expression>("EXPRESSIONS_DEL");

    private final DelegateParser<String, ApplyExpression> APPLY_DEL = new DelegateParser<String, ApplyExpression>("APPLY_DEL");

    private final Parser<String, VariableExpression> IDENTIFIER = new AbstractParser<String, VariableExpression>("IDENTIFIER") {

        @Override
        protected ParsingResult<String, VariableExpression> executeDelegate(ConsList<String> tokens) {
            String head = tokens.head();
            if (head == ILLexer.CRLF) {
                return new ParseError<String, VariableExpression>("Expecting identifier", tokens);
            }
            if (head.length() > 1) {
                if (isKeyword(head)) {
                    return new ParseError<String, VariableExpression>("Expecting identifier", tokens);
                }
                return success(new VariableExpression(head), tokens.tail());
            }
            char ch = head.charAt(0);
            if (ILLexer.isDivider(ch)) {
                return new ParseError<String, VariableExpression>("Expecting identifier", tokens);
            }
            return success(new VariableExpression(head), tokens.tail());
        }
    };

    private final Parser<String, List<VariableExpression>> IDENTIFIERS = zeroOrMore("IDENTIFIERS", IDENTIFIER);

    private final Parser<String, ValueExpression> INTEGER_VALUE = apply("INTEGER_VALUE", simpleIntegerParser("NUMBER"), new Function1<ValueExpression, Integer>() {

        @Override
        public ValueExpression execute(Integer p) {
            return new ValueExpression(p);
        }
    });

    private final Parser<String, ValueExpression> BOOLEAN_VALUE = new AbstractParser<String, ValueExpression>("BOOLEAN_VALUE") {

        @Override
        protected ParsingResult<String, ValueExpression> executeDelegate(ConsList<String> tokens) {
            String head = tokens.head();
            if (head.equals("true")) {
                return success(new ValueExpression(Boolean.TRUE), tokens.tail());
            } else if (head.equals("false")) {
                return success(new ValueExpression(Boolean.FALSE), tokens.tail());
            }
            return new ParseError<String, ValueExpression>("Expecting true or false", tokens);
        }
    };

    private final Parser<String, ValueExpression> STRING_VALUE = applyCons("STRING_VALUE", justString("STRING"), ValueExpression.class);

    private final Parser<String, ValueExpression> VALUE = oneOf1("VALUE", INTEGER_VALUE, BOOLEAN_VALUE, STRING_VALUE);

    private final Parser<String, String> CURLY_OPEN = thenParserJust1("CURLY_OPEN", literal("{"), literal(ILLexer.CRLF));

    private final Parser<String, String> CURLY_CLOSE = thenParserJust1("CURLY_OPEN", literal(ILLexer.CRLF), literal("}"));

    private final Parser<String, BlockExpression> BLOCK = thenParser3Cons2("BLOCK", CURLY_OPEN, ROOT_EXPRESSIONS_DEL, CURLY_CLOSE, BlockExpression.class);

    private final Parser<String, Expression> LAMBDA_BODY = oneOf1("LAMBDA_BODY", BLOCK, EXPRESSION_DEL);

    private final Parser<String, LambdaExpression> LAMBDA_REST = thenParser3Cons13("LAMBDA_REST", IDENTIFIERS, literal("="), LAMBDA_BODY, LambdaExpression.class);

    private final Parser<String, LambdaExpression> LAMBDA = thenParserJust2("LAMBDA", literal("\\"), LAMBDA_REST);

    private final Parser<String, PrecedenceExpression> PRECEDENSE = thenParser3Cons2("PRECEDENCE", literal("("), EXPRESSION_DEL, literal(")"), PrecedenceExpression.class);

    private final Parser<String, Expression> ELSE = zeroOrOne("ELSE_OPTION", thenParserJust2("ELSE", literal("else"), LAMBDA_BODY));

    private final Parser<String, IfExpression> IF = thenParser4Cons234("IF", literal("if"), PRECEDENSE, LAMBDA_BODY, ELSE, IfExpression.class);

    private final Parser<String, SetExpression> SET = thenParser3Cons13("SET", IDENTIFIER, literal("="), EXPRESSION_DEL, SetExpression.class);

    private final Parser<String, ImportExpression> IMPORT = thenParserCons2("IMPORT", literal("import"), IDENTIFIER, ImportExpression.class);

    private final Parser<String, SetExpression> FUNCTION = thenParser3Cons23("FUNCTION", literal("fun"), IDENTIFIER, LAMBDA_REST, SetExpression.class);

    private final Parser<String, ClassExpression> CLASS = thenParser3Cons23("CLASS", literal("class"), IDENTIFIER, BLOCK, ClassExpression.class);

    private final Parser<String, Expression> SIMPLE_EXPRESSION = oneOf1("EXPRESSION", LAMBDA, PRECEDENSE, IF, VALUE, IDENTIFIER);

    private final Parser<String, List<Expression>> APPLY_BODY = oneOrMore("SIMPLE_EXPRESSIONS", SIMPLE_EXPRESSION);

    private final Parser<String, ApplyExpression> APPLY = applyCons("APPLY", APPLY_BODY, ApplyExpression.class);

    private final Parser<String, Expression> EXPRESSION = oneOf1("EXPRESSION", APPLY, SIMPLE_EXPRESSION);

    private final Parser<String, Expression> ROOT_EXPRESSION = oneOf1("ROOT_EXPRESSION", CLASS, FUNCTION, IMPORT, BLOCK, SET, EXPRESSION);

    private final Parser<String, List<Expression>> ROOT_EXPRESSIONS = oneOrMoreSep("ROOT_EXPRESSIONS", ROOT_EXPRESSION, literal(ILLexer.CRLF));

    public ILParser() {
        ROOT_EXPRESSIONS_DEL.delegate = ROOT_EXPRESSIONS;
        EXPRESSION_DEL.delegate = EXPRESSION;
        APPLY_DEL.delegate = APPLY;
    }

    @Override
    protected Parser<String, List<Expression>> getParser() {
        return ROOT_EXPRESSIONS;
    }
}
