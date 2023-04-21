package jpicedt.format.input.latex;

import jpicedt.format.input.util.*;
import jpicedt.graphic.model.*;
import static jpicedt.format.input.util.ExpressionConstants.*;
import static jpicedt.graphic.model.PicAttributeName.*;
import static jpicedt.graphic.model.StyleConstants.*;

/**
 * "0/1 0/1" arrow type (we take for granted that this string is followed by a LaTeX-picPoint, as in
 * %Line 0 1 (2.3,4.5)..., possibly with leading whitespaces)
 * @author Sylvain Reynal
 * @since jpicedt 1.3
 * @version $Id: PicArrowTypeExpression.java,v 1.8 2011/07/23 05:14:04 vincentb1 Exp $
 *
 */
public class PicArrowTypeExpression extends WordExpression {

    private Pool pool;

    public PicArrowTypeExpression(Pool pl) {
        super("(", false);
        pool = pl;
    }

    public void action(ParserEvent e) {
        if (DEBUG) System.out.println(e);
        String s = (String) e.getValue();
        if (s.startsWith("0 1")) {
            pool.currentObj.setAttribute(LEFT_ARROW, ArrowStyle.NONE);
            pool.currentObj.setAttribute(RIGHT_ARROW, ArrowStyle.ARROW_HEAD);
            return;
        } else if (s.startsWith("1 0")) {
            pool.currentObj.setAttribute(LEFT_ARROW, ArrowStyle.ARROW_HEAD);
            pool.currentObj.setAttribute(RIGHT_ARROW, ArrowStyle.NONE);
            return;
        } else if (s.startsWith("1 1")) {
            pool.currentObj.setAttribute(LEFT_ARROW, ArrowStyle.ARROW_HEAD);
            pool.currentObj.setAttribute(RIGHT_ARROW, ArrowStyle.ARROW_HEAD);
            return;
        } else {
            pool.currentObj.setAttribute(LEFT_ARROW, ArrowStyle.NONE);
            pool.currentObj.setAttribute(RIGHT_ARROW, ArrowStyle.NONE);
        }
    }
}
