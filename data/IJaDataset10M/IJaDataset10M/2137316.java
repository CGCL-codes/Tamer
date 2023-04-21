package uk.ac.imperial.ma.deliveryEngine.qti.domImpl;

import org.w3c.dom.*;
import uk.ac.imperial.ma.deliveryEngine.Context;
import uk.ac.imperial.ma.deliveryEngine.DEItem;
import uk.ac.imperial.ma.deliveryEngine.VariableValue;

/**
 * TODO Description
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 *
 */
public class AssessmentItemImpl extends org.servingMathematics.qti.domImpl.AssessmentItemImpl implements DEItem {

    /**
     * @param element
     */
    public AssessmentItemImpl(Element element) {
        super(element);
    }

    public VariableValue evaluate(Context context) {
        VariableValue value = null;
        NodeList childNodes = getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            switch(childNodes.item(i).getNodeType()) {
                case Node.ELEMENT_NODE:
                    DEItem deItem = Utils.getDEItemImplementation(childNodes.item(i));
                    if (deItem != null) {
                        deItem.evaluate(context);
                    }
                    break;
                default:
            }
        }
        return value;
    }

    public Node getOutputXML(Context context) {
        Document doc = context.getXHTMLDocument();
        Element documentElement = doc.getDocumentElement();
        Text titleText = doc.createTextNode(this.getTitle());
        Element titleElement = doc.createElement("title");
        titleElement.appendChild(titleText);
        Element headElement = doc.createElement("head");
        headElement.appendChild(titleElement);
        documentElement.appendChild(headElement);
        ItemBodyImpl itemBody = new ItemBodyImpl(getItemBody());
        Node bodyNode = documentElement.getOwnerDocument().importNode(itemBody.getOutputXML(context), true);
        documentElement.appendChild(bodyNode);
        return doc;
    }
}
