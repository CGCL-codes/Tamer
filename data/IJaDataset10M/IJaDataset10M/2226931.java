package UMLAA.impl;

import UMLAA.StructuralFeature;
import UMLAA.UMLAAPackage;
import UMLAA.Visibility;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

public abstract class StructuralFeatureImpl extends TypedElementImpl implements StructuralFeature {

    protected static final Visibility VISIBILITY_EDEFAULT = Visibility.PRIVATE;

    protected Visibility visibility = VISIBILITY_EDEFAULT;

    protected static final boolean STATIC_EDEFAULT = false;

    protected boolean static_ = STATIC_EDEFAULT;

    protected StructuralFeatureImpl() {
        super();
    }

    @Override
    protected EClass eStaticClass() {
        return UMLAAPackage.Literals.STRUCTURAL_FEATURE;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility newVisibility) {
        Visibility oldVisibility = visibility;
        visibility = newVisibility == null ? VISIBILITY_EDEFAULT : newVisibility;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLAAPackage.STRUCTURAL_FEATURE__VISIBILITY, oldVisibility, visibility));
    }

    public boolean isStatic() {
        return static_;
    }

    public void setStatic(boolean newStatic) {
        boolean oldStatic = static_;
        static_ = newStatic;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLAAPackage.STRUCTURAL_FEATURE__STATIC, oldStatic, static_));
    }

    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UMLAAPackage.STRUCTURAL_FEATURE__VISIBILITY:
                return getVisibility();
            case UMLAAPackage.STRUCTURAL_FEATURE__STATIC:
                return isStatic() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case UMLAAPackage.STRUCTURAL_FEATURE__VISIBILITY:
                setVisibility((Visibility) newValue);
                return;
            case UMLAAPackage.STRUCTURAL_FEATURE__STATIC:
                setStatic(((Boolean) newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case UMLAAPackage.STRUCTURAL_FEATURE__VISIBILITY:
                setVisibility(VISIBILITY_EDEFAULT);
                return;
            case UMLAAPackage.STRUCTURAL_FEATURE__STATIC:
                setStatic(STATIC_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case UMLAAPackage.STRUCTURAL_FEATURE__VISIBILITY:
                return visibility != VISIBILITY_EDEFAULT;
            case UMLAAPackage.STRUCTURAL_FEATURE__STATIC:
                return static_ != STATIC_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (visibility: ");
        result.append(visibility);
        result.append(", static: ");
        result.append(static_);
        result.append(')');
        return result.toString();
    }
}
