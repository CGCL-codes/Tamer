package org.jbehave.core.story.domain;

/**
 * By default, a Scenario takes place in a HashMapWorld.
 * 
 * Story.createWorld() can be overridden to provide your own
 * custom World. We recommend that if you want to provide your
 * own accessors, you should override CustomWorld which allows
 * you to ignore the interface below.
 * 
 * The methods on this interface are a legacy of JBehave 1.0.
 * We might remove them from the interface at some point.
 * 
 * World keys should work as HashMap keys, ie: they should
 * be immutable and their hashCode() should play nice with their
 * equals().
 * 
 * @see HashMapWorld CustomWorld Given CleansUpWorld
 */
public interface World {

    /**
     * Get a value out of the world. If it doesn't exist, store the default value.
     */
    Object get(Object key, Object defaultValue);

    /**
     * Get a value out of the world. If it doesn't exist, return null.
     */
    Object get(Object key);

    /**
     * Put a value into the world, replacing any existing value.
     */
    void put(Object key, Object value);

    /**
     * Reset the world to its default state
     */
    void clear();
}
