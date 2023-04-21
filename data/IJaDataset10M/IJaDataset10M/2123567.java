package uk.ac.imperial.ma.metric.computerese3.mathematics.numbers;

import org.w3c.dom.Element;
import uk.ac.imperial.ma.metric.computerese3.ComputereseLeafNodeImpl;

/**
 * TODO Describe this type here.
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 19 Nov 2008
 */
public class IntegerImplAsIntPrimitive extends ComputereseLeafNodeImpl implements Integer {

    public static final String NAMESPACE = "mathematics:numbers:";

    public static final String NAME = "integer";

    public static final String[] ALIASES = { "wholeNumber", "int" };

    private int value;

    /**
	 * Constructs.
	 *
	 */
    public IntegerImplAsIntPrimitive(int value) {
        this.value = value;
    }

    @Override
    public int getValueAsPrimitiveInt() {
        return value;
    }
}
