package com.google.gwt.safehtml.shared;

/**
 * A string wrapped as an object of type {@link SafeHtml}.
 *
 * <p>
 * This class is intended only for use in generated code where the code
 * generator guarantees that instances of this type will adhere to the
 * {@link SafeHtml} contract (hence the purposely unwieldy class name).
 */
public class OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml implements SafeHtml {

    private String html;

    /**
   * Constructs an instance from a given HTML String.
   *
   * @param html an HTML String that is assumed to be safe
   */
    public OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(String html) {
        if (html == null) {
            throw new NullPointerException("html is null");
        }
        this.html = html;
    }

    /**
   * {@inheritDoc}
   */
    public String asString() {
        return html;
    }

    /**
   * Compares this string to the specified object.
   */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SafeHtml)) {
            return false;
        }
        return html.equals(((SafeHtml) obj).asString());
    }

    /**
   * Returns a hash code for this string.
   */
    @Override
    public int hashCode() {
        return html.hashCode();
    }
}
