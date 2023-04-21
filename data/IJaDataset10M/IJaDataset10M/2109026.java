package com.bigfatgun.fixjures.handlers;

import com.bigfatgun.fixjures.FixtureType;
import com.google.common.base.Supplier;

/** Handles conversion of source data to destination data. */
public interface Unmarshaller<T> {

    boolean canUnmarshallObjectToType(Object sourceObject, FixtureType typeDef);

    Class<T> getReturnType();

    Supplier<? extends T> unmarshall(UnmarshallingContext helper, Object source, FixtureType typeDef);
}
