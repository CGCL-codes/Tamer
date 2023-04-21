package com.safi.core.scripting;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.safi.core.scripting.ScriptingPackage
 * @generated
 */
public interface ScriptingFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    ScriptingFactory eINSTANCE = com.safi.core.scripting.impl.ScriptingFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Rhino Saflet Script</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rhino Saflet Script</em>'.
	 * @generated
	 */
    RhinoSafletScript createRhinoSafletScript();

    /**
	 * Returns a new object of class '<em>Rhino Saflet Script Environment</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rhino Saflet Script Environment</em>'.
	 * @generated
	 */
    RhinoSafletScriptEnvironment createRhinoSafletScriptEnvironment();

    /**
	 * Returns a new object of class '<em>Rhino Saflet Script Factory</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rhino Saflet Script Factory</em>'.
	 * @generated
	 */
    RhinoSafletScriptFactory createRhinoSafletScriptFactory();

    /**
	 * Returns a new object of class '<em>Rhino Script Scope</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rhino Script Scope</em>'.
	 * @generated
	 */
    RhinoScriptScope createRhinoScriptScope();

    /**
	 * Returns a new object of class '<em>Rhino Script Scope Factory</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rhino Script Scope Factory</em>'.
	 * @generated
	 */
    RhinoScriptScopeFactory createRhinoScriptScopeFactory();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    ScriptingPackage getScriptingPackage();
}
