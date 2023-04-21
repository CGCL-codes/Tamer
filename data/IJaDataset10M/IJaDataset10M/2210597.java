package net.sf.doolin.util.expression;

import java.text.ParseException;
import java.util.Locale;
import java.util.Stack;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * A {@link Expression} is basically a text string whose content can be created
 * from bundle keys or properties in a bean model.
 * 
 * <p>
 * Tokens that are used are:
 * <ul>
 * <li>\@{propertyPath} - this token is replaced by the value defined by the
 * property in the bean model.
 * <li>%{key} or #{key} - this token is replaced by a string whose bundle key is
 * <code>key</code>. If the key has parameters, parameters can be indicated
 * using <code>%{key:param1,param2}</code> where parameters themselves are
 * {@link Expression} instances.
 * <li>Any other text is considered as hard-coded text
 * <li>The \ character can be used to escape any special character
 * </ul>
 * 
 * @author Damien Coraboeuf
 */
public class Expression {

    private final Locale locale;

    private final String pattern;

    private Object context;

    private String value = null;

    private Token token;

    /**
	 * Parses the expression
	 * 
	 * @param locale
	 *            Locale for the expression
	 * @param context
	 *            Context object
	 * @param pattern
	 *            Pattern used to build the expression
	 */
    public Expression(Locale locale, Object context, String pattern) {
        this.locale = locale;
        this.pattern = pattern;
        this.context = context;
        try {
            this.token = parse(pattern);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse " + pattern + " at position " + e.getErrorOffset() + ": " + e.getMessage(), e);
        }
        evaluate();
    }

    /**
	 * Builds an expression with a fixed text.
	 * 
	 * @param locale
	 *            Locale for this expression
	 * @param text
	 *            Fixed value for this expression (not parsed)
	 */
    public Expression(Locale locale, String text) {
        this.locale = locale;
        this.pattern = text;
        this.token = new TextToken(text);
        evaluate();
    }

    /**
	 * Parses the expression
	 * 
	 * @param context
	 *            Context object
	 * @param pattern
	 *            Pattern used to build the expression
	 */
    public Expression(Object context, String pattern) {
        this(Locale.getDefault(), context, pattern);
    }

    /**
	 * Builds an expression with a fixed text and for the default locale
	 * 
	 * @param text
	 *            Fixed text (not parsed)
	 * @see Locale#getDefault()
	 */
    public Expression(String text) {
        this(Locale.getDefault(), text);
    }

    private void add(Stack<Token> stack, Token token) {
        Token current = stack.peek();
        if (current.supportAdd()) {
            current.add(token);
            stack.push(token);
        } else {
            stack.pop();
            add(stack, token);
        }
    }

    private void addText(Stack<Token> stack, char c) {
        Token token = stack.peek();
        Token newToken = token.addText(c);
        if (newToken != null) {
            stack.push(newToken);
        }
    }

    private void close(Stack<Token> stack, Token token) {
        token.close();
        stack.pop();
    }

    /**
	 * Internal method used to evaluate the {@link #getValue() value} of the
	 * expression.
	 */
    protected void evaluate() {
        Object o = this.token.evaluate(this.locale, this.context);
        setValue(ObjectUtils.toString(o));
    }

    /**
	 * Gets the value for the expression
	 * 
	 * @return Value
	 */
    public String getValue() {
        return this.value;
    }

    /**
	 * Checks if the expression contains parts which can vary according to the
	 * model.
	 * 
	 * @return <code>true</code> if some parts of the expression are dependent
	 *         on the model.
	 */
    public boolean isVariable() {
        return this.token.isVariable();
    }

    private Token parse(String pattern) throws ParseException {
        if (StringUtils.isEmpty(pattern)) {
            return new TextToken();
        }
        Stack<Token> stack = new Stack<Token>();
        stack.push(new SequenceToken());
        int len = pattern.length();
        boolean escaping = false;
        TokenType maybeToken = null;
        for (int pos = 0; pos < len; pos++) {
            char c = pattern.charAt(pos);
            switch(c) {
                case '\\':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        escaping = true;
                    }
                    break;
                case '#':
                case '%':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        maybeToken = TokenType.KEY;
                    }
                    break;
                case '@':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        maybeToken = TokenType.VALUE;
                    }
                    break;
                case '{':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        if (maybeToken != null) {
                            switch(maybeToken) {
                                case KEY:
                                    KeyToken key = new KeyToken();
                                    add(stack, key);
                                    maybeToken = null;
                                    break;
                                case VALUE:
                                    ValueToken value = new ValueToken();
                                    add(stack, value);
                                    maybeToken = null;
                                    break;
                                default:
                                    throw new ParseException("Unexpected token: " + maybeToken, pos);
                            }
                        } else {
                            addText(stack, c);
                        }
                    }
                    break;
                case '}':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        Token token = stack.peek();
                        switch(token.getType()) {
                            case KEY:
                            case VALUE:
                                close(stack, token);
                                break;
                            case TEXT:
                                stack.pop();
                                Token parentToken = stack.peek();
                                if (parentToken.getType() == TokenType.KEY) {
                                    close(stack, parentToken);
                                } else {
                                    stack.push(token);
                                    addText(stack, c);
                                }
                                break;
                            default:
                                addText(stack, c);
                                break;
                        }
                    }
                    break;
                case ':':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        Token token = stack.peek();
                        switch(token.getType()) {
                            case KEY:
                                KeyToken key = (KeyToken) token;
                                key.switchToParams();
                                break;
                            default:
                                addText(stack, c);
                                break;
                        }
                    }
                    break;
                case ',':
                    if (escaping) {
                        addText(stack, c);
                        escaping = false;
                    } else {
                        Token token = stack.peek();
                        switch(token.getType()) {
                            case KEY:
                                KeyToken key = (KeyToken) token;
                                if (!key.hasSwitchedToParams()) {
                                    throw new ParseException("Unexpected ','", pos);
                                }
                                break;
                            case TEXT:
                                stack.pop();
                                Token parentToken = stack.peek();
                                if (parentToken.getType() == TokenType.KEY) {
                                    token.close();
                                } else {
                                    stack.push(token);
                                    addText(stack, c);
                                }
                                break;
                            default:
                                throw new ParseException("Unexpected ','", pos);
                        }
                    }
                    break;
                default:
                    if (escaping) {
                        throw new ParseException(c + " is not an escapable character", pos);
                    } else {
                        if (maybeToken != null) {
                            switch(maybeToken) {
                                case KEY:
                                    addText(stack, '%');
                                    break;
                                case VALUE:
                                    addText(stack, '@');
                                    break;
                            }
                            maybeToken = null;
                        }
                        addText(stack, c);
                    }
                    break;
            }
        }
        while (stack.size() > 1) {
            stack.pop();
        }
        return stack.pop();
    }

    /**
	 * Internal method that sets a new value to the expression
	 * 
	 * @param newValue
	 *            New value
	 */
    protected void setValue(String newValue) {
        this.value = newValue;
    }

    @Override
    public String toString() {
        return this.pattern;
    }

    /**
	 * Visits a the whole token tree with a visitor.
	 * 
	 * @param visitor
	 *            Visitor to apply for each token.
	 */
    protected void visitTokensWith(TokenVisitor visitor) {
        this.token.visitTokensWith(visitor);
    }
}
