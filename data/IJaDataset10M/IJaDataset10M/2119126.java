package com.google.gwt.i18n.client.impl.plurals;

/**
 * Plural forms for Croatian are x1 (but not x11), x2-x4 (but not x12-x14),
 * and n.
 */
public class DefaultRule_hr extends DefaultRule {

    @Override
    public PluralForm[] pluralForms() {
        return DefaultRule_x1_x234_n.pluralForms();
    }

    @Override
    public int select(int n) {
        return DefaultRule_x1_x234_n.select(n);
    }
}
