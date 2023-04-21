package de.hu_berlin.sam.mmunit.example.company.companymm.impl;

import de.hu_berlin.sam.mmunit.example.company.companymm.CompanymmPackage;
import de.hu_berlin.sam.mmunit.example.company.companymm.Developer;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Developer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DeveloperImpl extends EmployeeImpl implements Developer {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DeveloperImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CompanymmPackage.Literals.DEVELOPER;
    }
}
