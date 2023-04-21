package javaaxp.core.service.model.document;

public class FullOrShorthandData<T> {

    public final T fFull;

    public final String fShorthand;

    public FullOrShorthandData(String shorthand, T full) {
        fFull = full;
        fShorthand = shorthand;
    }
}
