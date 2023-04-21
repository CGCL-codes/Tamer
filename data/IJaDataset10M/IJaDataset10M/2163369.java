package uk.ac.imperial.ma.deliveryEngine.qti.domImpl;

import org.w3c.dom.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.imperial.ma.deliveryEngine.Context;
import uk.ac.imperial.ma.deliveryEngine.DEItem;
import uk.ac.imperial.ma.deliveryEngine.VariableValue;

/**
 * This represents an assessmentItem element from a QTI document.
 * 
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @version 0.1, 26 September 2005
 */
public class TextEntryInteractionImpl extends org.servingMathematics.qti.domImpl.TextEntryInteractionImpl implements DEItem {

    public TextEntryInteractionImpl(Element element) {
        super(element);
    }

    public VariableValue evaluate(Context context) {
        return null;
    }

    public Node getOutputXML(Context context) {
        Document doc = context.getXHTMLDocument();
        Element inputTextElement = doc.createElement("input");
        inputTextElement.setAttribute("type", "text");
        inputTextElement.setAttribute("size", "10");
        return inputTextElement;
    }
}
