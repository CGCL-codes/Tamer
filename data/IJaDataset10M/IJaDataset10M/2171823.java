package org.chernovia.net.games.arcade.cubeball;

import java.awt.Toolkit;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

public class CubeSphere {

    private Sphere shape;

    private Texture tex;

    private Appearance app;

    private TransformGroup TG;

    private Transform3D locTrans, rotTrans;

    private Point3d loc;

    private Vector3d dir, transVec;

    private AxisAngle4d rotAngle;

    private double rotSpeed = Math.PI / 12d;

    private boolean isPlayer;

    public CubeSphere(float rad, int type, String texfile) {
        isPlayer = type == CubeBallClient.PLAYER;
        shape = new Sphere(rad, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, isPlayer ? 16 : 48, null);
        app = new Appearance();
        if (isPlayer) {
            setPlayer();
            setVisCaps(false);
            tex = null;
        } else {
            TextureLoader TL = new TextureLoader(Toolkit.getDefaultToolkit().createImage(texfile), null);
            tex = TL.getTexture();
            app.setTexture(tex);
        }
        if (type == CubeBallClient.FIREBALL) {
            setVisCaps(false);
        }
        shape.setAppearance(app);
        TG = new TransformGroup();
        TG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TG.addChild(shape);
        loc = new Point3d();
        dir = new Vector3d();
        transVec = new Vector3d();
        locTrans = new Transform3D();
        rotTrans = new Transform3D();
        rotAngle = new AxisAngle4d();
    }

    private void setPlayer() {
        ColoringAttributes CA = new ColoringAttributes();
        CA.setColor(.16f + (float) (Math.random() / 3), .16f + (float) (Math.random() / 3), .16f + (float) (Math.random() / 3));
        app.setColoringAttributes(CA);
        PolygonAttributes PA = new PolygonAttributes();
        PA.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        PA.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(PA);
    }

    private void setVisCaps(boolean vis) {
        app.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
        RenderingAttributes RA = new RenderingAttributes();
        RA.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
        RA.setVisible(vis);
        app.setRenderingAttributes(RA);
    }

    public boolean isVisible() {
        return app.getRenderingAttributes().getVisible();
    }

    public void setVisible(boolean v) {
        app.getRenderingAttributes().setVisible(v);
    }

    public Texture getTex() {
        return tex;
    }

    public TransformGroup getTrans() {
        return TG;
    }

    public Point3d getLoc() {
        return loc;
    }

    public void setLoc(Point3d l) {
        transVec.set(l);
        dir.set(l);
        dir.sub(loc);
        dir.normalize();
        rotAngle.set(dir, rotSpeed);
        TG.getTransform(locTrans);
        locTrans.setTranslation(transVec);
        rotTrans.setRotation(rotAngle);
        if (!isPlayer) locTrans.mul(rotTrans);
        TG.setTransform(locTrans);
        loc.set(l);
    }
}
