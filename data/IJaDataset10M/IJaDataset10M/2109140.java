package royere.cwi.input;

import royere.cwi.util.RoyereException;

/**
* The layout which the implementor has attempted to create with an input factory is unknown to the metric factory.
*/
public class UnknownInput extends RoyereException {

    public UnknownInput() {
        super();
    }

    public UnknownInput(String s) {
        super(s);
    }
}
