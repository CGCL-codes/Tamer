package spinja.options;

public class BooleanOption extends Option {

    private boolean set;

    public BooleanOption(final char letter, final String description) {
        super(letter, description);
        set = false;
    }

    @Override
    public boolean isSet() {
        return set;
    }

    @Override
    public void parseOption(final String rest) {
        set = true;
    }
}
