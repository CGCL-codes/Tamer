package steal.examples.expression.model;

/**
 * @author  pawlar
 */
public interface Literal extends Expression {

    /**
	 * @return
	 */
    double getValue();

    /**
	 * @param value
	 */
    void setValue(double value);
}
