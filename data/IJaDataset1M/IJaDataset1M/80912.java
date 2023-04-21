package de.grogra.ext.x3d.io;

import java.io.IOException;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import de.grogra.ext.x3d.Util;
import de.grogra.ext.x3d.X3DExport;
import de.grogra.ext.x3d.objects.X3DSpotLight;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.SpotLight;
import de.grogra.imp3d.objects.SceneTree.Leaf;

/**
 * Used to import and export a spot light.
 * 
 * @author Udo Bischof, Uwe Mannl
 *
 */
public class X3DSpotLightIO extends X3DLightIO {

    @Override
    protected Node doImportImpl(Attributes atts) {
        X3DSpotLight newSpotLight = new X3DSpotLight();
        String valueString;
        valueString = atts.getValue("ambientIntensity");
        if (valueString != null) newSpotLight.setX3dAmbientIntensity(Float.valueOf(valueString));
        valueString = atts.getValue("attenuation");
        if (valueString != null) newSpotLight.setX3dAttenuation(Util.splitStringToTuple3f(new Vector3f(), valueString));
        valueString = atts.getValue("beamWidth");
        if (valueString != null) newSpotLight.setX3dBeamWidth(Float.valueOf(valueString));
        valueString = atts.getValue("color");
        if (valueString != null) newSpotLight.setX3dColor(Util.splitStringToTuple3f(new Color3f(), valueString));
        valueString = atts.getValue("cutOffAngle");
        if (valueString != null) newSpotLight.setX3dCutOffAngle(Float.valueOf(valueString));
        valueString = atts.getValue("direction");
        if (valueString != null) newSpotLight.setX3dDirection(Util.splitStringToTuple3f(new Vector3f(), valueString));
        valueString = atts.getValue("intensity");
        if (valueString != null) newSpotLight.setX3dIntensity(Float.valueOf(valueString));
        valueString = atts.getValue("location");
        if (valueString != null) newSpotLight.setX3dLocation(Util.splitStringToTuple3f(new Point3f(), valueString));
        valueString = atts.getValue("on");
        if (valueString != null) newSpotLight.setX3dOn(Boolean.valueOf(valueString));
        valueString = atts.getValue("radius");
        if (valueString != null) newSpotLight.setX3dRadius(Float.valueOf(valueString));
        return newSpotLight;
    }

    protected static void exportSpotLightImpl(Leaf node, SpotLight light, X3DExport export, Element parentElement) throws IOException {
        Element spotLightElement = export.getDoc().createElement("SpotLight");
        X3DSpotLight defaultSpotLight = new X3DSpotLight();
        Tuple3f color = light.getColor();
        if (!color.equals(defaultSpotLight.getX3dColor())) spotLightElement.setAttribute("color", color.x + " " + color.y + " " + color.z);
        Vector3d dirVec = new Vector3d(0, 0, 1);
        if (!dirVec.equals(defaultSpotLight.getX3dDirection())) spotLightElement.setAttribute("direction", dirVec.x + " " + dirVec.z + " " + -dirVec.y);
        float power = light.getPower();
        if (power != defaultSpotLight.getX3dIntensity()) spotLightElement.setAttribute("intensity", String.valueOf(power / 100f));
        float innerAngle = light.getInnerAngle();
        float outerAngle = light.getOuterAngle();
        if (innerAngle != defaultSpotLight.getX3dBeamWidth()) spotLightElement.setAttribute("beamWidth", String.valueOf(innerAngle));
        if (outerAngle != defaultSpotLight.getX3dCutOffAngle()) spotLightElement.setAttribute("cutOffAngle", String.valueOf(outerAngle));
        if (node.object instanceof X3DSpotLight) {
            float ambientIntensity = node.getFloat(de.grogra.ext.x3d.Attributes.X3DAMBIENT_INTENSITY);
            if (ambientIntensity != defaultSpotLight.getX3dAmbientIntensity()) spotLightElement.setAttribute("ambientIntensity", String.valueOf(ambientIntensity));
            boolean on = node.getBoolean(de.grogra.ext.x3d.Attributes.X3DON);
            if (on != defaultSpotLight.isX3dOn()) spotLightElement.setAttribute("on", String.valueOf(on));
            float radius = node.getFloat(de.grogra.ext.x3d.Attributes.X3DRADIUS);
            if (radius != defaultSpotLight.getX3dRadius()) spotLightElement.setAttribute("radius", String.valueOf(radius));
        }
        parentElement.appendChild(spotLightElement);
    }
}
