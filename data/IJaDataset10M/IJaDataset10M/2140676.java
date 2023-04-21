package net.sourceforge.jdefprog.mcl.interpret.types;

public class ArrayType extends Type {

    private Type basicType;

    public ArrayType(Type basicType) {
        this.basicType = basicType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ArrayType)) {
            return false;
        }
        ArrayType at = (ArrayType) obj;
        return at.basicType.equals(this.basicType);
    }

    @Override
    public int hashCode() {
        return this.basicType.hashCode();
    }

    @Override
    public String toString() {
        return "Array of " + this.basicType;
    }

    @Override
    public String getQualifiedName() {
        return this.basicType.getQualifiedName();
    }
}
