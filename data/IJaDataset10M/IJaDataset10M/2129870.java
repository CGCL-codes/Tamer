package org.thechiselgroup.choosel.core.client.resources;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.thechiselgroup.choosel.core.client.resources.ResourceSetTestUtils.captureOnResourceSetChanged;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.core.client.util.predicates.Predicate;
import org.thechiselgroup.choosel.core.shared.test.matchers.collections.CollectionMatchers;

public class FilteredResourceSetTest extends AbstractResourceSetTest {

    private FilteredResourceSet underTest;

    @Mock
    private Predicate<Resource> predicate;

    private ResourceSet source;

    @Test
    public void addingFalseResourceDoesNotAddResource() {
        preparePredicate(1, false);
        addToSource(1);
        assertSizeEquals(0);
        assertContainsResource(1, false);
    }

    @Test
    public void addingFalseResourcesDoesNotFireEvent() {
        preparePredicate(1, false);
        preparePredicate(2, false);
        registerEventHandler();
        addToSource(1, 2);
        verifyChangeHandlerNotCalled();
    }

    @Test
    public void addingMixedResourcesFireSingleEvent() {
        preparePredicate(1, true);
        preparePredicate(2, false);
        preparePredicate(3, true);
        registerEventHandler();
        addToSource(1, 2, 3);
        verifyOnResourcesAdded(1, 3);
    }

    @Test
    public void addingTrueResourceDoesAddResource() {
        preparePredicate(1, true);
        addToSource(1);
        assertSizeEquals(1);
        assertContainsResource(1, true);
    }

    private void addToSource(int... resourceNumbers) {
        source.addAll(ResourceSetTestUtils.createResources(resourceNumbers));
    }

    @Test
    public void changePredicateUpdatesDelegate() {
        preparePredicate(1, true);
        preparePredicate(2, true);
        preparePredicate(3, false);
        preparePredicate(4, false);
        addToSource(1, 3);
        invertOnSource(1, 2, 3, 4);
        assertSizeEquals(1);
        assertContainsResource(1, false);
        assertContainsResource(2, true);
    }

    @Test
    public void defaultPredicateDoesNotFilterOutResources() {
        underTest = new FilteredResourceSet(source, new DefaultResourceSet());
        underTestAsResourceSet = underTest;
        addToSource(1);
        assertSizeEquals(1);
        assertContainsResource(1, true);
    }

    @Test
    public void invertMixedResourcesDoesFireSingleEvent() {
        preparePredicate(1, true);
        preparePredicate(2, true);
        preparePredicate(3, false);
        preparePredicate(4, false);
        addToSource(1, 3);
        registerEventHandler();
        invertOnSource(1, 2, 3, 4);
        ResourceSetChangedEvent event = captureOnResourceSetChanged(1, changedHandler).getValue();
        assertThat(event.getRemovedResources(), CollectionMatchers.containsExactly(ResourceSetTestUtils.createResources(1)));
        assertThat(event.getAddedResources(), CollectionMatchers.containsExactly(ResourceSetTestUtils.createResources(2)));
    }

    @Test
    public void invertMixedResourcesUpdatesDelegate() {
        preparePredicate(1, true);
        preparePredicate(2, true);
        preparePredicate(3, false);
        preparePredicate(4, false);
        addToSource(1, 3);
        Predicate<Resource> newPredicate = mock(Predicate.class);
        preparePredicate(1, false, newPredicate);
        preparePredicate(2, false, newPredicate);
        preparePredicate(3, true, newPredicate);
        preparePredicate(4, true, newPredicate);
        underTest.setFilterPredicate(newPredicate);
        assertSizeEquals(1);
        assertContainsResource(3, true);
    }

    private void invertOnSource(int... resourceNumbers) {
        source.invertAll(ResourceSetTestUtils.createResources(resourceNumbers));
    }

    private void preparePredicate(int resourceNumber, boolean value) {
        preparePredicate(resourceNumber, value, predicate);
    }

    private void preparePredicate(int resourceNumber, boolean value, Predicate<Resource> predicate) {
        when(predicate.evaluate(ResourceSetTestUtils.createResource(resourceNumber))).thenReturn(value);
    }

    @Test
    public void removeContainedResourceDoesRemoveResource() {
        preparePredicate(1, true);
        addToSource(1);
        removeFromSource(1);
        assertSizeEquals(0);
        assertContainsResource(1, false);
    }

    private void removeFromSource(int... resourceNumbers) {
        source.removeAll(ResourceSetTestUtils.createResources(resourceNumbers));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        source = new DefaultResourceSet();
        underTest = new FilteredResourceSet(source, new DefaultResourceSet());
        underTestAsResourceSet = underTest;
        underTest.setFilterPredicate(predicate);
    }
}
