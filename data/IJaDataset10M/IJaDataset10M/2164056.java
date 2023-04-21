package de.ifgi.argoomap.wordcomposition;

import java.util.ArrayList;
import java.util.List;
import de.ifgi.argoomap.MessageDocument;
import de.ifgi.argoomap.Token;

public class WordCompositionsDetector {

    private List<Token> tokens;

    private MessageDocument md;

    private List<WordCompositionRule> rules = new ArrayList<WordCompositionRule>();

    private List<WordComposition> wordCompositions = new ArrayList<WordComposition>();

    private boolean upToDate = false;

    public WordCompositionsDetector(MessageDocument md) {
        this.md = md;
    }

    public List<WordComposition> getWordCompositions() {
        if (!upToDate) {
            updateWordCompositions();
        }
        return wordCompositions;
    }

    public void setTokens(List<Token> tokens) {
        upToDate = false;
        this.tokens = tokens;
    }

    public void addWordCompositionRule(WordCompositionRule wcr) {
        rules.add(wcr);
        upToDate = false;
    }

    private void updateWordCompositions() {
        tokens = md.getTokens();
        wordCompositions.clear();
        for (WordCompositionRule wcr : rules) {
            int ruleTokensCnt = wcr.getTokensCount();
            for (int i = 0; i <= tokens.size() - ruleTokensCnt; i++) {
                Token[] tokensToCheck = new Token[ruleTokensCnt];
                Token t = tokens.get(i);
                tokensToCheck[0] = t;
                for (int j = 1; j < ruleTokensCnt; j++) {
                    tokensToCheck[j] = tokensToCheck[j - 1].getNextToken();
                }
                if (wcr.check(tokensToCheck)) {
                    wordCompositions.add(new WordComposition(tokensToCheck, md));
                }
            }
        }
        upToDate = true;
    }
}
