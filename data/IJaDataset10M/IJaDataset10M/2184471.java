package net.sf.osadm.linedata.table.polisher;

import java.util.ArrayList;
import java.util.List;
import net.sf.osadm.linedata.table.ValuePolisher;
import net.sourceforge.argval.ArgumentValidation;
import net.sourceforge.argval.impl.ArgumentValidationImpl;

public class ValuePolisherCollection implements ValuePolisher {

    private final List<ValuePolisher> valuePolisherList;

    public ValuePolisherCollection(List<ValuePolisher> valuePolisherList) {
        super();
        ArgumentValidation argVal = new ArgumentValidationImpl();
        argVal.isValidCollectionWhenMinElements("valuePolisherList", valuePolisherList, 1);
        if (argVal.containsIllegalArgument()) {
            throw argVal.createIllegalArgumentException();
        }
        this.valuePolisherList = new ArrayList<ValuePolisher>(valuePolisherList);
    }

    public String polishValue(String value) {
        if (value == null) {
            return null;
        }
        for (ValuePolisher valuePolisher : valuePolisherList) {
            value = valuePolisher.polishValue(value);
        }
        return value;
    }
}
