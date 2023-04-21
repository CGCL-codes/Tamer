package visolate.simulator;

import java.util.*;

public class LineCenterPrimitive extends MacroPrimitive {

    private static final String cvsid = "$Id: LineCenterPrimitive.java,v 1.1.1.1 2004/06/24 05:46:01 vona Exp $";

    public LineCenterPrimitive(final List<MacroExpression> exprs) {
        super(exprs);
    }

    public PrimitiveInstance getInstanceInternal(final List<Double> actuals) {
        int i = 0;
        int exposure = (int) getParam(i++, actuals);
        if (exposure != EXPOSURE_ON) return null;
        double width = getParam(i++, actuals);
        double height = getParam(i++, actuals);
        double xCenter = getParam(i++, actuals);
        double yCenter = getParam(i++, actuals);
        double rotation = getParam(i++, actuals);
        return new LineCenterInstance(width, height, xCenter, yCenter, rotation);
    }

    public String getName() {
        return "line (center)";
    }
}
