package org.activebpel.rt.bpel.def.visitors.preprocess;

import org.activebpel.rt.bpel.IAeExpressionLanguageFactory;
import org.activebpel.rt.bpel.def.activity.AeChildExtensionActivityDef;
import org.activebpel.rt.bpel.def.adapter.IAeValidationPreprocessingAdapter;
import org.activebpel.rt.bpel.def.visitors.AeAbstractDefVisitor;
import org.activebpel.rt.bpel.def.visitors.AeDefTraverser;
import org.activebpel.rt.bpel.def.visitors.AeTraversalVisitor;
import org.activebpel.rt.wsdl.IAeContextWSDLProvider;
import org.activebpel.rt.xml.def.AeExtensionElementDef;
import org.activebpel.rt.xml.def.IAeAdapter;
import org.activebpel.rt.xml.def.IAeExtensionObject;

/**
 * <p>Implementation of a visitor to perform validation preprocessing on extensions.</p>
 * <p>
 * Applies an extension adapter to a bpel extension element.</p>
 */
public class AeValidationPreprocessingVisitor extends AeAbstractDefVisitor {

    /** The expression language factory set during visitor creation. */
    private IAeExpressionLanguageFactory mExpressionLanguageFactory;

    /** The WSDL provider used to located WSDL definitions */
    IAeContextWSDLProvider mContextProvider;

    /**
    * C'tor
    */
    public AeValidationPreprocessingVisitor(IAeContextWSDLProvider aContextProvider, IAeExpressionLanguageFactory aExpressionLanguageFactory) {
        mExpressionLanguageFactory = aExpressionLanguageFactory;
        mContextProvider = aContextProvider;
        setTraversalVisitor(new AeTraversalVisitor(new AeDefTraverser(), this));
    }

    /**
    * @see org.activebpel.rt.xml.def.visitors.AeBaseXmlDefVisitor#visit(org.activebpel.rt.xml.def.AeExtensionElementDef)
    */
    public void visit(AeExtensionElementDef aDef) {
        IAeExtensionObject extObj = aDef.getExtensionObject();
        preprocessForValidation(extObj);
    }

    /**
    * @see org.activebpel.rt.bpel.def.visitors.AeAbstractDefVisitor#visit(org.activebpel.rt.bpel.def.activity.AeChildExtensionActivityDef)
    */
    public void visit(AeChildExtensionActivityDef aDef) {
        IAeExtensionObject extObj = aDef.getExtensionObject();
        preprocessForValidation(extObj);
        super.visit(aDef);
    }

    /**
    * Gets adapter from ext object and does preprocessing for validation
    * @param aExtObj
    */
    protected void preprocessForValidation(IAeExtensionObject aExtObj) {
        if (aExtObj != null) {
            IAeAdapter adapter = aExtObj.getAdapter(IAeValidationPreprocessingAdapter.class);
            if (adapter != null) ((IAeValidationPreprocessingAdapter) adapter).preprocessForValidation(mContextProvider, mExpressionLanguageFactory);
        }
    }
}
