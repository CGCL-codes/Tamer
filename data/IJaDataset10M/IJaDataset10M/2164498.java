package view;

import com.mxgraph.util.mxConstants;

public class StyleConst {

    public static final String cardConcept = "strokeColor=#424BD4;fillColor=#424BD4;fontColor=#000000;shape=rectangle;fontStyle=" + mxConstants.FONT_BOLD;

    /**@comment Noeud dans l'ontologie 
	 */
    public static final String ontoConcept = "fillColor=#E860C6;fontColor=#000000;shape=rectangle;fontStyle=" + mxConstants.FONT_BOLD;

    /**@comment Noeud dans la carte cognitive 
	 */
    public static final String cstConcept = "fillColor=#60E898;fontColor=#000000;shape=rectangle;fontStyle=" + mxConstants.FONT_BOLD;

    /**@comment Noeud appartenant a un cycle
	 */
    public static final String cyclicConcept = "fillColor=#FF0000;fontColor=#000000;shape=ellipse;fontStyle=" + mxConstants.FONT_BOLD;

    /**@comment Noeud isol�
	 */
    public static final String isolatedConcept = "fillColor=#FF0000;fontColor=#000000;shape=rhombus;fontStyle=" + mxConstants.FONT_BOLD;

    /**@comment Noeud sur lequel on a chang� une contrainte
	 */
    public static final String changedConcept = "fillColor=#FF0000;fontColor=#000000";

    /**@comment Lien repr�sentant la hierarchie dans l'ontologie
	 */
    public static final String hierarchicalLink = "strokeColor=#7E8CD0;strokeWidth=3";

    /**@comment Lien de valuations entre deux concept dans la carte cognitive
	 * si la valeure est positive
	 */
    public static final String cardValuationLink = "strokeColor=#55EC5A;strokeWidth=2";

    /**@comment Lien de valuations entre deux concept dans la carte cognitive
	 * si la valeure est n�gative
	 */
    public static final String cstLink = "strokeColor=#FF2C00;strokeWidth=2";

    /**@comment un lien violantu une contrainte portant sur sa valeure
	 */
    public static final String violatedValuationLink = "strokeColor=#FF2C00;strokeWidth=4";

    /**@comment La contrainte viol�e porte sur l'abscence la pr�sence ou le sens du lien, pas sa valeure
	 */
    public static final String violatedShapeLink = "strokeColor=#FF2C00;strokeWidth=12";

    /**@comment Invisibilit�
	 */
    public static final String invisibleNode = "opacity=0;fontColor=#FFFFFF";
}
