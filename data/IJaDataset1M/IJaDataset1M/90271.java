package org.eclipse.uml2.uml.internal.operations;

import java.util.Map;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.uml2.uml.Continuation;
import org.eclipse.uml2.uml.util.UMLValidator;

/**
 * <!-- begin-user-doc -->
 * A static utility class that provides operations related to '<em><b>Continuation</b></em>' model objects.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following operations are supported:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.Continuation#validateSameName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Same Name</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.Continuation#validateGlobal(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Global</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.Continuation#validateFirstOrLastInteractionFragment(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate First Or Last Interaction Fragment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContinuationOperations extends NamedElementOperations {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ContinuationOperations() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Continuations with the same name may only cover the same set of Lifelines (within one Classifier).
	 * true
	 * @param continuation The receiving '<em><b>Continuation</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateSameName(Continuation continuation, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.CONTINUATION__SAME_NAME, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateSameName", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(continuation, context) }), new Object[] { continuation }));
            }
            return false;
        }
        return true;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Continuations are always global in the enclosing InteractionFragment e.g. it always covers all Lifelines covered by the enclosing InteractionFragment.
	 * true
	 * @param continuation The receiving '<em><b>Continuation</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateGlobal(Continuation continuation, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.CONTINUATION__GLOBAL, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateGlobal", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(continuation, context) }), new Object[] { continuation }));
            }
            return false;
        }
        return true;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Continuations always occur as the very first InteractionFragment or the very last InteractionFragment of the enclosing InteractionFragment.
	 * true
	 * @param continuation The receiving '<em><b>Continuation</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateFirstOrLastInteractionFragment(Continuation continuation, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.CONTINUATION__FIRST_OR_LAST_INTERACTION_FRAGMENT, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateFirstOrLastInteractionFragment", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(continuation, context) }), new Object[] { continuation }));
            }
            return false;
        }
        return true;
    }
}
