package de.grogra.ext.x3d.importation;

import java.util.HashMap;
import de.grogra.ext.x3d.ProtoInstanceImport;
import de.grogra.ext.x3d.X3DImport;
import de.grogra.ext.x3d.xmlbeans.Circle2DDocument.Circle2D;
import de.grogra.graph.EdgePatternImpl;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.ColoredNull;
import de.grogra.imp3d.objects.NURBSCurve;
import de.grogra.imp3d.objects.ShadedNull;
import de.grogra.imp3d.shading.Phong;
import de.grogra.imp3d.shading.Shader;
import de.grogra.math.ChannelMap;
import de.grogra.math.Circle;
import de.grogra.math.RGBColor;

public class Circle2DImport {

    public static ColoredNull createInstance(Circle2D circle, Node parent) {
        NURBSCurve n = null;
        HashMap<String, Object> referenceMap = X3DImport.getTheImport().getCurrentParser().getReferenceMap();
        if (circle.isSetUSE()) {
            try {
                n = (NURBSCurve) ((Node) referenceMap.get(circle.getUSE())).cloneGraph(EdgePatternImpl.TREE, true);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            n = new NURBSCurve();
            HashMap<String, String> finalValueMap = new HashMap<String, String>();
            if (circle.isSetIS()) {
                ProtoInstanceImport.calcFinalValueMap(finalValueMap, circle);
            }
            setValues(n, circle, finalValueMap, parent);
            if (circle.isSetDEF()) {
                referenceMap.put(circle.getDEF(), n);
            }
        }
        n.setName("X3DCircle2D");
        return n;
    }

    private static void setValues(NURBSCurve node, Circle2D circle, HashMap<String, String> valueMap, Node parent) {
        float radius;
        radius = valueMap.containsKey("radius") ? Float.valueOf(valueMap.get("radius")) : circle.isSetRadius() ? circle.getRadius() : 1f;
        RGBColor c = new RGBColor(0, 0, 0);
        if (parent instanceof ShadedNull) {
            Shader s = ((ShadedNull) parent).getShader();
            if (s instanceof Phong) {
                ChannelMap cm = ((Phong) s).getEmissive();
                if (cm instanceof RGBColor) {
                    c = (RGBColor) cm;
                }
            }
        }
        Circle ci = new Circle(radius);
        ci.setPlane(1);
        node.setCurve(ci);
        node.setColor(c);
    }
}
