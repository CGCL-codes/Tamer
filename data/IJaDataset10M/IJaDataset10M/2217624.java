package bpmn.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import bpmn.BpmnPackage;
import bpmn.Participant;
import bpmn.WebService;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Web Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link bpmn.impl.WebServiceImpl#getParticipant <em>Participant</em>}</li>
 *   <li>{@link bpmn.impl.WebServiceImpl#getInterface <em>Interface</em>}</li>
 *   <li>{@link bpmn.impl.WebServiceImpl#getOperation <em>Operation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WebServiceImpl extends EObjectImpl implements WebService {

    /**
	 * The cached value of the '{@link #getParticipant() <em>Participant</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParticipant()
	 * @generated
	 * @ordered
	 */
    protected Participant participant = null;

    /**
	 * The default value of the '{@link #getInterface() <em>Interface</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterface()
	 * @generated
	 * @ordered
	 */
    protected static final String INTERFACE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getInterface() <em>Interface</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterface()
	 * @generated
	 * @ordered
	 */
    protected String interface_ = INTERFACE_EDEFAULT;

    /**
	 * The default value of the '{@link #getOperation() <em>Operation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
    protected static final String OPERATION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getOperation() <em>Operation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
    protected String operation = OPERATION_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WebServiceImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return BpmnPackage.Literals.WEB_SERVICE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getInterface() {
        return interface_;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInterface(String newInterface) {
        String oldInterface = interface_;
        interface_ = newInterface;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BpmnPackage.WEB_SERVICE__INTERFACE, oldInterface, interface_));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getOperation() {
        return operation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOperation(String newOperation) {
        String oldOperation = operation;
        operation = newOperation;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BpmnPackage.WEB_SERVICE__OPERATION, oldOperation, operation));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (interface: ");
        result.append(interface_);
        result.append(", operation: ");
        result.append(operation);
        result.append(')');
        return result.toString();
    }
}
