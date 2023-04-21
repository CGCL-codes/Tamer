package visolate.simulator;

import java.util.*;
import javax.vecmath.*;

public class OutlinePrimitive extends MacroPrimitive {

    private static final String cvsid = "$Id: OutlinePrimitive.java,v 1.1.1.1 2004/06/24 05:46:01 vona Exp $";

    public OutlinePrimitive(List<MacroExpression> exprs) {
        super(exprs);
    }

    @Override
    protected PrimitiveInstance getInstanceInternal(List<Double> actuals) {
        int i = 0;
        int exposure = (int) getParam(i++, actuals);
        if (exposure != EXPOSURE_ON) {
            return null;
        }
        int n = (int) getParam(i++, actuals);
        List<Point2d> pts = new LinkedList<Point2d>();
        while (i < (actuals.size() - 1)) {
            pts.add(new Point2d(getParam(i++, actuals), getParam(i++, actuals)));
        }
        double rotation = getParam(i++, actuals);
        return new OutlineInstance(n, pts, rotation);
    }

    protected String getName() {
        return "outline";
    }
}
