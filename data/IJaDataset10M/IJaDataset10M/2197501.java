package edu.asu.vogon.digitalHPS.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import edu.asu.vogon.digitalHPS.DigitalHPSPackage;
import edu.asu.vogon.digitalHPS.IActor;
import edu.asu.vogon.digitalHPS.ICreationEvent;
import edu.asu.vogon.digitalHPS.IText;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IText</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getId <em>Id</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getFilePath <em>File Path</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getText <em>Text</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getSourceReference <em>Source Reference</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getCreationEvents <em>Creation Events</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getAuthors <em>Authors</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.impl.ITextImpl#getCreators <em>Creators</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ITextImpl extends EObjectImpl implements IText {

    /**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected static final String TITLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected String title = TITLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilePath()
	 * @generated
	 * @ordered
	 */
    protected static final String FILE_PATH_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilePath()
	 * @generated
	 * @ordered
	 */
    protected String filePath = FILE_PATH_EDEFAULT;

    /**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
    protected static final String TEXT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
    protected String text = TEXT_EDEFAULT;

    /**
	 * The default value of the '{@link #getSourceReference() <em>Source Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceReference()
	 * @generated
	 * @ordered
	 */
    protected static final String SOURCE_REFERENCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSourceReference() <em>Source Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceReference()
	 * @generated
	 * @ordered
	 */
    protected String sourceReference = SOURCE_REFERENCE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getCreationEvents() <em>Creation Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationEvents()
	 * @generated
	 * @ordered
	 */
    protected EList<ICreationEvent> creationEvents;

    /**
	 * The default value of the '{@link #getAuthors() <em>Authors</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthors()
	 * @generated
	 * @ordered
	 */
    protected static final String AUTHORS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getAuthors() <em>Authors</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthors()
	 * @generated
	 * @ordered
	 */
    protected String authors = AUTHORS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getCreators() <em>Creators</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreators()
	 * @generated
	 * @ordered
	 */
    protected EList<IActor> creators;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ITextImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DigitalHPSPackage.Literals.ITEXT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTitle(String newTitle) {
        String oldTitle = title;
        title = newTitle;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DigitalHPSPackage.ITEXT__TITLE, oldTitle, title));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getId() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DigitalHPSPackage.ITEXT__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFilePath() {
        return filePath;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFilePath(String newFilePath) {
        String oldFilePath = filePath;
        filePath = newFilePath;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DigitalHPSPackage.ITEXT__FILE_PATH, oldFilePath, filePath));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getText() {
        return text;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setText(String newText) {
        String oldText = text;
        text = newText;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DigitalHPSPackage.ITEXT__TEXT, oldText, text));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSourceReference() {
        return sourceReference;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSourceReference(String newSourceReference) {
        String oldSourceReference = sourceReference;
        sourceReference = newSourceReference;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DigitalHPSPackage.ITEXT__SOURCE_REFERENCE, oldSourceReference, sourceReference));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ICreationEvent> getCreationEvents() {
        if (creationEvents == null) {
            creationEvents = new EObjectContainmentEList<ICreationEvent>(ICreationEvent.class, this, DigitalHPSPackage.ITEXT__CREATION_EVENTS);
        }
        return creationEvents;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getAuthors() {
        return authors;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAuthors(String newAuthors) {
        String oldAuthors = authors;
        authors = newAuthors;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DigitalHPSPackage.ITEXT__AUTHORS, oldAuthors, authors));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<IActor> getCreators() {
        if (creators == null) {
            creators = new EObjectContainmentEList<IActor>(IActor.class, this, DigitalHPSPackage.ITEXT__CREATORS);
        }
        return creators;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DigitalHPSPackage.ITEXT__CREATION_EVENTS:
                return ((InternalEList<?>) getCreationEvents()).basicRemove(otherEnd, msgs);
            case DigitalHPSPackage.ITEXT__CREATORS:
                return ((InternalEList<?>) getCreators()).basicRemove(otherEnd, msgs);
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
            case DigitalHPSPackage.ITEXT__TITLE:
                return getTitle();
            case DigitalHPSPackage.ITEXT__ID:
                return getId();
            case DigitalHPSPackage.ITEXT__FILE_PATH:
                return getFilePath();
            case DigitalHPSPackage.ITEXT__TEXT:
                return getText();
            case DigitalHPSPackage.ITEXT__SOURCE_REFERENCE:
                return getSourceReference();
            case DigitalHPSPackage.ITEXT__CREATION_EVENTS:
                return getCreationEvents();
            case DigitalHPSPackage.ITEXT__AUTHORS:
                return getAuthors();
            case DigitalHPSPackage.ITEXT__CREATORS:
                return getCreators();
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
            case DigitalHPSPackage.ITEXT__TITLE:
                setTitle((String) newValue);
                return;
            case DigitalHPSPackage.ITEXT__ID:
                setId((String) newValue);
                return;
            case DigitalHPSPackage.ITEXT__FILE_PATH:
                setFilePath((String) newValue);
                return;
            case DigitalHPSPackage.ITEXT__TEXT:
                setText((String) newValue);
                return;
            case DigitalHPSPackage.ITEXT__SOURCE_REFERENCE:
                setSourceReference((String) newValue);
                return;
            case DigitalHPSPackage.ITEXT__CREATION_EVENTS:
                getCreationEvents().clear();
                getCreationEvents().addAll((Collection<? extends ICreationEvent>) newValue);
                return;
            case DigitalHPSPackage.ITEXT__AUTHORS:
                setAuthors((String) newValue);
                return;
            case DigitalHPSPackage.ITEXT__CREATORS:
                getCreators().clear();
                getCreators().addAll((Collection<? extends IActor>) newValue);
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
            case DigitalHPSPackage.ITEXT__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case DigitalHPSPackage.ITEXT__ID:
                setId(ID_EDEFAULT);
                return;
            case DigitalHPSPackage.ITEXT__FILE_PATH:
                setFilePath(FILE_PATH_EDEFAULT);
                return;
            case DigitalHPSPackage.ITEXT__TEXT:
                setText(TEXT_EDEFAULT);
                return;
            case DigitalHPSPackage.ITEXT__SOURCE_REFERENCE:
                setSourceReference(SOURCE_REFERENCE_EDEFAULT);
                return;
            case DigitalHPSPackage.ITEXT__CREATION_EVENTS:
                getCreationEvents().clear();
                return;
            case DigitalHPSPackage.ITEXT__AUTHORS:
                setAuthors(AUTHORS_EDEFAULT);
                return;
            case DigitalHPSPackage.ITEXT__CREATORS:
                getCreators().clear();
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
            case DigitalHPSPackage.ITEXT__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case DigitalHPSPackage.ITEXT__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case DigitalHPSPackage.ITEXT__FILE_PATH:
                return FILE_PATH_EDEFAULT == null ? filePath != null : !FILE_PATH_EDEFAULT.equals(filePath);
            case DigitalHPSPackage.ITEXT__TEXT:
                return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
            case DigitalHPSPackage.ITEXT__SOURCE_REFERENCE:
                return SOURCE_REFERENCE_EDEFAULT == null ? sourceReference != null : !SOURCE_REFERENCE_EDEFAULT.equals(sourceReference);
            case DigitalHPSPackage.ITEXT__CREATION_EVENTS:
                return creationEvents != null && !creationEvents.isEmpty();
            case DigitalHPSPackage.ITEXT__AUTHORS:
                return AUTHORS_EDEFAULT == null ? authors != null : !AUTHORS_EDEFAULT.equals(authors);
            case DigitalHPSPackage.ITEXT__CREATORS:
                return creators != null && !creators.isEmpty();
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
        result.append(" (title: ");
        result.append(title);
        result.append(", id: ");
        result.append(id);
        result.append(", filePath: ");
        result.append(filePath);
        result.append(", text: ");
        result.append(text);
        result.append(", sourceReference: ");
        result.append(sourceReference);
        result.append(", authors: ");
        result.append(authors);
        result.append(')');
        return result.toString();
    }
}
