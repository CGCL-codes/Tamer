package com.tykhe.systems.snmpoller.template.expression;

import java.util.ArrayList;
import java.util.regex.Matcher;
import com.tykhe.systems.snmpoller.exceptions.CalculatorException;
import com.tykhe.systems.snmpoller.exceptions.InvalidExpressionException;
import com.tykhe.systems.snmpoller.exceptions.InvalidTokenException;
import com.tykhe.systems.snmpoller.template.calculator.Calculator;
import com.tykhe.systems.snmpoller.template.token.CalculationToken;
import com.tykhe.systems.snmpoller.template.token.LiteralBooleanToken;
import com.tykhe.systems.snmpoller.template.token.LiteralLongToken;
import com.tykhe.systems.snmpoller.template.token.LiteralStringToken;
import com.tykhe.systems.snmpoller.template.token.OperatorToken;
import com.tykhe.systems.snmpoller.template.token.ParenToken;
import com.tykhe.systems.snmpoller.template.token.Token;
import com.tykhe.systems.snmpoller.template.token.VariableLongToken;
import com.tykhe.systems.snmpoller.template.token.VariableToken;
import com.tykhe.systems.snmpoller.template.variable.Variable;
import com.tykhe.systems.snmpoller.util.Regex;
import com.tykhe.systems.snmpoller.util.SymbolTable;
import com.tykhe.systems.snmpoller.util.Tools;

/**
 * A class that handles the tokenization of an expression that can be "calculated".  That
 * is to say, it has values, operators, and respects the scoping offered by parentheses.
 * It extends Expression&lt;Long,T&gt;, which is to say that the variables that it will use
 * are expected to be Integer valued variables.
 * @author plubans
 *
 * @param <T> The expected type of the value of this expression
 */
public abstract class CalculableExpression<T> extends Expression<Long, T> {

    /**
	 * The Calculator that does the actual parsing of the tokens and computation of values. 
	 */
    protected Calculator<T> c;

    /**
	 * The list of tokens that manifest the calculation represented by this expression. 
	 */
    protected ArrayList<CalculationToken> calculationTokens;

    /**
	 * Create a new CalculableExpression from a given expression string and list of Variables.  
	 * @param expr The expression to parse.
	 * @param v The list of variables to refer to when finding a variable reference in the expression string.
	 * @throws InvalidExpressionException Thrown if there is a compile-time problem with the expression string,
	 *                                    for instance a syntax error or a problem with Variable usage. 
	 */
    public CalculableExpression(String expr, SymbolTable<Long> v) throws InvalidExpressionException {
        super(expr, v);
        calculationTokens = new ArrayList<CalculationToken>();
        for (Token t : tokens) {
            if (CalculationToken.class.isInstance(t)) {
                calculationTokens.add((CalculationToken) t);
            } else if (LiteralStringToken.class.isInstance(t)) {
                String s = ((LiteralStringToken) t).getValue();
                try {
                    tokenizeString(s);
                } catch (InvalidExpressionException e) {
                    throw new InvalidExpressionException("Couldn't tokenize Expression: " + e.getMessage());
                }
            } else {
                throw new InvalidExpressionException("Cannot use a '" + t.getClass().getSimpleName() + "' Token in a calculation context!");
            }
        }
        try {
            c = new Calculator<T>(calculationTokens);
        } catch (CalculatorException e) {
            throw new InvalidExpressionException("Couldn't make a calculator from the token list '" + Tools.tokensToString(calculationTokens) + "': " + e.getMessage());
        }
    }

    /**
	 * Builds a new VariableIntegerToken, as that is the useful variable for this type of Expression
	 */
    protected VariableToken<Long> buildNewVariableToken(String dereferencerString, Variable<Long> associate) throws InvalidTokenException {
        return new VariableLongToken(dereferencerString, associate);
    }

    /**
	 * Turn the String into CalculableTokens like operators, integers,
	 * booleans and parentheses.
	 * @param expr The expression to tokenize.
	 * @throws InvalidExpressionException Thrown if there is something in the string 
	 *                                    that cannot be turned into a CalculableToken.
	 */
    protected void tokenizeString(String expr) throws InvalidExpressionException {
        int length = expr.length();
        int lengthMinusOne = length - 1;
        int lookingAt = 0;
        String nextTwoCharacters = "";
        while (lookingAt < length) {
            if (lookingAt < lengthMinusOne) {
                nextTwoCharacters = expr.substring(lookingAt, lookingAt + 2);
                if (nextTwoCharacters.equals("<<") || nextTwoCharacters.equals(">>") || nextTwoCharacters.equals(">=") || nextTwoCharacters.equals("<=") || nextTwoCharacters.equals("==") || nextTwoCharacters.equals("!=") || nextTwoCharacters.equals("&&") || nextTwoCharacters.equals("||")) {
                    try {
                        calculationTokens.add(new OperatorToken(nextTwoCharacters));
                    } catch (InvalidTokenException e) {
                        throw new InvalidExpressionException("Problem tokenizing operator '" + nextTwoCharacters + "': " + e.getMessage());
                    }
                    lookingAt += 2;
                    continue;
                }
            }
            Matcher numberMatcher = Regex.signedInteger.matcher(expr.substring(lookingAt));
            if (numberMatcher.matches()) {
                String number = numberMatcher.group(1);
                try {
                    calculationTokens.add(new LiteralLongToken(number));
                } catch (InvalidTokenException e) {
                    throw new InvalidExpressionException("Couldn't tokenize number '" + number + "': " + e.getMessage());
                }
                lookingAt += number.length();
            } else {
                char current = expr.charAt(lookingAt);
                if (current == '(' || current == ')') {
                    try {
                        calculationTokens.add(new ParenToken(String.valueOf(current)));
                    } catch (InvalidTokenException e) {
                        throw new InvalidExpressionException("Couldn't tokenize parenthesis '" + current + "': " + e.getMessage());
                    }
                } else if (current == '+' || current == '-' || current == '*' || current == '/' || current == '<' || current == '>') {
                    try {
                        calculationTokens.add(new OperatorToken(String.valueOf(current)));
                    } catch (InvalidTokenException e) {
                        throw new InvalidExpressionException("Problem tokenizing operator '" + current + "': " + e.getMessage());
                    }
                } else if (current == 'T' || current == 'F' || current == 't' || current == 'f') {
                    try {
                        calculationTokens.add(new LiteralBooleanToken(String.valueOf(current)));
                    } catch (InvalidTokenException e) {
                        throw new InvalidExpressionException("Problem tokenizing booelan '" + current + "': " + e.getMessage());
                    }
                } else if (current != ' ') {
                    throw new InvalidExpressionException("Cannot tokenize string at '" + expr.substring(lookingAt) + "' as a numerical operation.");
                }
                lookingAt++;
            }
        }
    }
}
