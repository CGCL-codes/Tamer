package imported_model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Library</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link imported_model.Library#getBooks <em>Books</em>}</li>
 * </ul>
 * </p>
 *
 * @see imported_model.Imported_modelPackage#getLibrary()
 * @model
 * @generated
 */
public interface Library extends EObject {

    /**
	 * Returns the value of the '<em><b>Books</b></em>' reference list.
	 * The list contents are of type {@link imported_model.Book}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Books</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Books</em>' reference list.
	 * @see imported_model.Imported_modelPackage#getLibrary_Books()
	 * @model
	 * @generated
	 */
    EList<Book> getBooks();
}
