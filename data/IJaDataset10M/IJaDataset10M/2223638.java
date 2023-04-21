package com.vanga.example.linux;

import java.util.Map;
import com.vanga.example.AbstractCalculatorHandler;
import com.vanga.grammar.lexemes.LexemeFactory;
import com.vanga.grammar.match.MatchResult;
import com.vanga.grammar.match.Matchable;

public class TransformCalculatorLexemeHandler extends AbstractCalculatorHandler {

    /**
	 * Initialize a new instance with the provided arguments.
	 * @param creationArgs a {@code Map} containing String/String key/value pairs for all the settings.
	 */
    public TransformCalculatorLexemeHandler(Map<String, String> creationArgs) {
        super(creationArgs);
    }

    @Override
    protected MatchResult createNumpadLexeme(String numpadName, MatchResult inputMatch) {
        Matchable numpadLexeme = LexemeFactory.createConstant(numpadName);
        MatchResult l1 = MatchResult.evaluateExpression(inputMatch, "button-row[2].button[0]");
        MatchResult l2 = MatchResult.evaluateExpression(inputMatch, "button-row[5].button[2]");
        MatchResult numpad = new MatchResult(l1.getAreaOfMatch().union(l2.getAreaOfMatch()), numpadLexeme);
        int[][] indeces = { { 5, 0 }, { 4, 0 }, { 4, 1 }, { 4, 2 }, { 3, 0 }, { 3, 1 }, { 3, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };
        MatchResult buttonRoot = null;
        MatchResult buttonRows = inputMatch.get("button-row");
        for (int i = 0; i < indeces.length; i++) {
            MatchResult button = buttonRows.get(indeces[i][0]).get("button", indeces[i][1]);
            if (buttonRoot != null) {
                buttonRoot.addSibling(button.cloneShallow());
            } else {
                numpad.getChildren().add(buttonRoot = button.cloneShallow());
            }
        }
        return numpad;
    }
}
