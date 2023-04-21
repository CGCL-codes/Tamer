package org.eclipse.uml2.uml.internal.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.uml2.uml.resource.XMI212UMLResource;

/**
 * Resource that converts .xmi models.
 * OMG:  UML 2.1.x and UML 2.2 
 * API:  UML2 2.2.x and UML2 3.0.x 
 * 
 * @since 3.0
 */
public class XMI212UMLResourceImpl extends UMLResourceImpl implements XMI212UMLResource {

    public XMI212UMLResourceImpl(URI uri) {
        super(uri);
    }

    @Override
    protected XMLHelper createXMLHelper() {
        return new XMI2UMLHelperImpl(this);
    }

    @Override
    protected boolean assignIDsWhileLoading() {
        return false;
    }

    @Override
    protected XMLLoad createXMLLoad() {
        return new XMI212UMLLoadImpl(createXMLHelper());
    }

    @Override
    protected XMLSave createXMLSave() {
        return new XMI2UMLSaveImpl(createXMLHelper());
    }
}
