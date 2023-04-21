package it.unisannio.rcost.callgraphanalyzer.impl;

import it.unisannio.rcost.callgraphanalyzer.CallGraphFactory;
import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.Interface;
import it.unisannio.rcost.callgraphanalyzer.Node;
import it.unisannio.rcost.callgraphanalyzer.NodeContainer;
import it.unisannio.rcost.callgraphanalyzer.Package;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.impl.PackageImpl#getInnerModulesList <em>Inner Modules</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.impl.PackageImpl#getPackagesList <em>Packages</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PackageImpl extends NodeContainerImpl implements it.unisannio.rcost.callgraphanalyzer.Package {

    /**
	 * The cached value of the '{@link #getInnerModulesList() <em>Inner Modules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInnerModulesList()
	 * @generated
	 * @ordered
	 */
    protected EList<Interface> innerModules;

    /**
	 * The empty value for the '{@link #getInnerModules() <em>Inner Modules</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInnerModules()
	 * @generated
	 * @ordered
	 */
    protected static final Interface[] INNER_MODULES_EEMPTY_ARRAY = new Interface[0];

    /**
	 * The cached value of the '{@link #getPackagesList() <em>Packages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackagesList()
	 * @generated
	 * @ordered
	 */
    protected EList<it.unisannio.rcost.callgraphanalyzer.Package> packages;

    /**
	 * The empty value for the '{@link #getPackages() <em>Packages</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackages()
	 * @generated
	 * @ordered
	 */
    protected static final it.unisannio.rcost.callgraphanalyzer.Package[] PACKAGES_EEMPTY_ARRAY = new it.unisannio.rcost.callgraphanalyzer.Package[0];

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected PackageImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CallGraphPackage.Literals.PACKAGE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public Interface[] getInnerModules() {
        if (innerModules == null || innerModules.isEmpty()) return INNER_MODULES_EEMPTY_ARRAY;
        BasicEList<Interface> list = (BasicEList<Interface>) innerModules;
        list.shrink();
        return (Interface[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Interface getInnerModules(int index) {
        return getInnerModulesList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getInnerModulesLength() {
        return innerModules == null ? 0 : innerModules.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void setInnerModules(Interface[] newInnerModules) {
        ((BasicEList<Interface>) getInnerModulesList()).setData(newInnerModules.length, newInnerModules);
        for (int i = 0; i < newInnerModules.length; i++) {
            newInnerModules[i].setNodeContainer(this);
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void setInnerModules(int index, Interface element) {
        getInnerModulesList().set(index, element);
        element.setNodeContainer(this);
    }

    public void addInnerModule(Interface element) {
        EList<Interface> interfaces = getInnerModulesList();
        if (!interfaces.contains(element)) {
            interfaces.add(element);
            element.setNodeContainer(this);
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Interface> getInnerModulesList() {
        if (innerModules == null) {
            innerModules = new EObjectContainmentEList<Interface>(Interface.class, this, CallGraphPackage.PACKAGE__INNER_MODULES);
        }
        return innerModules;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public it.unisannio.rcost.callgraphanalyzer.Package[] getPackages() {
        if (packages == null || packages.isEmpty()) return PACKAGES_EEMPTY_ARRAY;
        BasicEList<it.unisannio.rcost.callgraphanalyzer.Package> list = (BasicEList<it.unisannio.rcost.callgraphanalyzer.Package>) packages;
        list.shrink();
        return (it.unisannio.rcost.callgraphanalyzer.Package[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public it.unisannio.rcost.callgraphanalyzer.Package getPackages(int index) {
        return getPackagesList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getPackagesLength() {
        return packages == null ? 0 : packages.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void setPackages(it.unisannio.rcost.callgraphanalyzer.Package[] newPackages) {
        ((BasicEList<Package>) getPackagesList()).setData(newPackages.length, newPackages);
        for (int i = 0; i < newPackages.length; i++) {
            newPackages[i].setNodeContainer(this);
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void setPackages(int index, it.unisannio.rcost.callgraphanalyzer.Package element) {
        if (!this.equals(element)) {
            getPackagesList().set(index, element);
            element.setNodeContainer(this);
        }
    }

    public void addPackage(Package element) {
        EList<Package> packages = getPackagesList();
        if (!packages.contains(element)) {
            packages.add(element);
            element.setNodeContainer(this);
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<it.unisannio.rcost.callgraphanalyzer.Package> getPackagesList() {
        if (packages == null) {
            packages = new EObjectContainmentEList<it.unisannio.rcost.callgraphanalyzer.Package>(it.unisannio.rcost.callgraphanalyzer.Package.class, this, CallGraphPackage.PACKAGE__PACKAGES);
        }
        return packages;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case CallGraphPackage.PACKAGE__INNER_MODULES:
                return ((InternalEList<?>) getInnerModulesList()).basicRemove(otherEnd, msgs);
            case CallGraphPackage.PACKAGE__PACKAGES:
                return ((InternalEList<?>) getPackagesList()).basicRemove(otherEnd, msgs);
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
            case CallGraphPackage.PACKAGE__INNER_MODULES:
                return getInnerModulesList();
            case CallGraphPackage.PACKAGE__PACKAGES:
                return getPackagesList();
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
            case CallGraphPackage.PACKAGE__INNER_MODULES:
                getInnerModulesList().clear();
                getInnerModulesList().addAll((Collection<? extends Interface>) newValue);
                return;
            case CallGraphPackage.PACKAGE__PACKAGES:
                getPackagesList().clear();
                getPackagesList().addAll((Collection<? extends it.unisannio.rcost.callgraphanalyzer.Package>) newValue);
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
            case CallGraphPackage.PACKAGE__INNER_MODULES:
                getInnerModulesList().clear();
                return;
            case CallGraphPackage.PACKAGE__PACKAGES:
                getPackagesList().clear();
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
            case CallGraphPackage.PACKAGE__INNER_MODULES:
                return innerModules != null && !innerModules.isEmpty();
            case CallGraphPackage.PACKAGE__PACKAGES:
                return packages != null && !packages.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    @Override
    boolean isValidModifiers() {
        return false;
    }

    @Override
    public void setNodeContainer(NodeContainer newNodeContainer) {
        if (nodeContainer == null || !nodeContainer.equals(newNodeContainer)) {
            super.setNodeContainer(newNodeContainer);
            if (newNodeContainer instanceof Package) {
                ((Package) newNodeContainer).addPackage(this);
            }
        }
    }

    @Override
    protected Node cloneNode() {
        return CallGraphFactory.eINSTANCE.createPackage();
    }
}
