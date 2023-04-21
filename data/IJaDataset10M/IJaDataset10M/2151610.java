package parser.valueObjects;

public class PortMapping {

    public Expression expression;

    public Target port;

    public PortMapping(Target port, Expression expression) {
        this.port = port;
        this.expression = expression;
    }
}
