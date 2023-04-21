package tudresden.ocl20.pivot.pivotmodel;

import org.eclipse.emf.common.util.EList;
import java.util.List;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Namespace</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * <p>
 * A <code>Namespace</code> is a container for types and 
 * other namespaces.
 * </p>
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedType <em>Owned Type</em>}</li>
 *   <li>{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedRule <em>Owned Rule</em>}</li>
 *   <li>{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestedNamespace <em>Nested Namespace</em>}</li>
 *   <li>{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestingNamespace <em>Nesting Namespace</em>}</li>
 * </ul>
 * </p>
 *
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamespace()
 * @model
 * @generated
 */
public interface Namespace extends NamedElement, GenericElement {

    /**
	 * Returns the value of the '<em><b>Owned Rule</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint}. It is bidirectional
	 * and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getNamespace
	 * <em>Namespace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * Specifies a set of {@link Constraint}s owned by this <code>Namespace</code>
	 * .
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owned Rule</em>' containment reference list.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getNamespace
	 * @generated
	 */
    List<Constraint> getOwnedRule();

    /**
	 * Returns the value of the '<em><b>Owned Type</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type}. It is bidirectional and its
	 * opposite is '{@link tudresden.ocl20.pivot.pivotmodel.Type#getNamespace
	 * <em>Namespace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * This is the set of {@link Type types} that are contained in this namespace.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owned Type</em>' containment reference list.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getNamespace
	 * @generated
	 */
    List<Type> getOwnedType();

    /**
	 * Returns the value of the '<em><b>Nested Namespace</b></em>' containment reference list.
	 * The list contents are of type {@link tudresden.ocl20.pivot.pivotmodel.Namespace}.
	 * It is bidirectional and its opposite is '{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestingNamespace <em>Nesting Namespace</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * This is the set of namespaces contained in this namespace.
	 * </p>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Nested Namespace</em>' containment reference list.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamespace_NestedNamespace()
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getNestingNamespace
	 * @model opposite="nestingNamespace" containment="true"
	 * @generated
	 */
    List<Namespace> getNestedNamespace();

    /**
	 * Returns the value of the '<em><b>Nesting Namespace</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestedNamespace <em>Nested Namespace</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * This specifies the <code>Namespace</code> that is the 
	 * owner of this namespace.
	 * </p>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Nesting Namespace</em>' container reference.
	 * @see #setNestingNamespace(Namespace)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamespace_NestingNamespace()
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getNestedNamespace
	 * @model opposite="nestedNamespace" resolveProxies="false" transient="false"
	 * @generated
	 */
    Namespace getNestingNamespace();

    /**
	 * Sets the value of the '{@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestingNamespace <em>Nesting Namespace</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nesting Namespace</em>' container reference.
	 * @see #getNestingNamespace()
	 * @generated
	 */
    void setNestingNamespace(Namespace value);

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link Type} to this <code>Namespace</code>. This is an additional
	 * operation in the Pivot Model to support cloning namespaces when type
	 * parameters are bound. The operation returns a reference to this
	 * <code>Namespace</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
    Namespace addType(Type type);

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link Constraint} to this <code>Namespace</code>. This is an
	 * additional operation in the Pivot Model to support parsers of constraint
	 * languages with a textual syntax (e.g. OCL) that allow to specify the
	 * context of a constraint without explicitly adding the
	 * <code>Constraint</code> instance to the model.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
    Namespace addRule(Constraint rule);

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a nested <code>Namespace</code> to this <code>Namespace</code>. This
	 * is an additional operation in the Pivot Model to support cloning namespaces
	 * when type parameters are bound. The operation returns a reference to this
	 * <code>Namespace</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
    Namespace addNestedNamespace(Namespace namespace);

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns all {@link Constraint}s that are owned by this or any nested
	 * {@link Namespace}.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
    List<Constraint> getOwnedAndNestedRules();

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns a {@link Type} in this <code>Namespace</code> with the given name.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Namespace
	 * def: lookupType(name : String) : Type =
	 *    self.ownedType->any(t | t.name = name)
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
    Type lookupType(String name);

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns a {@link Namespace} in this <code>Namespace</code> with the given
	 * name.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Namespace
	 * def: lookupNamespace(name : String) : Namespace =
	 *    self.nestedNamespace->any(ns | ns.name = name)
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
    Namespace lookupNamespace(String name);

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Removes all {@link Constraint}s that are owned by this or any nested
	 * {@link Namespace}.
	 * </p>
	 * 
	 * @return <code>true</code> if the {@link Constraint}s have been removed.
	 * <!-- end-model-doc -->
	 * @generated
	 */
    boolean removeOwnedAndNestedRules();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * Removes all {@link Constraint}s that are owned by this or any nested {@link Namespace} and contained in the given {@link Collection}.
	 * </p>
	 * 
	 * @param constraints The {@link Constraint}s that shall be removed.
	 * @return <code>true</code> if the {@link Constraint}s have been removed.
	 * <!-- end-model-doc -->
	 * @model dataType="tudresden.ocl20.pivot.datatypes.Boolean" required="true" constraintsMany="true"
	 * @generated
	 */
    boolean removeOwnedAndNestedRules(List<Constraint> constraints);

    /**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
    Namespace clone();
}
