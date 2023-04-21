package uk.ac.imperial.ma.metric.exerciseEngine;

/**
 * A resource is the super-interface for the different objects kinds of 
 * object which might be contained within the exercises system. Here are
 * some examples of resources: items, tests, etc.
 * 
 * TODO: get clearer here. A resource is something that you can have an interaction
 * with so that rules out metadata.
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 17 Sep 2008
 */
public interface Resource {

    /** The type of this resource. */
    public ResourceType getResourceType();
}
