package org.xith3d.utility.sgtree.infoitems;

import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import org.xith3d.scenegraph.PointLight;

/**
 * Xith3DTree
 * 
 * Display a Xith3D scenegraph in a Swing Tree control
 * 
 * @author Daniel Selman (Java3D version)
 * @author Hawkwind
 * @author Amos Wenger (aka BlueSky)
 */
public class SGTInfo_PointLight extends SGTInfo_Light {

    private static final int[] m_kCapabilityArray = {};

    public SGTInfo_PointLight() {
    }

    @Override
    public int[] getCapabilityBits() {
        return createCompoundArray(m_kCapabilityArray, super.getCapabilityBits());
    }

    @Override
    public String getInfo(Object obj) {
        String szText = super.getInfo(obj);
        szText = insertSectionBreak(szText);
        szText += "PointLight\r\n";
        PointLight pointLight = (PointLight) obj;
        Tuple3f attenuation = pointLight.getAttenuation();
        szText += "Attenuation: " + attenuation + "\r\n";
        Point3f position = pointLight.getLocation();
        szText += "Position: " + position + "\r\n";
        return szText;
    }
}
