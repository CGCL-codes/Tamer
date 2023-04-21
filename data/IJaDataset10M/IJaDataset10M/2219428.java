package de.kugihan.dictionaryformids.dictgen.dictionaryupdate;

import java.util.Vector;
import de.kugihan.dictionaryformids.dataaccess.DictionaryDataFile;
import de.kugihan.dictionaryformids.dataaccess.DictionaryUpdateIF;
import de.kugihan.dictionaryformids.dictgen.DictionaryGeneration;
import de.kugihan.dictionaryformids.general.DictionaryException;
import de.kugihan.dictionaryformids.general.Util;

public class DictionaryUpdate implements DictionaryUpdateIF {

    protected int indexLanguage;

    final String delimiterStart = "{{";

    final String delimiterEnd = "}}";

    public String updateDictionaryExpression(String dictionaryExpression) throws DictionaryException {
        String returnString;
        if (DictionaryDataFile.dictionaryGenerationOmitParFromIndex) {
            boolean replacementDone;
            StringBuffer expressionUpdated = new StringBuffer(dictionaryExpression);
            do {
                replacementDone = false;
                int posDelimiterStart = expressionUpdated.toString().indexOf(delimiterStart);
                if (posDelimiterStart >= 0) {
                    expressionUpdated.delete(posDelimiterStart, posDelimiterStart + delimiterStart.length());
                    replacementDone = true;
                } else {
                    int posDelimiterEnd = expressionUpdated.toString().indexOf(delimiterEnd);
                    if (posDelimiterEnd >= 0) {
                        expressionUpdated.delete(posDelimiterEnd, posDelimiterEnd + delimiterEnd.length());
                        replacementDone = true;
                    }
                }
            } while (replacementDone);
            returnString = expressionUpdated.toString();
        } else {
            returnString = dictionaryExpression;
        }
        return returnString;
    }

    public String removeNonSearchParts(String expression) throws DictionaryException {
        String returnString;
        if (DictionaryDataFile.dictionaryGenerationOmitParFromIndex) {
            int nestingLevel = 0;
            int posDelimiterStartAtNestingLevel0 = 0;
            int posBehindLastDelimiter = 0;
            int posDelimiterStart;
            int posDelimiterEnd;
            StringBuffer expressionUpdated = new StringBuffer(expression);
            do {
                if ((posBehindLastDelimiter >= expressionUpdated.length()) && (nestingLevel > 0)) {
                    throw new DictionaryException("Number of " + delimiterStart + " does not match " + delimiterEnd + " at end of expression");
                }
                posDelimiterEnd = expressionUpdated.toString().indexOf(delimiterEnd, posBehindLastDelimiter);
                posDelimiterStart = expressionUpdated.toString().indexOf(delimiterStart, posBehindLastDelimiter);
                if ((posDelimiterStart == -1) && (posDelimiterEnd == -1)) {
                    if (nestingLevel > 0) {
                        throw new DictionaryException("Number of " + delimiterStart + " does not match " + delimiterEnd + " at end of expression");
                    }
                } else if (posDelimiterEnd == -1) {
                    throw new DictionaryException(delimiterEnd + " without previous " + delimiterStart);
                } else if ((posDelimiterEnd < posDelimiterStart) || (posDelimiterStart == -1)) {
                    --nestingLevel;
                    posBehindLastDelimiter = posDelimiterEnd + delimiterEnd.length();
                    if (nestingLevel == 0) {
                        expressionUpdated.delete(posDelimiterStartAtNestingLevel0, posBehindLastDelimiter);
                        posDelimiterStartAtNestingLevel0 = 0;
                        posBehindLastDelimiter = 0;
                    } else if (nestingLevel < 0) {
                        throw new DictionaryException(delimiterEnd + " without previous " + delimiterStart);
                    }
                } else {
                    if (nestingLevel == 0) {
                        posDelimiterStartAtNestingLevel0 = posDelimiterStart;
                    }
                    ++nestingLevel;
                    posBehindLastDelimiter = posDelimiterStart + delimiterStart.length();
                }
            } while (!((posDelimiterStart == -1) && (posDelimiterEnd == -1)));
            returnString = expressionUpdated.toString();
        } else {
            returnString = expression;
        }
        return returnString;
    }

    public void updateKeyWordVector(Vector keyWordVector) throws DictionaryException {
    }

    public String createKeyWordsExpression(String expression) throws DictionaryException {
        String keyWordsExpression;
        if (DictionaryDataFile.dictionaryGenerationOmitParFromIndex) {
            keyWordsExpression = expression;
        } else {
            keyWordsExpression = updateDictionaryExpression(expression);
        }
        String keyWordsExpressionWithoutContentDelimiters = DictionaryGeneration.removeContentDelimiters(keyWordsExpression, indexLanguage);
        StringBuffer keyWordsExpressionWithoutSeparatorChars = new StringBuffer(keyWordsExpressionWithoutContentDelimiters);
        Util.getUtil().replaceFieldAndLineSeparatorChars(keyWordsExpressionWithoutSeparatorChars);
        String keyWordsCleanedUp = removeNonSearchParts(keyWordsExpressionWithoutSeparatorChars.toString());
        return keyWordsCleanedUp;
    }

    public Vector createKeyWordVector(String expression, String expressionSplitString) throws DictionaryException {
        String keyWordsExpression = createKeyWordsExpression(expression);
        int posStartKeyWords = 0;
        int posEndKeyWords;
        Vector keyWordVector = new Vector();
        do {
            if (expressionSplitString != null) {
                posEndKeyWords = keyWordsExpression.indexOf(expressionSplitString, posStartKeyWords);
                if (posEndKeyWords == -1) posEndKeyWords = keyWordsExpression.length();
            } else {
                posEndKeyWords = keyWordsExpression.length();
            }
            String keyWordsSplitUp = keyWordsExpression.substring(posStartKeyWords, posEndKeyWords);
            addKeyWordsSplitUpToKeyWordVector(keyWordsSplitUp, keyWordVector);
            posStartKeyWords = posEndKeyWords + 1;
        } while (posEndKeyWords < keyWordsExpression.length());
        return keyWordVector;
    }

    public void addKeyWordsSplitUpToKeyWordVector(String keyWordsSplitUp, Vector keyWordVector) throws DictionaryException {
        DictionaryUpdateLib.addKeyWordExpressions(keyWordsSplitUp, keyWordVector);
    }

    public void setIndexLanguage(int indexLanguageParam) {
        indexLanguage = indexLanguageParam;
    }
}
