package org.herasaf.xacml.core.policy.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>
 * Java class for ResourceAttributeDesignatorType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name=&quot;ResourceAttributeDesignatorType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeDesignatorType&quot;&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 62, for further information.
 *
 * @version 1.0
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceAttributeDesignatorType")
public class ResourceAttributeDesignatorType extends AttributeDesignatorType implements Serializable {

    private static final long serialVersionUID = 632768732L;

    @Override
    public Object handle(RequestType request, RequestInformation reqInfo) throws ExpressionProcessingException, MissingAttributeException, SyntaxException {
        List<Object> returnValues = new ArrayList<Object>();
        List<ResourceType> resources = request.getResources();
        for (ResourceType res : resources) {
            for (AttributeType attr : res.getAttributes()) {
                if (attributeId.equals(attr.getAttributeId()) && dataType.toString().equals(attr.getDataType().toString())) {
                    if (issuer != null) {
                        if (issuer.equals(attr.getIssuer())) {
                            addAndConvertAttrValue(returnValues, attr.getAttributeValues());
                        }
                    } else {
                        addAndConvertAttrValue(returnValues, attr.getAttributeValues());
                    }
                }
            }
        }
        if (returnValues.size() == 0) {
            List<AttributeValueType> attrValues = reqInfo.getAttributeFinder().requestResourceAttributes(request, attributeId, dataType.toString(), issuer);
            addAndConvertAttrValue(returnValues, attrValues);
        }
        if (returnValues.size() == 0 && isMustBePresent()) {
            throw new MissingAttributeException(attributeId, dataType, issuer);
        }
        return returnValues;
    }
}
