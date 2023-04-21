package net.boogie.calamari.genetic.model;

import java.io.Serializable;

public interface IItemFactory<T> extends Serializable {

    public T newItem();
}
