package drarch.trace.logModel.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.isistan.flabot.trace.log.Tag;
import drarch.trace.LogSearcher.NumberKeyComparator;
import drarch.trace.logModel.LogModelFactory;
import drarch.trace.logModel.LogModelPackage;
import drarch.trace.logModel.LogNode;
import drarch.trace.logModel.PropertyLogNode;
import drarch.trace.logModel.TagLogNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tag Log Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link drarch.trace.logModel.impl.TagLogNodeImpl#getChildrens <em>Childrens</em>}</li>
 *   <li>{@link drarch.trace.logModel.impl.TagLogNodeImpl#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TagLogNodeImpl extends LogNodeImpl implements TagLogNode {

    /**
	 * The default value of the '{@link #getChildrens() <em>Childrens</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildrens()
	 * @generated
	 * @ordered
	 */
    protected static final LogNode[] CHILDRENS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getChildrens() <em>Childrens</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildrens()
	 * @generated
	 * @ordered
	 */
    protected LogNode[] childrens = CHILDRENS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getTag() <em>Tag</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected Tag tag = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TagLogNodeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return LogModelPackage.eINSTANCE.getTagLogNode();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @SuppressWarnings("unchecked")
    public LogNode[] getChildrens() {
        if (childrens == null) {
            EMap tags = tag.getTags();
            EMap properties = tag.getProperties();
            childrens = new LogNode[properties.size() + tags.size()];
            List sortedTagKeys = new ArrayList(tags.keySet());
            Collections.sort(sortedTagKeys, new NumberKeyComparator());
            Iterator tagsIterator = sortedTagKeys.iterator();
            Iterator propertiesIterator = properties.keySet().iterator();
            for (int i = 0; i < properties.size(); i++) {
                String key = (String) propertiesIterator.next();
                PropertyLogNode pln = LogModelFactory.eINSTANCE.createPropertyLogNode();
                pln.setName(key);
                pln.setValue((String) properties.get(key));
                childrens[i] = pln;
            }
            for (int i = properties.size(); i < childrens.length; i++) {
                String key = (String) tagsIterator.next();
                Tag t = (Tag) tags.get(key);
                TagLogNode tln = LogModelFactory.eINSTANCE.createTagLogNode();
                tln.setName(key);
                tln.setTag(t);
                childrens[i] = tln;
            }
        }
        return childrens;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setChildrens(LogNode[] newChildrens) {
        LogNode[] oldChildrens = childrens;
        childrens = newChildrens;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LogModelPackage.TAG_LOG_NODE__CHILDRENS, oldChildrens, childrens));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Tag getTag() {
        if (tag != null && tag.eIsProxy()) {
            Tag oldTag = tag;
            tag = (Tag) eResolveProxy((InternalEObject) tag);
            if (tag != oldTag) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, LogModelPackage.TAG_LOG_NODE__TAG, oldTag, tag));
            }
        }
        return tag;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Tag basicGetTag() {
        return tag;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTag(Tag newTag) {
        Tag oldTag = tag;
        tag = newTag;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LogModelPackage.TAG_LOG_NODE__TAG, oldTag, tag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(EStructuralFeature eFeature, boolean resolve) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case LogModelPackage.TAG_LOG_NODE__NAME:
                return getName();
            case LogModelPackage.TAG_LOG_NODE__CHILDRENS:
                return getChildrens();
            case LogModelPackage.TAG_LOG_NODE__TAG:
                if (resolve) return getTag();
                return basicGetTag();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(EStructuralFeature eFeature, Object newValue) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case LogModelPackage.TAG_LOG_NODE__NAME:
                setName((String) newValue);
                return;
            case LogModelPackage.TAG_LOG_NODE__CHILDRENS:
                setChildrens((LogNode[]) newValue);
                return;
            case LogModelPackage.TAG_LOG_NODE__TAG:
                setTag((Tag) newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(EStructuralFeature eFeature) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case LogModelPackage.TAG_LOG_NODE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case LogModelPackage.TAG_LOG_NODE__CHILDRENS:
                setChildrens(CHILDRENS_EDEFAULT);
                return;
            case LogModelPackage.TAG_LOG_NODE__TAG:
                setTag((Tag) null);
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(EStructuralFeature eFeature) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case LogModelPackage.TAG_LOG_NODE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case LogModelPackage.TAG_LOG_NODE__CHILDRENS:
                return CHILDRENS_EDEFAULT == null ? childrens != null : !CHILDRENS_EDEFAULT.equals(childrens);
            case LogModelPackage.TAG_LOG_NODE__TAG:
                return tag != null;
        }
        return eDynamicIsSet(eFeature);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (childrens: ");
        result.append(childrens);
        result.append(')');
        return result.toString();
    }
}
