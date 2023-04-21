package com.c4j.composition;

import java.util.Set;
import java.util.SortedSet;
import com.c4j.workspace.IReceptaclePort;

public interface IPublicReceptaclePort extends IPublicPort, IReceptaclePort {

    IPublicReceptacleReference createPublicReceptacleReference(String instance, String receptacle, int cardinality);

    IPublicReceptacleReference createPublicReceptacleReference(IInstance instance, IReceptaclePort receptacle, int cardinality);

    void removePublicReceptacleReference(IPublicReceptacleReference receptacle);

    Set<? extends IPublicReceptacleReference> getPublicReceptacleReferences();

    SortedSet<? extends IPublicReceptacleReference> getSortedPublicReceptacleReferences();
}
