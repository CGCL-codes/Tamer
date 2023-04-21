package org.handwerkszeug.kvsview.model;

import org.handwerkszeug.kvsview.NotNull;

public interface Leaf<V> {

    @NotNull
    V value();

    void remove();
}
