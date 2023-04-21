package de.mpiwg.vspace.metamodel.transformed;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exhibition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getTitle <em>Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getDescription <em>Description</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getScenes <em>Scenes</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getStartScene <em>Start Scene</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getId <em>Id</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getIdentificationNr <em>Identification Nr</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getCopyright <em>Copyright</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getSitemap <em>Sitemap</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getMapContainer <em>Map Container</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getClonedMapContainers <em>Cloned Map Containers</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getModuleCategories <em>Module Categories</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getVspaceMap <em>Vspace Map</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getClonedVSpaceMaps <em>Cloned VSpace Maps</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition()
 * @model
 * @generated
 */
public interface Exhibition extends GenerableItem {

    /**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_Title()
	 * @model
	 * @generated
	 */
    String getTitle();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_Description()
	 * @model
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Scenes</b></em>' containment reference list.
	 * The list contents are of type {@link de.mpiwg.vspace.metamodel.transformed.Scene}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenes</em>' containment reference list.
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_Scenes()
	 * @model containment="true"
	 * @generated
	 */
    EList<Scene> getScenes();

    /**
	 * Returns the value of the '<em><b>Start Scene</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Scene</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Scene</em>' reference.
	 * @see #setStartScene(Scene)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_StartScene()
	 * @model
	 * @generated
	 */
    Scene getStartScene();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getStartScene <em>Start Scene</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Scene</em>' reference.
	 * @see #getStartScene()
	 * @generated
	 */
    void setStartScene(Scene value);

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_Id()
	 * @model
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Identification Nr</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Identification Nr</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Identification Nr</em>' attribute.
	 * @see #setIdentificationNr(int)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_IdentificationNr()
	 * @model
	 * @generated
	 */
    int getIdentificationNr();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getIdentificationNr <em>Identification Nr</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identification Nr</em>' attribute.
	 * @see #getIdentificationNr()
	 * @generated
	 */
    void setIdentificationNr(int value);

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
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_Copyright()
	 * @model containment="true"
	 * @generated
	 */
    Copyright getCopyright();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getCopyright <em>Copyright</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Copyright</em>' containment reference.
	 * @see #getCopyright()
	 * @generated
	 */
    void setCopyright(Copyright value);

    /**
	 * Returns the value of the '<em><b>Sitemap</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sitemap</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sitemap</em>' containment reference.
	 * @see #setSitemap(Sitemap)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_Sitemap()
	 * @model containment="true"
	 * @generated
	 */
    Sitemap getSitemap();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getSitemap <em>Sitemap</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sitemap</em>' containment reference.
	 * @see #getSitemap()
	 * @generated
	 */
    void setSitemap(Sitemap value);

    /**
	 * Returns the value of the '<em><b>Map Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Container</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Container</em>' containment reference.
	 * @see #setMapContainer(MapContainer)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_MapContainer()
	 * @model containment="true"
	 * @generated
	 */
    MapContainer getMapContainer();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getMapContainer <em>Map Container</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Container</em>' containment reference.
	 * @see #getMapContainer()
	 * @generated
	 */
    void setMapContainer(MapContainer value);

    /**
	 * Returns the value of the '<em><b>Cloned Map Containers</b></em>' containment reference list.
	 * The list contents are of type {@link de.mpiwg.vspace.metamodel.transformed.MapContainer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cloned Map Containers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cloned Map Containers</em>' containment reference list.
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_ClonedMapContainers()
	 * @model containment="true"
	 * @generated
	 */
    EList<MapContainer> getClonedMapContainers();

    /**
	 * Returns the value of the '<em><b>Module Categories</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Module Categories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module Categories</em>' containment reference.
	 * @see #setModuleCategories(ModuleCategoryContainer)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_ModuleCategories()
	 * @model containment="true"
	 * @generated
	 */
    ModuleCategoryContainer getModuleCategories();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getModuleCategories <em>Module Categories</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module Categories</em>' containment reference.
	 * @see #getModuleCategories()
	 * @generated
	 */
    void setModuleCategories(ModuleCategoryContainer value);

    /**
	 * Returns the value of the '<em><b>Vspace Map</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vspace Map</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vspace Map</em>' containment reference.
	 * @see #setVspaceMap(VSpaceMap)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_VspaceMap()
	 * @model containment="true"
	 * @generated
	 */
    VSpaceMap getVspaceMap();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition#getVspaceMap <em>Vspace Map</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vspace Map</em>' containment reference.
	 * @see #getVspaceMap()
	 * @generated
	 */
    void setVspaceMap(VSpaceMap value);

    /**
	 * Returns the value of the '<em><b>Cloned VSpace Maps</b></em>' containment reference list.
	 * The list contents are of type {@link de.mpiwg.vspace.metamodel.transformed.VSpaceMap}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cloned VSpace Maps</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cloned VSpace Maps</em>' containment reference list.
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibition_ClonedVSpaceMaps()
	 * @model containment="true"
	 * @generated
	 */
    EList<VSpaceMap> getClonedVSpaceMaps();
}
