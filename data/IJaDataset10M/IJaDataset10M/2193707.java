package de.mpiwg.vspace.metamodel.transformed;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exhibition Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getTitle <em>Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getDescription <em>Description</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getMapImage <em>Map Image</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getLinksToScenes <em>Links To Scenes</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getLinksToExhibitionModules <em>Links To Exhibition Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibitionMap()
 * @model
 * @generated
 */
public interface ExhibitionMap extends EObject {

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
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibitionMap_Title()
	 * @model
	 * @generated
	 */
    String getTitle();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getTitle <em>Title</em>}' attribute.
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
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibitionMap_Description()
	 * @model
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Map Image</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Image</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Image</em>' containment reference.
	 * @see #setMapImage(Image)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibitionMap_MapImage()
	 * @model containment="true"
	 * @generated
	 */
    Image getMapImage();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionMap#getMapImage <em>Map Image</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Image</em>' containment reference.
	 * @see #getMapImage()
	 * @generated
	 */
    void setMapImage(Image value);

    /**
	 * Returns the value of the '<em><b>Links To Scenes</b></em>' containment reference list.
	 * The list contents are of type {@link de.mpiwg.vspace.metamodel.transformed.SceneLink}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Links To Scenes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Links To Scenes</em>' containment reference list.
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibitionMap_LinksToScenes()
	 * @model containment="true"
	 * @generated
	 */
    EList<SceneLink> getLinksToScenes();

    /**
	 * Returns the value of the '<em><b>Links To Exhibition Modules</b></em>' containment reference list.
	 * The list contents are of type {@link de.mpiwg.vspace.metamodel.transformed.ExhibitionModuleLink}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Links To Exhibition Modules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Links To Exhibition Modules</em>' containment reference list.
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getExhibitionMap_LinksToExhibitionModules()
	 * @model containment="true"
	 * @generated
	 */
    EList<ExhibitionModuleLink> getLinksToExhibitionModules();
}
