package hub.sam.lang.mf2b;

import hub.metrik.lang.eprovide.debuggingstate.MProgramContext;
import hub.sam.lang.mf2b.abstractions.RuntimeClassifier;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.lang.mf2b.Program#getFunctions <em>Functions</em>}</li>
 *   <li>{@link hub.sam.lang.mf2b.Program#getInitialExpressions <em>Initial Expressions</em>}</li>
 *   <li>{@link hub.sam.lang.mf2b.Program#getRunningPrograms <em>Running Programs</em>}</li>
 *   <li>{@link hub.sam.lang.mf2b.Program#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.lang.mf2b.Mf2bPackage#getProgram()
 * @model annotation="http://www.eclipse.org/OCL/derivedProperty contexts='runningPrograms->collect(rp : RunningProgram | rp.initialEvaluations)->asOrderedSet()'"
 * @generated
 */
public interface Program extends RuntimeClassifier<RunningProgram>, MProgramContext {

    /**
     * Returns the value of the '<em><b>Functions</b></em>' containment reference list.
     * The list contents are of type {@link hub.sam.lang.mf2b.Function}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Functions</em>' containment reference list.
     * @see hub.sam.lang.mf2b.Mf2bPackage#getProgram_Functions()
     * @model containment="true"
     * @generated
     */
    EList<Function> getFunctions();

    /**
     * Returns the value of the '<em><b>Initial Expressions</b></em>' containment reference list.
     * The list contents are of type {@link hub.sam.lang.mf2b.Expression}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial Expressions</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initial Expressions</em>' containment reference list.
     * @see hub.sam.lang.mf2b.Mf2bPackage#getProgram_InitialExpressions()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getInitialExpressions();

    /**
     * Returns the value of the '<em><b>Running Programs</b></em>' containment reference list.
     * The list contents are of type {@link hub.sam.lang.mf2b.RunningProgram}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Running Programs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Running Programs</em>' containment reference list.
     * @see hub.sam.lang.mf2b.Mf2bPackage#getProgram_RunningPrograms()
     * @model containment="true"
     * @generated
     */
    EList<RunningProgram> getRunningPrograms();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see hub.sam.lang.mf2b.Mf2bPackage#getProgram_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link hub.sam.lang.mf2b.Program#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);
}
