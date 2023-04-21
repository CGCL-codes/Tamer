package fr.inria.zvtm.glyphs.projection;

/**project coordinates of a triangle
 * @author Emmanuel Pietriga
 */
public class ProjTriangle extends BProjectedCoordsP {

    /**length of half an edge  (in camera space) for efficiency*/
    public int halfEdge;

    /**length of a third of height  (in camera space) for efficiency*/
    public int thirdHeight;

    /**length of half an edge  (in camera space) for efficiency*/
    public int lhalfEdge;

    /**length of a third of height  (in camera space) for efficiency*/
    public int lthirdHeight;
}
