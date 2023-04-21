package com.aptana.ide.parsing.experimental;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;
import com.aptana.ide.lexer.ILexer;
import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.lexer.RegexLexerBuilderBase;
import com.aptana.ide.lexer.LexerException;
import com.aptana.ide.lexer.LexerInitializationException;
import com.aptana.ide.lexer.ascii.AsciiLexerBuilder;

/**
 * @author Kevin Lindsey
 */
public class Grammar {

    /**
	 * Error
	 */
    public static final int ERROR = -2;

    /**
	 * EOS
	 */
    public static final int EOS = -1;

    /**
	 * Comment
	 */
    public static final int COMMENT = 0;

    /**
	 * Whitespace
	 */
    public static final int WHITESPACE = 1;

    /**
	 * Lookahead
	 */
    public static final int LOOKAHEAD = 2;

    /**
	 * Not
	 */
    public static final int NOT = 3;

    /**
	 * Identifier
	 */
    public static final int IDENTIFIER = 4;

    /**
	 * Token
	 */
    public static final int TOKEN = 5;

    /**
	 * Colon
	 */
    public static final int COLON = 6;

    /**
	 * Semicolon
	 */
    public static final int SEMICOLON = 7;

    /**
	 * Pipe
	 */
    public static final int PIPE = 8;

    /**
	 * Option
	 */
    public static final int OPTION = 9;

    /**
	 * Kleene
	 */
    public static final int KLEENE = 10;

    /**
	 * Positive
	 */
    public static final int POSITIVE = 11;

    /**
	 * Left Bracket
	 */
    public static final int LBRACKET = 12;

    /**
	 * Right Bracket
	 */
    public static final int RBRACKET = 13;

    /**
	 * Left Curly Brace
	 */
    public static final int LCURLY = 14;

    /**
	 * Right Curly Brace
	 */
    public static final int RCURLY = 15;

    private static final int[] whitespaceSet = new int[] { COMMENT, WHITESPACE };

    private static final int[] alternateSet = new int[] { IDENTIFIER, TOKEN };

    private static final int[] closureSet = new int[] { OPTION, POSITIVE, KLEENE };

    private ILexer _lexer;

    private Lexeme _currentLexeme;

    private Hashtable _rules;

    private String _startRule;

    /**
	 * Get all the rule names in this grammar
	 * 
	 * @return Returns all rule names in this grammar as a sorted array
	 */
    public String[] getRuleNames() {
        Set nameSet = this._rules.keySet();
        String[] names = (String[]) nameSet.toArray(new String[0]);
        Arrays.sort(names);
        return names;
    }

    /**
	 * Get the rule for the given name
	 * 
	 * @param ruleName
	 *            The name of the rule to return
	 * @return The rule with the specified name
	 */
    public Rule getRule(String ruleName) {
        if (this._rules.containsKey(ruleName) == false) {
            throw new IllegalArgumentException("Grammar does not contain the specified rule: " + ruleName);
        }
        return (Rule) this._rules.get(ruleName);
    }

    /**
	 * Get the starting rule for this grammar
	 * 
	 * @return This grammar's starting rule
	 */
    public Rule getStartRule() {
        return this.getRule(this._startRule);
    }

    static {
        Arrays.sort(whitespaceSet);
        Arrays.sort(alternateSet);
        Arrays.sort(closureSet);
    }

    /**
	 * Create a new instance of Grammar
	 */
    public Grammar() {
        this._rules = new Hashtable();
    }

    /**
	 * Load a BNF file for analysis
	 * 
	 * @param filename
	 * @throws LexerInitializationException
	 * @throws LexerException
	 * @throws IOException
	 * @throws ParseException
	 */
    public void load(String filename) throws LexerInitializationException, LexerException, IOException, ParseException {
        RegexLexerBuilderBase lp = new AsciiLexerBuilder();
        lp.loadXML(new FileInputStream("Parser Files/BNFTokens.xml"));
        ILexer lexer = lp.buildLexer();
        File file = new File(filename);
        FileReader fr = new FileReader(filename);
        char[] chars = new char[(int) file.length()];
        fr.read(chars);
        fr.close();
        this._rules.clear();
        lexer.setSource(chars);
        this._lexer = lexer;
        this.advance();
        while (this._currentLexeme != null) {
            this.parse();
        }
    }

    /**
	 * Advance to the next lexeme
	 * 
	 * @return Returns the next lexeme in the source file
	 */
    private Lexeme advance() {
        this._currentLexeme = this._lexer.getNextLexeme();
        while (inSet(whitespaceSet)) {
            this._currentLexeme = this._lexer.getNextLexeme();
        }
        return this._currentLexeme;
    }

