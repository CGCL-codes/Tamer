package org.ourgrid.common.spec.semantic;

import org.ourgrid.common.spec.semantic.exception.SemanticException;
import org.ourgrid.common.spec.token.Token;

/**
 * This implementation of the semantic analyzer uses more then one language to
 * compile. Thats why uses SemanticActions as sets to each different language
 * actions.
 */
public class CommonSemanticAnalyzer implements SemanticAnalyzer {

    SemanticActions actions;

    /**
	 * The constructor.
	 * 
	 * @param actions The set of the actions that will be used for a determined
	 *        language.
	 */
    public CommonSemanticAnalyzer(SemanticActions actions) {
        this.actions = actions;
    }

    /**
	 * @see org.ourgrid.common.spec.semantic.SemanticAnalyzer#performAction(java.lang.String,
	 *      org.ourgrid.common.spec.token.Token)
	 */
    public void performAction(String action, Token token) throws SemanticException {
        StringBuffer sb = new StringBuffer(action);
        String actionNumber = sb.substring(1);
        actions.performAction("action" + actionNumber, token);
    }

    /**
	 * @see org.ourgrid.common.spec.semantic.SemanticAnalyzer#getOperationMode()
	 */
    public int getOperationMode() {
        return actions.getOperationalMode();
    }
}
