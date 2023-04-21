package gnu.inet.ldap;

import java.util.Set;

/**
 * An attribute type and set of values to associate with it.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class AttributeValues {

    /**
   * The attribute type.
   */
    protected final String type;

    /**
   * The values to assign.
   */
    protected final Set values;

    /**
   * Constructor.
   * @param type the attribute type
   * @param values the values to assign
   */
    public AttributeValues(String type, Set values) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        this.type = type;
        this.values = values;
    }

    /**
   * @see #type
   */
    public String getType() {
        return type;
    }

    /**
   * @see #values
   */
    public Set getValues() {
        return values;
    }
}
