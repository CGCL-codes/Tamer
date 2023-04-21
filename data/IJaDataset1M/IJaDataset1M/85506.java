package org.openemed.LQS;

public final class Definition {

    public Definition() {
    }

    public Definition(String text, boolean preferred, String language_id, QualifiedCode source_id) {
        this.text = text;
        this.preferred = preferred;
        this.language_id = language_id;
        this.source_id = source_id;
    }

    public String text;

    public boolean preferred;

    public String language_id;

    public QualifiedCode source_id;
}
