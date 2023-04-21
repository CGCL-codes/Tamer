package org.examples.library.elements.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.examples.library.elements.*;
import org.examples.library.hr.Person;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.examples.library.elements.ElementsPackage
 * @generated
 */
public class ElementsAdapterFactory extends AdapterFactoryImpl {

    /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected static ElementsPackage modelPackage;

    /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ElementsAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = ElementsPackage.eINSTANCE;
        }
    }

    /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected ElementsSwitch modelSwitch = new ElementsSwitch() {

        public Object caseBook(Book object) {
            return createBookAdapter();
        }

        public Object caseWriter(Writer object) {
            return createWriterAdapter();
        }

        public Object casePerson(Person object) {
            return createPersonAdapter();
        }

        public Object defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
    public Adapter createAdapter(Notifier target) {
        return (Adapter) modelSwitch.doSwitch((EObject) target);
    }

    /**
   * Creates a new adapter for an object of class '{@link org.examples.library.elements.Book <em>Book</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.examples.library.elements.Book
   * @generated
   */
    public Adapter createBookAdapter() {
        return null;
    }

    /**
   * Creates a new adapter for an object of class '{@link org.examples.library.elements.Writer <em>Writer</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.examples.library.elements.Writer
   * @generated
   */
    public Adapter createWriterAdapter() {
        return null;
    }

    /**
   * Creates a new adapter for an object of class '{@link org.examples.library.hr.Person <em>Person</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.examples.library.hr.Person
   * @generated
   */
    public Adapter createPersonAdapter() {
        return null;
    }

    /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
