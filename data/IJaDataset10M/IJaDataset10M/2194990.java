package net.randomice.gengmf.template;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.gmfgraph.Compartment;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Compartment Template</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link net.randomice.gengmf.template.CompartmentTemplate#getCompartments
 * <em>Compartments</em>}</li>
 * </ul>
 * </p>
 * 
 * @see net.randomice.gengmf.template.TemplatePackage#getCompartmentTemplate()
 * @model
 * @generated
 */
public interface CompartmentTemplate extends AbstractNodeTemplate {

    /**
	 * Returns the value of the '<em><b>Compartments</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.gmf.gmfgraph.Compartment}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Compartments</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Compartments</em>' containment reference
	 *         list.
	 * @see net.randomice.gengmf.template.TemplatePackage#getCompartmentTemplate_Compartments()
	 * @model containment="true"
	 * @generated
	 */
    EList<Compartment> getCompartments();
}
