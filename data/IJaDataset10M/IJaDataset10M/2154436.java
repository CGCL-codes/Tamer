package org.xith3d.utility.sgtree.infoitems;

/**
 * Xith3DTree
 * 
 * Display a Xith3D scenegraph in a Swing Tree control
 * 
 * @author Daniel Selman (Java3D version)
 * @author Hawkwind
 * @author Amos Wenger (aka BlueSky)
 */
public class SGTInfo_Interpolator extends SGTInfo_Behavior {

    public SGTInfo_Interpolator() {
    }

    @Override
    java.util.Enumeration getChildren(Object obj) {
        return null;
    }

    @Override
    public int[] getCapabilityBits() {
        return super.getCapabilityBits();
    }

    @Override
    public String getInfo(Object obj) {
        String szText = super.getInfo(obj);
        szText = insertSectionBreak(szText);
        szText += "Interpolator\r\n";
        return szText;
    }
}
