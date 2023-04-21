package IC.AST;

import IC.LiteralTypes;

/**
 * Literal value AST node.
 * 
 * @author Tovi Almozlino
 */
public class Literal extends Expression {

    private LiteralTypes type;

    private Object value;

    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    /** Accepts a propagating visitor parameterized by two types.
	 * 
	 * @param <DownType> The type of the object holding the context.
	 * @param <UpType> The type of the result object.
	 * @param visitor A propagating visitor.
	 * @param context An object holding context information.
	 * @return The result of visiting this node.
	 * @throws Exception 
	 */
    @Override
    public <DownType, UpType> UpType accept(PropagatingVisitor<DownType, UpType> visitor, DownType context) throws Exception {
        return visitor.visit(this, context);
    }

    /**
	 * Constructs a new literal node.
	 * 
	 * @param line
	 *            Line number of the literal.
	 * @param type
	 *            Literal type.
	 */
    public Literal(int line, LiteralTypes type) {
        super(line);
        this.type = type;
        value = type.getValue();
    }

    /**
	 * Constructs a new literal node, with a value.
	 * 
	 * @param line
	 *            Line number of the literal.
	 * @param type
	 *            Literal type.
	 * @param value
	 *            Value of literal.
	 */
    public Literal(int line, LiteralTypes type, Object value) {
        this(line, type);
        this.value = value;
    }

    public LiteralTypes getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
