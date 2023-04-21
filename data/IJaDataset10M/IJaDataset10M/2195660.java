package webml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Content Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link webml.ContentUnit#getTopic <em>Topic</em>}</li>
 * </ul>
 * </p>
 *
 * @see webml.WebmlPackage#getContentUnit()
 * @model annotation="gmf.node label='name'"
 * @generated
 */
public interface ContentUnit extends Unit {

    /**
	 * Returns the value of the '<em><b>Topic</b></em>' containment reference list.
	 * The list contents are of type {@link webml.DocTopic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Topic</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Topic</em>' containment reference list.
	 * @see webml.WebmlPackage#getContentUnit_Topic()
	 * @model containment="true"
	 *        annotation="gmf.compartment foo='bar' layout='list'"
	 * @generated
	 */
    EList<DocTopic> getTopic();
}
