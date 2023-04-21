package com.docum.test.data;

import com.docum.domain.po.IdentifiedEntity;

public interface TestDataEntityConstructor<T extends IdentifiedEntity, S> {

    public T construct(S name);
}
