package de.mpiwg.vspace.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @model
 *
 */
public interface Exhibition extends EObject {

    /**
	 * @model
	 *
	 */
    public String getTitle();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * @model
	 *
	 */
    public String getDescription();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * @model containment="true"
	 *
	 */
    public MapContainer getMapContainer();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getMapContainer <em>Map Container</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Container</em>' containment reference.
	 * @see #getMapContainer()
	 * @generated
	 */
    void setMapContainer(MapContainer value);

    /**
	 * @model containment="true"
	 *
	 */
    public EList<Scene> getScenes();

    /**
	 * @model containment="true"
	 *
	 */
    public EList<ExhibitionModuleReference> getExhibitionModuleReferences();

    /**
	 * @model
	 */
    public Scene getStartScene();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getStartScene <em>Start Scene</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Scene</em>' reference.
	 * @see #getStartScene()
	 * @generated
	 */
    void setStartScene(Scene value);

    /**
	 * @model
	 */
    public String getId();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Copyright</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Copyright</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Copyright</em>' containment reference.
	 * @see #setCopyright(Copyright)
	 * @see de.mpiwg.vspace.metamodel.ExhibitionPackage#getExhibition_Copyright()
	 * @model containment="true"
	 * @generated
	 */
    Copyright getCopyright();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getCopyright <em>Copyright</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Copyright</em>' containment reference.
	 * @see #getCopyright()
	 * @generated
	 */
    void setCopyright(Copyright value);

    /**
	 * Returns the value of the '<em><b>Module Category Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Module Category Container</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module Category Container</em>' containment reference.
	 * @see #setModuleCategoryContainer(ModuleCategoryContainer)
	 * @see de.mpiwg.vspace.metamodel.ExhibitionPackage#getExhibition_ModuleCategoryContainer()
	 * @model containment="true" required="true"
	 * @generated
	 */
    ModuleCategoryContainer getModuleCategoryContainer();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Exhibition#getModuleCategoryContainer <em>Module Category Container</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module Category Container</em>' containment reference.
	 * @see #getModuleCategoryContainer()
	 * @generated
	 */
    void setModuleCategoryContainer(ModuleCategoryContainer value);
}
