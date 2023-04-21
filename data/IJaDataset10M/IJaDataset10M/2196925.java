package tudresden.ocl20.pivot.essentialocl.standardlibrary.factory;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclAny;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBag;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBoolean;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclCollection;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclEnumLiteral;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclInteger;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclModelInstanceObject;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclOrderedSet;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclReal;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclSequence;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclSet;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclString;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclTuple;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclType;
import tudresden.ocl20.pivot.essentialocl.types.CollectionType;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceBoolean;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceCollection;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceEnumerationLiteral;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceInteger;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceReal;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceString;
import tudresden.ocl20.pivot.modelinstancetype.types.base.BasisJavaModelInstanceFactory;
import tudresden.ocl20.pivot.modelinstancetype.types.base.JavaModelInstanceBoolean;
import tudresden.ocl20.pivot.modelinstancetype.types.base.JavaModelInstanceCollection;
import tudresden.ocl20.pivot.modelinstancetype.types.base.JavaModelInstanceInteger;
import tudresden.ocl20.pivot.modelinstancetype.types.base.JavaModelInstanceReal;
import tudresden.ocl20.pivot.modelinstancetype.types.base.JavaModelInstanceString;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * The {@link IStandardLibraryFactory} provides methods to create {@link OclAny}
 * objects required during OCL interpretation.
 * </p>
 * 
 * <p>
 * All {@link OclAny} types - except {@link OclModelInstanceObject} - can be
 * created inside an OCL expression and therefore offer two versions of factory
 * methods:
 * <ul>
 * <li>The first one takes an {@link IModelInstanceElement} as argument and
 * creates the {@link OclAny} wrapper for it. This is the standard way for
 * elements that are return values of invoked library or model operation calls,
 * i.e. elements that are already adapted, since they are part of the
 * {@link IModelInstance}.</li>
 * <li>The second version takes a special subtype of {@link Object} as argument
 * and creates the {@link OclAny} wrapper for the standard java implementation (
 * {@link BasisJavaModelInstanceFactory}) of {@link IModelInstanceElement}s.
 * This is used for {@link OclAny} objects that are created inside OCL
 * expressions, e.g. <br>
 * <code>context Person<br>
 * Set{self.getName(), self.getFirstName()}->includes("aba")</code><br>
 * where the <code>Set</code> is created by the standard library without
 * accessing the model and therefore no {@link IModelInstanceCollection} exists
 * for that element.</li>
 * </ul>
 * </p>
 * 
 * @author Claas Wilke
 * @author Michael Thiele
 */
public interface IStandardLibraryFactory {

    /**
	 * <p>
	 * Convenience method to create subtypes of {@link OclAny}.
	 * </p>
	 * 
	 * @param modelInstanceElement
	 *            the {@link IModelInstanceElement} that shall be adapted to an
	 *            subtype of {@link OclAny}.
	 * @return the created specific {@link OclAny}.
	 */
    OclAny createOclAny(final IModelInstanceElement modelInstanceElement);

