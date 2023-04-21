package net.sourceforge.olympos.dsl.platform.platform;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sourceforge.olympos.dsl.platform.platform.Section#getMembers <em>Members</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sourceforge.olympos.dsl.platform.platform.PlatformPackage#getSection()
 * @model
 * @generated
 */
public interface Section extends Elements, SectionMember {

    /**
	 * Returns the value of the '<em><b>Members</b></em>' containment reference list.
	 * The list contents are of type {@link net.sourceforge.olympos.dsl.platform.platform.SectionMember}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Members</em>' containment reference list.
	 * @see net.sourceforge.olympos.dsl.platform.platform.PlatformPackage#getSection_Members()
	 * @model containment="true"
	 * @generated
	 */
    EList<SectionMember> getMembers();
}
