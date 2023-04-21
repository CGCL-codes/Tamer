package alf2java.papyrus.alfmetamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>AOperation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link alf2java.papyrus.alfmetamodel.AOperation#getAOperation_has_body <em>AOperation has body</em>}</li>
 *   <li>{@link alf2java.papyrus.alfmetamodel.AOperation#getName <em>Name</em>}</li>
 *   <li>{@link alf2java.papyrus.alfmetamodel.AOperation#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link alf2java.papyrus.alfmetamodel.AOperation#getAOperation_has_param <em>AOperation has param</em>}</li>
 *   <li>{@link alf2java.papyrus.alfmetamodel.AOperation#isIsStatic <em>Is Static</em>}</li>
 *   <li>{@link alf2java.papyrus.alfmetamodel.AOperation#getVisibility <em>Visibility</em>}</li>
 * </ul>
 * </p>
 *
 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation()
 * @model
 * @generated
 */
public interface AOperation extends EObject {

    /**
	 * Returns the value of the '<em><b>AOperation has body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AOperation has body</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AOperation has body</em>' containment reference.
	 * @see #setAOperation_has_body(AAnnotation)
	 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation_AOperation_has_body()
	 * @model containment="true"
	 * @generated
	 */
    AAnnotation getAOperation_has_body();

    /**
	 * Sets the value of the '{@link alf2java.papyrus.alfmetamodel.AOperation#getAOperation_has_body <em>AOperation has body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>AOperation has body</em>' containment reference.
	 * @see #getAOperation_has_body()
	 * @generated
	 */
    void setAOperation_has_body(AAnnotation value);

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
	 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link alf2java.papyrus.alfmetamodel.AOperation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Return Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Type</em>' attribute.
	 * @see #setReturnType(String)
	 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation_ReturnType()
	 * @model
	 * @generated
	 */
    String getReturnType();

    /**
	 * Sets the value of the '{@link alf2java.papyrus.alfmetamodel.AOperation#getReturnType <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Type</em>' attribute.
	 * @see #getReturnType()
	 * @generated
	 */
    void setReturnType(String value);

    /**
	 * Returns the value of the '<em><b>AOperation has param</b></em>' containment reference list.
	 * The list contents are of type {@link alf2java.papyrus.alfmetamodel.AParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AOperation has param</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AOperation has param</em>' containment reference list.
	 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation_AOperation_has_param()
	 * @model containment="true"
	 * @generated
	 */
    EList<AParameter> getAOperation_has_param();

    /**
	 * Returns the value of the '<em><b>Is Static</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Static</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Static</em>' attribute.
	 * @see #setIsStatic(boolean)
	 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation_IsStatic()
	 * @model
	 * @generated
	 */
    boolean isIsStatic();

    /**
	 * Sets the value of the '{@link alf2java.papyrus.alfmetamodel.AOperation#isIsStatic <em>Is Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Static</em>' attribute.
	 * @see #isIsStatic()
	 * @generated
	 */
    void setIsStatic(boolean value);

    /**
	 * Returns the value of the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visibility</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Visibility</em>' attribute.
	 * @see #setVisibility(String)
	 * @see alf2java.papyrus.alfmetamodel.AlfmetamodelPackage#getAOperation_Visibility()
	 * @model
	 * @generated
	 */
    String getVisibility();

    /**
	 * Sets the value of the '{@link alf2java.papyrus.alfmetamodel.AOperation#getVisibility <em>Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Visibility</em>' attribute.
	 * @see #getVisibility()
	 * @generated
	 */
    void setVisibility(String value);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='if (eIsProxy()) return super.toString();\n\t\t\n\t\tString result = this.returnType +\"  \"+this.name+\" (\";\n\t\tif (this.aOperation_has_param != null) {\n\t\t\tfor(AParameter ap : this.aOperation_has_param) {\n\t\t\t\tresult += ap+\", \";\n\t\t\t}\n\t\t\tresult.substring(0,result.length()-3);\n\t\t}\n\t\tresult += \") {\\n\";\n\t\tif (this.aOperation_has_body != null) {\n\t\t\tresult +=this.aOperation_has_body+\"\\n\";\n\t\t}\n\t\tresult += \"}\";\n\t\treturn result;'"
	 * @generated
	 */
    String toString();
}
