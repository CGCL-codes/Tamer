package cat.quadriga.parsers.code.types;

import cat.quadriga.parsers.code.ErrorLog;
import cat.quadriga.parsers.code.SymbolTable;
import cat.quadriga.runtime.ComputedValue;
import cat.quadriga.runtime.JavaReference;

public final class NullType extends ReferenceTypeRef {

    public static NullType instance = new NullType();

    private NullType() {
        super(Object.class, "Null type");
    }

    @Override
    public NullType getValid(SymbolTable symbolTable, ErrorLog errorLog) {
        return this;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isAssignableFrom(BaseType rightOperand) {
        return false;
    }

    @Override
    public ComputedValue getDefaultValue() {
        return new JavaReference(null);
    }

    @Override
    public boolean isSerializable() {
        return false;
    }
}
