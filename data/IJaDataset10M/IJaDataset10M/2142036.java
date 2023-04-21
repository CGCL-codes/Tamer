package xbird.xquery.type.xs;

import xbird.xquery.type.AtomicType;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public abstract class BinaryType extends AtomicType {

    protected BinaryType(final int typeId, final String type) {
        super(typeId, type);
    }

    public Class getJavaObjectType() {
        return byte[].class;
    }

    public boolean isValid(String literal) {
        return true;
    }

    @Override
    protected boolean isSuperTypeOf(final AtomicType expected) {
        return expected instanceof BinaryType;
    }
}
