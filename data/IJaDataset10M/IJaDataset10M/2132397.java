package com.bluebrim.layout.impl.server.manager;

import org.w3c.dom.Node;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.impl.shared.CoNoLocationIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

public class CoNoLocation extends CoLocationSpec implements CoNoLocationIF {

    public static final String XML_TAG = "no-location";

    public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        return new CoNoLocation();
    }

    CoNoLocation() {
    }

    public boolean equals(Object o) {
        return (this == o) || (o instanceof CoNoLocation) || super.equals(o);
    }

    public String getFactoryKey() {
        return NO_LOCATION;
    }

    public double getHeightAfterLocation(CoLayoutableIF layoutable) {
        CoProfile tAnchorProfile = CoProfile.createTopProfileForHeightDetermination(layoutable);
        CoProfile tTargetProfile = CoProfile.createBottomProfileForHeightDetermination(layoutable);
        double tLayoutAreaHeight = CoLayoutManagerUtilities.getHeightFromTopToBottomMargin(layoutable.getLayoutParent());
        double tHeightAfterLocation = tTargetProfile.getLowestY() - tAnchorProfile.getHighestY();
        layoutable.setLayoutY(tAnchorProfile.getHighestY());
        if (tHeightAfterLocation == 0) return tLayoutAreaHeight;
        if (tHeightAfterLocation < tLayoutAreaHeight) return tHeightAfterLocation; else return tLayoutAreaHeight;
    }

    public String getIconName() {
        return "CoNoLocation.gif";
    }

    public static CoNoLocation getInstance() {
        return (CoNoLocation) getFactory().getNoLocation();
    }

    public CoLocationSpecIF getLocationSpecAfterReshape(boolean dx0, boolean dy0, boolean dx1, boolean dy1) {
        return null;
    }

    public String getType() {
        return CoPageItemStringResources.getName(NO_LOCATION);
    }

    public double getWidthAfterLocation(CoLayoutableIF layoutable) {
        CoProfile tAnchorProfile = CoProfile.createLeftProfileForWidthDetermination(layoutable);
        CoProfile tTargetProfile = CoProfile.createRightProfileForWidthDetermination(layoutable);
        double tLayoutAreaWidth = CoLayoutManagerUtilities.getWidthFromLeftToRightMargin(layoutable.getLayoutParent());
        double tWidthAfterLocation = tTargetProfile.getLowestX() - tAnchorProfile.getHighestX();
        layoutable.setLayoutX(tAnchorProfile.getHighestX());
        if (tWidthAfterLocation == 0) return tLayoutAreaWidth;
        if (tWidthAfterLocation < tLayoutAreaWidth) return tWidthAfterLocation; else return tLayoutAreaWidth;
    }

    public String getXmlTag() {
        return XML_TAG;
    }

    public boolean isAbsolutePosition() {
        return true;
    }

    public boolean isNull() {
        return true;
    }

    /**
 * 	Placerar sidelementet med �vre v�nstra h�rnet i punkten x,y.
 */
    public void placeLayoutable(CoLayoutableIF layoutable) {
        layoutable.setLayoutSuccess(true);
    }
}
