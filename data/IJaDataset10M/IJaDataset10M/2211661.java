package publications;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Publication</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link publications.Publication#getTitle <em>Title</em>}</li>
 *   <li>{@link publications.Publication#getAuthors <em>Authors</em>}</li>
 *   <li>{@link publications.Publication#getYear <em>Year</em>}</li>
 *   <li>{@link publications.Publication#getReferences <em>References</em>}</li>
 *   <li>{@link publications.Publication#isRead <em>Read</em>}</li>
 *   <li>{@link publications.Publication#getImportance <em>Importance</em>}</li>
 *   <li>{@link publications.Publication#getCitedBy <em>Cited By</em>}</li>
 *   <li>{@link publications.Publication#getContains <em>Contains</em>}</li>
 *   <li>{@link publications.Publication#getContainedIn <em>Contained In</em>}</li>
 *   <li>{@link publications.Publication#getOriginalReferenceText <em>Original Reference Text</em>}</li>
 *   <li>{@link publications.Publication#getLocalKey <em>Local Key</em>}</li>
 *   <li>{@link publications.Publication#getTags <em>Tags</em>}</li>
 *   <li>{@link publications.Publication#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see publications.PublicationsPackage#getPublication()
 * @model
 * @generated
 */
public interface Publication extends EObject {

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
	 * @see publications.PublicationsPackage#getPublication_Title()
	 * @model
	 * @generated
	 */
    String getTitle();

    /**
	 * Sets the value of the '{@link publications.Publication#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * Returns the value of the '<em><b>Authors</b></em>' reference list.
	 * The list contents are of type {@link publications.Person}.
	 * It is bidirectional and its opposite is '{@link publications.Person#getPublications <em>Publications</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Authors</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Authors</em>' reference list.
	 * @see publications.PublicationsPackage#getPublication_Authors()
	 * @see publications.Person#getPublications
	 * @model opposite="Publications" required="true"
	 * @generated
	 */
    EList<Person> getAuthors();

    /**
	 * Returns the value of the '<em><b>Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Year</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year</em>' attribute.
	 * @see #setYear(int)
	 * @see publications.PublicationsPackage#getPublication_Year()
	 * @model
	 * @generated
	 */
    int getYear();

    /**
	 * Sets the value of the '{@link publications.Publication#getYear <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year</em>' attribute.
	 * @see #getYear()
	 * @generated
	 */
    void setYear(int value);

    /**
	 * Returns the value of the '<em><b>References</b></em>' reference list.
	 * The list contents are of type {@link publications.Publication}.
	 * It is bidirectional and its opposite is '{@link publications.Publication#getCitedBy <em>Cited By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>References</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>References</em>' reference list.
	 * @see publications.PublicationsPackage#getPublication_References()
	 * @see publications.Publication#getCitedBy
	 * @model opposite="CitedBy"
	 * @generated
	 */
    EList<Publication> getReferences();

    /**
	 * Returns the value of the '<em><b>Read</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Read</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Read</em>' attribute.
	 * @see #setRead(boolean)
	 * @see publications.PublicationsPackage#getPublication_Read()
	 * @model
	 * @generated
	 */
    boolean isRead();

    /**
	 * Sets the value of the '{@link publications.Publication#isRead <em>Read</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Read</em>' attribute.
	 * @see #isRead()
	 * @generated
	 */
    void setRead(boolean value);

    /**
	 * Returns the value of the '<em><b>Importance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Importance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Importance</em>' attribute.
	 * @see #setImportance(byte)
	 * @see publications.PublicationsPackage#getPublication_Importance()
	 * @model
	 * @generated
	 */
    byte getImportance();

    /**
	 * Sets the value of the '{@link publications.Publication#getImportance <em>Importance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Importance</em>' attribute.
	 * @see #getImportance()
	 * @generated
	 */
    void setImportance(byte value);

    /**
	 * Returns the value of the '<em><b>Cited By</b></em>' reference list.
	 * The list contents are of type {@link publications.Publication}.
	 * It is bidirectional and its opposite is '{@link publications.Publication#getReferences <em>References</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cited By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cited By</em>' reference list.
	 * @see publications.PublicationsPackage#getPublication_CitedBy()
	 * @see publications.Publication#getReferences
	 * @model opposite="References"
	 * @generated
	 */
    EList<Publication> getCitedBy();

    /**
	 * Returns the value of the '<em><b>Contains</b></em>' reference list.
	 * The list contents are of type {@link publications.Publication}.
	 * It is bidirectional and its opposite is '{@link publications.Publication#getContainedIn <em>Contained In</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contains</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contains</em>' reference list.
	 * @see publications.PublicationsPackage#getPublication_Contains()
	 * @see publications.Publication#getContainedIn
	 * @model opposite="ContainedIn"
	 * @generated
	 */
    EList<Publication> getContains();

    /**
	 * Returns the value of the '<em><b>Contained In</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link publications.Publication#getContains <em>Contains</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained In</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained In</em>' reference.
	 * @see #setContainedIn(Publication)
	 * @see publications.PublicationsPackage#getPublication_ContainedIn()
	 * @see publications.Publication#getContains
	 * @model opposite="Contains"
	 * @generated
	 */
    Publication getContainedIn();

    /**
	 * Sets the value of the '{@link publications.Publication#getContainedIn <em>Contained In</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contained In</em>' reference.
	 * @see #getContainedIn()
	 * @generated
	 */
    void setContainedIn(Publication value);

    /**
	 * Returns the value of the '<em><b>Original Reference Text</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Reference Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Reference Text</em>' attribute.
	 * @see #setOriginalReferenceText(String)
	 * @see publications.PublicationsPackage#getPublication_OriginalReferenceText()
	 * @model default=""
	 * @generated
	 */
    String getOriginalReferenceText();

    /**
	 * Sets the value of the '{@link publications.Publication#getOriginalReferenceText <em>Original Reference Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Reference Text</em>' attribute.
	 * @see #getOriginalReferenceText()
	 * @generated
	 */
    void setOriginalReferenceText(String value);

    /**
	 * Returns the value of the '<em><b>Local Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Local Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Local Key</em>' attribute.
	 * @see #setLocalKey(String)
	 * @see publications.PublicationsPackage#getPublication_LocalKey()
	 * @model
	 * @generated
	 */
    String getLocalKey();

    /**
	 * Sets the value of the '{@link publications.Publication#getLocalKey <em>Local Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Local Key</em>' attribute.
	 * @see #getLocalKey()
	 * @generated
	 */
    void setLocalKey(String value);

    /**
	 * Returns the value of the '<em><b>Tags</b></em>' reference list.
	 * The list contents are of type {@link publications.Tag}.
	 * It is bidirectional and its opposite is '{@link publications.Tag#getPublications <em>Publications</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tags</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tags</em>' reference list.
	 * @see publications.PublicationsPackage#getPublication_Tags()
	 * @see publications.Tag#getPublications
	 * @model opposite="Publications"
	 * @generated
	 */
    EList<Tag> getTags();

    /**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see publications.PublicationsPackage#getPublication_Notes()
	 * @model
	 * @generated
	 */
    String getNotes();

    /**
	 * Sets the value of the '{@link publications.Publication#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
    void setNotes(String value);
}
