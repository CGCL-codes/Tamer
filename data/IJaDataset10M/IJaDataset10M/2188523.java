package com.leantell.lp.lexical;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ozgur.tumer@gmail.com
 */
public class LexicalAnalyser {

    protected List<Terminal> terminals;

    public LexicalAnalyser(List<Terminal> patterns) {
        this.terminals = patterns;
    }

    public LexicalAnalyser() {
        this.terminals = new ArrayList<Terminal>();
    }

    public List<Token> tokenize(String line) throws InvalidSyntaxException {
        List<Token> tokens = new ArrayList<Token>();
        List<Token> allTokens = allTokens(line);
        List<Token> unrecognizedTokens = new ArrayList<Token>();
        for (Token token : allTokens) {
            if (token.isUnrecognized()) unrecognizedTokens.add(token);
            token.process(tokens);
        }
        if (!unrecognizedTokens.isEmpty()) throw new InvalidSyntaxException(unrecognizedTokens);
        return tokens;
    }

    public List<Token> tokenizeIgnoringUnrecognizedTokens(String line) {
        return tokenizeProcessMergeWeek(line);
    }

    public List<Token> tokenizeProcessMergeWeek(String line) {
        List<Token> allTokens = allTokens(line);
        List<Token> processedTokens = processAll(allTokens);
        List<Token> mergedDokens = TokenProcessor.INSTANCE.mergeWeekTokens(processedTokens);
        return mergedDokens;
    }

    public List<Token> tokenizeMergeWeekProcess(String line) {
        List<Token> allTokens = allTokens(line);
        List<Token> mergedDokens = TokenProcessor.INSTANCE.mergeWeekTokens(allTokens);
        return processAll(mergedDokens);
    }

    private List<Token> processAll(List<Token> tokens) {
        List<Token> processedTokens = new ArrayList<Token>();
        for (Token token : tokens) {
            token.process(processedTokens);
        }
        return processedTokens;
    }

    public List<Token> allTokens(String code) {
        UnrecognizedTokenProcessor unrecognizedTokenProcessor = new UnrecognizedTokenProcessor();
        List<Token> tokens = new ArrayList<Token>();
        int offset = 0;
        int lineNumber = 1;
        while (code.length() > 0) {
            try {
                Token token = getNextToken(code, offset, lineNumber);
                lineNumber = unrecognizedTokenProcessor.process(tokens, offset, lineNumber);
                lineNumber += token.getNumberOfLinesSpanned();
                offset += token.length();
                token.preProcess(tokens);
                code = code.substring(token.length(), code.length());
            } catch (InvalidSyntaxException e) {
                unrecognizedTokenProcessor.append(code.substring(0, 1));
                code = code.substring(1, code.length());
                offset++;
            }
        }
        lineNumber = unrecognizedTokenProcessor.process(tokens, offset, lineNumber);
        return tokens;
    }

    public List<Token> allTokensLineProcess(String code) {
        UnrecognizedTokenProcessor unrecognizedTokenProcessor = new UnrecognizedTokenProcessor();
        List<Token> tokens = new ArrayList<Token>();
        int offset = 0;
        int lineNumber = 1;
        while (code.length() > 0) {
            try {
                Token token = getNextToken(code, offset, lineNumber);
                lineNumber = unrecognizedTokenProcessor.process(tokens, offset, lineNumber);
                lineNumber += token.getNumberOfLinesSpanned();
                offset += token.length();
                token.preProcess(tokens);
                code = code.substring(token.length(), code.length());
            } catch (InvalidSyntaxException e) {
                unrecognizedTokenProcessor.append(code.substring(0, 1));
                code = code.substring(1, code.length());
                offset++;
            }
        }
        lineNumber = unrecognizedTokenProcessor.process(tokens, offset, lineNumber);
        return tokens;
    }

    public List<String> getTokenStrings(String line) {
        List<String> tokenStrings = new ArrayList<String>();
        for (Token match : tokenizeProcessMergeWeek(line)) {
            tokenStrings.add(match.getLexeme().toString());
        }
        return tokenStrings;
    }

    private Token getNextToken(String line, int offset, int lineNumber) throws InvalidSyntaxException {
        Token token = null;
        for (Terminal terminal : terminals) {
            if (terminal.matches(line)) {
                Token newToken = new Token(terminal, terminal.matchedSegment(line), offset, lineNumber);
                if (token == null || token.length() < newToken.length()) token = newToken;
            }
        }
        if (token != null) return token;
        throw new InvalidSyntaxException(line.toString());
    }

    public void addTerminal(Terminal terminal) {
        terminals.add(terminal);
    }

    public List<Terminal> getTerminals() {
        List<Terminal> newTerminals = new ArrayList<Terminal>(terminals);
        newTerminals.add(new UnrecognizedToken.UnrecognizedTerminal());
        return newTerminals;
    }

    protected class UnrecognizedTokenProcessor {

        private StringBuffer unrecognized = new StringBuffer();

        public UnrecognizedTokenProcessor() {
        }

        private boolean hasToken() {
            return unrecognized.length() > 0;
        }

        protected void append(CharSequence string) {
            unrecognized.append(string);
        }

        private Token getToken(int offset, int lineNumber) {
            if (!hasToken()) throw new AssertionError("Cannot get null token");
            String lineSegment = unrecognized.toString();
            Token token = createUnrecognizedToken(lineSegment, offset, lineNumber);
            unrecognized = new StringBuffer();
            return token;
        }

        private UnrecognizedToken createUnrecognizedToken(String lineSegment, int offset, int lineNumber) {
            return new UnrecognizedToken(lineSegment, offset - lineSegment.length(), lineNumber);
        }

        int process(List<Token> tokens, int offset, int lineNumber) {
            if (hasToken()) {
                Token unrecognizedToken = getToken(offset, lineNumber);
                tokens.add(unrecognizedToken);
                lineNumber = lineNumber + unrecognizedToken.getNumberOfLinesSpanned();
            }
            return lineNumber;
        }
    }
}
