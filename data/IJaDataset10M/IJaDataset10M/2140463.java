package org.monet.kernel.model;

public class SequenceValue extends BaseObject {

    protected String codeSubSequence;

    protected Integer value;

    public SequenceValue(String code, String codeSubSequence, int value) {
        super();
        this.code = code;
        this.codeSubSequence = codeSubSequence;
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
