package org.jmlspecs.jir.binding;

public final class JirMethodBinding extends JirBinding {

    protected JirNameBinding declaringTypeName;

    protected String name;

    protected String signature;

    protected boolean isStatic;

    public static final int DESCRIPTOR = 8;

    public JirMethodBinding() {
    }

    public final JirNameBinding getDeclaringTypeName() {
        return this.declaringTypeName;
    }

    @Override
    public final int getDescriptor() {
        return JirMethodBinding.DESCRIPTOR;
    }

    public final boolean getIsStatic() {
        return this.isStatic;
    }

    public final String getName() {
        return this.name;
    }

    public final String getSignature() {
        return this.signature;
    }

    public final void setDeclaringTypeName(final JirNameBinding declaringTypeName) {
        assert declaringTypeName != null;
        this.declaringTypeName = declaringTypeName;
    }

    public final void setIsStatic(final boolean isStatic) {
        this.isStatic = isStatic;
    }

    public final void setName(final String name) {
        assert name != null;
        if (name != null) {
            this.name = name.intern();
        } else {
            this.name = null;
        }
    }

    public final void setSignature(final String signature) {
        assert signature != null;
        if (signature != null) {
            this.signature = signature.intern();
        } else {
            this.signature = null;
        }
    }
}
