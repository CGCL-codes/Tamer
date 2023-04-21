package org.helianto.core.validation;

import org.helianto.core.InternalEnumerator;
import org.springframework.stereotype.Component;

/**
 * Default <code>InternalEnumerator</code> property editor.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated
 */
@Component
public class InternalEnumeratorPropertyEditor extends AbstractSessionPropertyEditor {

    @Override
    public String getAsText() {
        InternalEnumerator internalEnumerator = (InternalEnumerator) getValue();
        return String.valueOf(internalEnumerator.getLastNumber());
    }

    @Override
    public void setAsText(String id) throws IllegalArgumentException {
        setAsText(id, InternalEnumerator.class);
    }
}
