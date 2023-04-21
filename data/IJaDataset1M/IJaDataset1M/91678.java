package parser.valueObjects;

public class NumericLiteral implements Literal {

    public String numeric;

    public NumericLiteral(String numeric) {
        this.numeric = numeric;
    }

    @Override
    public String compose() {
        return "INTEGER.fromLiteral(" + Integer.parseInt(numeric) + ")";
    }
}
