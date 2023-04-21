package net.sf.rcpforms.emf.test;

import java.util.Date;
import net.sf.rcpforms.test.adapter.ITestModel;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * @extends ITestModel
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getGender <em>Gender</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getBirthDate <em>Birth Date</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getOverdrawAccount <em>Overdraw Account</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getChildCount <em>Child Count</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getAge <em>Age</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getAccountBalance <em>Account Balance</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getIsSelectable <em>Is Selectable</em>}</li>
 *   <li>{@link net.sf.rcpforms.emf.test.TestModel#getAddress <em>Address</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel()
 * @model
 * @generated
 */
public interface TestModel extends EObject, ITestModel {

    /**
	 * Returns the value of the '<em><b>Gender</b></em>' attribute.
	 * The literals are from the enumeration {@link net.sf.rcpforms.emf.test.Gender}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Gender</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Gender</em>' attribute.
	 * @see net.sf.rcpforms.emf.test.Gender
	 * @see #setGender(Gender)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_Gender()
	 * @model
	 * @generated
	 */
    Gender getGender();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getGender <em>Gender</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Gender</em>' attribute.
	 * @see net.sf.rcpforms.emf.test.Gender
	 * @see #getGender()
	 * @generated
	 */
    void setGender(Gender value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Birth Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Birth Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Birth Date</em>' attribute.
	 * @see #setBirthDate(Date)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_BirthDate()
	 * @model
	 * @generated
	 */
    Date getBirthDate();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getBirthDate <em>Birth Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Birth Date</em>' attribute.
	 * @see #getBirthDate()
	 * @generated
	 */
    void setBirthDate(Date value);

    /**
	 * Returns the value of the '<em><b>Overdraw Account</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Overdraw Account</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Overdraw Account</em>' attribute.
	 * @see #setOverdrawAccount(Boolean)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_OverdrawAccount()
	 * @model
	 * @generated
	 */
    Boolean getOverdrawAccount();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getOverdrawAccount <em>Overdraw Account</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Overdraw Account</em>' attribute.
	 * @see #getOverdrawAccount()
	 * @generated
	 */
    void setOverdrawAccount(Boolean value);

    /**
	 * Returns the value of the '<em><b>Child Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Count</em>' attribute.
	 * @see #setChildCount(Integer)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_ChildCount()
	 * @model
	 * @generated
	 */
    Integer getChildCount();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getChildCount <em>Child Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Count</em>' attribute.
	 * @see #getChildCount()
	 * @generated
	 */
    void setChildCount(Integer value);

    /**
	 * Returns the value of the '<em><b>Age</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Age</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Age</em>' attribute.
	 * @see #setAge(int)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_Age()
	 * @model
	 * @generated
	 */
    int getAge();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getAge <em>Age</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Age</em>' attribute.
	 * @see #getAge()
	 * @generated
	 */
    void setAge(int value);

    /**
	 * Returns the value of the '<em><b>Account Balance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Account Balance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Account Balance</em>' attribute.
	 * @see #setAccountBalance(Double)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_AccountBalance()
	 * @model
	 * @generated
	 */
    Double getAccountBalance();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getAccountBalance <em>Account Balance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Account Balance</em>' attribute.
	 * @see #getAccountBalance()
	 * @generated
	 */
    void setAccountBalance(Double value);

    /**
	 * Returns the value of the '<em><b>Is Selectable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Selectable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Selectable</em>' attribute.
	 * @see #setIsSelectable(Boolean)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_IsSelectable()
	 * @model
	 * @generated
	 */
    Boolean getIsSelectable();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getIsSelectable <em>Is Selectable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Selectable</em>' attribute.
	 * @see #getIsSelectable()
	 * @generated
	 */
    void setIsSelectable(Boolean value);

    /**
	 * Returns the value of the '<em><b>Address</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Address</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Address</em>' containment reference.
	 * @see #setAddress(AddressModel)
	 * @see net.sf.rcpforms.emf.test.TestPackage#getTestModel_Address()
	 * @model containment="true" required="true"
	 * @generated
	 */
    AddressModel getAddress();

    /**
	 * Sets the value of the '{@link net.sf.rcpforms.emf.test.TestModel#getAddress <em>Address</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Address</em>' containment reference.
	 * @see #getAddress()
	 * @generated
	 */
    void setAddress(AddressModel value);
}
