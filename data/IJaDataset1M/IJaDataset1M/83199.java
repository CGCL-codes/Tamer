package net.sf.orcc.cal.linking;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.orcc.cal.cal.AstProcedure;
import net.sf.orcc.cal.cal.AstState;
import net.sf.orcc.cal.cal.CalFactory;
import net.sf.orcc.cal.cal.CalPackage;
import net.sf.orcc.cal.util.Util;
import net.sf.orcc.ir.Type;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.parsetree.AbstractNode;

/**
 * This class defines a linking service for built-in functions/procedures and
 * FSM states.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class CalLinkingService extends DefaultLinkingService {

    private Map<String, AstProcedure> procedures;

    private Resource stubsResource = null;

    /**
	 * Creates a new CAL linking service which creates builtin functions.
	 */
    public CalLinkingService() {
        procedures = new HashMap<String, AstProcedure>();
        addProcedure("print");
        addProcedure("println");
    }

    /**
	 * Adds a new procedure to the built-in procedures map with the given
	 * parameters types .
	 * 
	 * @param name
	 *            function name
	 * @param parameters
	 *            types of function parameters
	 */
    private void addProcedure(String name, Type... parameters) {
        AstProcedure procedure;
        procedure = CalFactory.eINSTANCE.createAstProcedure();
        procedure.setName(name);
        procedures.put(name, procedure);
    }

    /**
	 * Returns a singleton if <code>name</code> is a builtin procedure, and an
	 * empty list otherwise.
	 * 
	 * @param context
	 *            the context in which a procedure is referenced.
	 * @param name
	 *            procedure name
	 * @return a list
	 */
    private List<EObject> builtinProcedure(EObject context, String name) {
        AstProcedure procedure = procedures.get(name);
        if (procedure != null) {
            EObject cter = Util.getTopLevelContainer(context);
            Resource res = makeResource(cter.eResource());
            res.getContents().add(procedure);
            return Collections.singletonList((EObject) procedure);
        }
        return Collections.emptyList();
    }

    @Override
    public List<EObject> getLinkedObjects(EObject context, EReference ref, AbstractNode node) {
        List<EObject> result = super.getLinkedObjects(context, ref, node);
        if (result != null && !result.isEmpty()) {
            return result;
        }
        final EClass requiredType = ref.getEReferenceType();
        final String s = getCrossRefNodeAsString(node);
        if (requiredType != null && s != null) {
            if (CalPackage.Literals.AST_PROCEDURE.isSuperTypeOf(requiredType)) {
                return builtinProcedure(context, s);
            } else if (CalPackage.Literals.AST_STATE.isSuperTypeOf(requiredType)) {
                return getState(context, ref, s);
            }
        }
        return Collections.emptyList();
    }

    private List<EObject> getState(EObject context, EReference reference, String name) {
        EObject cter = Util.getTopLevelContainer(context);
        AstState state = CalFactory.eINSTANCE.createAstState();
        state.setName(name);
        Resource res = makeResource(cter.eResource());
        res.getContents().add(state);
        return Collections.singletonList((EObject) state);
    }

    /**
	 * Use a temporary 'child' resource to hold created stubs. The real resource
	 * URI is used to generate a 'temporary' resource to be the container for
	 * stub EObjects.
	 * 
	 * @param source
	 *            the real resource that is being parsed
	 * @return the cached reference to a resource named by the real resource
	 *         with the added extension 'xmi'
	 */
    private Resource makeResource(Resource source) {
        if (null != stubsResource) return stubsResource;
        URI stubURI = source.getURI().appendFileExtension("xmi");
        stubsResource = source.getResourceSet().getResource(stubURI, false);
        if (null == stubsResource) {
            stubsResource = source.getResourceSet().createResource(stubURI);
        }
        return stubsResource;
    }
}
