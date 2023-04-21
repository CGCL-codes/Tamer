package org.nakedobjects.distribution;

import org.nakedobjects.object.LoadedObjects;
import org.nakedobjects.object.TypedNakedCollection;

public interface InstanceSet {

    TypedNakedCollection recreateInstances(LoadedObjects loadedObjects);
}
