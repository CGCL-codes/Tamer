package jpicedt.format.input.pstricks;

import jpicedt.format.input.util.*;
import jpicedt.graphic.model.*;
import jpicedt.graphic.PicPoint;
import static jpicedt.graphic.model.PicAttributeName.*;
import static jpicedt.graphic.model.StyleConstants.*;
import static jpicedt.format.input.util.ExpressionConstants.*;

/**
 * Quick circle :<ul>
 * <li>\\pscircle[parameters](x0,y0){rad}
 * <li>\\pscircle*[parameters](x0,y0){rad}
 * </ul>
 * @author Sylvain Reynal
 * @since jpicedt 1.3.1
 * @version $Id: PsCircleExpression.java,v 1.8 2011/07/23 05:15:29 vincentb1 Exp $
 * <p>
 * 
 */
public class PsCircleExpression extends SequenceExpression {

    private Pool pool;

    /**
	 * Uses default tag.
	 */
    public PsCircleExpression(Pool pl) {
        this(pl, null);
    }

    /**
	 * @param tag if null, default to \\pscircle
	 */
    public PsCircleExpression(Pool pl, String tag) {
        super(true);
        pool = pl;
        if (tag == null) tag = "\\pscircle";
        add(new PSTInstanciationExpression(tag, new PicEllipse(), pool));
        add(WHITE_SPACES_OR_EOL);
        add(new OptionalExpression(new StarExpression(pool)));
        add(WHITE_SPACES_OR_EOL);
        add(new OptionalExpression(new EnclosingExpression("[", new PSTParametersExpression(pool, Pool.CURRENT_OBJ_ATTRIBUTES), "]")));
        add(WHITE_SPACES_OR_EOL);
        add(new PSTPicPointExpression(PicEllipse.P_CENTER, pool));
        add(WHITE_SPACES_OR_EOL);
        add(new LiteralExpression("{"));
        add(WHITE_SPACES_OR_EOL);
        add(new NumericalExpression(DOUBLE, POSITIVE, "}", true) {

            public void action(ParserEvent e) {
                if (DEBUG) System.out.println(e);
                double radius = ((Double) e.getValue()).doubleValue() * pool.get(PstricksParser.KEY_R_UNIT);
                PicPoint pBL = pool.currentObj.getCtrlPt(PicEllipse.P_CENTER, null);
                PicPoint pTR = new PicPoint(pBL);
                pBL.translate(-radius, -radius);
                pTR.translate(radius, radius);
                ((PicEllipse) (pool.currentObj)).setCtrlPt(PicEllipse.P_BL, pBL, null);
                ((PicEllipse) (pool.currentObj)).setCtrlPt(PicEllipse.P_TR, pTR, null);
            }
        });
    }

    public String toString() {
        return "[PsCircleExpression]";
    }
}
