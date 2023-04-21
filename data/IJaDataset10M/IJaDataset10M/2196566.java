package de.fraunhofer.isst.eastadl.elements;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- BEGIN-EAST-ADL2-SPEC -->
 * <strong>Comment</strong> (from Elements)
 * <p>
 * <strong>Generalizations</strong>
 * <br> None
 * <p>
 * <strong>Description</strong>
 * <br> Comment represents a textual annotation.
 * <p>
 * <strong>Attributes</strong>
 * <br> body : String [1]
 * <br> - Specifies a string that is the comment.
 * <p>
 * <strong>Associations</strong>
 * <br> No additional associations
 * <p>
 * <strong>Constraints</strong>
 * <br> No additional constraints
 * <p>
 * <strong>Semantics</strong>
 * <br> -
 * <!-- END-EAST-ADL2-SPEC -->
 * 
 * @author dprenzel
 *
 * @model
 */
public interface Comment extends EObject {

    /**
	 * Returns the value of the '<em><b>Body</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>body : String [1]</strong>
	 * <br> Specifies a string that is the comment.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' attribute.
	 * @see #setBody(String)
	 * @see de.fraunhofer.isst.eastadl.elements.ElementsPackage#getComment_Body()
	 * @model required="true"
	 */
    String getBody();

    /**
	 * Sets the value of the '{@link de.fraunhofer.isst.eastadl.elements.Comment#getBody <em>Body</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>body : String [1]</strong>
	 * <br> Specifies a string that is the comment.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body</em>' attribute.
	 * @see #getBody()
	 * @generated
	 */
    void setBody(String value);
}
