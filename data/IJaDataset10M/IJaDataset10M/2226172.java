package org.thobe.compiler.sea;

class InvocationNode extends ValueNode {

    private final InvocationType invocation;

    private final Value[] arguments;

    private final Value result = new Value(this) {
    };

    InvocationNode(InvocationType invocation, Value[] arguments) {
        this.invocation = invocation;
        this.arguments = arguments;
    }

    @Override
    public Value result() {
        return result;
    }

    @Override
    Node accept(GraphTraverser traverser) {
        return traverser.invoke(this, invocation, arguments).traverse(traverser);
    }

    @Override
    public String toString() {
        StringBuilder args = new StringBuilder(invocation.toString());
        String sep = "(";
        for (Value arg : arguments) {
            args.append(sep);
            args.append(arg.toString());
            sep = ", ";
        }
        args.append(")");
        return "InvocationNode[" + args + "]";
    }
}
