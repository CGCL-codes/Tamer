package hu.cubussapiens.modembed.model.infrastructure;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.infrastructure.Package#getSubPackages <em>Sub Packages</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.model.infrastructure.Package#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.cubussapiens.modembed.model.infrastructure.InfrastructurePackage#getPackage()
 * @model
 * @generated
 */
public interface Package extends NamedElement {

    /**
	 * Returns the value of the '<em><b>Sub Packages</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.infrastructure.Package}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Packages</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Packages</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.infrastructure.InfrastructurePackage#getPackage_SubPackages()
	 * @model containment="true"
	 * @generated
	 */
    EList<Package> getSubPackages();

    /**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.infrastructure.PackageElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.infrastructure.InfrastructurePackage#getPackage_Elements()
	 * @model containment="true"
	 * @generated
	 */
    EList<PackageElement> getElements();
}
