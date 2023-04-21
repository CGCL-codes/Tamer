package org.thechiselgroup.choosel.core.client.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.thechiselgroup.choosel.core.client.resources.ResourceSetTestUtils.captureOnResourceSetChanged;
import static org.thechiselgroup.choosel.core.client.resources.ResourceSetTestUtils.verifyOnResourcesAdded;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UnionResourceSetTest {

    @Mock
    private ResourceSetChangedEventHandler resourceSetChangedHandler;

    private UnionResourceSet underTest;

    @Test
    public void addMultipleResourcesToContainedResourceSet() {
        ResourceSet resources = ResourceSetTestUtils.createLabeledResources(1, 2, 3);
        underTest.addResourceSet(resources);
        resources.addAll(ResourceSetTestUtils.createResources(4, 5));
        assertEquals(true, underTest.contains(ResourceSetTestUtils.createResource(4)));
        assertEquals(true, underTest.contains(ResourceSetTestUtils.createResource(5)));
    }

    @Test
    public void addResourcesAddsToAllResources() {
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(1, 2, 3));
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(3, 4, 5));
        assertEquals(5, underTest.size());
        assertTrue(underTest.containsAll(ResourceSetTestUtils.createLabeledResources(1, 2, 3)));
        assertTrue(underTest.containsAll(ResourceSetTestUtils.createLabeledResources(3, 4, 5)));
    }

    @Test
    public void containsResourcesAddedToChildren() {
        ResourceSet resources = ResourceSetTestUtils.createLabeledResources(1, 2, 3);
        underTest.addResourceSet(resources);
        resources.add(ResourceSetTestUtils.createResource(5));
        assertEquals(4, underTest.size());
        assertTrue(underTest.containsAll(resources));
    }

    @Test
    public void doesNotContainResourcesRemovedFromChildren() {
        ResourceSet resources = ResourceSetTestUtils.createLabeledResources(1, 2, 3);
        underTest.addResourceSet(resources);
        resources.remove(ResourceSetTestUtils.createResource(1));
        assertEquals(2, underTest.size());
        assertTrue(underTest.containsAll(resources));
        assertFalse(underTest.contains(ResourceSetTestUtils.createResource(1)));
    }

    @Test
    public void doesNotRemoveDuplicateResourceOnRemove() {
        ResourceSet resources = ResourceSetTestUtils.createLabeledResources(1, 2, 3);
        underTest.addResourceSet(resources);
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(3, 4, 5));
        resources.remove(ResourceSetTestUtils.createResource(3));
        assertEquals(5, underTest.size());
        assertTrue(underTest.containsAll(resources));
        assertTrue(underTest.containsAll(ResourceSetTestUtils.createLabeledResources(3, 4, 5)));
    }

    @Test
    public void doNotFireEventWhenResourceSetAddedButNoNewResources() {
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(1, 2, 3));
        underTest.addEventHandler(resourceSetChangedHandler);
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(1, 2, 3));
        captureOnResourceSetChanged(0, resourceSetChangedHandler);
    }

    @Test
    public void fireEventWhenResourceSetAdded() {
        underTest.addEventHandler(resourceSetChangedHandler);
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(1, 2, 3));
        verifyOnResourcesAdded(ResourceSetTestUtils.createResources(1, 2, 3), resourceSetChangedHandler);
    }

    @Test
    public void noContainmenChangeWhenResourceAddedAfterRemove() {
        Resource addedResource = ResourceSetTestUtils.createResource(8);
        ResourceSet resources = ResourceSetTestUtils.createLabeledResources(1, 2, 3);
        underTest.addResourceSet(resources);
        underTest.removeResourceSet(resources);
        resources.add(addedResource);
        assertEquals(0, underTest.size());
        assertFalse(underTest.contains(addedResource));
    }

    @Test
    public void noFailureOnRemoveInvalidResourceSet() {
        ResourceSet resources1 = ResourceSetTestUtils.createLabeledResources(1, 2, 3);
        underTest.removeResourceSet(resources1);
    }

    @Test
    public void removeResourcesDoesNotRemoveDuplicateResource() {
        ResourceSet resources = ResourceSetTestUtils.createLabeledResources(3, 4, 5);
        underTest.addResourceSet(ResourceSetTestUtils.createLabeledResources(1, 2, 3));
        underTest.addResourceSet(resources);
        underTest.removeResourceSet(resources);
        assertEquals(3, underTest.size());
        assertTrue(underTest.containsAll(ResourceSetTestUtils.createLabeledResources(1, 2, 3)));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new UnionResourceSet(new DefaultResourceSet());
    }
}
