package org.codespin.silkworm.filter;

/**
 * <p>Attribute Filters are used to simplify access to attributes of the
 * matched element in a template. They are specified in the template
 * configuration file, and specify a mapping between an attribute and a
 * Velocity context variable.</p>
 *
 * <p>A filter method and/or filter class can be optionally specified to
 * provide manipulation of the attribute's value before placing it in the
 * Velocity context. When both a filter method and a filter class are
 * supplied, the attribute will first be filtered by the filter class, and
 * then the filter method will be called on the result. The developer must
 * take care to ensure that the filter method exists in the class that is
 * returned by the filter object.</p>
 *
 * <p>Filters should be used in circumstances similar to those that would
 * cause a developer to use a custom tag in JSP: to isolate complex business
 * logic from the presentation layer. In the case of a template system,
 * business logic includes transformation and manipulation of the data that
 * the template is being applied to, while the presentation layer is the
 * template itself. One example filter is included with Silkworm - the
 * PackageDirectoryName filter, which is useful to convert package names (eg.
 * <code>org.codespin.silkworm</code> into package directories
 * (<code>org/codespin/silkworm</code>) that can be used in filenames.</p>
 *
 * @author <a href="mailto:gjritter@codespin.org">Greg Ritter</a>
 * @version $Revision: 1.2 $
 */
public class AttributeFilterConfig {

    /**
     * Creates a new <code>AttributeFilterConfig</code> instance.
     *
     * @param attribute a <code>String</code> value that is the attribute name
     * @param contextName a <code>String</code> value that is the name by
     * which the attribute's value will be exposed in the Velocity context
     * @param filterMethod a <code>String</code> value that is the name of a
     * method to be applied to the attribute's value before placing it in the
     * context; null if no method should be applied
     * @param filterClass a <code>String</code> value that is the name of a
     * filter class to be used in filtering the attribute's value before
     * placing it in the context; null if no filter class should be used
     */
    public AttributeFilterConfig(String attribute, String newAttributeName, String filterMethod, String filterClass) {
        this.attribute = attribute;
        this.newAttributeName = newAttributeName;
        this.filterMethod = filterMethod;
        this.filterClass = filterClass;
    }

    /**
     * Get the name of the original attribute that should be filtered and
     * added to the matched element as a new attribute.
     *
     * @return a <code>String</code> value that is the name of the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Get the new name that the filtered attribute should be exposed as in
     * the Velocity context.
     *
     * @return a <code>String</code> value that is the new attribute name
     */
    public String getNewAttributeName() {
        return newAttributeName;
    }

    /**
     * Get the name of the method to be applied to the original attribute's
     * value before placing it in the Velocity context.
     *
     * @return a <code>String</code> value that is the filter method name
     */
    public String getFilterMethod() {
        return filterMethod;
    }

    /**
     * Get the name of the filter class that should be used to manipulate the
     * value that will be placed in the Velocity context.
     *
     * @return a <code>String</code> value that is the filter class name
     */
    public String getFilterClass() {
        return filterClass;
    }

    /**
     * The original attribute name.
     */
    private String attribute;

    /**
     * The new attribute name.
     */
    private String newAttributeName;

    /**
     * The filter method name.
     */
    private String filterMethod;

    /**
     * The filter class name.
     */
    private String filterClass;
}
