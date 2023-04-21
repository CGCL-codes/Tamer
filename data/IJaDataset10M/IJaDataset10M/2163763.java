package edu.asu.vogon.model.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import edu.asu.vogon.model.Annotation;
import edu.asu.vogon.model.GeneralTextTerm;
import edu.asu.vogon.model.ModelPackage;
import edu.asu.vogon.model.Text;
import edu.asu.vogon.model.TextMetadata;
import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Text</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getFilePath <em>File Path</em>}</li>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getText <em>Text</em>}</li>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getTextId <em>Text Id</em>}</li>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link edu.asu.vogon.model.impl.TextImpl#getResults <em>Results</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextImpl extends EObjectImpl implements Text {

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
	 * The default value of the '{@link #getTextId() <em>Text Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTextId()
	 * @generated
	 * @ordered
	 */
    protected static final String TEXT_ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTextId() <em>Text Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTextId()
	 * @generated
	 * @ordered
	 */
    protected String textId = TEXT_ID_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAnnotations() <em>Annotations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotations()
	 * @generated
	 * @ordered
	 */
    protected EList<Annotation> annotations;

    /**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
    protected TextMetadata metadata;

    /**
	 * The cached value of the '{@link #getResults() <em>Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResults()
	 * @generated
	 * @ordered
	 */
    protected EList<GeneralTextTerm> results;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TextImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.TEXT;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TEXT__FILE_PATH, oldFilePath, filePath));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TEXT__TITLE, oldTitle, title));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TEXT__TEXT, oldText, text));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTextId() {
        return textId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setTextId(String newTextId) {
        String oldTextId = textId;
        newTextId = newTextId.trim();
        textId = newTextId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TEXT__TEXT_ID, oldTextId, textId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Annotation> getAnnotations() {
        if (annotations == null) {
            annotations = new EObjectContainmentEList<Annotation>(Annotation.class, this, ModelPackage.TEXT__ANNOTATIONS);
        }
        return annotations;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TextMetadata getMetadata() {
        return metadata;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMetadata(TextMetadata newMetadata, NotificationChain msgs) {
        TextMetadata oldMetadata = metadata;
        metadata = newMetadata;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.TEXT__METADATA, oldMetadata, newMetadata);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMetadata(TextMetadata newMetadata) {
        if (newMetadata != metadata) {
            NotificationChain msgs = null;
            if (metadata != null) msgs = ((InternalEObject) metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.TEXT__METADATA, null, msgs);
            if (newMetadata != null) msgs = ((InternalEObject) newMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.TEXT__METADATA, null, msgs);
            msgs = basicSetMetadata(newMetadata, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TEXT__METADATA, newMetadata, newMetadata));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<GeneralTextTerm> getResults() {
        if (results == null) {
            results = new EObjectContainmentEList.Resolving<GeneralTextTerm>(GeneralTextTerm.class, this, ModelPackage.TEXT__RESULTS);
        }
        return results;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelPackage.TEXT__ANNOTATIONS:
                return ((InternalEList<?>) getAnnotations()).basicRemove(otherEnd, msgs);
            case ModelPackage.TEXT__METADATA:
                return basicSetMetadata(null, msgs);
            case ModelPackage.TEXT__RESULTS:
                return ((InternalEList<?>) getResults()).basicRemove(otherEnd, msgs);
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
            case ModelPackage.TEXT__FILE_PATH:
                return getFilePath();
            case ModelPackage.TEXT__TITLE:
                return getTitle();
            case ModelPackage.TEXT__TEXT:
                return getText();
            case ModelPackage.TEXT__TEXT_ID:
                return getTextId();
            case ModelPackage.TEXT__ANNOTATIONS:
                return getAnnotations();
            case ModelPackage.TEXT__METADATA:
                return getMetadata();
            case ModelPackage.TEXT__RESULTS:
                return getResults();
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
            case ModelPackage.TEXT__FILE_PATH:
                setFilePath((String) newValue);
                return;
            case ModelPackage.TEXT__TITLE:
                setTitle((String) newValue);
                return;
            case ModelPackage.TEXT__TEXT:
                setText((String) newValue);
                return;
            case ModelPackage.TEXT__TEXT_ID:
                setTextId((String) newValue);
                return;
            case ModelPackage.TEXT__ANNOTATIONS:
                getAnnotations().clear();
                getAnnotations().addAll((Collection<? extends Annotation>) newValue);
                return;
            case ModelPackage.TEXT__METADATA:
                setMetadata((TextMetadata) newValue);
                return;
            case ModelPackage.TEXT__RESULTS:
                getResults().clear();
                getResults().addAll((Collection<? extends GeneralTextTerm>) newValue);
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
            case ModelPackage.TEXT__FILE_PATH:
                setFilePath(FILE_PATH_EDEFAULT);
                return;
            case ModelPackage.TEXT__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case ModelPackage.TEXT__TEXT:
                setText(TEXT_EDEFAULT);
                return;
            case ModelPackage.TEXT__TEXT_ID:
                setTextId(TEXT_ID_EDEFAULT);
                return;
            case ModelPackage.TEXT__ANNOTATIONS:
                getAnnotations().clear();
                return;
            case ModelPackage.TEXT__METADATA:
                setMetadata((TextMetadata) null);
                return;
            case ModelPackage.TEXT__RESULTS:
                getResults().clear();
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
            case ModelPackage.TEXT__FILE_PATH:
                return FILE_PATH_EDEFAULT == null ? filePath != null : !FILE_PATH_EDEFAULT.equals(filePath);
            case ModelPackage.TEXT__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case ModelPackage.TEXT__TEXT:
                return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
            case ModelPackage.TEXT__TEXT_ID:
                return TEXT_ID_EDEFAULT == null ? textId != null : !TEXT_ID_EDEFAULT.equals(textId);
            case ModelPackage.TEXT__ANNOTATIONS:
                return annotations != null && !annotations.isEmpty();
            case ModelPackage.TEXT__METADATA:
                return metadata != null;
            case ModelPackage.TEXT__RESULTS:
                return results != null && !results.isEmpty();
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
        result.append(" (filePath: ");
        result.append(filePath);
        result.append(", title: ");
        result.append(title);
        result.append(", text: ");
        result.append(text);
        result.append(", textId: ");
        result.append(textId);
        result.append(')');
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Text)) return false;
        if (((Text) obj).eIsProxy()) {
            EcoreUtil.resolve((Text) obj, ((Text) obj).eResource());
        }
        return ((Text) obj).getTextId().equals(getTextId());
    }
}
