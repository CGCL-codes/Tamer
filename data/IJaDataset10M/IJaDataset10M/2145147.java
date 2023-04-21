package GShape.Core.Commands;

import java.util.Vector;
import processing.core.PApplet;
import GShape.Core.GCore;
import GShape.Core.Expression.GExpression;
import GShape.Core.Objects.iGObject;
import GShape.Core.Objects.PDisplay.*;
import GShape.Core.Objects.Primitives.*;

public class Rotate implements iCommand {

    public void PerformRule(GCore core, Vector<iGObject> source, GExpression exp) throws Exception {
        for (iGObject src : source) {
            Object base = src.GetPrimitive(vrect.class);
            if (base != null) {
                vrect me = (vrect) base;
                float angleDegree = (float) 0;
                String Name = "";
                if (exp.Items.size() == 1) {
                    angleDegree = exp.Items.get(0).getNumber(0, 0);
                    Name = exp.Items.get(0).getString();
                }
                new PVRect(core, Name, new vrect(me.p1, 0, me.distance, me.angle + PApplet.radians(angleDegree), me.z, me.height), src);
                src.Delete();
            } else System.out.println("Rotate: Base is not a valid geometry (vrect,...)!");
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
