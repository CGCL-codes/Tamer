package com.sebscape.sebcms.persistence.domain;

public interface IUpdateable<T> {

    public void update(T newValues);
}
