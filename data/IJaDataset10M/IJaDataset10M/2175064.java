package org.genxdm.typed.variant;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A bridge used for converting between (N,A) and the uniform (X) representation.
 * <p>
 * The variant representation is useful when specifying interfaces that can take any value from the XQuery Data Model.
 * </p>
 * 
 * @param <N>
 *            The node handle parameter.
 * @param <A>
 *            The atom handle parameter.
 */
public interface VariantBridge<N, A> {

    /**
     * Converts an atomic value to a variant value (X).
     */
    XmlVariant atom(A atom);

    /**
     * Converts a list of atomic values to a variant value (X).
     */
    XmlVariant atomSet(Iterable<? extends A> atoms);

    /**
     * Converts the Java primitive for xs:boolean to a variant value (X).
     */
    XmlVariant booleanValue(Boolean booval);

    /**
     * Converts the Java primitive for xs:decimal to a variant value (X).
     */
    XmlVariant decimalValue(BigDecimal decval);

    /**
     * Converts the Java primitive for xs:double to a variant value (X).
     */
    XmlVariant doubleValue(Double dblval);

    /**
     * Returns the variant value representation for the empty-sequence.
     */
    XmlVariant empty();

    /**
     * Converts a variant value (X) known to be {@link VariantKind#ATOM} to an atom.
     */
    A getAtom(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#ATOMS} to a list of atoms.
     */
    Iterable<? extends A> getAtomSet(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#BOOLEAN} to a {@link Boolean}.
     */
    Boolean getBoolean(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#DECIMAL} to a {@link BigDecimal}.
     */
    BigDecimal getDecimal(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#DOUBLE} to a {@link Double}.
     */
    Double getDouble(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#INTEGER} to a {@link BigInteger}.
     */
    BigInteger getInteger(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#ITEM} to an item handle.
     */
    Item<N, A> getItem(XmlVariant value);

    /**
     * Converts a variant value (X) known to be {@link VariantKind#ITEMS} to a list of item handles.
     */
    ItemIterable<N, A> getItemSet(XmlVariant value);

    /**
     * Returns an enumeration representing the nature of a variant value allowing it to be correctly converted back to (I,N,A) representation.
     */
    VariantKind getNature(XmlVariant value);

    /**
     * Converts a variant value (X) know to be {@link VariantKind#NODE} to a node handle.
     */
    N getNode(XmlVariant value);

    /**
     * Converts a variant value (X) know to be {@link VariantKind#NODES} to a list of node handles.
     */
    Iterable<N> getNodeSet(XmlVariant value);

    /**
     * Converts a variant value (X) know to be {@link VariantKind#STRING} to a {@link String}.
     */
    String getString(XmlVariant value);

    /**
     * Converts the Java object for xs:integer to a variant value (X).
     * <p>
     * A <code>null</code> {@link BigInteger} value will be converted to an empty sequence.
     * <p>
     */
    XmlVariant integerValue(BigInteger intval);

    /**
     * Converts an item handle into a variant value (X).
     */
    XmlVariant item(Item<N, A> item);

    /**
     * Converts a list of item handles to a variant value (X).
     */
    XmlVariant itemSet(ItemIterable<N, A> items);

    /**
     * Converts a node handle into a variant value (X).
     */
    XmlVariant node(N node);

    /**
     * Converts a list of node handles to a variant value (X).
     */
    XmlVariant nodeSet(Iterable<? extends N> nodes);

    /**
     * Converts the Java object for xs:string to a variant value (X).
     * <p>
     * A <code>null</code> {@link String} value will be converted to an empty sequence.
     * <p>
     */
    XmlVariant stringValue(String strval);

    /**
     * Allocates an empty array of variant values (X).
     * 
     * @param size
     *            The size of the array of values.
     */
    XmlVariant[] valueArray(int size);
}
