package org.helianto.core.validation;

import org.helianto.core.KeyType;
import org.springframework.stereotype.Component;

/**
 * Default <code>KeyType</code> property editor.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated
 */
@Component
public class KeyTypePropertyEditor extends AbstractSessionPropertyEditor {

    @Override
    public String getAsText() {
        KeyType keyType = (KeyType) getValue();
        return String.valueOf(keyType.getKeyCode());
    }

    @Override
    public void setAsText(String id) throws IllegalArgumentException {
        setAsText(id, KeyType.class);
    }
}
