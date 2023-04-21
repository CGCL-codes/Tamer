package ru.satseqsys.model;

import java.util.List;

/** 
 * A representation of the model object '<em><b>Tariff</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Информация о тарифе
 * <!-- end-model-doc -->
 * @generated 
 */
public interface Tariff extends DataBaseObject {

    /**
	 * Returns the value of '<em><b>name</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>name</b></em>' feature
	 * @generated
	 */
    public String getName();

    /**
	 * Sets the '{@link Tariff#getName() <em>name</em>}' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param the new value of the '{@link Tariff#getName() <em>name</em>}' feature.
	 * @generated
	 */
    public void setName(String newName);

    /**
	 * Returns the value of '<em><b>active</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>active</b></em>' feature
	 * @generated
	 */
    public boolean isActive();

    /**
	 * Sets the '{@link Tariff#isActive() <em>active</em>}' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param the new value of the '{@link Tariff#isActive() <em>active</em>}' feature.
	 * @generated
	 */
    public void setActive(boolean newActive);

    /**
	 * Returns the value of '<em><b>volumePayments</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>volumePayments</b></em>' feature
	 * @generated
	 */
    public List<VolumePayment> getVolumePayments();

    public void setVolumePayments(List<VolumePayment> volumePayments);

    /**
	 * Returns the value of '<em><b>periodicalPayments</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>periodicalPayments</b></em>' feature
	 * @generated
	 */
    public List<PeriodicalPayment> getPeriodicalPayments();

    public void setPeriodicalPayments(List<PeriodicalPayment> periodicalPayments);

    /**
	 * Returns the value of '<em><b>initialPayment</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>initialPayment</b></em>' feature
	 * @generated
	 */
    public List<OneTimePayment> getInitialPayments();

    public void setInitialPayments(List<OneTimePayment> initialPayments);

    /**
	 * Returns the value of '<em><b>services</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>services</b></em>' feature
	 * @generated
	 */
    public List<Service> getServices();

    public void setServices(List<Service> services);

    /**
	 * Returns the value of '<em><b>id</em></b>' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of '<em><b>id</b></em>' feature
	 * @generated
	 */
    public Long getId();

    /**
	 * Sets the '{@link Tariff#getId() <em>id</em>}' feature.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param the new value of the '{@link Tariff#getId() <em>id</em>}' feature.
	 * @generated
	 */
    public void setId(Long newId);
}
