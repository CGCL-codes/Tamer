package org.eclipse.wst.xml.search.editor.util;

import org.eclipse.wst.xml.search.core.properties.IPropertiesQuerySpecification;
import org.eclipse.wst.xml.search.core.properties.PropertiesQuerySpecificationManager;
import org.eclipse.wst.xml.search.editor.references.IXMLReferenceToProperty;

public class PropertiesQuerySpecificationUtil {

    public static IPropertiesQuerySpecification getQuerySpecification(IXMLReferenceToProperty referenceToProperty) {
        return PropertiesQuerySpecificationManager.getDefault().getQuerySpecification(referenceToProperty.getQuerySpecificationId());
    }

    public static IPropertiesQuerySpecification[] getQuerySpecifications(IXMLReferenceToProperty referenceToProperty) {
        IPropertiesQuerySpecification querySpecification = getQuerySpecification(referenceToProperty);
        if (querySpecification != null) {
            IPropertiesQuerySpecification[] result = new IPropertiesQuerySpecification[1];
            result[0] = querySpecification;
            return result;
        }
        return IPropertiesQuerySpecification.EMPTY;
    }
}