    /**
	 * <p>
	 * Creates a new {@link OclBag} for a given {@link IModelInstanceCollection}.
	 * 
	 * @param <T>
	 *            the generic Type of the created {@link OclBag}.
	 * @param elements
	 *            the {@link IModelInstanceCollection} to be wrapped
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclBag}.
	 */
    <T extends OclAny> OclBag<T> createOclBag(final IModelInstanceCollection<IModelInstanceElement> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclBag} for a given {@link Collection}.
	 * 
	 * @param <T>
	 *            The generic Type of the created {@link OclBag}.
	 * @param elements
	 *            the {@link Collection} to be wrapped by the
	 *            {@link JavaModelInstanceCollection} that is created by this
	 *            method and is wrapped by the {@link OclBag} that is created as
	 *            the return value.
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclBag}.
	 */
    <T extends OclAny> OclBag<T> createOclBag(final List<?> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclBoolean} instance for a given
	 * {@link IModelInstanceBoolean}.
	 * </p>
	 * 
	 * @param value
	 *            the {@link IModelInstanceBoolean} to be wrapped
	 * @return the created {@link OclBoolean}.
	 */
    OclBoolean createOclBoolean(final IModelInstanceBoolean value);

    /**
	 * <p>
	 * Creates a new {@link OclBoolean} instance for a given boolean value.
	 * </p>
	 * 
	 * @param value
	 *            the boolean value to be wrapped by the
	 *            {@link JavaModelInstanceBoolean} that is created by this
	 *            method and is wrapped by the {@link OclBoolean} that is
	 *            created as the return value
	 * @return the created {@link OclBoolean}.
	 */
    OclBoolean createOclBoolean(final Boolean value);

    /**
	 * Creates an empty {@link OclCollection} of the given
	 * {@link CollectionType}.
	 * 
	 * @param elements
	 *            the elements to adapt
	 * @param collectionType
	 *            the type of collection to create (Bag, OrderedSet, Sequence,
	 *            Set).
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return an empty {@link OclCollection} of the given
	 *         {@link CollectionType}
	 */
    OclCollection<OclAny> createOclCollection(Collection<?> elements, CollectionType collectionType, Type genericType);

    /**
	 * Creates an {@link OclCollection} for the given
	 * {@link IModelInstanceCollection}.
	 * 
	 * @param imiCollection
	 *            the {@link IModelInstanceCollection} to wrap
	 * @param genericType
	 *            the generic {@link Type} of the {@link OclCollection}
	 * @return an {@link OclCollection} for the given
	 *         {@link IModelInstanceCollection}
	 */
    OclCollection<OclAny> createOclCollection(IModelInstanceCollection<IModelInstanceElement> imiCollection, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclEnumLiteral} instance for a given
	 * {@link IModelInstanceEnumerationLiteral}.
	 * </p>
	 * 
	 * @param value
	 *            the {@link IModelInstanceEnumerationLiteral} to be wrapped
	 * @return the created {@link OclEnumLiteral}.
	 */
    OclEnumLiteral createOclEnumLiteral(final IModelInstanceEnumerationLiteral value);

    /**
	 * <p>
	 * Creates a new {@link OclEnumLiteral} instance for a given enumeration
	 * literal value.
	 * </p>
	 * 
	 * @param value
	 *            the enumeration literal value to be wrapped by the
	 *            {@link JavaModelInstanceEnumerationLiteral} that is created by
	 *            this method and is wrapped by the {@link OclEnumLiteral} that
	 *            is created as the return value
	 * @return the created {@link OclEnumLiteral}.
	 */
    OclEnumLiteral createOclEnumLiteral(final EnumerationLiteral value);

    /**
	 * <p>
	 * Creates a new {@link OclInteger} instance for a given
	 * {@link IModelInstanceInteger}.
	 * </p>
	 * 
	 * @param value
	 *            the {@link IModelInstanceInteger} to be wrapped
	 * @return the created {@link OclInteger}.
	 */
    OclInteger createOclInteger(final IModelInstanceInteger value);

    /**
	 * <p>
	 * Creates a new {@link OclInteger} instance for a given {@link Long}.
	 * </p>
	 * 
	 * @param value
	 *            the integer value to be wrapped by the
	 *            {@link JavaModelInstanceInteger} that is created by this
	 *            method and is wrapped by the {@link OclInteger} that is
	 *            created as the return value
	 * @return the created {@link OclInteger}.
	 */
    OclInteger createOclInteger(final Long value);

    /**
	 * <p>
	 * Creates a new {@link OclObject} for a given {@link IModelInstanceObject}.
	 * </p>
	 * 
	 * @param modelInstanceObject
	 *            the {@link IModelInstanceObject} that shall be wrapped.
	 * @return the created {@link OclObject}.
	 */
    OclModelInstanceObject createOclModelInstanceObject(final IModelInstanceObject modelInstanceObject);

    /**
	 * <p>
	 * Creates a new {@link OclOrderedSet} for a given
	 * {@link IModelInstanceCollection}.
	 * 
	 * @param <T>
	 *            the generic Type of the created {@link OclOrderedSet}.
	 * @param elements
	 *            the {@link IModelInstanceCollection} to be wrapped
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclOrderedSet}.
	 */
    <T extends OclAny> OclOrderedSet<T> createOclOrderedSet(final IModelInstanceCollection<IModelInstanceElement> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclOrderedSet} for a given {@link List}.
	 * 
	 * @param <T>
	 *            The generic Type of the created {@link OclOrderedSet}.
	 * @param elements
	 *            the {@link List} to be wrapped by the
	 *            {@link JavaModelInstanceCollection} that is created by this
	 *            method and is wrapped by the {@link OclOrderedSet} that is
	 *            created as the return value.
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclOrderedSet}.
	 */
    <T extends OclAny> OclOrderedSet<T> createOclOrderedSet(final List<?> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclReal} instance for a given
	 * {@link IModelInstanceReal}.
	 * </p>
	 * 
	 * @param value
	 *            the {@link IModelInstanceReal} to be wrapped
	 * @return the created {@link OclReal}.
	 */
    OclReal createOclReal(final IModelInstanceReal value);

    /**
	 * <p>
	 * Creates a new {@link OclReal} instance for a given {@link Number}.
	 * </p>
	 * 
	 * @param value
	 *            the real value to be wrapped by the
	 *            {@link JavaModelInstanceReal} that is created by this method
	 *            and is wrapped by the {@link OclReal} that is created as the
	 *            return value
	 * @return the created {@link OclReal}.
	 */
    OclReal createOclReal(final Number value);

    /**
	 * <p>
	 * Creates a new {@link OclSequence} for a given
	 * {@link IModelInstanceCollection}.
	 * 
	 * @param <T>
	 *            the generic Type of the created {@link OclSequence}.
	 * @param elements
	 *            the {@link IModelInstanceCollection} to be wrapped
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclSequence}.
	 */
    <T extends OclAny> OclSequence<T> createOclSequence(final IModelInstanceCollection<IModelInstanceElement> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclSequence} for a given {@link List}.
	 * 
	 * @param <T>
	 *            The generic Type of the created {@link OclSequence}.
	 * @param elements
	 *            the {@link List} to be wrapped by the
	 *            {@link JavaModelInstanceCollection} that is created by this
	 *            method and is wrapped by the {@link OclSequence} that is
	 *            created as the return value.
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclSequence}.
	 */
    <T extends OclAny> OclSequence<T> createOclSequence(final List<?> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclSet} for a given {@link IModelInstanceCollection}.
	 * 
	 * @param <T>
	 *            the generic Type of the created {@link OclSet}.
	 * @param elements
	 *            the {@link IModelInstanceCollection} to be wrapped
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclSet}.
	 */
    <T extends OclAny> OclSet<T> createOclSet(final IModelInstanceCollection<IModelInstanceElement> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclSet} for a given {@link Set}.
	 * 
	 * @param <T>
	 *            The generic Type of the created {@link OclSet}.
	 * @param elements
	 *            the {@link List} to be wrapped by the
	 *            {@link JavaModelInstanceCollection} that is created by this
	 *            method and is wrapped by the {@link OclSet} that is created as
	 *            the return value.
	 * @param genericType
	 *            the generic {@link Type} for this collection, e.g., AnyType,
	 *            UML2Class, etc.
	 * @return the created {@link OclSet}.
	 */
    <T extends OclAny> OclSet<T> createOclSet(final Set<?> elements, Type genericType);

    /**
	 * <p>
	 * Creates a new {@link OclString} instance for a given
	 * {@link IModelInstanceString}.
	 * </p>
	 * 
	 * @param value
	 *            the {@link IModelInstanceString} to be wrapped
	 * @return the created {@link OclString}.
	 */
    OclString createOclString(final IModelInstanceString value);

    /**
	 * <p>
	 * Creates a new {@link OclString} instance for a given {@link String}.
	 * </p>
	 * 
	 * @param value
	 *            the string value to be wrapped by the
	 *            {@link JavaModelInstanceString} that is created by this method
	 *            and is wrapped by the {@link OclString} that is created as the
	 *            return value
	 * @return the created {@link OclString}.
	 */
    OclString createOclString(final String value);

    /**
	 * <p>
	 * Creates a new OclTuple for a given {@link List} of
	 * {@link IModelInstanceString}s as the elements' names and a given
	 * {@link List} of {@link IModelInstanceElement}s as the element's values.
	 * </p>
	 * 
	 * <p>
	 * <strong>Note:</strong> Because of type erasure, use
	 * {@link #createOclTupleObject(List, List)
	 * createOclTuple<strong>Object</strong>(List, List)} for tuples that are
	 * created inside OCL expressions.
	 * </p>
	 * 
	 * @param names
	 *            the names of the elements as an {@link List} of
	 *            {@link IModelInstanceString}s.
	 * @param values
	 *            the values of the elements as an {@link List} of
	 *            {@link IModelInstanceElement}s.
	 * @param type
	 *            The {@link Type} of the {@link OclTuple}.
	 * @return the created {@link OclTuple}.
	 */
    OclTuple createOclTuple(final List<IModelInstanceString> names, final List<IModelInstanceElement> values, Type type);

    /**
	 * <p>
	 * Creates a new OclTuple for a given {@link List} of {@link String}s as the
	 * elements' names and a given {@link List} of {@link Object}s as the
	 * element's values.
	 * </p>
	 * 
	 * <p>
	 * <strong>Note:</strong> Because of type erasure, use
	 * {@link #createOclTupleObject(List, List)
	 * createOclTuple<strong>Object</strong>(List, List)} for tuples that are
	 * created inside OCL expressions.
	 * </p>
	 * 
	 * @param names
	 *            the names of the elements as an {@link List} of {@link String}
	 *            s.
	 * @param values
	 *            the values of the elements as an {@link List} of
	 *            {@link Object}s.
	 * @param type
	 *            The {@link Type} of the {@link OclTuple}.
	 * @return the created {@link OclTuple}.
	 */
    OclTuple createOclTupleObject(final List<String> names, final List<Object> values, Type type);

    /**
	 * <p>
	 * Creates or returns an {@link OclType} for a given {@link Type} and
	 * {@link OclAny}.
	 * </p>
	 * 
	 * @param type
	 *            The {@link Type} this {@link OclType} should wrap.
	 * 
	 * @return The created {@link OclType}.
	 */
    public <T extends OclAny> OclType<T> createOclType(final Type type);

    /**
	 * <p>
	 * Creates an undefined instance of an {@link OclAny} of the given
	 * {@link Type}.
	 * </p>
	 * 
	 * @param type
	 *            The {@link Type} of the undefined {@link OclAny} that shall be
	 *            created.
	 * @param reason
	 *            The reason why this {@link OclAny} is undefined.
	 * @return The created undefined {@link OclAny}.
	 */
    public <T extends OclAny> T createOclUndefined(final Type type, final String reason);

    /**
	 * <p>
	 * Creates an invalid instance of an {@link OclAny} of the given
	 * {@link Type}.
	 * </p>
	 * 
	 * @param type
	 *            The {@link Type} of the undefined {@link OclAny} that shall be
	 *            created.
	 * @param cause
	 *            The {@link Throwable} that caused this to be invalid
	 * @return The created invalid {@link OclAny}.
	 */
    <T extends OclAny> T createOclInvalid(final Type type, final Throwable cause);
}
