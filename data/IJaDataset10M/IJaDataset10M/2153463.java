package ru.beta2.testyard.config.defaults;

/**
 * Interface that define tagged message for {@link ru.beta2.testyard.config.defaults.DefaultTaggedHandler}.
 * <br/>
 * User: Inc
 * Date: 18.01.2009
 * Time: 14:47:23
 */
public interface TaggedObject {

    int getTag();

    void setTag(int tag);
}
