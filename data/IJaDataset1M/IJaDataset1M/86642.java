package drarch.model;

import org.eclipse.emf.ecore.EObject;

/**
 * 
 * @author Facundo Maldonado
 * @drarch.model
 */
public interface Rule extends EObject {

    /**
	 * @return
	 * @drarch.model containment="true" 
	 */
    Query getQuery();

    /**
	 * Sets the value of the '{@link drarch.model.Rule#getQuery <em>Query</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query</em>' containment reference.
	 * @see #getQuery()
	 * @generated
	 */
    void setQuery(Query value);

    /**
	 * @return
	 * @drarch.model containment="true"
	 */
    FactSet getFactSet();

    /**
	 * Sets the value of the '{@link drarch.model.Rule#getFactSet <em>Fact Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fact Set</em>' containment reference.
	 * @see #getFactSet()
	 * @generated
	 */
    void setFactSet(FactSet value);

    /**
	 * @return
	 * @drarch.model
	 */
    String getSuggestTemplate();

    /**
	 * Sets the value of the '{@link drarch.model.Rule#getSuggestTemplate <em>Suggest Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Suggest Template</em>' attribute.
	 * @see #getSuggestTemplate()
	 * @generated
	 */
    void setSuggestTemplate(String value);

    /**
	 * @drarch.model
	 * @return
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link drarch.model.Rule#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);
}
