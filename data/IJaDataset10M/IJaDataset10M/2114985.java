package com.dukesoftware.utils.java3d.model3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import com.dukesoftware.utils.java3d.Java3DUtils;
import com.sun.j3d.utils.geometry.Cylinder;

/**
 * 
 * 
 * 
 *
 *
 *
 */
public class RobovieTruck extends TransformGroupCombination {

    private final Material mat = new Material(Java3DUtils.GRAY3F, Java3DUtils.BLACK3F, Java3DUtils.GRAY3F, Java3DUtils.WHITE3F, 64);

    private final Material mat2 = new Material(Java3DUtils.DARK_RED3F, Java3DUtils.BLACK3F, Java3DUtils.GRAY3F, Java3DUtils.DARK_RED3F, 64);

    public RobovieTruck() {
        super(null);
        Appearance app = new Appearance();
        mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
        app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        app.setMaterial(mat);
        Cylinder truck1 = new Cylinder(4.0f, 0.3f, app);
        setGeometry(truck1);
        TransformGroup tg_tr = new TransformGroup();
        tg_tr.setTransform(Java3DUtils.getTrans3D(0.0f, 4.15f, 0.0f));
        tg_tr.addChild(truck1);
        addChild(tg_tr);
        Appearance app_red = new Appearance();
        mat2.setCapability(Material.ALLOW_COMPONENT_WRITE);
        app_red.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        app_red.setMaterial(mat2);
        Cylinder truck2 = new Cylinder(3.0f, 4.0f, app_red);
        setGeometry(truck2);
        TransformGroup tg_tr2 = new TransformGroup();
        tg_tr2.setTransform(Java3DUtils.getTrans3D(0.0f, 2.15f, 0.0f));
        tg_tr2.addChild(truck2);
        addChild(tg_tr2);
        Cylinder truck3 = new Cylinder(4.0f, 0.3f, app_red);
        setGeometry(truck3);
        addChild(truck3);
    }

    public void initializeColor() {
        mat.setAmbientColor(Java3DUtils.GRAY3F);
        mat.setEmissiveColor(Java3DUtils.BLACK3F);
        mat.setDiffuseColor(Java3DUtils.GRAY3F);
        mat.setSpecularColor(Java3DUtils.WHITE3F);
        mat2.setAmbientColor(Java3DUtils.DARK_RED3F);
        mat2.setEmissiveColor(Java3DUtils.BLACK3F);
        mat2.setDiffuseColor(Java3DUtils.GRAY3F);
        mat2.setSpecularColor(Java3DUtils.DARK_RED3F);
    }

    public void setColor(Color3f col) {
        mat.setAmbientColor(col);
        mat.setDiffuseColor(col);
        mat.setSpecularColor(Java3DUtils.WHITE3F);
        mat2.setAmbientColor(col);
        mat2.setDiffuseColor(col);
        mat2.setSpecularColor(Java3DUtils.WHITE3F);
    }
}
