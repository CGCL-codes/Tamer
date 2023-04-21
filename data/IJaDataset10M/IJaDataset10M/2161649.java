package de.grogra.ext.x3d.importation;

import java.util.HashMap;
import javax.vecmath.Vector3f;
import de.grogra.ext.x3d.ProtoInstanceImport;
import de.grogra.ext.x3d.X3DImport;
import de.grogra.ext.x3d.util.Util;
import de.grogra.ext.x3d.xmlbeans.PointLightDocument.PointLight;
import de.grogra.graph.EdgePatternImpl;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.LightNode;
import de.grogra.math.RGBColor;

public class PointLightImport {

    private static float intensityFactor = 100f;

    public static LightNode createInstance(PointLight pointLight) {
        LightNode l = null;
        HashMap<String, Object> referenceMap = X3DImport.getTheImport().getCurrentParser().getReferenceMap();
        if (pointLight.isSetUSE()) {
            try {
                l = (LightNode) ((Node) referenceMap.get(pointLight.getUSE())).cloneGraph(EdgePatternImpl.TREE, true);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            l = new LightNode();
            HashMap<String, String> finalValueMap = new HashMap<String, String>();
            if (pointLight.isSetIS()) {
                ProtoInstanceImport.calcFinalValueMap(finalValueMap, pointLight);
            }
            setValues(l, pointLight, finalValueMap);
            if (pointLight.isSetDEF()) {
                referenceMap.put(pointLight.getDEF(), l);
            }
        }
        l.setName("X3DPointLight");
        return l;
    }

    private static void setValues(LightNode node, PointLight pointLight, HashMap<String, String> valueMap) {
        de.grogra.imp3d.objects.PointLight l = new de.grogra.imp3d.objects.PointLight();
        RGBColor color = new RGBColor();
        String colorString = (valueMap.get("color") != null) ? valueMap.get("color") : pointLight.getColor();
        Util.splitStringToTuple3f(color, colorString);
        l.getColor().set(color);
        Vector3f location = new Vector3f();
        String locationString = (valueMap.get("location") != null) ? valueMap.get("location") : pointLight.getLocation();
        Util.convertStringToTuple3f(location, locationString);
        node.setTransform(location);
        float intensity;
        intensity = (valueMap.get("intensity") != null) ? Float.valueOf(valueMap.get("intensity")) : pointLight.getIntensity();
        l.setPower(intensity * intensityFactor);
        node.setLight(l);
    }
}
