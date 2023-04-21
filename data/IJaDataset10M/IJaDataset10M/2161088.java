package org.monet.kernel.exceptions;

public class AttributeNotFoundException extends BaseException {

    private static final long serialVersionUID = -6040539882396443354L;

    public AttributeNotFoundException(String code, String key) {
        super(code, key);
    }
}
