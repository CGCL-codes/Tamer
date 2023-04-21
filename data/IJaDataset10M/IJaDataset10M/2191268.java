package com.ssd.mdaworks.classdiagram.classDiagram;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.ssd.mdaworks.classdiagram.classDiagram.ClassDiagramPackage
 * @generated
 */
public interface ClassDiagramFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ClassDiagramFactory eINSTANCE = com.ssd.mdaworks.classdiagram.classDiagram.impl.ClassDiagramFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>MObject</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MObject</em>'.
	 * @generated
	 */
    MObject createMObject();

    /**
	 * Returns a new object of class '<em>MPackage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MPackage</em>'.
	 * @generated
	 */
    MPackage createMPackage();

    /**
	 * Returns a new object of class '<em>MClass</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MClass</em>'.
	 * @generated
	 */
    MClass createMClass();

    /**
	 * Returns a new object of class '<em>MOperation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MOperation</em>'.
	 * @generated
	 */
    MOperation createMOperation();

    /**
	 * Returns a new object of class '<em>MParameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MParameter</em>'.
	 * @generated
	 */
    MParameter createMParameter();

    /**
	 * Returns a new object of class '<em>MAttribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MAttribute</em>'.
	 * @generated
	 */
    MAttribute createMAttribute();

    /**
	 * Returns a new object of class '<em>MAnnotation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MAnnotation</em>'.
	 * @generated
	 */
    MAnnotation createMAnnotation();

    /**
	 * Returns a new object of class '<em>MReference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MReference</em>'.
	 * @generated
	 */
    MReference createMReference();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    ClassDiagramPackage getClassDiagramPackage();
}
