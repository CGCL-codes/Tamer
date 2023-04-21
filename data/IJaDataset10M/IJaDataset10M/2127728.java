package org.enml.subjects;

import org.eclipse.emf.common.util.EList;
import org.enml.net.Network;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Noise Owner</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.enml.subjects.NoiseOwner#getNetworks <em>Networks</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.enml.subjects.SubjectsPackage#getNoiseOwner()
 * @model
 * @generated
 */
public interface NoiseOwner extends Subject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * @return <code>NoiseOwner</code> owned <code>Network</code> collection
	 * @model required="true" opposite="owner"
	 */
    EList<Network> getNetworks();
}
