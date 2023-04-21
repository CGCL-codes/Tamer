package de.mpiwg.vspace.metamodel.impl;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import de.mpiwg.vspace.metamodel.BranchingPointChoice;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.Sequence;
import de.mpiwg.vspace.metamodel.extension.IPropertyRetriever;
import de.mpiwg.vspace.metamodel.extension.PropertyExtensionProvider;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Branching Point Choice</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.BranchingPointChoiceImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.BranchingPointChoiceImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.BranchingPointChoiceImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BranchingPointChoiceImpl extends EObjectImpl implements BranchingPointChoice {

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
	 * The cached value of the '{@link #getSequence() <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
    protected Sequence sequence;

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

    protected Map<Integer, Class> originalTypes;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected BranchingPointChoiceImpl() {
        super();
        id = EcoreUtil.generateUUID();
        originalTypes = new HashMap<Integer, Class>();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String getTitle() {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE);
        if (retriever == null) return title;
        return retriever.getProperty(this, ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE).toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setTitle(String newTitle) {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE);
        String oldTitle = title;
        if (retriever == null) {
            title = newTitle;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE, oldTitle, title));
        } else {
            Class type = originalTypes.get(newTitle.hashCode());
            if (type != null) originalTypes.remove(newTitle.hashCode());
            retriever.setProperty(this, ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE, newTitle, type);
            title = "";
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE, oldTitle, PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE).toString()));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Sequence getSequence() {
        if (sequence != null && sequence.eIsProxy()) {
            InternalEObject oldSequence = (InternalEObject) sequence;
            sequence = (Sequence) eResolveProxy(oldSequence);
            if (sequence != oldSequence) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExhibitionPackage.BRANCHING_POINT_CHOICE__SEQUENCE, oldSequence, sequence));
            }
        }
        return sequence;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Sequence basicGetSequence() {
        return sequence;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSequence(Sequence newSequence) {
        Sequence oldSequence = sequence;
        sequence = newSequence;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.BRANCHING_POINT_CHOICE__SEQUENCE, oldSequence, sequence));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.BRANCHING_POINT_CHOICE__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE:
                return getTitle();
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__SEQUENCE:
                if (resolve) return getSequence();
                return basicGetSequence();
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__ID:
                return getId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE:
                originalTypes.put(newValue.toString().hashCode(), newValue.getClass());
                setTitle(newValue.toString());
                return;
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__SEQUENCE:
                setSequence((Sequence) newValue);
                return;
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__ID:
                setId((String) newValue);
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
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__SEQUENCE:
                setSequence((Sequence) null);
                return;
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__ID:
                setId(ID_EDEFAULT);
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
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__SEQUENCE:
                return sequence != null;
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(')');
        return result.toString();
    }
}
