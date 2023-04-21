package com.google.gwt.i18n.client.impl.plurals;

/**
 * Plural forms for Tigrinya are 1 and n, with 0 treated as singular.
 */
public class DefaultRule_ti extends DefaultRule {

    @Override
    public PluralForm[] pluralForms() {
        return DefaultRule_01_n.pluralForms();
    }

    @Override
    public int select(int n) {
        return DefaultRule_01_n.select(n);
    }
}
