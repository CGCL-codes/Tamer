package UMLAA.impl;

import UMLAA.AlloyCode;
import UMLAA.UMLAAPackage;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

public class PackageImpl extends NamedElementImpl implements UMLAA.Package {

    protected EList<UMLAA.Class> classes;

    protected EList<AlloyCode> alloyCode;

    protected PackageImpl() {
        super();
    }

    @Override
    protected EClass eStaticClass() {
        return UMLAAPackage.Literals.PACKAGE;
    }

    public EList<UMLAA.Class> getClasses() {
        if (classes == null) {
            classes = new EObjectContainmentEList<UMLAA.Class>(UMLAA.Class.class, this, UMLAAPackage.PACKAGE__CLASSES);
        }
        return classes;
    }

    public EList<AlloyCode> getAlloyCode() {
        if (alloyCode == null) {
            alloyCode = new EObjectContainmentEList<AlloyCode>(AlloyCode.class, this, UMLAAPackage.PACKAGE__ALLOY_CODE);
        }
        return alloyCode;
    }

    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UMLAAPackage.PACKAGE__CLASSES:
                return ((InternalEList<?>) getClasses()).basicRemove(otherEnd, msgs);
            case UMLAAPackage.PACKAGE__ALLOY_CODE:
                return ((InternalEList<?>) getAlloyCode()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UMLAAPackage.PACKAGE__CLASSES:
                return getClasses();
            case UMLAAPackage.PACKAGE__ALLOY_CODE:
                return getAlloyCode();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case UMLAAPackage.PACKAGE__CLASSES:
                getClasses().clear();
                getClasses().addAll((Collection<? extends UMLAA.Class>) newValue);
                return;
            case UMLAAPackage.PACKAGE__ALLOY_CODE:
                getAlloyCode().clear();
                getAlloyCode().addAll((Collection<? extends AlloyCode>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case UMLAAPackage.PACKAGE__CLASSES:
                getClasses().clear();
                return;
            case UMLAAPackage.PACKAGE__ALLOY_CODE:
                getAlloyCode().clear();
                return;
        }
        super.eUnset(featureID);
    }

    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case UMLAAPackage.PACKAGE__CLASSES:
                return classes != null && !classes.isEmpty();
            case UMLAAPackage.PACKAGE__ALLOY_CODE:
                return alloyCode != null && !alloyCode.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    public String getAlloyTranslation() {
        StringBuilder alloyCode = new StringBuilder();
        alloyCode.append("module " + this.name + "\r\n");
        for (UMLAA.Class aClass : this.classes) if (aClass instanceof UMLAA.impl.ClassImpl) alloyCode.append(((UMLAA.impl.ClassImpl) aClass).getAlloyTranslation());
        for (UMLAA.AlloyCode alloy : this.alloyCode) if (alloy instanceof UMLAA.impl.AlloyCodeImpl) alloyCode.append(((UMLAA.impl.AlloyCodeImpl) alloy).getAlloyTranslation());
        return alloyCode.toString();
    }
}
