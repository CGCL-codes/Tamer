package org.wam.style;

/**
 * Contains style attributes pertaining to the background of a widget. These styles are supported by
 * {@link org.wam.core.WamElement} and all of its subclasses unless the
 * {@link org.wam.core.WamElement#draw(java.awt.Graphics2D)} method is overridden in a way that
 * ignores them.
 */
public class BackgroundStyles implements StyleDomain {

    private StyleAttribute<?>[] theAttributes;

    private BackgroundStyles() {
        theAttributes = new StyleAttribute[0];
    }

    private void register(StyleAttribute<?> attr) {
        theAttributes = prisms.util.ArrayUtils.add(theAttributes, attr);
    }

    private static final BackgroundStyles instance;

    /** The color of a widget's background */
    public static final StyleAttribute<java.awt.Color> color;

    /** The transparency of a widget's background. Does not apply to the entire widget. */
    public static final StyleAttribute<Float> transparency;

    static {
        instance = new BackgroundStyles();
        color = StyleAttribute.createColorStyle(instance, "color", new java.awt.Color(0, true));
        instance.register(color);
        transparency = StyleAttribute.createFloatStyle(instance, "transparency", 0, 1, 0);
        instance.register(transparency);
    }

    /**
	 * @return The style domain for all background styles
	 */
    public static BackgroundStyles getDomainInstance() {
        return instance;
    }

    public String getName() {
        return "bg";
    }

    public java.util.Iterator<StyleAttribute<?>> iterator() {
        return new DomainAttributeIterator(theAttributes);
    }
}
