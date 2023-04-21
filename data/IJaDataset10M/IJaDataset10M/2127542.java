package org.thechiselgroup.choosel.core.client.resources;

import static org.junit.Assert.assertEquals;
import static org.thechiselgroup.choosel.core.client.resources.ResourceSetTestUtils.captureOnResourceSetChanged;
import org.mockito.Mock;
import com.google.gwt.event.shared.HandlerRegistration;

public class AbstractResourceSetTest {

    protected ResourceSet underTestAsResourceSet;

    @Mock
    protected ResourceSetChangedEventHandler changedHandler;

    protected void verifyChangeHandlerNotCalled() {
        captureOnResourceSetChanged(0, changedHandler);
    }

    protected void verifyOnResourcesAdded(int... resourceNumbers) {
        ResourceSetTestUtils.verifyOnResourcesAdded(ResourceSetTestUtils.createResources(resourceNumbers), changedHandler);
    }

    protected HandlerRegistration registerEventHandler() {
        return underTestAsResourceSet.addEventHandler(changedHandler);
    }

    protected void assertSizeEquals(int size) {
        assertEquals(size, underTestAsResourceSet.size());
    }

    protected void assertContainsResource(int resourceNumber, boolean expected) {
        assertEquals(expected, underTestAsResourceSet.contains(ResourceSetTestUtils.createResource(resourceNumber)));
    }
}
