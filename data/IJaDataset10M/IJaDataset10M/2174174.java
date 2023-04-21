package au.edu.archer.metadata.msf.mss.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import au.edu.archer.metadata.msf.mss.MSSPackage;
import au.edu.archer.metadata.msf.mss.NamedNode;
import au.edu.archer.metadata.msf.mss.Term;
import au.edu.archer.metadata.msf.mss.util.InternationalText;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.TermImpl#getValue <em>Value</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.TermImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.TermImpl#getUri <em>Uri</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.TermImpl#getEffectiveLabel <em>Effective Label</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.TermImpl#getEffectiveUri <em>Effective Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TermImpl extends NodeImpl implements Term {

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected EList<InternationalText> label;

    /**
     * The default value of the '{@link #getUri() <em>Uri</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUri()
     * @generated
     * @ordered
     */
    protected static final String URI_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUri() <em>Uri</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUri()
     * @generated
     * @ordered
     */
    protected String uri = URI_EDEFAULT;

    /**
     * The default value of the '{@link #getEffectiveLabel() <em>Effective Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEffectiveLabel()
     * @generated
     * @ordered
     */
    protected static final String EFFECTIVE_LABEL_EDEFAULT = null;

    /**
     * The default value of the '{@link #getEffectiveUri() <em>Effective Uri</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEffectiveUri()
     * @generated
     * @ordered
     */
    protected static final String EFFECTIVE_URI_EDEFAULT = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TermImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MSSPackage.Literals.TERM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MSSPackage.TERM__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<InternationalText> getLabel() {
        if (label == null) {
            label = new EDataTypeUniqueEList<InternationalText>(InternationalText.class, this, MSSPackage.TERM__LABEL);
        }
        return label;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUri() {
        return uri;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUri(String newUri) {
        String oldUri = uri;
        uri = newUri;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MSSPackage.TERM__URI, oldUri, uri));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public String getEffectiveLabel() {
        InternationalText l = null;
        if (label != null) {
            for (InternationalText ll : label) {
                if (ll == null) {
                    continue;
                }
                if (ll.hasDefaultLanguage()) {
                    l = ll;
                    break;
                } else if (l == null) {
                    ll = l;
                }
            }
        }
        return (l != null) ? l.getText() : ((value != null) ? value.toString() : null);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public String getEffectiveUri() {
        if (uri != null && uri.length() > 0) {
            return uri;
        }
        EObject parent = eContainer();
        while (parent != null && !(parent instanceof NamedNode)) {
            parent = parent.eContainer();
        }
        if (parent != null) {
            String uri = ((NamedNode) parent).getEffectiveUri();
            if (uri != null && value != null && value.length() != 0) {
                return (uri.endsWith("/") ? (uri + value) : (uri + "/" + value));
            } else {
                return uri;
            }
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MSSPackage.TERM__VALUE:
                return getValue();
            case MSSPackage.TERM__LABEL:
                return getLabel();
            case MSSPackage.TERM__URI:
                return getUri();
            case MSSPackage.TERM__EFFECTIVE_LABEL:
                return getEffectiveLabel();
            case MSSPackage.TERM__EFFECTIVE_URI:
                return getEffectiveUri();
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
            case MSSPackage.TERM__VALUE:
                setValue((String) newValue);
                return;
            case MSSPackage.TERM__LABEL:
                getLabel().clear();
                getLabel().addAll((Collection<? extends InternationalText>) newValue);
                return;
            case MSSPackage.TERM__URI:
                setUri((String) newValue);
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
            case MSSPackage.TERM__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case MSSPackage.TERM__LABEL:
                getLabel().clear();
                return;
            case MSSPackage.TERM__URI:
                setUri(URI_EDEFAULT);
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
            case MSSPackage.TERM__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case MSSPackage.TERM__LABEL:
                return label != null && !label.isEmpty();
            case MSSPackage.TERM__URI:
                return URI_EDEFAULT == null ? uri != null : !URI_EDEFAULT.equals(uri);
            case MSSPackage.TERM__EFFECTIVE_LABEL:
                return EFFECTIVE_LABEL_EDEFAULT == null ? getEffectiveLabel() != null : !EFFECTIVE_LABEL_EDEFAULT.equals(getEffectiveLabel());
            case MSSPackage.TERM__EFFECTIVE_URI:
                return EFFECTIVE_URI_EDEFAULT == null ? getEffectiveUri() != null : !EFFECTIVE_URI_EDEFAULT.equals(getEffectiveUri());
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
        result.append(" (value: ");
        result.append(value);
        result.append(", label: ");
        result.append(label);
        result.append(", uri: ");
        result.append(uri);
        result.append(')');
        return result.toString();
    }
}
