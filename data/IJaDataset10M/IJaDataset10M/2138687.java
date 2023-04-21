package org.gvsig.symbology.fmap.labeling.placements;

import com.iver.cit.gvsig.fmap.rendering.styling.labeling.IPlacementConstraints;
import com.iver.utiles.XMLEntity;

/**
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class LinePlacementConstraints extends AbstractPlacementConstraints {

    public LinePlacementConstraints() {
        super();
        setPlacementMode(PARALLEL);
        setLocationAlongTheLine(IPlacementConstraints.AT_THE_MIDDLE_OF_THE_LINE);
        setPageOriented(false);
    }

    public String getClassName() {
        return getClass().getName();
    }

    public XMLEntity getXMLEntity() {
        XMLEntity xml = super.getXMLEntity();
        xml.putProperty("className", getClassName());
        return xml;
    }
}
