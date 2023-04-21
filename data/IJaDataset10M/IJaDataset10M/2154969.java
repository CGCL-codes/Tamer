package org.jowidgets.util.maybe;

public final class Some<TYPE> implements IMaybe<TYPE> {

    private final TYPE value;

    /**
	 * Creates a maybe with the value null
	 */
    public Some() {
        this(null);
    }

    /**
	 * Creates a maybe with an value
	 * 
	 * @param value the value, can be null
	 */
    public Some(final TYPE value) {
        this.value = value;
    }

    @Override
    public TYPE getValue() {
        return value;
    }

    @Override
    public TYPE getValueOrElse(final TYPE defaultValue) {
        return value;
    }

    @Override
    public boolean isNothing() {
        return false;
    }

    @Override
    public boolean isSomething() {
        return true;
    }
}
