package uk.ac.imperial.ma.deliveryEngine.qti.domImpl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.imperial.ma.deliveryEngine.Context;
import uk.ac.imperial.ma.deliveryEngine.DEItem;
import uk.ac.imperial.ma.deliveryEngine.VariableValue;

/**
 * This represents a <code>gt</code> element from a QTI document.
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @version 0.2, 26 October 2005
 */
public class GtImpl extends org.servingMathematics.qti.domImpl.GtImpl implements DEItem {

    /**
	 * Constructor.
	 * 
	 * @param element a representation of this Correct
	 */
    public GtImpl(Element element) {
        super(element);
    }

    public VariableValue evaluate(Context context) {
        VariableValue firstValue = null;
        VariableValue secondValue = null;
        firstValue = Utils.getNthChildAsVariableValue(context, this, 1);
        secondValue = Utils.getNthChildAsVariableValue(context, this, 2);
        if (((firstValue != null) & (secondValue != null)) == true) {
            boolean comparisonResult = firstValue.compareTo(secondValue).equals(VariableValue.Equality.GT);
            return new VariableValue(comparisonResult, VariableValue.ValueType.BOOLEAN);
        }
        return null;
    }

    public Node getOutputXML(Context context) {
        return null;
    }
}
