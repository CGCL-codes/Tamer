package org.thobe.compiler.sea;

public abstract class Value {

    public final ValueNode node;

    Value(ValueNode definingNode) {
        node = definingNode;
    }

    public static Value integer(final int integer) {
        return new Value(null) {

            @Override
            public String toString() {
                return "IntegerValue[" + integer + "]";
            }
        };
    }

    public static Value string(final String string) {
        return new Value(null) {

            @Override
            public String toString() {
                return "StringValue[\"" + string + "\"]";
            }
        };
    }

    @Override
    public String toString() {
        return "value_of(" + node + ")";
    }
}
