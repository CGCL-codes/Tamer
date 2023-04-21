package fr.inria.zvtm.glyphs.projection;

import java.awt.geom.Area;

/**project coordinates of a boolean shape
 * @author Emmanuel Pietriga
 */
public class ProjBoolean extends ProjectedCoords {

    /**main shape*/
    public Area mainArea;

    /**main shape size in camera space*/
    public float cszx, cszy;

    /**main shape in lens space*/
    public Area lmainArea;

    /**main shape size in lens space*/
    public float lcszx, lcszy;
}
