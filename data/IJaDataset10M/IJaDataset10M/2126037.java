package game.gui.structure3d.j3d;

import java.awt.Color;
import javax.media.j3d.*;
import javax.vecmath.Color3f;

public abstract class GOB3D {

    int id;

    int type;

    boolean hide;

    Color col;

    float transparency;

    boolean transparent;

    TransformGroup tg;

    Appearance ap;

    private Material mat;

    protected JKCanvasJ3D canvas3D;

    GOB3D(JKCanvasJ3D canvas3D) {
        tg = new TransformGroup();
        ap = new Appearance();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        float r = canvas3D.actColor.getRed() / 255.0f, g = canvas3D.actColor.getGreen() / 255.0f, b = canvas3D.actColor.getBlue() / 255.0f;
        mat = new Material(new Color3f(0.2f * r, 0.2f * g, 0.2f * b), new Color3f(0, 0, 0), new Color3f(r, g, b), new Color3f(r, g, b), 64.0f);
        ap.setMaterial(mat);
        ap.getMaterial().setCapability(Material.ALLOW_COMPONENT_READ);
        ap.getMaterial().setCapability(Material.ALLOW_COMPONENT_WRITE);
    }

    /**
     * This method must be in all descendants.
     * @param obg
     */
    public abstract void draw(Group obg);

    /**
     * Set new color. Doesn't need to redraw.
     * @param ocol new color
     */
    public void setColor(Color ocol) {
        float r = ocol.getRed() / 255.0f, g = ocol.getGreen() / 255.0f, b = ocol.getBlue() / 255.0f;
        mat.setAmbientColor(0.2f * r, 0.2f * g, 0.2f * b);
        mat.setDiffuseColor(r, g, b);
        mat.setSpecularColor(r, g, b);
    }
}
