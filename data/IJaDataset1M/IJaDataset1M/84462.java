package de.mpiwg.vspace.metamodel.impl;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.Sequence;
import de.mpiwg.vspace.metamodel.extension.IPropertyRetriever;
import de.mpiwg.vspace.metamodel.extension.PropertyExtensionProvider;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SequenceImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SequenceImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SequenceImpl#getSlideOrder <em>Slide Order</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.SequenceImpl#getSequenceId <em>Sequence Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SequenceImpl extends EObjectImpl implements Sequence {

    /**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated NOT
	 * @ordered
	 */
    protected static final String TITLE_EDEFAULT = "";

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
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated NOT
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
	 * The default value of the '{@link #getSlideOrder() <em>Slide Order</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlideOrder()
	 * @generated
	 * @ordered
	 */
    protected static final String SLIDE_ORDER_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSlideOrder() <em>Slide Order</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlideOrder()
	 * @generated
	 * @ordered
	 */
    protected String slideOrder = SLIDE_ORDER_EDEFAULT;

    /**
	 * The default value of the '{@link #getSequenceId() <em>Sequence Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceId()
	 * @generated
	 * @ordered
	 */
    protected static final String SEQUENCE_ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSequenceId() <em>Sequence Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceId()
	 * @generated
	 * @ordered
	 */
    protected String sequenceId = SEQUENCE_ID_EDEFAULT;

    protected Map<Integer, Class> originalTypes;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected SequenceImpl() {
        super();
        sequenceId = EcoreUtil.generateUUID();
        originalTypes = new HashMap<Integer, Class>();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExhibitionPackage.Literals.SEQUENCE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String getTitle() {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.SEQUENCE__TITLE);
        if (retriever == null) return title;
        return retriever.getProperty(this, ExhibitionPackage.Literals.SEQUENCE__TITLE).toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setTitle(String newTitle) {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.SEQUENCE__TITLE);
        String oldTitle = title;
        if (retriever == null) {
            title = newTitle;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SEQUENCE__TITLE, oldTitle, title));
        } else {
            Class type = originalTypes.get(newTitle.hashCode());
            if (type != null) originalTypes.remove(newTitle.hashCode());
            retriever.setProperty(this, ExhibitionPackage.Literals.SEQUENCE__TITLE, newTitle, type);
            title = "";
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SEQUENCE__TITLE, oldTitle, PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.SEQUENCE__TITLE).toString()));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String getDescription() {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.SEQUENCE__DESCRIPTION);
        if (retriever == null) return description;
        return retriever.getProperty(this, ExhibitionPackage.Literals.SEQUENCE__DESCRIPTION).toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setDescription(String newDescription) {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.SEQUENCE__DESCRIPTION);
        String oldDescription = description;
        if (retriever == null) {
            description = newDescription;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SEQUENCE__DESCRIPTION, oldDescription, description));
        } else {
            Class type = originalTypes.get(newDescription.hashCode());
            if (type != null) originalTypes.remove(newDescription.hashCode());
            retriever.setProperty(this, ExhibitionPackage.Literals.SEQUENCE__DESCRIPTION, newDescription, type);
            description = "";
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SEQUENCE__DESCRIPTION, oldDescription, PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.SEQUENCE__DESCRIPTION).toString()));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSlideOrder() {
        return slideOrder;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSlideOrder(String newSlideOrder) {
        String oldSlideOrder = slideOrder;
        slideOrder = newSlideOrder;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SEQUENCE__SLIDE_ORDER, oldSlideOrder, slideOrder));
    }

    /**
	 * Method for retrieving an array of slide ids, that are specified in slideOrider.
	 * @return array of slide ids
	 */
    public String[] getSlideIds() {
        if (slideOrder == null) return null;
        return slideOrder.split(";");
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSequenceId() {
        return sequenceId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSequenceId(String newSequenceId) {
        String oldSequenceId = sequenceId;
        sequenceId = newSequenceId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.SEQUENCE__SEQUENCE_ID, oldSequenceId, sequenceId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ExhibitionPackage.SEQUENCE__TITLE:
                return getTitle();
            case ExhibitionPackage.SEQUENCE__DESCRIPTION:
                return getDescription();
            case ExhibitionPackage.SEQUENCE__SLIDE_ORDER:
                return getSlideOrder();
            case ExhibitionPackage.SEQUENCE__SEQUENCE_ID:
                return getSequenceId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ExhibitionPackage.SEQUENCE__TITLE:
                originalTypes.put(newValue.toString().hashCode(), newValue.getClass());
                setTitle(newValue.toString());
                return;
            case ExhibitionPackage.SEQUENCE__DESCRIPTION:
                originalTypes.put(newValue.toString().hashCode(), newValue.getClass());
                setDescription(newValue.toString());
                return;
            case ExhibitionPackage.SEQUENCE__SLIDE_ORDER:
                setSlideOrder((String) newValue);
                return;
            case ExhibitionPackage.SEQUENCE__SEQUENCE_ID:
                setSequenceId((String) newValue);
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
            case ExhibitionPackage.SEQUENCE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case ExhibitionPackage.SEQUENCE__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case ExhibitionPackage.SEQUENCE__SLIDE_ORDER:
                setSlideOrder(SLIDE_ORDER_EDEFAULT);
                return;
            case ExhibitionPackage.SEQUENCE__SEQUENCE_ID:
                setSequenceId(SEQUENCE_ID_EDEFAULT);
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
            case ExhibitionPackage.SEQUENCE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case ExhibitionPackage.SEQUENCE__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case ExhibitionPackage.SEQUENCE__SLIDE_ORDER:
                return SLIDE_ORDER_EDEFAULT == null ? slideOrder != null : !SLIDE_ORDER_EDEFAULT.equals(slideOrder);
            case ExhibitionPackage.SEQUENCE__SEQUENCE_ID:
                return SEQUENCE_ID_EDEFAULT == null ? sequenceId != null : !SEQUENCE_ID_EDEFAULT.equals(sequenceId);
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
        result.append(", description: ");
        result.append(description);
        result.append(", slideOrder: ");
        result.append(slideOrder);
        result.append(", sequenceId: ");
        result.append(sequenceId);
        result.append(')');
        return result.toString();
    }
}
