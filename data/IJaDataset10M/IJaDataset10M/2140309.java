package com.rapidminer.operator.text.io.wordfilter;

import java.util.Iterator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.text.Document;
import com.rapidminer.operator.text.Token;
import com.rapidminer.operator.text.io.AbstractTokenProcessor;

/**
 * This operator will remove tokens that do not start with uppercase letters
 * 
 * @author Sebastian Land
 */
public class TokenCaseFilterOperator extends AbstractTokenProcessor {

    public TokenCaseFilterOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    protected Document doWork(Document document) throws OperatorException {
        Iterator<Token> iterator = document.getTokenSequence().iterator();
        while (iterator.hasNext()) {
            char firstChar = iterator.next().getToken().charAt(0);
            if (!Character.isUpperCase(firstChar)) {
                iterator.remove();
            }
        }
        return document;
    }
}
