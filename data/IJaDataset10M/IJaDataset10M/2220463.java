package com.objectcarpentry.kaylee;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Filtered Playlist</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.objectcarpentry.kaylee.FilteredPlaylist#getFilter <em>Filter</em>}</li>
 *   <li>{@link com.objectcarpentry.kaylee.FilteredPlaylist#getDelegate <em>Delegate</em>}</li>
 *   <li>{@link com.objectcarpentry.kaylee.FilteredPlaylist#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.objectcarpentry.kaylee.KayleePackage#getFilteredPlaylist()
 * @model
 * @generated
 */
public interface FilteredPlaylist extends AbstractPlaylist<Playable> {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright 2009. Felix Dorner.\nThis code is licensed under the Eclipse Public License v1.0\nhttp://www.eclipse.org/org/documents/epl-v10.html";

    /**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(IPlayableFilter)
	 * @see com.objectcarpentry.kaylee.KayleePackage#getFilteredPlaylist_Filter()
	 * @model dataType="com.objectcarpentry.kaylee.IPlayableFilter"
	 * @generated
	 */
    IPlayableFilter getFilter();

    /**
	 * Sets the value of the '{@link com.objectcarpentry.kaylee.FilteredPlaylist#getFilter <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter</em>' attribute.
	 * @see #getFilter()
	 * @generated
	 */
    void setFilter(IPlayableFilter value);

    /**
	 * Returns the value of the '<em><b>Delegate</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Delegate</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Delegate</em>' reference.
	 * @see #setDelegate(AbstractPlaylist)
	 * @see com.objectcarpentry.kaylee.KayleePackage#getFilteredPlaylist_Delegate()
	 * @model
	 * @generated
	 */
    AbstractPlaylist<? extends Playable> getDelegate();

    /**
	 * Sets the value of the '{@link com.objectcarpentry.kaylee.FilteredPlaylist#getDelegate <em>Delegate</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delegate</em>' reference.
	 * @see #getDelegate()
	 * @generated
	 */
    void setDelegate(AbstractPlaylist<? extends Playable> value);

    /**
	 * Returns the value of the '<em><b>Elements</b></em>' reference list.
	 * The list contents are of type {@link com.objectcarpentry.kaylee.Playable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' reference list.
	 * @see com.objectcarpentry.kaylee.KayleePackage#getFilteredPlaylist_Elements()
	 * @model derived="true"
	 * @generated
	 */
    EList<Playable> getElements();
}
