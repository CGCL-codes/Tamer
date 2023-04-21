package org.eclipse.uml2.uml.internal.operations;

import java.util.Map;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.util.UMLValidator;

/**
 * <!-- begin-user-doc -->
 * A static utility class that provides operations related to '<em><b>Link Action</b></em>' model objects.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following operations are supported:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.LinkAction#validateSameAssociation(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Same Association</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.LinkAction#validateNotStatic(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Not Static</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.LinkAction#validateSamePins(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Same Pins</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.LinkAction#association() <em>Association</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LinkActionOperations extends ActivityNodeOperations {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected LinkActionOperations() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The association ends of the link end data must all be from the same association and include all and only the association ends of that association.
	 * self.endData->collect(end) = self.association()->collect(connection))
	 * @param linkAction The receiving '<em><b>Link Action</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateSameAssociation(LinkAction linkAction, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.LINK_ACTION__SAME_ASSOCIATION, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateSameAssociation", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(linkAction, context) }), new Object[] { linkAction }));
            }
            return false;
        }
        return true;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The association ends of the link end data must not be static.
	 * self.endData->forall(end.oclisKindOf(NavigableEnd) implies end.isStatic = #false
	 * @param linkAction The receiving '<em><b>Link Action</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateNotStatic(LinkAction linkAction, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.LINK_ACTION__NOT_STATIC, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateNotStatic", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(linkAction, context) }), new Object[] { linkAction }));
            }
            return false;
        }
        return true;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The input pins of the action are the same as the pins of the link end data and insertion pins.
	 * self.input->asSet() =
	 * let ledpins : Set = self.endData->collect(value) in
	 * if self.oclIsKindOf(LinkEndCreationData)
	 * then ledpins->union(self.endData.oclAsType(LinkEndCreationData).insertAt)
	 * else ledpins
	 * 
	 * @param linkAction The receiving '<em><b>Link Action</b></em>' model object.
	 * @param diagnostics The chain of diagnostics to which problems are to be appended.
	 * @param context The cache of context-specific information.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    public static boolean validateSamePins(LinkAction linkAction, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (false) {
            if (diagnostics != null) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UMLValidator.DIAGNOSTIC_SOURCE, UMLValidator.LINK_ACTION__SAME_PINS, org.eclipse.emf.ecore.plugin.EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "validateSamePins", org.eclipse.emf.ecore.util.EObjectValidator.getObjectLabel(linkAction, context) }), new Object[] { linkAction }));
            }
            return false;
        }
        return true;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The association operates on LinkAction. It returns the association of the action.
	 * result = self.endData->asSequence().first().end.association
	 * @param linkAction The receiving '<em><b>Link Action</b></em>' model object.
	 * <!-- end-model-doc -->
	 * @generated NOT
	 */
    public static Association association(LinkAction linkAction) {
        EList<LinkEndData> endData = linkAction.getEndData();
        if (endData.size() > 0) {
            Property end = endData.get(0).getEnd();
            if (end != null) {
                return end.getAssociation();
            }
        }
        return null;
    }
}
