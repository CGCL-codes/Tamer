package net.claribole.zvtm.glyphs.projection;

import java.awt.Polygon;

/**project coordinates of a slice
 * @author Emmanuel Pietriga
 */
public class ProjSlice extends ProjectedCoords {

    public Polygon boundingPolygon;

    public int outerCircleRadius;

    public int p1x, p1y, p2x, p2y;

    public Polygon lboundingPolygon;

    public int louterCircleRadius;

    public int lp1x, lp1y, lp2x, lp2y;
}
