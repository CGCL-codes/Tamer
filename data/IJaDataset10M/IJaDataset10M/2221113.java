package tms.shared;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Entity that represents the Field settings of the Filter.
 * 
 * @author Wildrich Fourie
 */
public class FieldFilter implements IsSerializable {

    private boolean contains;

    private long fieldId;

    private String fieldText;

    private boolean andOr;

    private int fieldtypeid;

    private ArrayList<FieldFilter> subFilters;

    /** Empty constructor that is needed in order for this object to be serialised. */
    public FieldFilter() {
    }

    /**
	 * Creates an new field filter for a singular field.
	 * @param contains Boolean that specifies whether the record should or should not contain the field.
	 * <li> TRUE  : Contains
	 * <li> FALSE : Does not Contain
	 * @param fieldId The id of the selected field.
	 * @param fieldText The text that should be contained in the specified field of the record.
	 * @param empty Boolean that specifies whether the field of the record is empty or not.
	 * <li> TRUE  : The field should be empty
	 * <li> FALSE : The field is not empty
	 * @param andOr Boolean denotes the connector from the previous FieldFilter to this one.
	 * <li> TRUE  : AND
	 * <li> FALSE : OR
	 */
    public FieldFilter(boolean contains, long fieldId, String fieldText, boolean andOr, int fieldtypeid) {
        this.contains = contains;
        this.fieldId = fieldId;
        this.fieldText = fieldText;
        this.andOr = andOr;
        this.fieldtypeid = fieldtypeid;
    }

    /**
	 * Creates an new field filter for a singular field.
	 * @param contains Boolean that specifies whether the record should or should not contain the field.
	 * <li> TRUE  : Contains
	 * <li> FALSE : Does not Contain
	 * @param fieldId The id of the selected field.
	 * @param fieldText The text that should be contained in the specified field of the record.
	 * @param empty Boolean that specifies whether the field of the record is empty or not.
	 * <li> TRUE  : The field should be empty
	 * <li> FALSE : The field is not empty
	 * @param andOr Boolean denotes the connector from the previous FieldFilter to this one.
	 * <li> TRUE  : AND
	 * <li> FALSE : OR
	 */
    public FieldFilter(boolean contains, long fieldId, String fieldText, boolean andOr, int fieldtypeid, ArrayList<FieldFilter> subFilters) {
        this.contains = contains;
        this.fieldId = fieldId;
        this.fieldText = fieldText;
        this.andOr = andOr;
        this.fieldtypeid = fieldtypeid;
        this.subFilters = subFilters;
    }

    /**
	 * Returns the connector to the previous FieldFilter.
	 * @return
	 * <li> TRUE  : AND
	 * <li> FALSE : OR
	 */
    public boolean isAndOr() {
        return andOr;
    }

    /**
	 * <li> TRUE  : Contains
	 * <li> FALSE : Does not Contain
	 */
    public boolean isContains() {
        return contains;
    }

    /**
	 * Returns the field id of the field chosen for this filter.
	 */
    public long getFieldId() {
        return fieldId;
    }

    /**
	 * Returns the text that should be in the chosen field of a record.
	 */
    public String getFieldText() {
        return fieldText;
    }

    /** Returns the Field type ID of this Field */
    public int getFieldtypeid() {
        return fieldtypeid;
    }

    /** Returns an <code>ArrayList</code> of FieldFilters containing the sub field filters of this Field. */
    public ArrayList<FieldFilter> getSubFilters() {
        return subFilters;
    }
}
