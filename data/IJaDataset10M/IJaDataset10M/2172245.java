package de.hackerdan.projectcreator.model.impl;

import de.hackerdan.projectcreator.model.File;
import de.hackerdan.projectcreator.model.ProjectCreatorPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.hackerdan.projectcreator.model.impl.FileImpl#getContent <em>Content</em>}</li>
 *   <li>{@link de.hackerdan.projectcreator.model.impl.FileImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link de.hackerdan.projectcreator.model.impl.FileImpl#isOpenOnFinish <em>Open On Finish</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FileImpl extends NamedImpl implements File {

    /**
    * The default value of the '{@link #getContent() <em>Content</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getContent()
    * @generated
    * @ordered
    */
    protected static final String CONTENT_EDEFAULT = null;

    /**
    * The cached value of the '{@link #getContent() <em>Content</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getContent()
    * @generated
    * @ordered
    */
    protected String content = CONTENT_EDEFAULT;

    /**
    * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getEncoding()
    * @generated
    * @ordered
    */
    protected static final String ENCODING_EDEFAULT = null;

    /**
    * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getEncoding()
    * @generated
    * @ordered
    */
    protected String encoding = ENCODING_EDEFAULT;

    /**
    * The default value of the '{@link #isOpenOnFinish() <em>Open On Finish</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #isOpenOnFinish()
    * @generated
    * @ordered
    */
    protected static final boolean OPEN_ON_FINISH_EDEFAULT = false;

    /**
    * The cached value of the '{@link #isOpenOnFinish() <em>Open On Finish</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #isOpenOnFinish()
    * @generated
    * @ordered
    */
    protected boolean openOnFinish = OPEN_ON_FINISH_EDEFAULT;

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    protected FileImpl() {
        super();
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    protected EClass eStaticClass() {
        return ProjectCreatorPackage.Literals.FILE;
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public String getContent() {
        return content;
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public void setContent(String newContent) {
        String oldContent = content;
        content = newContent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ProjectCreatorPackage.FILE__CONTENT, oldContent, content));
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public String getEncoding() {
        return encoding;
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public void setEncoding(String newEncoding) {
        String oldEncoding = encoding;
        encoding = newEncoding;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ProjectCreatorPackage.FILE__ENCODING, oldEncoding, encoding));
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public boolean isOpenOnFinish() {
        return openOnFinish;
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public void setOpenOnFinish(boolean newOpenOnFinish) {
        boolean oldOpenOnFinish = openOnFinish;
        openOnFinish = newOpenOnFinish;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ProjectCreatorPackage.FILE__OPEN_ON_FINISH, oldOpenOnFinish, openOnFinish));
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ProjectCreatorPackage.FILE__CONTENT:
                return getContent();
            case ProjectCreatorPackage.FILE__ENCODING:
                return getEncoding();
            case ProjectCreatorPackage.FILE__OPEN_ON_FINISH:
                return isOpenOnFinish() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ProjectCreatorPackage.FILE__CONTENT:
                setContent((String) newValue);
                return;
            case ProjectCreatorPackage.FILE__ENCODING:
                setEncoding((String) newValue);
                return;
            case ProjectCreatorPackage.FILE__OPEN_ON_FINISH:
                setOpenOnFinish(((Boolean) newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public void eUnset(int featureID) {
        switch(featureID) {
            case ProjectCreatorPackage.FILE__CONTENT:
                setContent(CONTENT_EDEFAULT);
                return;
            case ProjectCreatorPackage.FILE__ENCODING:
                setEncoding(ENCODING_EDEFAULT);
                return;
            case ProjectCreatorPackage.FILE__OPEN_ON_FINISH:
                setOpenOnFinish(OPEN_ON_FINISH_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ProjectCreatorPackage.FILE__CONTENT:
                return CONTENT_EDEFAULT == null ? content != null : !CONTENT_EDEFAULT.equals(content);
            case ProjectCreatorPackage.FILE__ENCODING:
                return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
            case ProjectCreatorPackage.FILE__OPEN_ON_FINISH:
                return openOnFinish != OPEN_ON_FINISH_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (content: ");
        result.append(content);
        result.append(", encoding: ");
        result.append(encoding);
        result.append(", openOnFinish: ");
        result.append(openOnFinish);
        result.append(')');
        return result.toString();
    }
}
