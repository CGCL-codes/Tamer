package net.sourceforge.jpalm.mobiledb;

import net.sourceforge.jpalm.DataBlock;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.juint.Int8;
import net.sourceforge.juint.UInt8;

/**
 * The filter criterion in a MobileDB database. This contains information regarding how the database
 * was last filtered and does not enforce filtering.
 */
public class FilterCriterion extends DataBlock {

    /**
     * The length of this <code>DataBlock</code> in <code>byte</code>s.<br>
     * {@value}
     */
    public static final int DATA_LENGTH = 42;

    /**
     * The maximum length of the filter text.<br>
     * {@value}
     */
    public static final int FIELD_LENGTH = 40;

    /**
     * This filter doesn't apply to any fields (Disables the filter).
     */
    public static final Int8 FIELD_NONE = new Int8(0x00);

    /**
     * This filter applies to all fields.
     */
    public static final Int8 FIELD_ALL = new Int8(0x01);

    /**
     * This filter applies to a specific field--it should be added to this offset. If the field is
     * 3, then it should be set as
     * 
     * <pre>
     * setFieldNumber(new Int8(FilterCriterion.FIELD_OFFSET.int8Value() + 3));
     * </pre>
     */
    public static final Int8 FIELD_OFFSET = new Int8(0x02);

    /**
     * Filter records by matching all filter criterions. xor this with <code>flags</code> on the
     * first criterion only.
     */
    public static final UInt8 FLAG_MATCH_ALL = new UInt8(0x00);

    /**
     * Filter records by matching any filter criterions. xor this with <code>flags</code> on the
     * first criterion only.
     */
    public static final UInt8 FLAG_MATCH_ANY = new UInt8(0x80);

    /**
     * Filter "is".
     */
    public static final UInt8 FLAG_IS = new UInt8(0x00);

    /**
     * Filter "contains".
     */
    public static final UInt8 FLAG_CONTAINS = new UInt8(0x01);

    /**
     * Filter "is not".
     */
    public static final UInt8 FLAG_IS_NOT = new UInt8(0x02);

    /**
     * Filter "contains not".
     */
    public static final UInt8 FLAG_CONTAINS_NOT = new UInt8(0x03);

    private String filter;

    private Int8 fieldNumber;

    private UInt8 flags;

    /**
     * Creates a new filter criterion. Defaults to an empty filter.
     */
    public FilterCriterion() {
        filter = "";
        fieldNumber = new Int8(0);
        flags = new UInt8(0);
    }

    /**
     * Creates a new criterion from the <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     */
    public FilterCriterion(byte[] data) {
        deserialize(data);
    }

    /**
     * Gets the filter.
     * 
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Sets the filter.
     * 
     * @param filter
     *            the filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Gets the field number to filter on.
     * <p>
     * If not one of {@link #FIELD_ALL} or {@link #FIELD_NONE} the field number should be subtracted
     * from {@link #FIELD_OFFSET}.
     * 
     * @return the field number
     */
    public Int8 getFieldNumber() {
        return fieldNumber;
    }

    /**
     * Sets the field number to filter on.
     * <p>
     * If not one of {@link #FIELD_ALL} or {@link #FIELD_NONE} the field number should be added to
     * {@link #FIELD_OFFSET}.
     * 
     * @param fieldNumber
     *            the field number
     */
    public void setFieldNumber(Int8 fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    /**
     * Gets the flags.
     * 
     * @return the flags
     * @see #FLAG_CONTAINS
     * @see #FLAG_CONTAINS_NOT
     * @see #FLAG_IS
     * @see #FLAG_IS_NOT
     * @see #FLAG_MATCH_ALL
     * @see #FLAG_MATCH_ANY
     */
    public UInt8 getFlags() {
        return flags;
    }

    /**
     * Sets the flags.
     * 
     * @param flags
     *            the flags
     * @see #FLAG_CONTAINS
     * @see #FLAG_CONTAINS_NOT
     * @see #FLAG_IS
     * @see #FLAG_IS_NOT
     * @see #FLAG_MATCH_ALL
     * @see #FLAG_MATCH_ANY
     */
    public void setFlags(UInt8 flags) {
        this.flags = flags;
    }

    @Override
    public void deserialize(byte[] data) {
        String raw = new String(Utilities.subbyte(data, 0, FIELD_LENGTH));
        filter = raw.substring(0, raw.indexOf(0x00));
        int last = FIELD_LENGTH;
        fieldNumber = new Int8(data[last]);
        last++;
        flags = new UInt8(data[last]);
    }

    @Override
    public byte[] serialize() {
        byte[] bytes = new byte[DATA_LENGTH];
        int length = (filter.length() < FIELD_LENGTH) ? filter.length() : FIELD_LENGTH;
        System.arraycopy(filter.getBytes(), 0, bytes, 0, length);
        int last = FIELD_LENGTH;
        bytes[last] = fieldNumber.byteValue();
        last++;
        bytes[last] = flags.byteValue();
        return bytes;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FilterCriterion)) return false;
        return super.equals(object);
    }
}
