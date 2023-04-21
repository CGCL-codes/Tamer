package org.axsl.area;

import org.axsl.fo.fo.Marker;
import org.axsl.fo.fo.RetrieveMarker;
import org.axsl.fo.fo.SimplePageMaster;
import java.util.Collection;

/**
 * Area for a page.
 */
public interface PageArea {

    /**
     * Returns the parent PageCollection instance.
     * @return The parent PageCollection.
     */
    PageCollection getPageCollection();

    /**
     * Returns page master from which this page was generated.
     * @return The page master from which this page was generated.
     */
    SimplePageMaster getPageMaster();

    /**
     * Returns the region-after area.
     * @return The region-after area.
     */
    RegionRefArea getRegionAfter();

    /**
     * Returns the region-before area.
     * @return The region-before area.
     */
    RegionRefArea getRegionBefore();

    /**
     * Returns the region-start area.
     * @return The region-start area.
     */
    RegionRefArea getRegionStart();

    /**
     * Returns the region-end area.
     * @return The region-end area.
     */
    RegionRefArea getRegionEnd();

    /**
     * Returns the region-body areas as a Collection.
     * @return The region-body areas as a Collection.
     */
    Collection<? extends RegionBodyRefArea> getRegionBodies();

    /**
     * Returns a region-body for this page by name.
     * @param regionName The name of the region-body instance to return.
     * @return The names region-body area, or null if it does not exist.
     */
    RegionBodyRefArea getRegionBody(String regionName);

    /**
     * Returns the raw page number for this page.
     * @return The raw page number.
     */
    int getNumber();

    /**
     * Allows a layout system to notify the page that it is done adding content
     * to it.
     * @throws AreaTreeException For layout errors that cannot be resolved
     * by the AreaTree.
     */
    void layoutComplete() throws AreaTreeException;

    /**
     * Places a {@link Marker} instance on this page.
     * @param marker The marker to be associated with this page.
     */
    void registerMarker(Marker marker);

    /**
     * Provides the marker instance best fitting the requirements of a given
     * retrieve-marker.
     * @param retrieveMarker The retrieve-marker that is looking for marker
     * content by which it can be replaced.
     * @return The marker instance best fitting the requirements of
     * {@code retrieveMarker}
     */
    Marker bestMarker(RetrieveMarker retrieveMarker);
}
