package uk.ac.imperial.ma.deliveryEngine.qti.domImpl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.imperial.ma.deliveryEngine.Context;
import uk.ac.imperial.ma.deliveryEngine.DEItem;
import uk.ac.imperial.ma.deliveryEngine.VariableValue;

/**
 * TODO Description
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 *
 */
public class TemplateElseImpl extends org.servingMathematics.qti.domImpl.ResponseElseImpl implements DEItem {

    public TemplateElseImpl(Element element) {
        super(element);
    }

    public VariableValue evaluate(Context context) {
        return Utils.getNthChildAsVariableValue(context, this, 0);
    }

    public Node getOutputXML(Context context) {
        return null;
    }
}
