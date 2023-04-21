package syntelos.sys;

import alto.io.Code;
import alto.io.Check;
import alto.io.Input;
import alto.io.Output;
import syntelos.lang.Address;
import alto.lang.Component;

/**
 * Notational placeholder for Type Index.
 * 
 */
public class Index extends alto.lang.Value {

    private final Reference reference;

    public Index(Reference reference) throws java.io.IOException {
        super(reference.getBuffer());
        this.reference = reference;
    }
}
