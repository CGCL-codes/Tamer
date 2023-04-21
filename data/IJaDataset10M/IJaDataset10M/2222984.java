package org.thymeleaf.processor;

import org.thymeleaf.dom.Element;

/**
 * <p>
 *   Sub-interface of {@link IProcessorMatcher} for matchers based on element names.
 * </p>
 * <p>
 *   Every processor matching elements by name should have matchers implementing this
 *   interface, as this enables certain precomputations and boosts performance.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public interface IElementNameProcessorMatcher extends IProcessorMatcher<Element> {

    public String getElementName(final ProcessorMatchingContext context);
}
