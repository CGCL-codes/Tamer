package de.mpiwg.vspace.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.SlideTemplate;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Slide Template</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SlideTemplateImpl#getSlideTemplateId <em>Slide Template Id</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SlideTemplateImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SlideTemplateImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SlideTemplateImpl#getTemplateFilePath <em>Template File Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SlideTemplateImpl extends EObjectImpl implements SlideTemplate {

    /**
	 * The default value of the '{@link #getSlideTemplateId() <em>Slide Template Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSlideTemplateId()
	 * @generated
	 * @ordered
	 */
    protected static final String SLIDE_TEMPLATE_ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSlideTemplateId() <em>Slide Template Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSlideTemplateId()
	 * @generated
	 * @ordered
	 */
    protected String slideTemplateId = SLIDE_TEMPLATE_ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected static final String TITLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected String title = TITLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
	 * The default value of the '{@link #getTemplateFilePath() <em>Template File Path</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTemplateFilePath()
	 * @generated
	 * @ordered
	 */
    protected static final String TEMPLATE_FILE_PATH_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTemplateFilePath() <em>Template File Path</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTemplateFilePath()
	 * @generated
	 * @ordered
	 */
    protected String templateFilePath = TEMPLATE_FILE_PATH_EDEFAULT;

    protected String htmlContent = "";

    protected String cssFilename = "";

    protected int templateType = 0;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected SlideTemplateImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExhibitionPackage.Literals.SLIDE_TEMPLATE;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getSlideTemplateId() {
        return slideTemplateId;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setSlideTemplateId(String newSlideTemplateId) {
        String oldSlideTemplateId = slideTemplateId;
        slideTemplateId = newSlideTemplateId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SLIDE_TEMPLATE__SLIDE_TEMPLATE_ID, oldSlideTemplateId, slideTemplateId));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setTitle(String newTitle) {
        String oldTitle = title;
        title = newTitle;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SLIDE_TEMPLATE__TITLE, oldTitle, title));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
        String oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SLIDE_TEMPLATE__DESCRIPTION, oldDescription, description));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getTemplateFilePath() {
        return templateFilePath;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setTemplateFilePath(String newTemplateFilePath) {
        String oldTemplateFilePath = templateFilePath;
        templateFilePath = newTemplateFilePath;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SLIDE_TEMPLATE__TEMPLATE_FILE_PATH, oldTemplateFilePath, templateFilePath));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ExhibitionPackage.SLIDE_TEMPLATE__SLIDE_TEMPLATE_ID:
                return getSlideTemplateId();
            case ExhibitionPackage.SLIDE_TEMPLATE__TITLE:
                return getTitle();
            case ExhibitionPackage.SLIDE_TEMPLATE__DESCRIPTION:
                return getDescription();
            case ExhibitionPackage.SLIDE_TEMPLATE__TEMPLATE_FILE_PATH:
                return getTemplateFilePath();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ExhibitionPackage.SLIDE_TEMPLATE__SLIDE_TEMPLATE_ID:
                setSlideTemplateId((String) newValue);
                return;
            case ExhibitionPackage.SLIDE_TEMPLATE__TITLE:
                setTitle((String) newValue);
                return;
            case ExhibitionPackage.SLIDE_TEMPLATE__DESCRIPTION:
                setDescription((String) newValue);
                return;
            case ExhibitionPackage.SLIDE_TEMPLATE__TEMPLATE_FILE_PATH:
                setTemplateFilePath((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case ExhibitionPackage.SLIDE_TEMPLATE__SLIDE_TEMPLATE_ID:
                setSlideTemplateId(SLIDE_TEMPLATE_ID_EDEFAULT);
                return;
            case ExhibitionPackage.SLIDE_TEMPLATE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case ExhibitionPackage.SLIDE_TEMPLATE__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case ExhibitionPackage.SLIDE_TEMPLATE__TEMPLATE_FILE_PATH:
                setTemplateFilePath(TEMPLATE_FILE_PATH_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ExhibitionPackage.SLIDE_TEMPLATE__SLIDE_TEMPLATE_ID:
                return SLIDE_TEMPLATE_ID_EDEFAULT == null ? slideTemplateId != null : !SLIDE_TEMPLATE_ID_EDEFAULT.equals(slideTemplateId);
            case ExhibitionPackage.SLIDE_TEMPLATE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case ExhibitionPackage.SLIDE_TEMPLATE__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case ExhibitionPackage.SLIDE_TEMPLATE__TEMPLATE_FILE_PATH:
                return TEMPLATE_FILE_PATH_EDEFAULT == null ? templateFilePath != null : !TEMPLATE_FILE_PATH_EDEFAULT.equals(templateFilePath);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (slideTemplateId: ");
        result.append(slideTemplateId);
        result.append(", title: ");
        result.append(title);
        result.append(", description: ");
        result.append(description);
        result.append(", templateFilePath: ");
        result.append(templateFilePath);
        result.append(')');
        return result.toString();
    }

    public String getDisplayTitle() {
        return getSlideTemplateId() + ": " + getTitle();
    }

    public String getHTMLContent() {
        return htmlContent;
    }

    public void setHMTLContent(String content) {
        htmlContent = content;
    }

    public String getCSSFilename() {
        return cssFilename;
    }

    public void setCSSFilename(String filename) {
        cssFilename = filename;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int type) {
        this.templateType = type;
    }
}
