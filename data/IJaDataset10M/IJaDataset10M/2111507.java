package org.thechiselgroup.choosel.core.client.resources;

import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.util.predicates.Predicate;
import org.thechiselgroup.choosel.core.client.util.predicates.TruePredicate;

public class FilteredResourceSet extends DelegatingResourceSet {

    private Predicate<Resource> filterPredicate = new TruePredicate<Resource>();

    private final ResourceSet sourceSet;

    /**
     * Instantiates a new filtered resource set.
     * 
     * @param sourceSet
     *            The resource set that should be filtered by this ResourceSet.
     * 
     * @param delegateSet
     *            Delegate in which the filtered resources are stored.
     */
    public FilteredResourceSet(ResourceSet sourceSet, ResourceSet delegateSet) {
        super(delegateSet);
        assert sourceSet != null;
        this.sourceSet = sourceSet;
        this.sourceSet.addEventHandler(new ResourceSetChangedEventHandler() {

            @Override
            public void onResourceSetChanged(ResourceSetChangedEvent event) {
                processEvent(event);
            }
        });
    }

    @Override
    public boolean add(Resource resource) {
        throw new UnsupportedOperationException("FilteredResourceSet.add is not supported");
    }

    @Override
    public boolean addAll(Iterable<Resource> resources) {
        throw new UnsupportedOperationException("FilteredResourceSet.addAll is not supported");
    }

    @Override
    public boolean change(Iterable<Resource> addedResources, Iterable<Resource> removedResources) {
        throw new UnsupportedOperationException("FilteredResourceSet.change is not supported");
    }

    private LightweightList<Resource> filter(LightweightCollection<Resource> resources) {
        LightweightList<Resource> filteredResources = CollectionFactory.createLightweightList();
        for (Resource resource : resources) {
            if (filterPredicate.evaluate(resource)) {
                filteredResources.add(resource);
            }
        }
        return filteredResources;
    }

    public Predicate<Resource> getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public void invert(Resource resource) {
        throw new UnsupportedOperationException("FilteredResourceSet.invert is not supported");
    }

    @Override
    public void invertAll(Iterable<Resource> resources) {
        throw new UnsupportedOperationException("FilteredResourceSet.invertAll is not supported");
    }

    @Override
    public boolean isModifiable() {
        return false;
    }

    private void processEvent(ResourceSetChangedEvent event) {
        getDelegate().change(filter(event.getAddedResources()), filter(event.getRemovedResources()));
    }

    @Override
    public boolean remove(Resource resource) {
        throw new UnsupportedOperationException("FilteredResourceSet.remove is not supported");
    }

    @Override
    public boolean removeAll(Iterable<Resource> resources) {
        throw new UnsupportedOperationException("FilteredResourceSet.removeAll is not supported");
    }

    @Override
    public boolean retainAll(ResourceSet resources) {
        throw new UnsupportedOperationException("FilteredResourceSet.retainAll is not supported");
    }

    public void setFilterPredicate(Predicate<Resource> newFilterPredicate) {
        assert newFilterPredicate != null;
        Predicate<Resource> currentFilterPredicate = filterPredicate;
        if (newFilterPredicate.equals(currentFilterPredicate)) {
            return;
        }
        LightweightList<Resource> resourcesToAdd = CollectionFactory.createLightweightList();
        LightweightList<Resource> resourcesToRemove = CollectionFactory.createLightweightList();
        for (Resource resource : sourceSet) {
            if (newFilterPredicate.evaluate(resource) && !currentFilterPredicate.evaluate(resource)) {
                resourcesToAdd.add(resource);
            } else if (!newFilterPredicate.evaluate(resource) && currentFilterPredicate.evaluate(resource)) {
                resourcesToRemove.add(resource);
            }
        }
        filterPredicate = newFilterPredicate;
        getDelegate().change(resourcesToAdd, resourcesToRemove);
    }
}
