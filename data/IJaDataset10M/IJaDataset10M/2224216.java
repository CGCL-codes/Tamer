package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is a unit test for the AbstractModelElement class which forms part of
 * the menu model.  It is abstract because it tests an abstract class.
 * Anything that extends {@link AbstractModelElement} should unit test by
 * extending this test class.
 */
public abstract class AbstractModelElementTestAbstract extends TestCaseAbstract {

    /**
     * Used for various element name set ups.
     */
    protected final String ELEMENT = "Element";

    /**
     * Used to create new ElementDetails instances for use in tests.
     */
    protected final Styles TEST_STYLES = StylesBuilder.getEmptyStyles();

    /**
     * Create a new instance of this test case.
     */
    public AbstractModelElementTestAbstract() {
    }

    /**
     * This method tests the getElementDetails() method of the
     * AbstractModelElement class.  It makes use of a simple test class
     * provided by {@link #createTestInstance createMutableStyleProperties()} to create
     * a concrete instance for testing purposes.
     */
    public void testGetElementDetails() throws Exception {
        ElementDetails elementDetails = createElementDetails(ELEMENT, TEST_STYLES);
        AbstractModelElement testClass = createTestInstance(elementDetails);
        ElementDetails testElementDetails = testClass.getElementDetails();
        assertNotNull("element details should not be null", testElementDetails);
        assertEquals("element details should be the same", elementDetails, testElementDetails);
    }

    /**
     * A utility method to create an instance of AbstractModelElement with the
     * default element name and Styles.
     *
     * @return            An initialised instance of a subclass of
     *                    AbstractModelElement
     */
    protected AbstractModelElement createTestInstance() {
        return createTestInstance(ELEMENT, TEST_STYLES);
    }

    /**
     * A utility method to create an instance of AbstractModelElement with the
     * specified element name and styles.
     *
     * @param elementName   The element name to use
     * @param styles        The Styles which are applicable to the PAPI element
     *                      that this ModelElement represents.
     * @return              An initialised instance of a subclass of
     *                      AbstractModelElement
     */
    protected AbstractModelElement createTestInstance(String elementName, Styles styles) {
        return createTestInstance(createElementDetails(elementName, TEST_STYLES));
    }

    /**
     * Provides a valid concrete implementation of {@link AbstractModelElement}
     * for testing purposes.  It is initialised with the provided element
     * details.
     *
     * @param elementDetails    The elementDetails to initialise the new
     *                          ModelElement instance with.
     * @return      An initialised instance of some class extending the
     *              abstract class
     */
    protected abstract AbstractModelElement createTestInstance(ElementDetails elementDetails);

    /**
     * A utility method to create an instance of ElementDetails with the
     * specified element name and Styles. An ElementDetails must have non null
     * Styles to be valid.
     *
     * @param element   The element name to use
     * @param styles    The Styles which are applicable to the PAPI element
     *                  that this ElementDetails represents.
     * @return          An initialised implementation instance of ElementDetails
     */
    protected ElementDetails createElementDetails(String element, Styles styles) {
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setElementName(element);
        elementDetails.setStyles(styles);
        return elementDetails;
    }
}
