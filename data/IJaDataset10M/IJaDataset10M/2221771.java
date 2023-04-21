package org.j3d.di.scene;

/**
 * An interface for creating IGrobject classes.
 * 
 * @since 1.0
 */
public interface IGrobjectFactory<T> {

    public IGrobject getObject(T obj);
}
