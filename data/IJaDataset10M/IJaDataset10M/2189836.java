package eu.medeia.ecore.apmm.bm.util;

import java.util.Map;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;
import eu.medeia.ecore.apmm.bm.BmPackage;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BmXMLProcessor extends XMLProcessor {

    /**
	 * Public constructor to instantiate the helper.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BmXMLProcessor() {
        super((EPackage.Registry.INSTANCE));
        BmPackage.eINSTANCE.eClass();
    }

    /**
	 * Register for "*" and "xml" file extensions the BmResourceFactoryImpl factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (registrations == null) {
            super.getRegistrations();
            registrations.put(XML_EXTENSION, new BmResourceFactoryImpl());
            registrations.put(STAR_EXTENSION, new BmResourceFactoryImpl());
        }
        return registrations;
    }
}
