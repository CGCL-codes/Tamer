package com.google.gwt.aria.client;

/**
 * Defines additional attributes that are interpreted by readers. Such an attribute is
 * 'tabindex' which indicates the tab order position of the element.
 *
 * @param <T> The extra attribute value type
 */
public final class ExtraAttribute<T> extends Attribute<T> {

    public static final ExtraAttribute<Integer> TABINDEX = new ExtraAttribute<Integer>("tabIndex", "");

    public ExtraAttribute(String name) {
        super(name);
    }

    public ExtraAttribute(String name, String defaultValue) {
        super(name, defaultValue);
    }
}
