package org.argouml.model;

/**
 * This is a wrapper class around an element from the Diagram Interchange Model.
 * The intention here is to pass a class of this type around instead of just
 * an Object. This will give us better compile time safety than we currently
 * get with model elments which are currently passed about as Object.
 *
 * @author Bob Tarling
 */
public interface DiElement {
}
