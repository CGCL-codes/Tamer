package net.sf.xqz.model.engine;

import net.sf.xqz.model.cmd.Cmd;
import org.eclipse.emf.cdo.CDOObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ICmd Engine Action</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see net.sf.xqz.model.engine.EnginePackage#getICmdEngineAction()
 * @model interface="true" abstract="true"
 * @extends CDOObject
 * @generated
 */
public interface ICmdEngineAction extends CDOObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void run(Qx qx, Cmd cmd);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void run(Qx qx);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void run(Qx qx, String args);
}
