package enzimaweb.ui.input;

import enzimaweb.WebListable;
import enzimaweb.WebSerializer;

/**
 * Web drop-down option input capable of rendering itself.
 * 
 * @author Edmundo Andrade
 */
public class DropDownOptionInput extends BaseOptionInput {

    private String label;

    /**
     * Constructs an web drop-down option input instance without a label, iterable or value.
     */
    public DropDownOptionInput(String id, boolean required) {
        this(id, null, null, null, required);
    }

    /**
     * Constructs an web drop-down option input instance with the specified id, label, iterable,
     * value and required.
     * 
     * @param id id
     * @param label label
     * @param iterable iterable
     * @param value value
     * @param required required
     */
    public DropDownOptionInput(String id, String label, Iterable<? extends WebListable> iterable, Object value, boolean required) {
        super(id, iterable, value, required);
        setLabel(label);
    }

    /**
     * Returns this option input's label.
     * 
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Defines this option input's label.
     * 
     * @param label new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public void serialize(WebSerializer serializer) {
        serializer.printDropDownOptionInput(getLabel(), getValue(), getIterable(), getInfo());
    }

    public String toString() {
        String result = getLabel();
        if (result == null) {
            result = super.toString();
        }
        return result;
    }
}
