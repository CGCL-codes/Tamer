package net.sourceforge.olympos.dsl.domain.domain;

import net.sourceforge.olympos.dsl.common.common.BooleanKind;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Searchable Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.SearchableConfiguration#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sourceforge.olympos.dsl.domain.domain.DomainPackage#getSearchableConfiguration()
 * @model
 * @generated
 */
public interface SearchableConfiguration extends EObject {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * The literals are from the enumeration {@link net.sourceforge.olympos.dsl.common.common.BooleanKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see net.sourceforge.olympos.dsl.common.common.BooleanKind
	 * @see #setValue(BooleanKind)
	 * @see net.sourceforge.olympos.dsl.domain.domain.DomainPackage#getSearchableConfiguration_Value()
	 * @model
	 * @generated
	 */
    BooleanKind getValue();

    /**
	 * Sets the value of the '{@link net.sourceforge.olympos.dsl.domain.domain.SearchableConfiguration#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see net.sourceforge.olympos.dsl.common.common.BooleanKind
	 * @see #getValue()
	 * @generated
	 */
    void setValue(BooleanKind value);
}
