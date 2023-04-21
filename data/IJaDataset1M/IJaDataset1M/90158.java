package edu.vt.middleware.ldap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Simple bean representing an ldap attribute. Contains a name and a set of
 * values.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public class LdapAttribute extends AbstractLdapBean {

    /** hash code seed. */
    private static final int HASH_CODE_SEED = 313;

    /** Name for this attribute. */
    private String attributeName;

    /** Values for this attribute. */
    private final LdapAttributeValues<?> attributeValues;

    /** Default constructor. */
    public LdapAttribute() {
        this(SortBehavior.getDefaultSortBehavior(), false);
    }

    /**
   * Creates a new ldap attribute.
   *
   * @param  sb  sort behavior of this attribute
   */
    public LdapAttribute(final SortBehavior sb) {
        this(sb, false);
    }

    /**
   * Creates a new ldap attribute.
   *
   * @param  binary  whether this attribute contains binary values
   */
    public LdapAttribute(final boolean binary) {
        this(SortBehavior.getDefaultSortBehavior(), binary);
    }

    /**
   * Creates a new ldap attribute.
   *
   * @param  sb  sort behavior of this attribute
   * @param  binary  whether this attribute contains binary values
   */
    public LdapAttribute(final SortBehavior sb, final boolean binary) {
        super(sb);
        if (binary) {
            attributeValues = new LdapAttributeValues<byte[]>(byte[].class);
        } else {
            attributeValues = new LdapAttributeValues<String>(String.class);
        }
    }

    /**
   * Creates a new ldap attribute.
   *
   * @param  name  of this attribute
   */
    public LdapAttribute(final String name) {
        this();
        setName(name);
    }

    /**
   * Creates a new ldap attribute.
   *
   * @param  name  of this attribute
   * @param  values  of this attribute
   */
    public LdapAttribute(final String name, final String... values) {
        this(false);
        setName(name);
        for (String value : values) {
            addStringValue(value);
        }
    }

    /**
   * Creates a new ldap attribute.
   *
   * @param  name  of this attribute
   * @param  values  of this attribute
   */
    public LdapAttribute(final String name, final byte[]... values) {
        this(true);
        setName(name);
        for (byte[] value : values) {
            addBinaryValue(value);
        }
    }

    /**
   * Returns the name of this attribute.
   *
   * @return  attribute name
   */
    public String getName() {
        return attributeName;
    }

    /**
   * Sets the name of this attribute.
   *
   * @param  name  to set
   */
    public void setName(final String name) {
        attributeName = name;
    }

    /**
   * Returns the values of this attribute as strings. Binary data is base64
   * encoded. The return collection cannot be modified.
   *
   * @return  set of string attribute values
   */
    public Set<String> getStringValues() {
        return attributeValues.getStringValues();
    }

    /**
   * Returns a single string value of this attribute. See {@link
   * #getStringValues()}.
   *
   * @return  single string attribute value
   */
    public String getStringValue() {
        final Set<String> s = getStringValues();
        if (s.size() == 0) {
            return null;
        }
        return s.iterator().next();
    }

    /**
   * Returns the values of this attribute as byte arrays. String data is UTF-8
   * encoded. The return collection cannot be modified.
   *
   * @return  set of byte array attribute values
   */
    public Set<byte[]> getBinaryValues() {
        return attributeValues.getBinaryValues();
    }

    /**
   * Returns a single byte array value of this attribute. See {@link
   * #getBinaryValues()}.
   *
   * @return  single byte array attribute value
   */
    public byte[] getBinaryValue() {
        final Set<byte[]> s = getBinaryValues();
        if (s.size() == 0) {
            return null;
        }
        return s.iterator().next();
    }

    /**
   * Returns whether this ldap attribute contains a value of type byte[].
   *
   * @return  whether this ldap attribute contains a value of type byte[]
   */
    public boolean isBinary() {
        return attributeValues.isType(byte[].class);
    }

    /**
   * Adds the supplied string as a value for this attribute.
   *
   * @param  value  to add
   *
   * @throws  NullPointerException  if value is null
   */
    public void addStringValue(final String... value) {
        for (String s : value) {
            attributeValues.add(s);
        }
    }

    /**
   * Adds all the strings in the supplied collection as values for this
   * attribute. See {@link #addStringValue(String...)}.
   *
   * @param  values  to add
   */
    public void addStringValues(final Collection<String> values) {
        for (String value : values) {
            addStringValue(value);
        }
    }

    /**
   * Adds the supplied byte array as a value for this attribute.
   *
   * @param  value  to add
   *
   * @throws  NullPointerException  if value is null
   */
    public void addBinaryValue(final byte[]... value) {
        for (byte[] b : value) {
            attributeValues.add(b);
        }
    }

    /**
   * Adds all the byte arrays in the supplied collection as values for this
   * attribute. See {@link #addBinaryValue(byte[][])}.
   *
   * @param  values  to add
   */
    public void addBinaryValues(final Collection<byte[]> values) {
        for (byte[] value : values) {
            addBinaryValue(value);
        }
    }

    /**
   * Removes the supplied value from the attribute values if it exists.
   *
   * @param  value  to remove
   */
    public void removeStringValue(final String... value) {
        for (String s : value) {
            attributeValues.remove(s);
        }
    }

    /**
   * Removes the supplied values from the attribute values if they exists. See
   * {@link #removeStringValue(String...)}.
   *
   * @param  values  to remove
   */
    public void removeStringValues(final Collection<String> values) {
        for (String value : values) {
            removeStringValue(value);
        }
    }

    /**
   * Removes the supplied value from the attribute values if it exists.
   *
   * @param  value  to remove
   */
    public void removeBinaryValue(final byte[]... value) {
        for (byte[] b : value) {
            attributeValues.remove(b);
        }
    }

    /**
   * Removes the supplied values from the attribute values if they exists. See
   * {@link #removeBinaryValue(byte[][])}.
   *
   * @param  values  to remove
   */
    public void removeBinaryValues(final Collection<byte[]> values) {
        for (byte[] value : values) {
            removeBinaryValue(value);
        }
    }

    /**
   * Returns the number of values in this ldap attribute.
   *
   * @return  number of values in this ldap attribute
   */
    public int size() {
        return attributeValues.size();
    }

    /** Removes all the values in this ldap attribute. */
    public void clear() {
        attributeValues.clear();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return LdapUtil.computeHashCode(HASH_CODE_SEED, attributeName != null ? attributeName.toLowerCase() : null, attributeValues);
    }

    /**
   * Provides a descriptive string representation of this instance.
   *
   * @return  string representation
   */
    @Override
    public String toString() {
        return String.format("[%s%s]", attributeName, attributeValues);
    }

    /**
   * Creates a new ldap attribute. The collection of values is inspected for
   * either String or byte[] and the appropriate attribute is created.
   *
   * @param  sb  sort behavior
   * @param  name  of this attribute
   * @param  values  of this attribute
   *
   * @return  ldap attribute
   *
   * @throws  IllegalArgumentException  if values contains something other than
   * String or byte[]
   */
    public static LdapAttribute createLdapAttribute(final SortBehavior sb, final String name, final Collection<Object> values) {
        final Set<String> stringValues = new HashSet<String>();
        final Set<byte[]> binaryValues = new HashSet<byte[]>();
        for (Object value : values) {
            if (value instanceof byte[]) {
                binaryValues.add((byte[]) value);
            } else if (value instanceof String) {
                stringValues.add((String) value);
            } else {
                throw new IllegalArgumentException("Values must contain either String or byte[]");
            }
        }
        LdapAttribute la = null;
        if (!binaryValues.isEmpty()) {
            la = new LdapAttribute(sb, true);
            la.setName(name);
            la.addBinaryValues(binaryValues);
        } else {
            la = new LdapAttribute(sb, false);
            la.setName(name);
            la.addStringValues(stringValues);
        }
        return la;
    }

    /**
   * Simple bean for ldap attribute values.
   *
   * @author  Middleware Services
   * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
   */
    private class LdapAttributeValues<T> {

        /** hash code seed. */
        private static final int HASH_CODE_SEED = 317;

        /** Type of values. */
        private final Class<T> type;

        /** Set of values. */
        private final Set<T> values;

        /**
     * Creates a new ldap attribute values.
     *
     * @param  t  type of values
     *
     * @throws  IllegalArgumentException  if t is not a String or byte[]
     */
        public LdapAttributeValues(final Class<T> t) {
            if (!(t.isAssignableFrom(String.class) || t.isAssignableFrom(byte[].class))) {
                throw new IllegalArgumentException("Only String and byte[] values are supported");
            }
            type = t;
            values = createSortBehaviorSet(type);
        }

        /**
     * Returns whether this ldap attribute values is of the supplied type.
     *
     * @param  c  type to check
     *
     * @return  whether this ldap attribute values is of the supplied type
     */
        public boolean isType(final Class<?> c) {
            return type.isAssignableFrom(c);
        }

        /**
     * Returns the values in string format. If the type of this values is
     * String, values are returned as is. If the type of this values is byte[],
     * values are base64 encoded. See {@link #convertValuesToString(Set)}.
     *
     * @return  unmodifiable set
     */
        @SuppressWarnings("unchecked")
        public Set<String> getStringValues() {
            if (isType(String.class)) {
                return Collections.unmodifiableSet((Set<String>) values);
            }
            return Collections.unmodifiableSet(convertValuesToString((Set<byte[]>) values));
        }

        /**
     * Returns the values in binary format. If the type of this values is
     * byte[], values are returned as is. If the type of this values is String,
     * values are UTF-8 encoded. See {@link #convertValuesToByteArray(Set)}.
     *
     * @return  unmodifiable set
     */
        @SuppressWarnings("unchecked")
        public Set<byte[]> getBinaryValues() {
            if (isType(byte[].class)) {
                return Collections.unmodifiableSet((Set<byte[]>) values);
            }
            return Collections.unmodifiableSet(convertValuesToByteArray((Set<String>) values));
        }

        /**
     * Adds the supplied object to this values.
     *
     * @param  o  to add
     *
     * @throws  IllegalArgumentException  if o is null or if o is not the
     * correct type
     */
        public void add(final Object o) {
            checkValue(o);
            values.add(type.cast(o));
        }

        /**
     * Removes the supplied object from this values if it exists.
     *
     * @param  o  to remove
     *
     * @throws  IllegalArgumentException  if o is null or if o is not the
     * correct type
     */
        public void remove(final Object o) {
            checkValue(o);
            values.remove(type.cast(o));
        }

        /**
     * Determines if the supplied object is acceptable to use in this values.
     *
     * @param  o  object to check
     *
     * @throws  IllegalArgumentException  if o is null or if o is not the
     * correct type
     */
        private void checkValue(final Object o) {
            if (o == null) {
                throw new IllegalArgumentException("Value cannot be null");
            }
            if (!isType(o.getClass())) {
                throw new IllegalArgumentException(String.format("Attribute %s does not support values of type %s", attributeName, type));
            }
        }

        /**
     * Returns the number of values.
     *
     * @return  number of values
     */
        public int size() {
            return values.size();
        }

        /** Removes all the values. */
        public void clear() {
            values.clear();
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return LdapUtil.computeHashCode(HASH_CODE_SEED, values);
        }

        /**
     * Provides a descriptive string representation of this instance.
     *
     * @return  string representation
     */
        @Override
        public String toString() {
            return values.toString();
        }

        /**
     * Base64 encodes the supplied set of values.
     *
     * @param  v  values to encode
     *
     * @return  set of string values
     */
        protected Set<String> convertValuesToString(final Set<byte[]> v) {
            final Set<String> s = createSortBehaviorSet(String.class);
            for (byte[] value : v) {
                s.add(LdapUtil.base64Encode(value));
            }
            return s;
        }

        /**
     * UTF-8 encodes the supplied set of values.
     *
     * @param  v  values to encode
     *
     * @return  set of byte array values
     */
        protected Set<byte[]> convertValuesToByteArray(final Set<String> v) {
            final Set<byte[]> s = createSortBehaviorSet(byte[].class);
            for (String value : v) {
                s.add(LdapUtil.utf8Encode(value));
            }
            return s;
        }

        /**
     * Returns an implementation of set for the sort behavior of this bean.
     *
     * @param  <E>  type contained in the set
     * @param  c  type of set to create
     *
     * @return  set
     */
        private <E> Set<E> createSortBehaviorSet(final Class<E> c) {
            Set<E> s = null;
            if (SortBehavior.UNORDERED == LdapAttribute.this.getSortBehavior()) {
                s = new HashSet<E>();
            } else if (SortBehavior.ORDERED == LdapAttribute.this.getSortBehavior()) {
                s = new LinkedHashSet<E>();
            } else if (SortBehavior.SORTED == LdapAttribute.this.getSortBehavior()) {
                s = new TreeSet<E>();
            }
            return s;
        }
    }
}
