package net.jonbuck.tassoo.model.impl;

import java.util.Collection;
import java.util.Date;
import net.jonbuck.tassoo.model.AttachmentContainer;
import net.jonbuck.tassoo.model.Category;
import net.jonbuck.tassoo.model.Comment;
import net.jonbuck.tassoo.model.Container;
import net.jonbuck.tassoo.model.Priority;
import net.jonbuck.tassoo.model.Recurrence;
import net.jonbuck.tassoo.model.Status;
import net.jonbuck.tassoo.model.Task;
import net.jonbuck.tassoo.model.TassooPackage;
import net.jonbuck.tassoo.model.Type;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getActualTime <em>Actual Time</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getAttachmentContainer <em>Attachment Container</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getCompletedDate <em>Completed Date</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getContainer <em>Container</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getEstimatedTime <em>Estimated Time</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getDueDate <em>Due Date</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getPosition <em>Position</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getPriority <em>Priority</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#isRecurrenceIndicator <em>Recurrence Indicator</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getRecurrenceSettings <em>Recurrence Settings</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#isReminderIndicator <em>Reminder Indicator</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getTask <em>Task</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getTaskId <em>Task Id</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getTaskName <em>Task Name</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getTaskSummary <em>Task Summary</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.TaskImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TaskImpl extends BaseObjectImpl implements Task {

    /**
	 * The default value of the '{@link #getActualTime() <em>Actual Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActualTime()
	 * @generated
	 * @ordered
	 */
    protected static final String ACTUAL_TIME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getActualTime() <em>Actual Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActualTime()
	 * @generated
	 * @ordered
	 */
    protected String actualTime = ACTUAL_TIME_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAttachmentContainer() <em>Attachment Container</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttachmentContainer()
	 * @generated
	 * @ordered
	 */
    protected AttachmentContainer attachmentContainer;

    /**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
    protected Category category;

    /**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
    protected EList<Comment> comment;

    /**
	 * The default value of the '{@link #getCompletedDate() <em>Completed Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompletedDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date COMPLETED_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCompletedDate() <em>Completed Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompletedDate()
	 * @generated
	 * @ordered
	 */
    protected Date completedDate = COMPLETED_DATE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getContainer() <em>Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainer()
	 * @generated
	 * @ordered
	 */
    protected Container container;

    /**
	 * The default value of the '{@link #getEstimatedTime() <em>Estimated Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEstimatedTime()
	 * @generated
	 * @ordered
	 */
    protected static final String ESTIMATED_TIME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getEstimatedTime() <em>Estimated Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEstimatedTime()
	 * @generated
	 * @ordered
	 */
    protected String estimatedTime = ESTIMATED_TIME_EDEFAULT;

    /**
	 * The default value of the '{@link #getDueDate() <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDueDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date DUE_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDueDate() <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDueDate()
	 * @generated
	 * @ordered
	 */
    protected Date dueDate = DUE_DATE_EDEFAULT;

    /**
	 * The default value of the '{@link #getPosition() <em>Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPosition()
	 * @generated
	 * @ordered
	 */
    protected static final int POSITION_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPosition()
	 * @generated
	 * @ordered
	 */
    protected int position = POSITION_EDEFAULT;

    /**
	 * The cached value of the '{@link #getPriority() <em>Priority</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
    protected Priority priority;

    /**
	 * The default value of the '{@link #isRecurrenceIndicator() <em>Recurrence Indicator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRecurrenceIndicator()
	 * @generated
	 * @ordered
	 */
    protected static final boolean RECURRENCE_INDICATOR_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isRecurrenceIndicator() <em>Recurrence Indicator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRecurrenceIndicator()
	 * @generated
	 * @ordered
	 */
    protected boolean recurrenceIndicator = RECURRENCE_INDICATOR_EDEFAULT;

    /**
	 * The cached value of the '{@link #getRecurrenceSettings() <em>Recurrence Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRecurrenceSettings()
	 * @generated
	 * @ordered
	 */
    protected Recurrence recurrenceSettings;

    /**
	 * The default value of the '{@link #isReminderIndicator() <em>Reminder Indicator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReminderIndicator()
	 * @generated
	 * @ordered
	 */
    protected static final boolean REMINDER_INDICATOR_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isReminderIndicator() <em>Reminder Indicator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReminderIndicator()
	 * @generated
	 * @ordered
	 */
    protected boolean reminderIndicator = REMINDER_INDICATOR_EDEFAULT;

    /**
	 * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date START_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
    protected Date startDate = START_DATE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
    protected Status status;

    /**
	 * The cached value of the '{@link #getTask() <em>Task</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTask()
	 * @generated
	 * @ordered
	 */
    protected EList<Task> task;

    /**
	 * The default value of the '{@link #getTaskId() <em>Task Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskId()
	 * @generated
	 * @ordered
	 */
    protected static final int TASK_ID_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getTaskId() <em>Task Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskId()
	 * @generated
	 * @ordered
	 */
    protected int taskId = TASK_ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getTaskName() <em>Task Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskName()
	 * @generated
	 * @ordered
	 */
    protected static final String TASK_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTaskName() <em>Task Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskName()
	 * @generated
	 * @ordered
	 */
    protected String taskName = TASK_NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getTaskSummary() <em>Task Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskSummary()
	 * @generated
	 * @ordered
	 */
    protected static final String TASK_SUMMARY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTaskSummary() <em>Task Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskSummary()
	 * @generated
	 * @ordered
	 */
    protected String taskSummary = TASK_SUMMARY_EDEFAULT;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected Type type;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TaskImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TassooPackage.Literals.TASK;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getActualTime() {
        return actualTime;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setActualTime(String newActualTime) {
        String oldActualTime = actualTime;
        actualTime = newActualTime;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__ACTUAL_TIME, oldActualTime, actualTime));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AttachmentContainer getAttachmentContainer() {
        return attachmentContainer;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAttachmentContainer(AttachmentContainer newAttachmentContainer, NotificationChain msgs) {
        AttachmentContainer oldAttachmentContainer = attachmentContainer;
        attachmentContainer = newAttachmentContainer;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__ATTACHMENT_CONTAINER, oldAttachmentContainer, newAttachmentContainer);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAttachmentContainer(AttachmentContainer newAttachmentContainer) {
        if (newAttachmentContainer != attachmentContainer) {
            NotificationChain msgs = null;
            if (attachmentContainer != null) msgs = ((InternalEObject) attachmentContainer).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TassooPackage.TASK__ATTACHMENT_CONTAINER, null, msgs);
            if (newAttachmentContainer != null) msgs = ((InternalEObject) newAttachmentContainer).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TassooPackage.TASK__ATTACHMENT_CONTAINER, null, msgs);
            msgs = basicSetAttachmentContainer(newAttachmentContainer, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__ATTACHMENT_CONTAINER, newAttachmentContainer, newAttachmentContainer));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Category getCategory() {
        if (category != null && category.eIsProxy()) {
            InternalEObject oldCategory = (InternalEObject) category;
            category = (Category) eResolveProxy(oldCategory);
            if (category != oldCategory) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TassooPackage.TASK__CATEGORY, oldCategory, category));
            }
        }
        return category;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Category basicGetCategory() {
        return category;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCategory(Category newCategory) {
        Category oldCategory = category;
        category = newCategory;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__CATEGORY, oldCategory, category));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Comment> getComment() {
        if (comment == null) {
            comment = new EObjectContainmentEList<Comment>(Comment.class, this, TassooPackage.TASK__COMMENT);
        }
        return comment;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getCompletedDate() {
        return completedDate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCompletedDate(Date newCompletedDate) {
        Date oldCompletedDate = completedDate;
        completedDate = newCompletedDate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__COMPLETED_DATE, oldCompletedDate, completedDate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Container getContainer() {
        if (container != null && container.eIsProxy()) {
            InternalEObject oldContainer = (InternalEObject) container;
            container = (Container) eResolveProxy(oldContainer);
            if (container != oldContainer) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TassooPackage.TASK__CONTAINER, oldContainer, container));
            }
        }
        return container;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Container basicGetContainer() {
        return container;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContainer(Container newContainer) {
        Container oldContainer = container;
        container = newContainer;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__CONTAINER, oldContainer, container));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getEstimatedTime() {
        return estimatedTime;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEstimatedTime(String newEstimatedTime) {
        String oldEstimatedTime = estimatedTime;
        estimatedTime = newEstimatedTime;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__ESTIMATED_TIME, oldEstimatedTime, estimatedTime));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getDueDate() {
        return dueDate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDueDate(Date newDueDate) {
        Date oldDueDate = dueDate;
        dueDate = newDueDate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__DUE_DATE, oldDueDate, dueDate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getPosition() {
        return position;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPosition(int newPosition) {
        int oldPosition = position;
        position = newPosition;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__POSITION, oldPosition, position));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Priority getPriority() {
        if (priority != null && priority.eIsProxy()) {
            InternalEObject oldPriority = (InternalEObject) priority;
            priority = (Priority) eResolveProxy(oldPriority);
            if (priority != oldPriority) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TassooPackage.TASK__PRIORITY, oldPriority, priority));
            }
        }
        return priority;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Priority basicGetPriority() {
        return priority;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPriority(Priority newPriority) {
        Priority oldPriority = priority;
        priority = newPriority;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__PRIORITY, oldPriority, priority));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isRecurrenceIndicator() {
        return recurrenceIndicator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRecurrenceIndicator(boolean newRecurrenceIndicator) {
        boolean oldRecurrenceIndicator = recurrenceIndicator;
        recurrenceIndicator = newRecurrenceIndicator;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__RECURRENCE_INDICATOR, oldRecurrenceIndicator, recurrenceIndicator));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Recurrence getRecurrenceSettings() {
        if (recurrenceSettings != null && recurrenceSettings.eIsProxy()) {
            InternalEObject oldRecurrenceSettings = (InternalEObject) recurrenceSettings;
            recurrenceSettings = (Recurrence) eResolveProxy(oldRecurrenceSettings);
            if (recurrenceSettings != oldRecurrenceSettings) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TassooPackage.TASK__RECURRENCE_SETTINGS, oldRecurrenceSettings, recurrenceSettings));
            }
        }
        return recurrenceSettings;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Recurrence basicGetRecurrenceSettings() {
        return recurrenceSettings;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRecurrenceSettings(Recurrence newRecurrenceSettings) {
        Recurrence oldRecurrenceSettings = recurrenceSettings;
        recurrenceSettings = newRecurrenceSettings;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__RECURRENCE_SETTINGS, oldRecurrenceSettings, recurrenceSettings));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isReminderIndicator() {
        return reminderIndicator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReminderIndicator(boolean newReminderIndicator) {
        boolean oldReminderIndicator = reminderIndicator;
        reminderIndicator = newReminderIndicator;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__REMINDER_INDICATOR, oldReminderIndicator, reminderIndicator));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getStartDate() {
        return startDate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStartDate(Date newStartDate) {
        Date oldStartDate = startDate;
        startDate = newStartDate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__START_DATE, oldStartDate, startDate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Status getStatus() {
        if (status != null && status.eIsProxy()) {
            InternalEObject oldStatus = (InternalEObject) status;
            status = (Status) eResolveProxy(oldStatus);
            if (status != oldStatus) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TassooPackage.TASK__STATUS, oldStatus, status));
            }
        }
        return status;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Status basicGetStatus() {
        return status;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatus(Status newStatus) {
        Status oldStatus = status;
        status = newStatus;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__STATUS, oldStatus, status));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Task> getTask() {
        if (task == null) {
            task = new EObjectContainmentEList<Task>(Task.class, this, TassooPackage.TASK__TASK);
        }
        return task;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getTaskId() {
        return taskId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTaskId(int newTaskId) {
        int oldTaskId = taskId;
        taskId = newTaskId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__TASK_ID, oldTaskId, taskId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTaskName() {
        return taskName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTaskName(String newTaskName) {
        String oldTaskName = taskName;
        taskName = newTaskName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__TASK_NAME, oldTaskName, taskName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTaskSummary() {
        return taskSummary;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTaskSummary(String newTaskSummary) {
        String oldTaskSummary = taskSummary;
        taskSummary = newTaskSummary;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__TASK_SUMMARY, oldTaskSummary, taskSummary));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Type getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (Type) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TassooPackage.TASK__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Type basicGetType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(Type newType) {
        Type oldType = type;
        type = newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.TASK__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TassooPackage.TASK__ATTACHMENT_CONTAINER:
                return basicSetAttachmentContainer(null, msgs);
            case TassooPackage.TASK__COMMENT:
                return ((InternalEList<?>) getComment()).basicRemove(otherEnd, msgs);
            case TassooPackage.TASK__TASK:
                return ((InternalEList<?>) getTask()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TassooPackage.TASK__ACTUAL_TIME:
                return getActualTime();
            case TassooPackage.TASK__ATTACHMENT_CONTAINER:
                return getAttachmentContainer();
            case TassooPackage.TASK__CATEGORY:
                if (resolve) return getCategory();
                return basicGetCategory();
            case TassooPackage.TASK__COMMENT:
                return getComment();
            case TassooPackage.TASK__COMPLETED_DATE:
                return getCompletedDate();
            case TassooPackage.TASK__CONTAINER:
                if (resolve) return getContainer();
                return basicGetContainer();
            case TassooPackage.TASK__ESTIMATED_TIME:
                return getEstimatedTime();
            case TassooPackage.TASK__DUE_DATE:
                return getDueDate();
            case TassooPackage.TASK__POSITION:
                return getPosition();
            case TassooPackage.TASK__PRIORITY:
                if (resolve) return getPriority();
                return basicGetPriority();
            case TassooPackage.TASK__RECURRENCE_INDICATOR:
                return isRecurrenceIndicator();
            case TassooPackage.TASK__RECURRENCE_SETTINGS:
                if (resolve) return getRecurrenceSettings();
                return basicGetRecurrenceSettings();
            case TassooPackage.TASK__REMINDER_INDICATOR:
                return isReminderIndicator();
            case TassooPackage.TASK__START_DATE:
                return getStartDate();
            case TassooPackage.TASK__STATUS:
                if (resolve) return getStatus();
                return basicGetStatus();
            case TassooPackage.TASK__TASK:
                return getTask();
            case TassooPackage.TASK__TASK_ID:
                return getTaskId();
            case TassooPackage.TASK__TASK_NAME:
                return getTaskName();
            case TassooPackage.TASK__TASK_SUMMARY:
                return getTaskSummary();
            case TassooPackage.TASK__TYPE:
                if (resolve) return getType();
                return basicGetType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case TassooPackage.TASK__ACTUAL_TIME:
                setActualTime((String) newValue);
                return;
            case TassooPackage.TASK__ATTACHMENT_CONTAINER:
                setAttachmentContainer((AttachmentContainer) newValue);
                return;
            case TassooPackage.TASK__CATEGORY:
                setCategory((Category) newValue);
                return;
            case TassooPackage.TASK__COMMENT:
                getComment().clear();
                getComment().addAll((Collection<? extends Comment>) newValue);
                return;
            case TassooPackage.TASK__COMPLETED_DATE:
                setCompletedDate((Date) newValue);
                return;
            case TassooPackage.TASK__CONTAINER:
                setContainer((Container) newValue);
                return;
            case TassooPackage.TASK__ESTIMATED_TIME:
                setEstimatedTime((String) newValue);
                return;
            case TassooPackage.TASK__DUE_DATE:
                setDueDate((Date) newValue);
                return;
            case TassooPackage.TASK__POSITION:
                setPosition((Integer) newValue);
                return;
            case TassooPackage.TASK__PRIORITY:
                setPriority((Priority) newValue);
                return;
            case TassooPackage.TASK__RECURRENCE_INDICATOR:
                setRecurrenceIndicator((Boolean) newValue);
                return;
            case TassooPackage.TASK__RECURRENCE_SETTINGS:
                setRecurrenceSettings((Recurrence) newValue);
                return;
            case TassooPackage.TASK__REMINDER_INDICATOR:
                setReminderIndicator((Boolean) newValue);
                return;
            case TassooPackage.TASK__START_DATE:
                setStartDate((Date) newValue);
                return;
            case TassooPackage.TASK__STATUS:
                setStatus((Status) newValue);
                return;
            case TassooPackage.TASK__TASK:
                getTask().clear();
                getTask().addAll((Collection<? extends Task>) newValue);
                return;
            case TassooPackage.TASK__TASK_ID:
                setTaskId((Integer) newValue);
                return;
            case TassooPackage.TASK__TASK_NAME:
                setTaskName((String) newValue);
                return;
            case TassooPackage.TASK__TASK_SUMMARY:
                setTaskSummary((String) newValue);
                return;
            case TassooPackage.TASK__TYPE:
                setType((Type) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case TassooPackage.TASK__ACTUAL_TIME:
                setActualTime(ACTUAL_TIME_EDEFAULT);
                return;
            case TassooPackage.TASK__ATTACHMENT_CONTAINER:
                setAttachmentContainer((AttachmentContainer) null);
                return;
            case TassooPackage.TASK__CATEGORY:
                setCategory((Category) null);
                return;
            case TassooPackage.TASK__COMMENT:
                getComment().clear();
                return;
            case TassooPackage.TASK__COMPLETED_DATE:
                setCompletedDate(COMPLETED_DATE_EDEFAULT);
                return;
            case TassooPackage.TASK__CONTAINER:
                setContainer((Container) null);
                return;
            case TassooPackage.TASK__ESTIMATED_TIME:
                setEstimatedTime(ESTIMATED_TIME_EDEFAULT);
                return;
            case TassooPackage.TASK__DUE_DATE:
                setDueDate(DUE_DATE_EDEFAULT);
                return;
            case TassooPackage.TASK__POSITION:
                setPosition(POSITION_EDEFAULT);
                return;
            case TassooPackage.TASK__PRIORITY:
                setPriority((Priority) null);
                return;
            case TassooPackage.TASK__RECURRENCE_INDICATOR:
                setRecurrenceIndicator(RECURRENCE_INDICATOR_EDEFAULT);
                return;
            case TassooPackage.TASK__RECURRENCE_SETTINGS:
                setRecurrenceSettings((Recurrence) null);
                return;
            case TassooPackage.TASK__REMINDER_INDICATOR:
                setReminderIndicator(REMINDER_INDICATOR_EDEFAULT);
                return;
            case TassooPackage.TASK__START_DATE:
                setStartDate(START_DATE_EDEFAULT);
                return;
            case TassooPackage.TASK__STATUS:
                setStatus((Status) null);
                return;
            case TassooPackage.TASK__TASK:
                getTask().clear();
                return;
            case TassooPackage.TASK__TASK_ID:
                setTaskId(TASK_ID_EDEFAULT);
                return;
            case TassooPackage.TASK__TASK_NAME:
                setTaskName(TASK_NAME_EDEFAULT);
                return;
            case TassooPackage.TASK__TASK_SUMMARY:
                setTaskSummary(TASK_SUMMARY_EDEFAULT);
                return;
            case TassooPackage.TASK__TYPE:
                setType((Type) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case TassooPackage.TASK__ACTUAL_TIME:
                return ACTUAL_TIME_EDEFAULT == null ? actualTime != null : !ACTUAL_TIME_EDEFAULT.equals(actualTime);
            case TassooPackage.TASK__ATTACHMENT_CONTAINER:
                return attachmentContainer != null;
            case TassooPackage.TASK__CATEGORY:
                return category != null;
            case TassooPackage.TASK__COMMENT:
                return comment != null && !comment.isEmpty();
            case TassooPackage.TASK__COMPLETED_DATE:
                return COMPLETED_DATE_EDEFAULT == null ? completedDate != null : !COMPLETED_DATE_EDEFAULT.equals(completedDate);
            case TassooPackage.TASK__CONTAINER:
                return container != null;
            case TassooPackage.TASK__ESTIMATED_TIME:
                return ESTIMATED_TIME_EDEFAULT == null ? estimatedTime != null : !ESTIMATED_TIME_EDEFAULT.equals(estimatedTime);
            case TassooPackage.TASK__DUE_DATE:
                return DUE_DATE_EDEFAULT == null ? dueDate != null : !DUE_DATE_EDEFAULT.equals(dueDate);
            case TassooPackage.TASK__POSITION:
                return position != POSITION_EDEFAULT;
            case TassooPackage.TASK__PRIORITY:
                return priority != null;
            case TassooPackage.TASK__RECURRENCE_INDICATOR:
                return recurrenceIndicator != RECURRENCE_INDICATOR_EDEFAULT;
            case TassooPackage.TASK__RECURRENCE_SETTINGS:
                return recurrenceSettings != null;
            case TassooPackage.TASK__REMINDER_INDICATOR:
                return reminderIndicator != REMINDER_INDICATOR_EDEFAULT;
            case TassooPackage.TASK__START_DATE:
                return START_DATE_EDEFAULT == null ? startDate != null : !START_DATE_EDEFAULT.equals(startDate);
            case TassooPackage.TASK__STATUS:
                return status != null;
            case TassooPackage.TASK__TASK:
                return task != null && !task.isEmpty();
            case TassooPackage.TASK__TASK_ID:
                return taskId != TASK_ID_EDEFAULT;
            case TassooPackage.TASK__TASK_NAME:
                return TASK_NAME_EDEFAULT == null ? taskName != null : !TASK_NAME_EDEFAULT.equals(taskName);
            case TassooPackage.TASK__TASK_SUMMARY:
                return TASK_SUMMARY_EDEFAULT == null ? taskSummary != null : !TASK_SUMMARY_EDEFAULT.equals(taskSummary);
            case TassooPackage.TASK__TYPE:
                return type != null;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (actualTime: ");
        result.append(actualTime);
        result.append(", completedDate: ");
        result.append(completedDate);
        result.append(", estimatedTime: ");
        result.append(estimatedTime);
        result.append(", dueDate: ");
        result.append(dueDate);
        result.append(", position: ");
        result.append(position);
        result.append(", recurrenceIndicator: ");
        result.append(recurrenceIndicator);
        result.append(", reminderIndicator: ");
        result.append(reminderIndicator);
        result.append(", startDate: ");
        result.append(startDate);
        result.append(", taskId: ");
        result.append(taskId);
        result.append(", taskName: ");
        result.append(taskName);
        result.append(", taskSummary: ");
        result.append(taskSummary);
        result.append(')');
        return result.toString();
    }
}
