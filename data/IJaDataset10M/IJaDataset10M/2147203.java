package net.sf.xqz.model.engine;

import org.eclipse.emf.cdo.CDOObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQx Event Notifier</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see net.sf.xqz.model.engine.EnginePackage#getIQxEventNotifier()
 * @model interface="true" abstract="true"
 * @extends CDOObject
 * @generated
 */
public interface IQxEventNotifier extends CDOObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void notifyQxEvent(Event event);
}
