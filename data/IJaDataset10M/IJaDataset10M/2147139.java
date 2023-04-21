package library.impl;

import java.util.Collection;
import library.Book;
import library.Books;
import library.LibraryPackage;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Books</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link library.impl.BooksImpl#getBooks <em>Books</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class BooksImpl extends EObjectImpl implements Books {

    /**
	 * The cached value of the '{@link #getBooks() <em>Books</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBooks()
	 * @generated
	 * @ordered
	 */
    protected EList<Book> books;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected BooksImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected EClass eStaticClass() {
        return LibraryPackage.Literals.BOOKS;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EList<Book> getBooks() {
        if (books == null) {
            books = new EObjectContainmentEList<Book>(Book.class, this, LibraryPackage.BOOKS__BOOKS);
        }
        return books;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case LibraryPackage.BOOKS__BOOKS:
                return ((InternalEList<?>) getBooks()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case LibraryPackage.BOOKS__BOOKS:
                return getBooks();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case LibraryPackage.BOOKS__BOOKS:
                getBooks().clear();
                getBooks().addAll((Collection<? extends Book>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case LibraryPackage.BOOKS__BOOKS:
                getBooks().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case LibraryPackage.BOOKS__BOOKS:
                return books != null && !books.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
