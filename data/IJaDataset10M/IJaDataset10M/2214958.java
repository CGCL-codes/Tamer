package net.sf.mytoolbox.lang.validate;

public final class IntValidator extends IntegralValidator<IntValidator> {

    private final int actual;

    IntValidator(FailHandler handler, int actual) {
        super(handler);
        this.actual = actual;
    }

    @Override
    public long longValue() {
        return this.actual;
    }

    @Override
    protected Integer value() {
        return this.actual;
    }
}
