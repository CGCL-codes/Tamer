package de.morknet.mrw.metamodel.impl;

import de.morknet.mrw.metamodel.Controller;
import de.morknet.mrw.metamodel.Gruppe;
import de.morknet.mrw.metamodel.Modell;
import de.morknet.mrw.metamodel.ModelrailwayPackage;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Modell</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.morknet.mrw.metamodel.impl.ModellImpl#getController <em>Controller</em>}</li>
 *   <li>{@link de.morknet.mrw.metamodel.impl.ModellImpl#getGruppe <em>Gruppe</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModellImpl extends ElementImpl implements Modell {

    /**
	 * The cached value of the '{@link #getController() <em>Controller</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getController()
	 * @generated
	 * @ordered
	 */
    protected EList<Controller> controller;

    /**
	 * The cached value of the '{@link #getGruppe() <em>Gruppe</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGruppe()
	 * @generated
	 * @ordered
	 */
    protected EList<Gruppe> gruppe;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ModellImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelrailwayPackage.Literals.MODELL;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Controller> getController() {
        if (controller == null) {
            controller = new EObjectContainmentWithInverseEList<Controller>(Controller.class, this, ModelrailwayPackage.MODELL__CONTROLLER, ModelrailwayPackage.CONTROLLER__MODELL);
        }
        return controller;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Gruppe> getGruppe() {
        if (gruppe == null) {
            gruppe = new EObjectContainmentWithInverseEList<Gruppe>(Gruppe.class, this, ModelrailwayPackage.MODELL__GRUPPE, ModelrailwayPackage.GRUPPE__MODELL);
        }
        return gruppe;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelrailwayPackage.MODELL__CONTROLLER:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getController()).basicAdd(otherEnd, msgs);
            case ModelrailwayPackage.MODELL__GRUPPE:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getGruppe()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelrailwayPackage.MODELL__CONTROLLER:
                return ((InternalEList<?>) getController()).basicRemove(otherEnd, msgs);
            case ModelrailwayPackage.MODELL__GRUPPE:
                return ((InternalEList<?>) getGruppe()).basicRemove(otherEnd, msgs);
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
            case ModelrailwayPackage.MODELL__CONTROLLER:
                return getController();
            case ModelrailwayPackage.MODELL__GRUPPE:
                return getGruppe();
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
            case ModelrailwayPackage.MODELL__CONTROLLER:
                getController().clear();
                getController().addAll((Collection<? extends Controller>) newValue);
                return;
            case ModelrailwayPackage.MODELL__GRUPPE:
                getGruppe().clear();
                getGruppe().addAll((Collection<? extends Gruppe>) newValue);
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
            case ModelrailwayPackage.MODELL__CONTROLLER:
                getController().clear();
                return;
            case ModelrailwayPackage.MODELL__GRUPPE:
                getGruppe().clear();
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
            case ModelrailwayPackage.MODELL__CONTROLLER:
                return controller != null && !controller.isEmpty();
            case ModelrailwayPackage.MODELL__GRUPPE:
                return gruppe != null && !gruppe.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
