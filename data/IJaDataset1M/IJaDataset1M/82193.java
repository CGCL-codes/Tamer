package com.tristia.entity.util;

import java.io.Serializable;
import java.util.List;

public interface GenericDataLoader<T, PK extends Serializable> {

    void loadData();

    void setObjects(List<T> object);

    void init();
}
