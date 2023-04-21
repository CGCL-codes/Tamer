package annone.local.linker;

class StringToken extends Token {

    private final String value;

    public StringToken(int column, int line, String value) {
        super(column, line);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected String tokenToString() {
        return value;
    }
}
