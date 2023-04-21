package com.doculibre.intelligid.index.helpers;

import java.io.Serializable;
import java.util.List;

public interface IndexHelper<T> extends Serializable {

    void rebuild();

    boolean isEmpty();

    boolean isIndexed(T object);

    void add(T object);

    void update(T object);

    void addOrUpdate(T object);

    void delete(T object);

    void deleteAll();

    List<T> search(String freeTextQuery);

    List<T> search(String freeTextQuery, String sortField, Boolean sortAscending);

    void release(List<T> resultatsRecherche);
}
