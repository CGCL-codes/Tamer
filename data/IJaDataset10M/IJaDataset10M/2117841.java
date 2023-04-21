package org.jmlspecs.jir.binding;

public final class JirArrayBinding<Type> extends JirBinding {

    protected JirTypeBinding<Type> arrayType;

    public static final int DESCRIPTOR = 7;

    public JirArrayBinding() {
    }

    public final JirTypeBinding<Type> getArrayType() {
        return this.arrayType;
    }

    @Override
    public final int getDescriptor() {
        return JirArrayBinding.DESCRIPTOR;
    }

    public final void setArrayType(final JirTypeBinding<Type> arrayType) {
        assert arrayType != null;
        this.arrayType = arrayType;
    }
}
