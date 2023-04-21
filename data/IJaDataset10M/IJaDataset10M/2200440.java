package org.eclipse.uml2.uml.internal.operations;

import java.util.Map;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.util.UMLValidator;

/**
 * <!-- begin-user-doc -->
 * A static utility class that provides operations related to '<em><b>Structured Activity Node</b></em>' model objects.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following operations are supported:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.StructuredActivityNode#validateEdges(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Edges</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StructuredActivityNodeOperations extends ActivityNodeOperations {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected StructuredActivityNodeOperations() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The edges owned by a structured node must have source and target nodes in the structured node, and vice versa.
	 * true
	 * @param structuredActivityNode The receiving '<em><b>Structured Activity Node</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateEdges(StructuredActivityNode structuredActivityNode, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.STRUCTURED_ACTIVITY_NODE__EDGES, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateEdges", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(structuredActivityNode, context) }), new Object[] { structuredActivityNode }));
            }
            return false;
        }
        return true;
    }
}
