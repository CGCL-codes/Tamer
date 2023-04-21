package org.dcopolis.util;

import java.util.*;

public interface Vertex<T extends Vertex> extends Iterable<T> {

    public Set<T> getNeighbors();

    public Iterator<T> iterator();
}
