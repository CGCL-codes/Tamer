package com.threerings.antidote.field;

public class TestBaseFieldWrapper extends BaseFieldWrapper<TestBaseField> {

    public TestBaseFieldWrapper(TestBaseField wrapped, Field parent) {
        super(wrapped, parent);
    }

    public TestBaseFieldWrapper(Class<? extends Field> clazz, Field parent) {
        super(clazz, parent);
    }

    public TestBaseFieldWrapper(String abstractName, Field parent) {
        super(abstractName, parent);
    }

    @Override
    protected void validateWrappedField() {
    }
}
