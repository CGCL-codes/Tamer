package parma_polyhedra_library;

public class Linear_Expression_Variable extends Linear_Expression {

    protected Variable arg;

    public Linear_Expression_Variable(Variable v) {
        arg = new Variable(v.id());
    }

    public Variable argument() {
        return arg;
    }

    public Linear_Expression_Variable clone() {
        return new Linear_Expression_Variable(new Variable(arg.id()));
    }
}
