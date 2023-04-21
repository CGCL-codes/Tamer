package com.ht.attribute;

/**
 * TODO_DOCUMENT_ME
 * 
 * @since 1.0
 */
public class DSimpleOverrideAttribute extends AAttributeWrapper {

    private IAttribute _overrideAttribute;

    /**
    * TODO_DOCUMENT_ME
    * @since 1.0
    */
    public DSimpleOverrideAttribute(IAttribute delegate, IAttribute override) {
        setDelegate(delegate);
        setOverrideAttribute(override);
    }

    @Override
    public Integer getValue() {
        return getDelegate().getValue() + getOverrideAttribute().getValue();
    }

    /**
    * @return the overridingAtt
    */
    public IAttribute getOverrideAttribute() {
        return _overrideAttribute;
    }

    /**
    * @param overridingAtt the overridingAtt to set
    */
    public void setOverrideAttribute(IAttribute overridingAtt) {
        _overrideAttribute = overridingAtt;
    }
}
