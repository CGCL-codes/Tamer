package com.google.gdata.model;

/**
 * The default metadata registry.  This is a helper around the default metadata
 * instance, which is a global singleton.
 *
 * the static metadata generation code to take a MetadataRegistryBuilder to
 * build on, instead of using the global static methods.  This requires changing
 * the code from a static initializer to an explicitly called initialization
 * method.
 *
 * 
 */
public class DefaultRegistry {

    private static final MetadataRegistryBuilder builder = MetadataRegistry.builder();

    /**
   * Returns the metadata for the given key.
   */
    public static <D, E extends Element> ElementMetadata<D, E> get(ElementKey<D, E> key) {
        return get().bind(key);
    }

    /**
   * Returns the default metadata registry builder
   */
    public static MetadataRegistryBuilder builder() {
        return builder;
    }

    /**
   * Get the default metadata registry instance.
   */
    public static MetadataRegistry get() {
        return builder.create();
    }

    /**
   * Builds the default metadata for the element.
   */
    public static ElementCreator build(ElementKey<?, ?> element) {
        return builder().build(element);
    }

    /**
   * Builds the metadata for the child inside the parent.
   */
    public static ElementCreator build(ElementKey<?, ?> parent, ElementKey<?, ?> element) {
        return builder().build(parent, element);
    }

    /**
   * Builds metadata for the element during the context.
   */
    public static ElementCreator build(ElementKey<?, ?> element, MetadataContext context) {
        return builder().build(element, context);
    }

    /**
   * Builds the metadata for the child inside the parent, during the context.
   */
    public static ElementCreator build(ElementKey<?, ?> parent, ElementKey<?, ?> element, MetadataContext context) {
        return builder().build(parent, element, context);
    }

    /**
   * Builds the metadata for the attribute inside the parent.
   */
    public static AttributeCreator build(ElementKey<?, ?> parent, AttributeKey<?> attribute) {
        return builder().build(parent, attribute);
    }

    /**
   * Builds the metadata for the attribute inside the parent, during the
   * context.
   */
    public static AttributeCreator build(ElementKey<?, ?> parent, AttributeKey<?> attribute, MetadataContext context) {
        return builder().build(parent, attribute, context);
    }

    /**
   * Adapts from the source type to the adaptation type on the given kind.
   */
    public static <D, E extends Element> void adapt(ElementKey<D, E> source, String kind, ElementKey<? extends D, ? extends E> adaptation) {
        build(source).addAdaptation(kind, adaptation);
    }

    private DefaultRegistry() {
    }
}
