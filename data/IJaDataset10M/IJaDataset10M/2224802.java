package org.lcelb.accounts.manager.data.resource;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.lcelb.accounts.manager.data.DataPackage;

/**
 * @author La Carotte
 */
public class AMXMLSaveImpl extends XMISaveImpl {

    /**
   * Constructor.
   * @param options_p
   * @param helper_p
   * @param encoding_p
   * @param xmlVersion_p
   */
    public AMXMLSaveImpl(Map<?, ?> options_p, XMLHelper helper_p, String encoding_p, String xmlVersion_p) {
        super(options_p, helper_p, encoding_p, xmlVersion_p);
    }

    /**
   * Constructor.
   * @param helper_p
   */
    public AMXMLSaveImpl(XMLHelper helper_p) {
        super(helper_p);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected boolean shouldSaveFeature(EObject o_p, EStructuralFeature f_p) {
        if (DataPackage.Literals.MODEL_ELEMENT_WITH_ID__ID.equals(f_p)) {
            return true;
        }
        return super.shouldSaveFeature(o_p, f_p);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected void saveElementID(EObject o_p) {
        saveFeatures(o_p);
    }
}
