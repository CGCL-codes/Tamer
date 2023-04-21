package v4view.web;

public class Submit extends InputPageElement {

    private static final String SUBMIT_KEY = "submit";

    {
        this.setType(SUBMIT_KEY);
    }

    public Submit() {
        super();
    }

    public Submit(final String _name, final String _value) {
        super(_name, _value);
    }

    public Submit(final String _value) {
        super(_value);
    }
}
