package com.msli.graphic.locale.space;

import com.msli.graphic.locale.Locatable;

/**
 * A Locatable whose locale is a geometric space, and whose locator
 * is a Spatializer.
 * <P>
 * Derived from gumbo.graphic.locale.space.Spatializable.
 * @see Spatializer
 * @author jonb
 */
public interface Spatializable extends Locatable {

    /**
	 * Gets this object's spatializer, which defines its geometric space.
	 * If this is an unmodifiable view, the return will be unmodifiable.
	 * @return Shared exposed spatializer. Possibly
	 * UniversalSpatializer, but never null.
	 */
    public Spatializer getLocator();
}
