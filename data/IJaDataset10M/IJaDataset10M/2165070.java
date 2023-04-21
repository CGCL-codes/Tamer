package ru.ifmo.fgrammar;

import java.util.ArrayList;
import java.util.HashMap;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import ru.ifmo.fgrammar.Token.TokenType;

/**
 * Parses input into tokens
 */
public class Lexer {

    private enum LexerState {

        START, ERROR, INTEGER_LITERAL, REAL_LITERAL, STRING_LITERAL, ONE_SYMBOL_LEXEM, TWO_SYMBOL_LEXEM_START, TWO_SYMBOL_LEXEM, COMMENT, KEYWORD_START, KEYWORD, IDENTIFIER
    }

    private static HashMap<String, Token.TokenType> keywordsToken = new HashMap<String, Token.TokenType>();

    private static HashMap<String, Token.TokenType> oneSymbolLexems = new HashMap<String, Token.TokenType>();

    private static HashMap<String, Token.TokenType> twoOrOneSymbolLexems = new HashMap<String, Token.TokenType>();

    private StringBuffer buffer = new StringBuffer();

    private LexerState state = LexerState.START;

    ArrayList<Token> tokens = new ArrayList<Token>();

    IdentifierTable table = new IdentifierTable();

    static {
        keywordsToken.put("if", Token.TokenType.KEYWORD_IF);
        keywordsToken.put("then", Token.TokenType.KEYWORD_THEN);
        keywordsToken.put("else", Token.TokenType.KEYWORD_ELSE);
        keywordsToken.put("while", Token.TokenType.KEYWORD_WHILE);
        keywordsToken.put("new", Token.TokenType.KEYWORD_NEW);
        keywordsToken.put("replaceStr", Token.TokenType.KEYWORD_REPLACESTR);
        keywordsToken.put("findStr", Token.TokenType.KEYWORD_FINDSTR);
        keywordsToken.put("subStr", Token.TokenType.KEYWORD_SUBSTR);
        keywordsToken.put("strLen", Token.TokenType.KEYWORD_STRLEN);
        twoOrOneSymbolLexems.put("//", Token.TokenType.COMMENT);
        twoOrOneSymbolLexems.put("+", Token.TokenType.NOT_DEFINED);
        twoOrOneSymbolLexems.put("-", Token.TokenType.NOT_DEFINED);
        twoOrOneSymbolLexems.put("/", Token.TokenType.INFIX_OP);
        twoOrOneSymbolLexems.put("++", Token.TokenType.NOT_DEFINED);
        twoOrOneSymbolLexems.put("--", Token.TokenType.NOT_DEFINED);
        twoOrOneSymbolLexems.put(">=", Token.TokenType.RELATION_OP);
        twoOrOneSymbolLexems.put(">", Token.TokenType.RELATION_OP);
        twoOrOneSymbolLexems.put("<=", Token.TokenType.RELATION_OP);
        twoOrOneSymbolLexems.put("<", Token.TokenType.RELATION_OP);
        twoOrOneSymbolLexems.put("==", Token.TokenType.RELATION_OP);
        twoOrOneSymbolLexems.put("=", Token.TokenType.ASSIGNMENT_OP);
        keywordsToken.put(",", Token.TokenType.COMMA);
        keywordsToken.put("int", Token.TokenType.IDENTIFIER);
        keywordsToken.put("float", Token.TokenType.IDENTIFIER);
        keywordsToken.put("String", Token.TokenType.IDENTIFIER);
        oneSymbolLexems.put("{", Token.TokenType.BRACE_OPEN);
        oneSymbolLexems.put("}", Token.TokenType.BRACE_CLOSE);
        oneSymbolLexems.put("(", Token.TokenType.BRACKET_OPEN);
        oneSymbolLexems.put(")", Token.TokenType.BRACKET_CLOSE);
        oneSymbolLexems.put(";", Token.TokenType.SEMICOLON);
        oneSymbolLexems.put("*", Token.TokenType.INFIX_OP);
    }

