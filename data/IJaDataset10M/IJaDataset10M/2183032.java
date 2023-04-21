package de.mpiwg.vspace.metamodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.VideoType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Video Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class VideoTypeImpl extends EObjectImpl implements VideoType {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected VideoTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExhibitionPackage.Literals.VIDEO_TYPE;
    }
}
