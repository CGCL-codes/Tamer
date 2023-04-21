package tefkat.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Source Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tefkat.model.SourceTerm#getTRuleSrc <em>TRule Src</em>}</li>
 * </ul>
 * </p>
 *
 * @see tefkat.model.TefkatPackage#getSourceTerm()
 * @model abstract="true"
 * @generated
 */
public interface SourceTerm extends Term {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley Pty Ltd 2003-2007";

    /**
     * Returns the value of the '<em><b>TRule Src</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link tefkat.model.TRule#getSrc <em>Src</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>TRule Src</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>TRule Src</em>' container reference.
     * @see #setTRuleSrc(TRule)
     * @see tefkat.model.TefkatPackage#getSourceTerm_TRuleSrc()
     * @see tefkat.model.TRule#getSrc
     * @model opposite="src"
     * @generated
     */
    TRule getTRuleSrc();

    /**
     * Sets the value of the '{@link tefkat.model.SourceTerm#getTRuleSrc <em>TRule Src</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>TRule Src</em>' container reference.
     * @see #getTRuleSrc()
     * @generated
     */
    void setTRuleSrc(TRule value);
}
