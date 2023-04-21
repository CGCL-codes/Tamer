package de.mpiwg.vspace.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @model
 *
 */
public interface LinkedMapContainer extends EObject {

    /**
	 * @model containment="true"
	 */
    public EList<OverviewMap> getOverviewMaps();

    /**
	 * Returns the value of the '<em><b>Main Overview Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Main Overview Map</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Main Overview Map</em>' reference.
	 * @see #setMainOverviewMap(OverviewMap)
	 * @see de.mpiwg.vspace.metamodel.ExhibitionPackage#getLinkedMapContainer_MainOverviewMap()
	 * @model
	 * @generated
	 */
    OverviewMap getMainOverviewMap();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.LinkedMapContainer#getMainOverviewMap <em>Main Overview Map</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Main Overview Map</em>' reference.
	 * @see #getMainOverviewMap()
	 * @generated
	 */
    void setMainOverviewMap(OverviewMap value);

    public String getId();

    public void setId(String id);
}
