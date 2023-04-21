package com.dukesoftware.utils.java3d.model3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import com.dukesoftware.utils.java3d.Java3DUtils;
import com.sun.j3d.utils.geometry.Box;

/**
 * 
 * 
 * 
 *
 *
 *
 */
public class RobovieBody extends TransformGroupCombination {

    private Material mat = new Material(Java3DUtils.BLACK3F, Java3DUtils.BLACK3F, Java3DUtils.GRAY3F, Java3DUtils.WHITE3F, 64);

    public RobovieBody() {
        super(null);
        mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
        Appearance app2 = new Appearance();
        app2.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        app2.setMaterial(mat);
        Box box = new Box(3.0f, 6.5f, 2.0f, app2);
        setGeometry(box);
        addChild(box);
        Box body_up = new Box(4.0f, 2.0f, 2.5f, app2);
        setGeometry(body_up);
        TransformGroup tg = new TransformGroup();
        tg.setTransform(Java3DUtils.getTrans3D(0.0f, 3.0f, 0.0f));
        tg.addChild(body_up);
        addChild(tg);
    }

    public void initializeColor() {
        mat.setAmbientColor(Java3DUtils.BLACK3F);
        mat.setEmissiveColor(Java3DUtils.BLACK3F);
        mat.setDiffuseColor(Java3DUtils.GRAY3F);
        mat.setSpecularColor(Java3DUtils.WHITE3F);
    }

    public void setColor(Color3f col) {
        mat.setAmbientColor(col);
        mat.setDiffuseColor(col);
        mat.setSpecularColor(Java3DUtils.WHITE3F);
    }
}
