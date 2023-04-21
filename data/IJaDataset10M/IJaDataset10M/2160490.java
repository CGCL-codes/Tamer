package md.formator.sql;

import md.formator.IFormatable;
import md.formator.sql.factory.FieldFactory;

/**
 * This class represents the parent of all fields.
 * It only needs a factory to format the field
 * @author Bruno da Silva
 * @date october 2008
 *
 */
public abstract class AbstractField implements IFormatable {

    /** The factory to format the field */
    private FieldFactory ff;

    /**
	 * Initialize a field with the given factory.
	 * @param ff The factory to format the field
	 */
    public AbstractField(FieldFactory ff) {
        this.ff = ff;
    }

    public String toString() {
        return (String) getFormator(ff).format(this);
    }
}
