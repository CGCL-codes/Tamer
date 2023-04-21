package nl.alterra.openmi.sdk.backbone;

import org.openmi.standard.ICategory;
import org.openmi.standard.IQuality;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A quality can be ordinal or nominal and contains a list of categories. An
 * ordinal quality can only contain instances of OrdinalCategory, that have
 * some kind of order and are "sortable". A nominal quality can only contain
 * instances of NominalCategory.
 */
public class Quality extends DataType implements IQuality, Serializable {

    private static final long serialVersionUID = 1L;

    private int nextOrdinal = 0;

    private IQuality.Type type;

    protected ArrayList<Category> categories;

    /**
     * Creates a default nominal quality with the data type set to
     * DataType.ValueType.String and an empty list of categories.
     *
     * @param id String ID of the instance
     */
    public Quality(String id) {
        this(id, Type.Nominal);
    }

    /**
     * Creates an instance with the specified id and type. The type must be
     * one of the IQuality.Type enumeration values, currently either Nominal
     * or Ordinal. The type can only be set when the instance is created. By
     * default the ValueType of a quality is a String.
     *
     * @param id   String ID of the instance
     * @param type IQuality.Type to create
     */
    public Quality(String id, Type type) {
        super(id, DataType.ValueType.String);
        categories = new ArrayList<Category>();
        this.type = type;
    }

    /**
     * Checks if the Quality already contains a category with the specified
     * value.
     *
     * @param value to look for
     * @return True if category exists
     */
    public boolean containsCategory(String value) {
        return (retrieveCategory(value) != null);
    }

    /**
     * Checks if the Quality already contains a category with the specified
     * ordinal. If the Quality is nominal the return value will always be false.
     *
     * @param ordinal to look for
     * @return True if Quality is ordinal and ordinal value already exists
     */
    public boolean containsOrdinal(int ordinal) {
        return (retrieveCategory(ordinal) != null);
    }

    /**
     * Creates a new category with the specified value and adds it to the
     * quality. If the quality is ordinal an OrdinalCategory will
     * be created, otherwise a NominalCategory.
     *
     * @param value String value for the new category
     * @return Category created
     */
    public Category createCategory(String value) {
        if (containsCategory(value)) throw new RuntimeException(String.format("Quality '%s' already contains a category '%s'", getCaption(), value));
        Category c;
        if (type == Type.Ordinal) c = new OrdinalCategory(value, nextOrdinal++); else c = new NominalCategory(value);
        categories.add(c);
        return c;
    }

    /**
     * Creates a new ordinal category with the specified value and ordinal
     * and adds it to the quality. The quality must be ordinal and the value
     * and ordinal number not already is use.
     *
     * @param value   String value for the new ordinal category
     * @param ordinal number for the new ordinal category
     * @return Category created
     */
    public Category createCategory(String value, int ordinal) {
        if (containsCategory(value)) throw new RuntimeException(String.format("Quality '%s' already contains a category '%s'", getCaption(), value));
        if (!isOrdered()) throw new RuntimeException(String.format("Quality '%s' is not ordinal", getCaption()));
        if (containsOrdinal(ordinal)) throw new RuntimeException(String.format("Quality '%s' already contains a category for ordinal '%d'", getCaption(), ordinal));
        Category c = new OrdinalCategory(value, ordinal);
        categories.add(c);
        return c;
    }

    /**
     * Retrieves the category with the specified value from the quality.
     * Returns null if no such category exists.
     *
     * @param value to look for
     * @return ICategory found, or null
     */
    public ICategory retrieveCategory(String value) {
        for (Category c : categories) if (c.getValue().equals(value)) return c;
        return null;
    }

    /**
     * Retrieves the category with the specified ordinal from the quality.
     * Returns null if no such category exists.
     *
     * @param ordinal to look for
     * @return ICategory found, or null
     */
    public ICategory retrieveCategory(int ordinal) {
        for (Category c : categories) if (c instanceof OrdinalCategory) if (((OrdinalCategory) c).getOrdinal() == ordinal) return c;
        return null;
    }

    /**
     * Removes the category with the specified value from the quality.
     *
     * @param value of category to remove
     * @return True when such a category was found and removed
     */
    public boolean removeCategory(String value) {
        for (Category c : categories) if (c.getValue().equals(value)) {
            removeCategory(c);
            return true;
        }
        return false;
    }

    /**
     * Removes the specified category from the quality.
     *
     * @param category to be removed
     * @return True when successful
     */
    public boolean removeCategory(ICategory category) {
        return categories.remove(category);
    }

    /**
     * Finds the category with the specified oldValue and changes it to the newValue.
     *
     * @param oldValue to change from
     * @param newValue to change to
     * @return True when successful
     */
    public boolean changeCategoryValue(String oldValue, String newValue) {
        for (Category c : categories) if (c.getValue().equals(oldValue)) {
            c.setValue(newValue);
            return true;
        }
        return false;
    }

    /**
     * Finds the category with the specified value and changes its ordinal to
     * the specified newOrdinal. The quality must be ordinal, a category with
     * the specified value must exist and the newOrdinal number must not be
     * used by another category.
     *
     * @param value      String value of the ordinal category to change
     * @param newOrdinal new ordinal number for category
     * @return True when successful
     */
    public boolean changeCategoryOrdinal(String value, int newOrdinal) {
        if (!isOrdered()) throw new RuntimeException(String.format("Quality '%s' is not ordinal", getCaption()));
        if (containsOrdinal(newOrdinal)) throw new RuntimeException(String.format("Quality '%s' already contains a category for ordinal '%d'", getCaption(), newOrdinal));
        for (Category c : categories) if (c.getValue().equals(value)) {
            ((OrdinalCategory) c).setOrdinal(newOrdinal);
            return true;
        }
        return false;
    }

    /**
     * Gets the list of categories for this quality.
     *
     * @return ICategory[] with the categories of the quality
     */
    public ICategory[] getCategories() {
        return categories.toArray(new ICategory[] {});
    }

    public Type getType() {
        return type;
    }

    /**
     * Checks if the quality is ordered (sorted).
     *
     * @return True if the quality is ordered
     */
    public boolean isOrdered() {
        return (type == Type.Ordinal);
    }

    @Override
    public String toString() {
        return getID();
    }

    @Override
    public boolean describesSameAs(Object obj) {
        if (obj == this) return true;
        if (!super.describesSameAs(obj)) return false;
        Quality q = (Quality) obj;
        if (type != q.type) return false;
        if (!categories.equals(q.categories)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + type.hashCode() + getID().hashCode() + getValueType().hashCode() + getDescription().hashCode() + categories.hashCode();
    }
}