    public Lexer() {
        state = LexerState.START;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Token> Parse(String text) {
        tokens.clear();
        for (int i = 0; i < text.length(); ++i) {
            processSymbol(text.charAt(i));
        }
        return (ArrayList<Token>) tokens.clone();
    }

    private void processSymbol(char c) {
        switch(state) {
            case START:
                processStart(c);
                break;
            case INTEGER_LITERAL:
                processIntegerLiteral(c);
                break;
            case REAL_LITERAL:
                processRealLiteral(c);
                break;
            case STRING_LITERAL:
                processStringLiteral(c);
                break;
            case TWO_SYMBOL_LEXEM_START:
                processTwoSymbolLexem(c);
                break;
            case COMMENT:
                processComment(c);
                break;
            case KEYWORD_START:
                processKeywordStart(c);
                break;
            case KEYWORD:
                processKeyword(c);
                break;
            case IDENTIFIER:
                processIdentifier(c);
                break;
        }
    }

    /**
     * Automaton transition from KEYWORD state
     * @param c Input character
     */
    private void processKeyword(char c) {
        String s = buffer.toString();
        assert (isKeyword(s));
        if (isAlpha(c) || isDigit(c)) {
            buffer.append(c);
            state = LexerState.IDENTIFIER;
            return;
        }
        tokens.add(new Token(keywordsToken.get(s), -1, buffer.toString()));
        state = LexerState.START;
        buffer.delete(0, buffer.length());
        processSymbol(c);
    }

    /**
     * Automaton transition from KEYWORD_START state
     * @param c Input character
     */
    private void processKeywordStart(char c) {
        String s = buffer.toString();
        if (isAlpha(c)) {
            buffer.append(c);
            s = s + c;
            if (isKeyword(s)) {
                state = LexerState.KEYWORD;
                return;
            }
            if (isKeywordPart(s)) return;
        }
        state = LexerState.IDENTIFIER;
        processIdentifier(c);
    }

    /**
     * Automaton transition from IDENTIFIER state
     * @param c Input character
     */
    private void processIdentifier(char c) {
        if (isAlpha(c) || isDigit(c)) {
            buffer.append(c);
            return;
        }
        updateIdTable(TokenType.IDENTIFIER);
        state = LexerState.START;
        processStart(c);
    }

    /**
     * Automaton transition from COMMENT state
     * @param c Input character
     */
    private void processComment(char c) {
        if (c == '\n') {
            state = LexerState.START;
            buffer.delete(0, buffer.length());
            return;
        }
        buffer.append(c);
    }

    /**
     * Automaton transition from TWO_SYMBOL_LEXEM_START state
     * @param c Input character
     */
    private void processTwoSymbolLexem(char c) {
        assert (buffer.length() == 1);
        String s = buffer.toString();
        if (twoOrOneSymbolLexems.containsKey(s + c)) {
            s = s + c;
            tokens.add(new Token(twoOrOneSymbolLexems.get(s), -1, s));
            buffer.delete(0, buffer.length());
            if (s.equals("//")) state = LexerState.COMMENT;
            return;
        }
        tokens.add(new Token(twoOrOneSymbolLexems.get(s), -1, s));
        buffer.delete(0, buffer.length());
        state = LexerState.START;
    }

    /**
     * Automaton transition from STRING_LITERAL state
     * @param c Input character
     */
    private void processStringLiteral(char c) {
        if (c == '\n') {
            state = LexerState.ERROR;
            return;
        }
        buffer.append(c);
        if (c == '\"') {
            updateIdTable(TokenType.LITERAL_STRING);
            state = LexerState.START;
            return;
        }
    }

    /**
     * Automaton transition from REAL_LITERAL state
     * @param c Input character
     */
    private void processRealLiteral(char c) {
        if (isDigit(c)) {
            buffer.append(c);
            return;
        }
        if (isSeparator(c)) {
            updateIdTable(TokenType.LITERAL_REAL);
            state = LexerState.START;
            return;
        }
        String s = Character.toString(c);
        if (twoOrOneSymbolLexems.containsKey(s) || oneSymbolLexems.containsKey(s)) {
            updateIdTable(TokenType.LITERAL_REAL);
            state = LexerState.START;
            processSymbol(c);
            return;
        }
        state = LexerState.ERROR;
    }

    /**
     * Automaton transition from INTEGER_LITERAL state
     * @param c Input character
     */
    private void processIntegerLiteral(char c) {
        if (isDigit(c)) {
            buffer.append(c);
            return;
        }
        if (c == '.') {
            buffer.append(c);
            state = LexerState.REAL_LITERAL;
            return;
        }
        if (isSeparator(c)) {
            updateIdTable(TokenType.LITERAL_INT);
            state = LexerState.START;
            return;
        }
        String s = Character.toString(c);
        if (twoOrOneSymbolLexems.containsKey(s) || oneSymbolLexems.containsKey(s)) {
            updateIdTable(TokenType.LITERAL_INT);
            state = LexerState.START;
            processSymbol(c);
            return;
        }
        state = LexerState.ERROR;
    }

    /**
     * Automaton transition from START state
     * @param c Input character
     */
    private void processStart(char c) {
        assert (buffer.length() == 0);
        String s = Character.toString(c);
        if (oneSymbolLexems.containsKey(s)) {
            tokens.add(new Token(oneSymbolLexems.get(s), -1, "" + c));
            return;
        }
        if (twoOrOneSymbolLexems.containsKey(s)) {
            buffer.append(s);
            state = LexerState.TWO_SYMBOL_LEXEM_START;
            return;
        }
        if (isDigit(c)) {
            buffer.append(s);
            state = LexerState.INTEGER_LITERAL;
            return;
        }
        if (c == '\"') {
            buffer.append(s);
            state = LexerState.STRING_LITERAL;
            return;
        }
        if (isKeywordPart(buffer.toString() + s)) {
            buffer.append(s);
            state = LexerState.KEYWORD_START;
            return;
        }
        if (isAlpha(c)) {
            buffer.append(s);
            state = LexerState.IDENTIFIER;
            return;
        }
        return;
    }

    /**
     * If character is digit
     * @param c character
     * @return true/false
     */
    static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    /**
     * If character is alphabetic symbol
     * @param c character
     * @return true/false
     */
    static boolean isAlpha(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    /**
     * If string is start part of on of keywords
     * @param s string
     * @return true/false
     */
    static boolean isKeywordPart(String s) {
        for (String a : keywordsToken.keySet()) {
            if (a.startsWith(s)) return true;
        }
        return false;
    }

    /**
     * If string is keyword
     * @param s string
     * @return true/false
     */
    static boolean isKeyword(String s) {
        for (String a : keywordsToken.keySet()) {
            if (a.equals(s)) return true;
        }
        return false;
    }

    /**
     * If character is separating symbol
     * @param c character
     * @return true/false
     */
    static boolean isSeparator(char c) {
        return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
    }

    /**
     * Update ID/const table and forms new token according to given token type
     * NB! Cleans the buffer
     * @param type Token type
     */
    void updateIdTable(TokenType type) {
        int idx = table.search(buffer.toString());
        if (idx == -1) {
            idx = table.put(new Identifier(buffer.toString()));
        }
        tokens.add(new Token(type, idx, ""));
        buffer.delete(0, buffer.length());
    }

    /**
     * @return the tokens
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * @return the table
     */
    public IdentifierTable getTable() {
        return table;
    }
}
