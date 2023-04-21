package org.eclipse.uml2.uml.internal.resource;

import java.util.Map;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.uml2.uml.resource.UML212UMLExtendedMetaData;

/**
 * Handler that converts .uml models.
 * OMG:  UML 2.1.x and UML 2.2 
 * API:  UML2 2.2.x and UML2 3.0.x 
 * 
 * @since 3.0
 */
public class UML212UMLHandler extends UMLHandler {

    public UML212UMLHandler(XMLResource xmiResource, XMLHelper helper, Map<?, ?> options) {
        super(xmiResource, helper, options);
    }

    @Override
    protected void createObject(EObject peekObject, EStructuralFeature feature) {
        if (!isNull()) {
            String xsiType = getXSIType();
            Map<String, Map<EClassifier, String>> featureToTypeMap = xsiType == null ? UML212UMLExtendedMetaData.getFeatureToTypeMap() : null;
            Map<EClassifier, String> typeMap = featureToTypeMap == null ? null : featureToTypeMap.get(feature.getName());
            if (typeMap != null) {
                EObject eObject = peekObject instanceof AnyType ? (EObject) objects.get(objects.size() - 2) : peekObject;
                for (Map.Entry<EClassifier, String> entry : typeMap.entrySet()) {
                    if (entry.getKey().isInstance(eObject)) {
                        xsiType = entry.getValue();
                        break;
                    }
                }
            }
            if (xsiType != null) {
                createObjectFromTypeName(peekObject, xsiType, feature);
            } else {
                super.createObject(peekObject, feature);
            }
        }
    }
}
