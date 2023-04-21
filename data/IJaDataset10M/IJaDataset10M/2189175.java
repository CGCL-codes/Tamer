package org.oslo.ocl20.generation.lib;

import org.oslo.ocl20.semantics.bridge.Classifier;
import org.oslo.ocl20.standard.lib.OclEnumeration;

/**
 * @author dha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OclEnumerationImpl extends OclAnyModelElementImpl implements OclEnumeration {

    public OclEnumerationImpl(Classifier type, String modelElem, StdLibGenerationAdapterImpl adapter, boolean newVariable) {
        super(type, modelElem, adapter, newVariable);
    }
}
