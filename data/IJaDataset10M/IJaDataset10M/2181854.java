package info.olteanu.utils.expressionparser.nodes;

public interface Visitor<Output> {

    public Output visit(AndNodeX n);

    public Output visit(OrNodeX n);

    public Output visit(ImplyNodeX n);

    public Output visit(EquivalentNodeX n);

    public Output visit(NotNodeX n);

    public Output visit(VariableNodeX n);

    public Output visit(ConstantNodeX n);
}
