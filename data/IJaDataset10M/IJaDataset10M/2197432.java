package GShape.Core.Commands;

import java.util.Vector;
import GShape.Core.GCore;
import GShape.Core.Expression.GExpression;
import GShape.Core.Expression.GExpressionItem;
import GShape.Core.Objects.iGObject;
import GShape.Core.Objects.PDisplay.PRect;
import GShape.Core.Objects.Primitives.iPrimitive;
import GShape.Core.Objects.Primitives.rect;

public class MultiplyX implements iCommand {

    @Override
    public void PerformRule(GCore core, Vector<iGObject> source, GExpression exp) throws Exception {
        MultiplyXY(core, source, exp, true);
    }

    public static void MultiplyXY(GCore core, Vector<iGObject> source, GExpression exp, boolean directionX) throws Exception {
        for (iGObject src : source) {
            Object base = src.GetPrimitive(rect.class);
            if (base != null) {
                rect oRect = (rect) base;
                for (int i = 0; i < exp.Items.size(); i++) {
                    GExpressionItem item = exp.Items.get(i);
                    if (item.isNumber) {
                        for (int ii = 0; ii < item.getNumber(0, 0) + 1; ii++) {
                            float offset, width;
                            rect newRect = null;
                            if (directionX) {
                                offset = oRect.width * ii;
                                width = offset + oRect.width;
                                newRect = new rect(offset, oRect.p1.y, width, oRect.p2.y, oRect.z);
                            }
                            if (!directionX) {
                                offset = oRect.depth * ii;
                                width = offset + oRect.depth;
                                newRect = new rect(oRect.p1.y, offset, oRect.p2.x, width, oRect.z);
                            }
                            new PRect(core, item.getString(), newRect, src);
                        }
                    }
                }
            } else System.out.println("Split X: Base is not a rect!");
            src.Delete();
        }
    }

    /**
	 * 
	 *  GetSupportedPrimitives
	 *  
	 *  Example primitive that can be used to perform rules off this command.
	 *  
	 */
    @Override
    public Vector<Class> GetSupportedPrimitives() {
        Vector<Class> primitives = new Vector<Class>();
        primitives.add(rect.class);
        return primitives;
    }
}
