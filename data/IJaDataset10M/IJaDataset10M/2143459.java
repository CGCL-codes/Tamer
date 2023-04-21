package edu.nps.moves.kmleditor.palette.items;

import static edu.nps.moves.kmleditor.KmlConstants.*;
import edu.nps.moves.kmleditor.palette.BaseCustomizer;
import edu.nps.moves.kmleditor.types.panels.GroundOverlayTypePanel;
import org.jdom.Element;

/**
 * GroundOverlay.java
 * Created on Feb 11, 2010
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey / jmbailey@nps.edu
 * @version $Id$
 */
public class GroundOverlay extends AbstractOverlay {

    public String altitude;

    public String altitudeMode;

    public LatLonBox llbox;

    public String altitudeDefault = "";

    public String altitudeModeDefault = "clampToGround";

    public GroundOverlay() {
        super();
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return GroundOverlayTypePanel.class;
    }

    @Override
    public void initializeEmpty() {
        super.initializeEmpty();
        altitudeMode = altitudeModeDefault;
        altitude = altitudeDefault;
        llbox = new LatLonBox();
        llbox.initializeEmpty();
    }

    @Override
    public void initializeFromJdom(Element mom) {
        initializeEmpty();
        super.initializeFromJdom(mom);
        Element e = null;
        e = mom.getChild("altitude", KML_NSO);
        if (e != null) altitude = e.getText();
        e = mom.getChild("altitudeMode", KML_NSO);
        if (e != null) altitudeMode = e.getText();
        e = mom.getChild("LatLonBox", KML_NSO);
        if (e != null) llbox.initializeFromJdom(e);
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
    }

    @Override
    public void updateContent(ContentWriter cw) {
        if (cw == null) cw = new ContentWriter(getJdomElement());
        super.updateContent(cw);
        cw.handleSimpleElement("altitude", (!altitude.equals(altitudeDefault)), altitude);
        cw.handleSimpleElement("altitudeMode", (!altitudeMode.equals(altitudeModeDefault)), altitudeMode);
        cw.handleComplexElement(llbox);
        cw.finalizeContent();
    }
}
