package eu.medeia.caex.model.CAEXClassModelV215;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ref Semantic Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.caex.model.CAEXClassModelV215.RefSemanticType#getCorrespondingAttributePath <em>Corresponding Attribute Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package#getRefSemanticType()
 * @model extendedMetaData="name='RefSemantic_._type' kind='elementOnly'"
 * @generated
 */
public interface RefSemanticType extends CAEXBasicObject {

    /**
	 * Returns the value of the '<em><b>Corresponding Attribute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Corresponding Attribute Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Corresponding Attribute Path</em>' attribute.
	 * @see #setCorrespondingAttributePath(String)
	 * @see eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package#getRefSemanticType_CorrespondingAttributePath()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='CorrespondingAttributePath' namespace='##targetNamespace'"
	 * @generated
	 */
    String getCorrespondingAttributePath();

    /**
	 * Sets the value of the '{@link eu.medeia.caex.model.CAEXClassModelV215.RefSemanticType#getCorrespondingAttributePath <em>Corresponding Attribute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Corresponding Attribute Path</em>' attribute.
	 * @see #getCorrespondingAttributePath()
	 * @generated
	 */
    void setCorrespondingAttributePath(String value);
}
