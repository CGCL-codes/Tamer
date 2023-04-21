package sg.edu.nus.comp.simTL.engine.interpreter.evaluators;

import org.eclipse.emf.ecore.EObject;
import sg.edu.nus.comp.simTL.engine.ITemplate;
import sg.edu.nus.comp.simTL.engine.exceptions.SimTLException;
import sg.edu.nus.comp.simTL.engine.exceptions.ValidationException;
import sg.edu.nus.comp.simTL.engine.tracing.IContext;
import sg.edu.nus.comp.simTL.engine.tracing.Reference2Element;

/**
 * @author Marcel B�hme
 * Comment created on: 26-Feb-2010
 */
public class TPlaceholder extends TElement {

    public static final String CLASS_TPLACEHOLDER = "TPlaceholder";

    public static final String REF_METHODSTATEMENT = "methodStatement";

    public static final String NAMING_PLACEHOLDER = "_PH";

    private EObject methodStatementEO;

    private TMethodStatement methodStatement;

    public TPlaceholder(EObject wrapped, IContext context, ITemplate template) throws SimTLException {
        super(wrapped);
        methodStatement = new TMethodStatement(methodStatementEO, context, template);
        if (methodStatement.getReferenceFromParent() instanceof Reference2Element) {
            throw new ValidationException("Placeholder " + toString() + " references an element and no attribute!");
        }
    }

    public static boolean isTPlaceholder(EObject eo) {
        return eo != null && hasTSuperClass(eo.eClass(), CLASS_TPLACEHOLDER);
    }

    @Override
    protected void load() throws SimTLException {
        methodStatementEO = loadReferencedEObject(REF_METHODSTATEMENT);
    }

    public TMethodStatement getTMethodStatement() {
        return methodStatement;
    }

    public static String attributeToPlaceHolder(String attributeName) {
        return attributeName + NAMING_PLACEHOLDER;
    }
}