    /**
	 * Advance if the current lexeme is of the given type, else throw parse
	 * error
	 * 
	 * @param type
	 * @return Returns the next lexeme in the source file
	 * @throws ParseException
	 */
    private Lexeme assertAndAdvance(int type) throws ParseException {
        assertType(type);
        return this.advance();
    }

    /**
	 * Make sure lexeme type is the specified type
	 * 
	 * @param type
	 * @throws ParseException
	 */
    private void assertType(int type) throws ParseException {
        int nameIndex = this._currentLexeme.typeIndex;
        if (nameIndex != type) {
            throw new ParseException("expected " + type + " but found " + nameIndex, -1);
        }
    }

    /**
	 * Determine if the current lexeme is in the specified set
	 * 
	 * @param set
	 *            The set to test against
	 * @return Returns true if the current lexeme is in the specified set
	 */
    private boolean inSet(int[] set) {
        boolean result = false;
        if (this._currentLexeme != null) {
            result = Arrays.binarySearch(set, this._currentLexeme.typeIndex) >= 0;
        }
        return result;
    }

    /**
	 * Parse BNF file
	 * <p>
	 * <code>
	 * RULES
	 *     : RULE*
	 *     ;
	 * RULE
	 *     : IDENTIFIER COLON ALTERNATES SEMICOLON
	 *     ;
	 * ALTERNATES
	 *     : ALTERNATE (PIPE ALTERNATE)*
	 *     ;
	 * </code>
	 * 
	 * @throws ParseException
	 */
    private void parse() throws ParseException {
        while (this._currentLexeme != null && this._currentLexeme.typeIndex == IDENTIFIER) {
            Rule rule;
            String name = this._currentLexeme.getText();
            this.advance();
            if (this._rules.containsKey(name)) {
                rule = (Rule) this._rules.get(name);
            } else {
                if (this._rules.size() == 0) {
                    this._startRule = name;
                }
                rule = new Rule(this, name);
                this._rules.put(name, rule);
            }
            this.assertAndAdvance(COLON);
            if (this._currentLexeme.typeIndex == LBRACKET) {
                this.parseBracketedExpression();
            }
            if (inSet(alternateSet)) {
                rule.addAlternate(this.parseAlternate());
            } else {
                rule.setEpsilon();
            }
            while (this._currentLexeme.typeIndex == PIPE) {
                this.advance();
                if (inSet(alternateSet)) {
                    rule.addAlternate(this.parseAlternate());
                } else {
                    rule.setEpsilon();
                }
            }
            this.assertAndAdvance(SEMICOLON);
        }
    }

    /**
	 * Parse an alternate
	 * <p>
	 * <code>
	 * alternate
	 *     :	// empty
	 *     |	alternate id
	 *     |	alternate id closure
	 *     |	alternate token
	 *     |	alternate token closure
	 *     ;
	 * </code>
	 */
    private Alternate parseAlternate() {
        Alternate result = new Alternate();
        while (inSet(alternateSet)) {
            Element element = null;
            switch(this._currentLexeme.typeIndex) {
                case IDENTIFIER:
                    element = new Element(this._currentLexeme.getText(), Element.RULE);
                    result.addElement(element);
                    this.advance();
                    break;
                case TOKEN:
                    element = new Element(this._currentLexeme.getText(), Element.TOKEN);
                    result.addElement(element);
                    this.advance();
                    break;
                default:
                    break;
            }
            if (inSet(closureSet)) {
                switch(this._currentLexeme.typeIndex) {
                    case OPTION:
                        this.advance();
                        element.setModifier(Element.ZERO_OR_ONE);
                        break;
                    case POSITIVE:
                        this.advance();
                        element.setModifier(Element.ONE_OR_MORE);
                        break;
                    case KLEENE:
                        this.advance();
                        element.setModifier(Element.ZERO_OR_MORE);
                        break;
                    default:
                        break;
                }
            }
            if (this._currentLexeme.typeIndex == LBRACKET) {
                this.parseBracketedExpression();
            }
        }
        return result;
    }

    /**
	 * parse a bracketed expression
	 */
    private void parseBracketedExpression() {
        this.advance();
        while (this._currentLexeme.typeIndex != RBRACKET) {
            this.advance();
        }
        this.advance();
    }

    /**
	 * Return a string representation of this grammar
	 * 
	 * @return The string representation of this grammar
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String[] ruleNames = this.getRuleNames();
        if (ruleNames.length > 0) {
            Rule rule = this.getRule(ruleNames[0]);
            sb.append(rule.toString());
            for (int i = 1; i < ruleNames.length; i++) {
                rule = this.getRule(ruleNames[i]);
                sb.append("\n");
                sb.append(rule.toString());
            }
        }
        return sb.toString();
    }
}
