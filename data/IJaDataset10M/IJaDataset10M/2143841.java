package org.oslo.ocl20.standard.lib;

import org.oslo.ocl20.semantics.bridge.Classifier;
import org.oslo.ocl20.semantics.bridge.EnumerationType;

/**
 * @author dha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OclEnumerationImpl extends OclAnyModelElementImpl implements OclEnumeration {

    EnumerationType type;

    public OclEnumerationImpl(Classifier type, java.lang.Object obj, StdLibAdapter adapter) {
        super(obj, adapter);
        this.type = (EnumerationType) type;
    }

    public OclBoolean equalTo(OclAny o2) {
        boolean b = adapter.getProcessor().getModelEvaluationAdapter().EnumLiteral_equalTo(this, o2);
        return adapter.Boolean(b);
    }

    public Object clone() {
        return null;
    }

    public boolean equals(Object o) {
        if (o instanceof OclAny) return ((Boolean) this.equalTo((OclAny) o).asJavaObject()).booleanValue(); else return false;
    }

    public OclType oclType() {
        return adapter.getProcessor().getStdLibAdapter().Type(type);
    }

    public Object asJavaObject() {
        return super.modelElement;
    }
}
