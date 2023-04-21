package net.sf.xmlunit.builder;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

/**
 * Holds the common builder methods for XSLT related builders.
 *
 * <p><code>B</code> is the derived builder interface.</p>
 */
public interface TransformationBuilderBase<B extends TransformationBuilderBase<B>> {

    /**
     * sets the TraX factory to use.
     */
    B usingFactory(TransformerFactory f);

    /**
     * Adds an output property.
     */
    B withOutputProperty(String name, String value);

    /**
     * Adds a parameter.
     */
    B withParameter(String name, Object value);

    /**
     * Sets the stylesheet to use.
     */
    B withStylesheet(Source s);

    /**
     * Sets the resolver to use for the document() function and
     * xsi:import/include.
     */
    B withURIResolver(URIResolver r);
}
