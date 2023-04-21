package uk.co.ordnancesurvey.confluence.model.nexttask.suggestion.base;

import uk.co.ordnancesurvey.confluence.IConfluenceEditorKit;
import uk.co.ordnancesurvey.confluence.ObjectInConfluence;
import uk.co.ordnancesurvey.confluence.model.nexttask.INextTaskSuggestion;

/**
 * Base abstract class for all next task suggestions.
 * 
 * @author rdenaux
 * 
 */
public abstract class AbstractNextTaskSuggestion extends ObjectInConfluence implements INextTaskSuggestion {

    public AbstractNextTaskSuggestion(IConfluenceEditorKit editorKit) {
        super(editorKit);
    }

    @Override
    public String toString() {
        return getType().name();
    }
}
