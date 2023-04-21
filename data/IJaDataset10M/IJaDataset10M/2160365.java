package net.sf.xqz.model.engine;

import net.sf.xqz.model.cmd.Cmd;
import org.eclipse.emf.cdo.CDOObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IX Cmd Interpreter</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see net.sf.xqz.model.engine.EnginePackage#getIXCmdInterpreter()
 * @model interface="true" abstract="true"
 * @extends CDOObject
 * @generated
 */
public interface IXCmdInterpreter extends CDOObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    byte[] cmd2ByteArray(Cmd cmd);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    int getCmdResultLength(Cmd cmd);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void processResults(Cmd cmd, byte[] stream);
}
