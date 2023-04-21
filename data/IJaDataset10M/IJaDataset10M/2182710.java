package org.axsl.areaW;

import org.axsl.foR.fo.ListItem;
import org.axsl.foR.fo.RetrieveMarker;

/**
 * Area for a list-block.
 */
public interface ListBlockArea extends AbstractNormalBlockArea {

    /**
     * Factory method for creation of ListItemArea instances.
     * @param listItem The FOTree ListItem that is generating the new
     * ListItemArea.
     * @param retrieveMarker The RetrieveMarker instance, if any, which controls
     * this Area.
     * @return A new child ListItemArea instance.
     */
    ListItemArea makeListItemArea(ListItem listItem, RetrieveMarker retrieveMarker);
}
