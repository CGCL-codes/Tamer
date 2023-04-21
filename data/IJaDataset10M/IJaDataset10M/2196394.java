package com.google.gwt.i18n.client.impl.plurals;

/**
 * Plural forms for Spanish are 1 and n, with 0 treated as plural.
 */
public class DefaultRule_es extends DefaultRule {

    @Override
    public PluralForm[] pluralForms() {
        return DefaultRule_1_0n.pluralForms();
    }

    @Override
    public int select(int n) {
        return DefaultRule_1_0n.select(n);
    }
}
