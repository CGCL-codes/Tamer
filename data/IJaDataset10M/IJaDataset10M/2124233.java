package de.kugihan.dictionaryformids.translation;

import de.kugihan.dictionaryformids.general.DictionaryException;

public class SearchIndicator {

    private final char beginOfExpressionChar = 'B';

    private final char substringOfExpressionChar = 'S';

    private boolean beginOfExpression;

    public SearchIndicator(char searchIndicatorCharacter) throws DictionaryException {
        if (searchIndicatorCharacter == beginOfExpressionChar) beginOfExpression = true; else if (searchIndicatorCharacter == substringOfExpressionChar) beginOfExpression = false; else throw new DictionaryException("Illegal value for searchindicator");
    }

    public SearchIndicator(boolean beginOfExpressionParam) {
        beginOfExpression = beginOfExpressionParam;
    }

    public boolean isBeginOfExpression() {
        return beginOfExpression;
    }

    public char asChar() {
        if (beginOfExpression) return beginOfExpressionChar; else return substringOfExpressionChar;
    }
}
