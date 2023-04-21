package de.uniAugsburg.MAF.attrmm.model.attrmm;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.attrmmPackage
 * @generated
 */
public interface attrmmFactory extends EFactory {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2009  Christian Saad, University of Augsburg, Germany <www.ds-lab.org>";

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    attrmmFactory eINSTANCE = de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Attribution Collection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribution Collection</em>'.
	 * @generated
	 */
    AttributionCollection createAttributionCollection();

    /**
	 * Returns a new object of class '<em>Attr Extension</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attr Extension</em>'.
	 * @generated
	 */
    AttrExtension createAttrExtension();

    /**
	 * Returns a new object of class '<em>Attr Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attr Definition</em>'.
	 * @generated
	 */
    AttrDefinition createAttrDefinition();

    /**
	 * Returns a new object of class '<em>Attr Occurrence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attr Occurrence</em>'.
	 * @generated
	 */
    AttrOccurrence createAttrOccurrence();

    /**
	 * Returns a new object of class '<em>Attr Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attr Assignment</em>'.
	 * @generated
	 */
    AttrAssignment createAttrAssignment();

    /**
	 * Returns a new object of class '<em>Attr Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attr Constraint</em>'.
	 * @generated
	 */
    AttrConstraint createAttrConstraint();

    /**
	 * Returns a new object of class '<em>Attribution</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribution</em>'.
	 * @generated
	 */
    Attribution createAttribution();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    attrmmPackage getattrmmPackage();
}
