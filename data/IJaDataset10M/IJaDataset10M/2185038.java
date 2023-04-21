package au.edu.archer.metadata.msf.mss.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import au.edu.archer.metadata.msf.mss.MSSPackage;
import au.edu.archer.metadata.msf.mss.MetadataSchema;
import au.edu.archer.metadata.msf.mss.Model;
import au.edu.archer.metadata.msf.mss.NamedNode;
import au.edu.archer.metadata.msf.mss.util.InternationalText;
import au.edu.archer.metadata.msf.mss.util.Name;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Named Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.NamedNodeImpl#getName <em>Name</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.NamedNodeImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.NamedNodeImpl#getUri <em>Uri</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.NamedNodeImpl#getEffectiveLabel <em>Effective Label</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.NamedNodeImpl#getEffectiveUri <em>Effective Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class NamedNodeImpl extends NodeImpl implements NamedNode {

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final Name NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected Name name = NAME_EDEFAULT;

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
    protected NamedNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MSSPackage.Literals.NAMED_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Name getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(Name newName) {
        Name oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MSSPackage.NAMED_NODE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<InternationalText> getLabel() {
        if (label == null) {
            label = new EDataTypeUniqueEList<InternationalText>(InternationalText.class, this, MSSPackage.NAMED_NODE__LABEL);
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MSSPackage.NAMED_NODE__URI, oldUri, uri));
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
        return (l != null) ? l.getText() : ((name != null) ? name.toString() : null);
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
        while (parent != null) {
            if (parent instanceof MetadataSchema) {
                return buildUri(((MetadataSchema) parent).getEffectiveUri());
            } else if (parent instanceof Model) {
                return buildUri(((Model) parent).getModelUri());
            }
            parent = parent.eContainer();
        }
        return null;
    }

    /**
     * This method is used by getEffectiveUri() to add the appropriate
     * local name to a parent URI.  If the local name or the parent URI
     * are {@code null}, the result is {@code null}.
     * 
     * @param uri the base URI or {@code null}
     * @return the full URI or {@code null}
     */
    private String buildUri(String uri) {
        if (uri != null && name != null && !name.isEmpty()) {
            return uri.endsWith("/") ? (uri + name) : (uri + "/" + name);
        } else {
            return uri;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MSSPackage.NAMED_NODE__NAME:
                return getName();
            case MSSPackage.NAMED_NODE__LABEL:
                return getLabel();
            case MSSPackage.NAMED_NODE__URI:
                return getUri();
            case MSSPackage.NAMED_NODE__EFFECTIVE_LABEL:
                return getEffectiveLabel();
            case MSSPackage.NAMED_NODE__EFFECTIVE_URI:
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
            case MSSPackage.NAMED_NODE__NAME:
                setName((Name) newValue);
                return;
            case MSSPackage.NAMED_NODE__LABEL:
                getLabel().clear();
                getLabel().addAll((Collection<? extends InternationalText>) newValue);
                return;
            case MSSPackage.NAMED_NODE__URI:
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
            case MSSPackage.NAMED_NODE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case MSSPackage.NAMED_NODE__LABEL:
                getLabel().clear();
                return;
            case MSSPackage.NAMED_NODE__URI:
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
            case MSSPackage.NAMED_NODE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case MSSPackage.NAMED_NODE__LABEL:
                return label != null && !label.isEmpty();
            case MSSPackage.NAMED_NODE__URI:
                return URI_EDEFAULT == null ? uri != null : !URI_EDEFAULT.equals(uri);
            case MSSPackage.NAMED_NODE__EFFECTIVE_LABEL:
                return EFFECTIVE_LABEL_EDEFAULT == null ? getEffectiveLabel() != null : !EFFECTIVE_LABEL_EDEFAULT.equals(getEffectiveLabel());
            case MSSPackage.NAMED_NODE__EFFECTIVE_URI:
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
        result.append(" (name: ");
        result.append(name);
        result.append(", label: ");
        result.append(label);
        result.append(", uri: ");
        result.append(uri);
        result.append(')');
        return result.toString();
    }
}
