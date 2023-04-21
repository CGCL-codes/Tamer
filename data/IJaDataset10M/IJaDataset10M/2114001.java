package gui.propertysheet.types;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import gui.propertysheet.abs.AbstractPropertyConverter;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class BooleanPropertyConverter extends AbstractPropertyConverter<BooleanProperty, Boolean> {

    public boolean canConvert(Class type) {
        return type.equals(BooleanProperty.class);
    }

    public String getNodeName() {
        return "boolNode";
    }

    public void createNewProp() {
        prop = new BooleanProperty();
    }

    public void writeValue(MarshallingContext context) {
        context.convertAnother(prop.getValue());
    }

    public void readValue(UnmarshallingContext context) {
        Boolean bool = (Boolean) context.convertAnother(prop, Boolean.class);
        prop.setValue(bool);
    }
}
