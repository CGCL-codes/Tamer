package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.ImageAssetReferenceMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;

/**
 * This class tests a {@link ConcreteMenuIcon}.
 */
public class ConcreteMenuIconTestCase extends AbstractModelElementTestAbstract {

    /**
     * This method tests the getNormalURL() method.  Since this is never
     * supposed to be null it tests the exception state as well as get/set
     * combinations.
     */
    public void testGetNormalURL() {
        final ImageAssetReference testNormalURL = new LiteralImageAssetReference("normal");
        ImageAssetReference testString;
        ConcreteMenuIcon testInstance = createTestClass();
        try {
            testString = testInstance.getNormalURL();
            fail("The retrieval should trigger a state exception");
        } catch (IllegalStateException iae) {
        }
        testInstance.setNormalURL(testNormalURL);
        testString = testInstance.getNormalURL();
        assertNotNull("The test string should not be null", testString);
        assertEquals("The strings should be the same", testNormalURL, testString);
    }

    /**
     * This method tests the getOverURL() method.
     */
    public void testGetOverURL() {
        ConcreteMenuIcon testInstance = createTestClass();
        ImageAssetReference testString = testInstance.getOverURL();
        assertNull("The test string should be null", testString);
        final ImageAssetReferenceMock overURL = new ImageAssetReferenceMock("overURL", expectations);
        testInstance.setOverURL(overURL);
        testString = testInstance.getOverURL();
        assertNotNull("The test string should not be null", testString);
        assertEquals("The strings should be the same", overURL, testString);
    }

    /**
     * A utility method for creating basic instances of ConcreteMenuIcon for
     * the various test methods.
     *
     * @return An initialised instance of ConcreteMenuIcon
     */
    private ConcreteMenuIcon createTestClass() {
        return (ConcreteMenuIcon) createTestInstance(createElementDetails(ELEMENT, TEST_STYLES));
    }

    protected AbstractModelElement createTestInstance(ElementDetails elementDetails) {
        return new ConcreteMenuIcon(elementDetails);
    }
}
