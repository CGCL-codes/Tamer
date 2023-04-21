package com.google.gwt.i18n.client.impl.plurals;

/**
 * Plural forms for Oriya are 1 and n, with 0 treated as plural.
 */
public class DefaultRule_or extends DefaultRule {

    @Override
    public PluralForm[] pluralForms() {
        return DefaultRule_1_0n.pluralForms();
    }

    @Override
    public int select(int n) {
        return DefaultRule_1_0n.select(n);
    }
}
