package org.helianto.core.validation;

import org.helianto.core.KeyType;
import org.helianto.core.test.AbstractHibernatePropertyEditorTest;

/**
 * @author Mauricio Fernandes de Castro
 */
public class KeyTypePropertyEditorTests extends AbstractHibernatePropertyEditorTest<KeyType, KeyTypePropertyEditor> {

    @Override
    protected Class<KeyType> getTargetClazz() {
        return KeyType.class;
    }

    @Override
    protected Class<KeyTypePropertyEditor> getPropertyEditorClazz() {
        return KeyTypePropertyEditor.class;
    }
}
