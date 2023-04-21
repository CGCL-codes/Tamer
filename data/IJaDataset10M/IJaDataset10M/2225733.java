package org.kaintoch.gps.gpx.gui;

import org.kaintoch.gps.gpx.GpxPoint;
import org.kaintoch.gps.gpx.GpxTrack;

/**
 * @author stefan
 *
 */
public interface ICoordListener {

    public void gotoCoord(GpxPoint pnt, GpxTrack trk);
}
