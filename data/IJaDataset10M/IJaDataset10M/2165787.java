package com.googlecode.pinthura.factory;

public interface FactoryCreator {

    <T> T create(final Class<T> factoryInterface);
}
