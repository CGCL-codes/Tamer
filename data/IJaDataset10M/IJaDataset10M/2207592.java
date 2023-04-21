package org.remus.infomngmnt.calendar.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.remus.infomngmnt.calendar.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "model";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///org/aspencloud/calypso/core/model.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "org.aspencloud.calypso.core.model";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ModelPackage eINSTANCE = org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl.init();

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.CEventImpl <em>CEvent</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.CEventImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getCEvent()
	 * @generated
	 */
    int CEVENT = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__NAME = 0;

    /**
	 * The feature id for the '<em><b>Date Absolute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__DATE_ABSOLUTE = 1;

    /**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__DATE = 2;

    /**
	 * The feature id for the '<em><b>Reference Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__REFERENCE_EVENT = 3;

    /**
	 * The feature id for the '<em><b>Alarm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__ALARM = 4;

    /**
	 * The feature id for the '<em><b>Predecessors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__PREDECESSORS = 5;

    /**
	 * The feature id for the '<em><b>Successors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__SUCCESSORS = 6;

    /**
	 * The feature id for the '<em><b>Alarm Due</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT__ALARM_DUE = 7;

    /**
	 * The number of structural features of the '<em>CEvent</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CEVENT_FEATURE_COUNT = 8;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.TaskImpl <em>Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.TaskImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getTask()
	 * @generated
	 */
    int TASK = 1;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__NAME = 0;

    /**
	 * The feature id for the '<em><b>Priority</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__PRIORITY = 1;

    /**
	 * The feature id for the '<em><b>Details</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__DETAILS = 2;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__OWNER = 3;

    /**
	 * The feature id for the '<em><b>Start</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__START = 4;

    /**
	 * The feature id for the '<em><b>End</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__END = 5;

    /**
	 * The feature id for the '<em><b>Due</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__DUE = 6;

    /**
	 * The feature id for the '<em><b>Cleared</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__CLEARED = 7;

    /**
	 * The feature id for the '<em><b>Progress</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__PROGRESS = 8;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__ID = 9;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__TYPE = 10;

    /**
	 * The feature id for the '<em><b>Notification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__NOTIFICATION = 11;

    /**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK__READONLY = 12;

    /**
	 * The number of structural features of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASK_FEATURE_COUNT = 13;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.ClearedEventImpl <em>Cleared Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.ClearedEventImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getClearedEvent()
	 * @generated
	 */
    int CLEARED_EVENT = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__NAME = CEVENT__NAME;

    /**
	 * The feature id for the '<em><b>Date Absolute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__DATE_ABSOLUTE = CEVENT__DATE_ABSOLUTE;

    /**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__DATE = CEVENT__DATE;

    /**
	 * The feature id for the '<em><b>Reference Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__REFERENCE_EVENT = CEVENT__REFERENCE_EVENT;

    /**
	 * The feature id for the '<em><b>Alarm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__ALARM = CEVENT__ALARM;

    /**
	 * The feature id for the '<em><b>Predecessors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__PREDECESSORS = CEVENT__PREDECESSORS;

    /**
	 * The feature id for the '<em><b>Successors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__SUCCESSORS = CEVENT__SUCCESSORS;

    /**
	 * The feature id for the '<em><b>Alarm Due</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__ALARM_DUE = CEVENT__ALARM_DUE;

    /**
	 * The feature id for the '<em><b>Task</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT__TASK = CEVENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Cleared Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLEARED_EVENT_FEATURE_COUNT = CEVENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.TasklistImpl <em>Tasklist</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.TasklistImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getTasklist()
	 * @generated
	 */
    int TASKLIST = 3;

    /**
	 * The feature id for the '<em><b>Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASKLIST__TASKS = 0;

    /**
	 * The number of structural features of the '<em>Tasklist</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TASKLIST_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.DueEventImpl <em>Due Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.DueEventImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getDueEvent()
	 * @generated
	 */
    int DUE_EVENT = 4;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__NAME = CEVENT__NAME;

    /**
	 * The feature id for the '<em><b>Date Absolute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__DATE_ABSOLUTE = CEVENT__DATE_ABSOLUTE;

    /**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__DATE = CEVENT__DATE;

    /**
	 * The feature id for the '<em><b>Reference Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__REFERENCE_EVENT = CEVENT__REFERENCE_EVENT;

    /**
	 * The feature id for the '<em><b>Alarm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__ALARM = CEVENT__ALARM;

    /**
	 * The feature id for the '<em><b>Predecessors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__PREDECESSORS = CEVENT__PREDECESSORS;

    /**
	 * The feature id for the '<em><b>Successors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__SUCCESSORS = CEVENT__SUCCESSORS;

    /**
	 * The feature id for the '<em><b>Alarm Due</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__ALARM_DUE = CEVENT__ALARM_DUE;

    /**
	 * The feature id for the '<em><b>Task</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT__TASK = CEVENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Due Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DUE_EVENT_FEATURE_COUNT = CEVENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.EndEventImpl <em>End Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.EndEventImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getEndEvent()
	 * @generated
	 */
    int END_EVENT = 5;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__NAME = CEVENT__NAME;

    /**
	 * The feature id for the '<em><b>Date Absolute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__DATE_ABSOLUTE = CEVENT__DATE_ABSOLUTE;

    /**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__DATE = CEVENT__DATE;

    /**
	 * The feature id for the '<em><b>Reference Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__REFERENCE_EVENT = CEVENT__REFERENCE_EVENT;

    /**
	 * The feature id for the '<em><b>Alarm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__ALARM = CEVENT__ALARM;

    /**
	 * The feature id for the '<em><b>Predecessors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__PREDECESSORS = CEVENT__PREDECESSORS;

    /**
	 * The feature id for the '<em><b>Successors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__SUCCESSORS = CEVENT__SUCCESSORS;

    /**
	 * The feature id for the '<em><b>Alarm Due</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__ALARM_DUE = CEVENT__ALARM_DUE;

    /**
	 * The feature id for the '<em><b>Task</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT__TASK = CEVENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>End Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int END_EVENT_FEATURE_COUNT = CEVENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.impl.StartEventImpl <em>Start Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.impl.StartEventImpl
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getStartEvent()
	 * @generated
	 */
    int START_EVENT = 6;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__NAME = CEVENT__NAME;

    /**
	 * The feature id for the '<em><b>Date Absolute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__DATE_ABSOLUTE = CEVENT__DATE_ABSOLUTE;

    /**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__DATE = CEVENT__DATE;

    /**
	 * The feature id for the '<em><b>Reference Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__REFERENCE_EVENT = CEVENT__REFERENCE_EVENT;

    /**
	 * The feature id for the '<em><b>Alarm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__ALARM = CEVENT__ALARM;

    /**
	 * The feature id for the '<em><b>Predecessors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__PREDECESSORS = CEVENT__PREDECESSORS;

    /**
	 * The feature id for the '<em><b>Successors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__SUCCESSORS = CEVENT__SUCCESSORS;

    /**
	 * The feature id for the '<em><b>Alarm Due</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__ALARM_DUE = CEVENT__ALARM_DUE;

    /**
	 * The feature id for the '<em><b>Task</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT__TASK = CEVENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Start Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int START_EVENT_FEATURE_COUNT = CEVENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link org.remus.infomngmnt.calendar.model.TaskType <em>Task Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.remus.infomngmnt.calendar.model.TaskType
	 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getTaskType()
	 * @generated
	 */
    int TASK_TYPE = 7;

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.CEvent <em>CEvent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>CEvent</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent
	 * @generated
	 */
    EClass getCEvent();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.CEvent#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#getName()
	 * @see #getCEvent()
	 * @generated
	 */
    EAttribute getCEvent_Name();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.CEvent#isDateAbsolute <em>Date Absolute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date Absolute</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#isDateAbsolute()
	 * @see #getCEvent()
	 * @generated
	 */
    EAttribute getCEvent_DateAbsolute();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.CEvent#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#getDate()
	 * @see #getCEvent()
	 * @generated
	 */
    EAttribute getCEvent_Date();

    /**
	 * Returns the meta object for the reference '{@link org.remus.infomngmnt.calendar.model.CEvent#getReferenceEvent <em>Reference Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference Event</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#getReferenceEvent()
	 * @see #getCEvent()
	 * @generated
	 */
    EReference getCEvent_ReferenceEvent();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.CEvent#isAlarm <em>Alarm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alarm</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#isAlarm()
	 * @see #getCEvent()
	 * @generated
	 */
    EAttribute getCEvent_Alarm();

    /**
	 * Returns the meta object for the reference list '{@link org.remus.infomngmnt.calendar.model.CEvent#getPredecessors <em>Predecessors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Predecessors</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#getPredecessors()
	 * @see #getCEvent()
	 * @generated
	 */
    EReference getCEvent_Predecessors();

    /**
	 * Returns the meta object for the reference list '{@link org.remus.infomngmnt.calendar.model.CEvent#getSuccessors <em>Successors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Successors</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#getSuccessors()
	 * @see #getCEvent()
	 * @generated
	 */
    EReference getCEvent_Successors();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.CEvent#isAlarmDue <em>Alarm Due</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alarm Due</em>'.
	 * @see org.remus.infomngmnt.calendar.model.CEvent#isAlarmDue()
	 * @see #getCEvent()
	 * @generated
	 */
    EAttribute getCEvent_AlarmDue();

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.Task <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task
	 * @generated
	 */
    EClass getTask();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getName()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Name();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getPriority <em>Priority</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getPriority()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Priority();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getDetails <em>Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Details</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getDetails()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Details();

    /**
	 * Returns the meta object for the container reference '{@link org.remus.infomngmnt.calendar.model.Task#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Owner</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getOwner()
	 * @see #getTask()
	 * @generated
	 */
    EReference getTask_Owner();

    /**
	 * Returns the meta object for the containment reference '{@link org.remus.infomngmnt.calendar.model.Task#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getStart()
	 * @see #getTask()
	 * @generated
	 */
    EReference getTask_Start();

    /**
	 * Returns the meta object for the containment reference '{@link org.remus.infomngmnt.calendar.model.Task#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getEnd()
	 * @see #getTask()
	 * @generated
	 */
    EReference getTask_End();

    /**
	 * Returns the meta object for the containment reference '{@link org.remus.infomngmnt.calendar.model.Task#getDue <em>Due</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Due</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getDue()
	 * @see #getTask()
	 * @generated
	 */
    EReference getTask_Due();

    /**
	 * Returns the meta object for the containment reference '{@link org.remus.infomngmnt.calendar.model.Task#getCleared <em>Cleared</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cleared</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getCleared()
	 * @see #getTask()
	 * @generated
	 */
    EReference getTask_Cleared();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getProgress <em>Progress</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Progress</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getProgress()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Progress();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getId()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Id();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getType()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Type();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#getNotification <em>Notification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notification</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#getNotification()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Notification();

    /**
	 * Returns the meta object for the attribute '{@link org.remus.infomngmnt.calendar.model.Task#isReadonly <em>Readonly</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Readonly</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Task#isReadonly()
	 * @see #getTask()
	 * @generated
	 */
    EAttribute getTask_Readonly();

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.ClearedEvent <em>Cleared Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cleared Event</em>'.
	 * @see org.remus.infomngmnt.calendar.model.ClearedEvent
	 * @generated
	 */
    EClass getClearedEvent();

    /**
	 * Returns the meta object for the container reference '{@link org.remus.infomngmnt.calendar.model.ClearedEvent#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Task</em>'.
	 * @see org.remus.infomngmnt.calendar.model.ClearedEvent#getTask()
	 * @see #getClearedEvent()
	 * @generated
	 */
    EReference getClearedEvent_Task();

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.Tasklist <em>Tasklist</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tasklist</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Tasklist
	 * @generated
	 */
    EClass getTasklist();

    /**
	 * Returns the meta object for the containment reference list '{@link org.remus.infomngmnt.calendar.model.Tasklist#getTasks <em>Tasks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tasks</em>'.
	 * @see org.remus.infomngmnt.calendar.model.Tasklist#getTasks()
	 * @see #getTasklist()
	 * @generated
	 */
    EReference getTasklist_Tasks();

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.DueEvent <em>Due Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Due Event</em>'.
	 * @see org.remus.infomngmnt.calendar.model.DueEvent
	 * @generated
	 */
    EClass getDueEvent();

    /**
	 * Returns the meta object for the container reference '{@link org.remus.infomngmnt.calendar.model.DueEvent#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Task</em>'.
	 * @see org.remus.infomngmnt.calendar.model.DueEvent#getTask()
	 * @see #getDueEvent()
	 * @generated
	 */
    EReference getDueEvent_Task();

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.EndEvent <em>End Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>End Event</em>'.
	 * @see org.remus.infomngmnt.calendar.model.EndEvent
	 * @generated
	 */
    EClass getEndEvent();

    /**
	 * Returns the meta object for the container reference '{@link org.remus.infomngmnt.calendar.model.EndEvent#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Task</em>'.
	 * @see org.remus.infomngmnt.calendar.model.EndEvent#getTask()
	 * @see #getEndEvent()
	 * @generated
	 */
    EReference getEndEvent_Task();

    /**
	 * Returns the meta object for class '{@link org.remus.infomngmnt.calendar.model.StartEvent <em>Start Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Start Event</em>'.
	 * @see org.remus.infomngmnt.calendar.model.StartEvent
	 * @generated
	 */
    EClass getStartEvent();

    /**
	 * Returns the meta object for the container reference '{@link org.remus.infomngmnt.calendar.model.StartEvent#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Task</em>'.
	 * @see org.remus.infomngmnt.calendar.model.StartEvent#getTask()
	 * @see #getStartEvent()
	 * @generated
	 */
    EReference getStartEvent_Task();

    /**
	 * Returns the meta object for enum '{@link org.remus.infomngmnt.calendar.model.TaskType <em>Task Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Task Type</em>'.
	 * @see org.remus.infomngmnt.calendar.model.TaskType
	 * @generated
	 */
    EEnum getTaskType();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    ModelFactory getModelFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.CEventImpl <em>CEvent</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.CEventImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getCEvent()
		 * @generated
		 */
        EClass CEVENT = eINSTANCE.getCEvent();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CEVENT__NAME = eINSTANCE.getCEvent_Name();

        /**
		 * The meta object literal for the '<em><b>Date Absolute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CEVENT__DATE_ABSOLUTE = eINSTANCE.getCEvent_DateAbsolute();

        /**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CEVENT__DATE = eINSTANCE.getCEvent_Date();

        /**
		 * The meta object literal for the '<em><b>Reference Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CEVENT__REFERENCE_EVENT = eINSTANCE.getCEvent_ReferenceEvent();

        /**
		 * The meta object literal for the '<em><b>Alarm</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CEVENT__ALARM = eINSTANCE.getCEvent_Alarm();

        /**
		 * The meta object literal for the '<em><b>Predecessors</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CEVENT__PREDECESSORS = eINSTANCE.getCEvent_Predecessors();

        /**
		 * The meta object literal for the '<em><b>Successors</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CEVENT__SUCCESSORS = eINSTANCE.getCEvent_Successors();

        /**
		 * The meta object literal for the '<em><b>Alarm Due</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CEVENT__ALARM_DUE = eINSTANCE.getCEvent_AlarmDue();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.TaskImpl <em>Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.TaskImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getTask()
		 * @generated
		 */
        EClass TASK = eINSTANCE.getTask();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__NAME = eINSTANCE.getTask_Name();

        /**
		 * The meta object literal for the '<em><b>Priority</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__PRIORITY = eINSTANCE.getTask_Priority();

        /**
		 * The meta object literal for the '<em><b>Details</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__DETAILS = eINSTANCE.getTask_Details();

        /**
		 * The meta object literal for the '<em><b>Owner</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TASK__OWNER = eINSTANCE.getTask_Owner();

        /**
		 * The meta object literal for the '<em><b>Start</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TASK__START = eINSTANCE.getTask_Start();

        /**
		 * The meta object literal for the '<em><b>End</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TASK__END = eINSTANCE.getTask_End();

        /**
		 * The meta object literal for the '<em><b>Due</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TASK__DUE = eINSTANCE.getTask_Due();

        /**
		 * The meta object literal for the '<em><b>Cleared</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TASK__CLEARED = eINSTANCE.getTask_Cleared();

        /**
		 * The meta object literal for the '<em><b>Progress</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__PROGRESS = eINSTANCE.getTask_Progress();

        /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__ID = eINSTANCE.getTask_Id();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__TYPE = eINSTANCE.getTask_Type();

        /**
		 * The meta object literal for the '<em><b>Notification</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__NOTIFICATION = eINSTANCE.getTask_Notification();

        /**
		 * The meta object literal for the '<em><b>Readonly</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TASK__READONLY = eINSTANCE.getTask_Readonly();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.ClearedEventImpl <em>Cleared Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.ClearedEventImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getClearedEvent()
		 * @generated
		 */
        EClass CLEARED_EVENT = eINSTANCE.getClearedEvent();

        /**
		 * The meta object literal for the '<em><b>Task</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLEARED_EVENT__TASK = eINSTANCE.getClearedEvent_Task();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.TasklistImpl <em>Tasklist</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.TasklistImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getTasklist()
		 * @generated
		 */
        EClass TASKLIST = eINSTANCE.getTasklist();

        /**
		 * The meta object literal for the '<em><b>Tasks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TASKLIST__TASKS = eINSTANCE.getTasklist_Tasks();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.DueEventImpl <em>Due Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.DueEventImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getDueEvent()
		 * @generated
		 */
        EClass DUE_EVENT = eINSTANCE.getDueEvent();

        /**
		 * The meta object literal for the '<em><b>Task</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DUE_EVENT__TASK = eINSTANCE.getDueEvent_Task();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.EndEventImpl <em>End Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.EndEventImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getEndEvent()
		 * @generated
		 */
        EClass END_EVENT = eINSTANCE.getEndEvent();

        /**
		 * The meta object literal for the '<em><b>Task</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference END_EVENT__TASK = eINSTANCE.getEndEvent_Task();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.impl.StartEventImpl <em>Start Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.impl.StartEventImpl
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getStartEvent()
		 * @generated
		 */
        EClass START_EVENT = eINSTANCE.getStartEvent();

        /**
		 * The meta object literal for the '<em><b>Task</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference START_EVENT__TASK = eINSTANCE.getStartEvent_Task();

        /**
		 * The meta object literal for the '{@link org.remus.infomngmnt.calendar.model.TaskType <em>Task Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.remus.infomngmnt.calendar.model.TaskType
		 * @see org.remus.infomngmnt.calendar.model.impl.ModelPackageImpl#getTaskType()
		 * @generated
		 */
        EEnum TASK_TYPE = eINSTANCE.getTaskType();
    }
}
