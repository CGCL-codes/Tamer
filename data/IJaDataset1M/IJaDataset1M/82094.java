package com.jemframework.domain;

import com.jemframework.domain.identifier.Identifier;

public interface Entity {

    Identifier getId();

    int getEntityType();

    void updateFrom(Entity anEntity);
}
