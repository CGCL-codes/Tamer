package org.axsl.fo.fo;

import org.axsl.fo.Fo;

/**
 * Super-interface for formatting objects that have no content of their own, but
 * which have other content grafted onto them, that is, {@link RetrieveMarker}
 * and {@link RetrieveTableMarker}.
 */
public interface GraftingPoint extends Fo {
}
