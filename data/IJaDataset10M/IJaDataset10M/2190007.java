package org.gwtcmis.model.property;

import org.gwtcmis.model.EnumPropertyType;
import java.util.List;

/**
 * @author <a href="mailto:andrey00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class DecimalProperty extends BaseProperty<Double> {

    /**
    * Default constructor.
    */
    public DecimalProperty() {
        super();
    }

    public DecimalProperty(DecimalProperty other) {
        super(other);
    }

    /**
    * @param id id
    * @param queryName query name
    * @param localName local name
    * @param displayName display name
    * @param value value
    */
    public DecimalProperty(String id, String queryName, String localName, String displayName, Double value) {
        super(id, queryName, localName, displayName, value);
    }

    /**
    * @param id id
    * @param queryName query name
    * @param localName local name
    * @param displayName display name
    * @param values values
    */
    public DecimalProperty(String id, String queryName, String localName, String displayName, List<Double> values) {
        super(id, queryName, localName, displayName, values);
    }

    /**
    * {@inheritDoc}
    */
    public final EnumPropertyType getType() {
        return EnumPropertyType.DECIMAL;
    }
}
