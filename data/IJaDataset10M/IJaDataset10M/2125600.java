package jpicedt.format.input.pstricks;

import jpicedt.format.input.util.*;
import jpicedt.graphic.model.*;
import jpicedt.graphic.PicPoint;
import static jpicedt.format.input.util.ExpressionConstants.*;
import static jpicedt.graphic.model.PicAttributeName.*;
import static jpicedt.graphic.model.StyleConstants.*;

/**
 * <ul>
 * <li>\\psframe[parameters](x0,y0)(x1,y1)
 * <li>\\psframe*[parameters](x0,y0)(x1,y1)
 * </ul>
 * @author Sylvain Reynal
 * @version $Id: PsFrameExpression.java,v 1.7 2011/07/23 05:15:39 vincentb1 Exp $ 
 */
public class PsFrameExpression extends SequenceExpression {

    private Pool pool;

    /**
	 * Uses default tag.
	 */
    public PsFrameExpression(Pool pl) {
        this(pl, null);
    }

    /**
	 * @param tag if null, default to "\\psframe"
	 */
    public PsFrameExpression(Pool pl, String tag) {
        super(true);
        pool = pl;
        if (tag == null) tag = "\\psframe";
        add(new PSTInstanciationExpression(tag, new PicParallelogram(), pool));
        add(WHITE_SPACES_OR_EOL);
        add(new OptionalExpression(new LiteralExpression("*") {

            public void action(ParserEvent e) {
                if (DEBUG) System.out.println(e);
                pool.currentObj.setAttribute(FILL_STYLE, FillStyle.SOLID);
            }
        }));
        add(WHITE_SPACES_OR_EOL);
        add(new OptionalExpression(new EnclosingExpression("[", new PSTParametersExpression(pool, Pool.CURRENT_OBJ_ATTRIBUTES), "]")));
        add(WHITE_SPACES_OR_EOL);
        add(new PSTPicPointExpression(PicParallelogram.P_BL, pool));
        add(WHITE_SPACES_OR_EOL);
        add(new PSTPicPointExpression(PicParallelogram.P_TR, pool));
    }

    public String toString() {
        return "[PsFrameExpression]";
    }
}
