package cartagows.wsframework.wssecurity.policy.builders;

import java.util.Iterator;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.builders.AssertionBuilder;
import cartagows.wsframework.wssecurity.policy.SP11Constants;
import cartagows.wsframework.wssecurity.policy.SPConstants;
import cartagows.wsframework.wssecurity.policy.model.RequiredElements;

public class RequiredElementsBuilder implements AssertionBuilder {

    public Assertion build(OMElement element, AssertionBuilderFactory factory) throws IllegalArgumentException {
        RequiredElements requiredElements = new RequiredElements(SPConstants.SP_V11);
        OMAttribute attrXPathVersion = element.getAttribute(SP11Constants.ATTR_XPATH_VERSION);
        if (attrXPathVersion != null) {
            requiredElements.setXPathVersion(attrXPathVersion.getAttributeValue());
        }
        for (Iterator iterator = element.getChildElements(); iterator.hasNext(); ) {
            processElement((OMElement) iterator.next(), requiredElements);
        }
        return requiredElements;
    }

    public QName[] getKnownElements() {
        return new QName[] { SP11Constants.REQUIRED_ELEMENTS };
    }

    private void processElement(OMElement element, RequiredElements parent) {
        QName name = element.getQName();
        if (SP11Constants.XPATH.equals(name)) {
            parent.addXPathExpression(element.getText());
            Iterator namespaces = element.getAllDeclaredNamespaces();
            while (namespaces.hasNext()) {
                OMNamespace nm = (OMNamespace) namespaces.next();
                parent.addDeclaredNamespaces(nm.getNamespaceURI(), nm.getPrefix());
            }
        }
    }
}
