package nl.flotsam.limbo.util;

import nl.flotsam.limbo.Document;

public class StringBuilderDocument implements Document {

    private StringBuilder builder;

    public StringBuilderDocument() {
        this(new StringBuilder());
    }

    public StringBuilderDocument(StringBuilder builder) {
        this.builder = builder;
    }

    public void link(Object object, String text) {
        builder.append(text);
    }

    public void text(String text) {
        builder.append(text);
    }

    public String toString() {
        return builder.toString();
    }

    public Document detail(String text) {
        return new NullDocument();
    }
}
