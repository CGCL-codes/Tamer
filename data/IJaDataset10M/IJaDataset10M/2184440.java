package org.genxdm.xs.types;

import java.util.Set;
import javax.xml.namespace.QName;
import org.genxdm.xs.components.SchemaComponent;
import org.genxdm.xs.enums.DerivationMethod;

/**
 * Represents all types in a schema, both simple types and complex types.
 */
public interface Type extends SchemaComponent, SequenceType {

    boolean derivedFrom(String namespace, String name, Set<DerivationMethod> derivationMethods);

    boolean derivedFromType(Type ancestorType, Set<DerivationMethod> derivationMethods);

    /**
     * Returns the {base type definition} of this type. This may be a simple type or a complex type.
     */
    Type getBaseType();

    /**
     * Returns the {derivation method} property of this type from its base type.
     */
    DerivationMethod getDerivationMethod();

    /**
     * Returns the {final} property. Applies to both simple types and complex types. This is a design-time constraint on
     * types. For simple types, this is a subset of {list, union, restriction}. For complex types, this is a subset of
     * {extension, restriction}.
     */
    Set<DerivationMethod> getFinal();

    /**
     * The {name} property, which is in fact the local-name part of an expanded-QName.
     */
    String getLocalName();

    /**
     * The {name} and {target namespace} properties.
     */
    QName getName();

    /**
     * The {target namespace} property.
     */
    String getTargetNamespace();

    /**
     * Returns the {abstract} property of this type. <br/>
     * Determines whether object of this type can be instantiated. An abstract type can only be used to derive subtypes.
     */
    boolean isAbstract();

    boolean isAnonymous();

    /**
     * Returns whether this type is a simple type with a variety of atomic.
     */
    boolean isAtomicType();

    /**
     * Returns whether this type is the Atomic Ur-Type.
     */
    boolean isAtomicUrType();

    /**
     * Returns whether this type is the Complex Ur-Type.
     */
    boolean isComplexUrType();

    /**
     * Determines whether a particular derivation method is final.
     * 
     * @param derivation
     *            The derivation method.
     */
    boolean isFinal(DerivationMethod derivation);

    /**
     * Returns whether this type is a built-in type.
     */
    boolean isNative();

    /**
     * Returns whether this type is the Simple Ur-Type.
     */
    boolean isSimpleUrType();
}
