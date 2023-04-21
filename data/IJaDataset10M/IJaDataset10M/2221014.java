package serene.datatype.rngnative;

import org.relaxng.datatype.Datatype;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;
import serene.datatype.DefaultBuilder;

public class NativeBuilder extends DefaultBuilder {

    public NativeBuilder(Datatype baseType) {
        super(baseType);
    }

    public void addParameter(String name, String strValue, ValidationContext context) throws DatatypeException {
        throw new DatatypeException("Native datatypes do not allow parameters.");
    }
}
