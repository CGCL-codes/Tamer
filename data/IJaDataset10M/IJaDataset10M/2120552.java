package org.xith3d.loaders.models.impl.md2;

import java.util.List;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.Shape3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.TriangleFanArray;
import org.xith3d.scenegraph.TriangleStripArray;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.GeometryArray;
import org.xith3d.loaders.models.base.Model;

/**
 * An instance of an MD2 Model that can be added to a scene tree.
 * 
 * @author Kevin Glass
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class MD2Model extends Model {

    /** The model being a template for this instance */
    private MD2ModelDefinition modelDef;

    /** An appearance that can override the default one provided from the model */
    private Appearance app;

    /** The list of frames to display */
    private List<MD2RenderedFrame> frames;

    /** The current frame */
    private int currentFrame;

    /** The target frame */
    private int targetFrame;

    /** The initial animation frame */
    private String initialAnim;

    /** The animtaion tag we'll set to */
    private String currentAnimation;

    /** The shape containing the geometry for this model */
    private Shape3D shape1;

    /** The shape containing the geometry for this model */
    private Shape3D shape2;

    /** The triangle fans */
    private TriangleFanArray fans;

    /** The triangle strips */
    private TriangleStripArray strips;

    /** The step */
    private float deltaStep;

    /** The local data */
    private Point3f[] localFanData;

    /** The local data */
    private Point3f[] localStripData;

    /** The local data (normals) */
    private Vector3f[] localFanDataN;

    /** The local data (normals) */
    private Vector3f[] localStripDataN;

    /**
     * Creates new MD2Model.
     * 
     * @param modelDef The model defining this instance
     * @param initialAnim The initial animation to display
     */
    protected MD2Model(MD2ModelDefinition modelDef, String initialAnim) {
        this.modelDef = modelDef;
        this.initialAnim = initialAnim;
        app = modelDef.getAppearance();
        buildBaseGeometry();
        TransformGroup grp = new TransformGroup();
        Transform3D trans = new Transform3D();
        trans.rotX((float) Math.toRadians(-90));
        grp.setTransform(trans);
        grp.addChild(shape1);
        grp.addChild(shape2);
        grp.setPickable(true);
        addChild(grp);
        addShapeNode(shape1);
        addShapeNode(shape2);
    }

    /**
     * Build the base geometry for the model
     */
    private void buildBaseGeometry() {
        int format;
        if (modelDef.hasNormals()) {
            format = GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2 | GeometryArray.NORMALS;
        } else {
            format = GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2;
        }
        frames = modelDef.getAnimation(initialAnim);
        MD2RenderedFrame frame = frames.get(0);
        fans = new TriangleFanArray(frame.getFans().length, format, frame.getFanCounts());
        fans.setTextureCoordinates(0, 0, frame.getFansTexCoords());
        if (modelDef.hasNormals()) fans.setNormals(0, frame.getFansNorms());
        strips = new TriangleStripArray(frame.getStrips().length, format, frame.getStripCounts());
        strips.setTextureCoordinates(0, 0, frame.getStripsTexCoords());
        if (modelDef.hasNormals()) strips.setNormals(0, frame.getStripsNorms());
        localFanData = new Point3f[frame.getFans().length];
        for (int i = 0; i < localFanData.length; i++) {
            localFanData[i] = new Point3f();
        }
        localStripData = new Point3f[frame.getStrips().length];
        for (int i = 0; i < localStripData.length; i++) {
            localStripData[i] = new Point3f();
        }
        localFanDataN = new Vector3f[frame.getFans().length];
        for (int i = 0; i < localFanDataN.length; i++) {
            localFanDataN[i] = new Vector3f();
        }
        localStripDataN = new Vector3f[frame.getStrips().length];
        for (int i = 0; i < localStripDataN.length; i++) {
            localStripDataN[i] = new Vector3f();
        }
        setFrame();
        shape1 = modelDef.getNodeFactory().createShape3D(fans, app);
        shape2 = modelDef.getNodeFactory().createShape3D(strips, app);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAnimationNames() {
        return (modelDef.getAnimationNames());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAnimations() {
        return (modelDef.hasAnimations());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dumpAnimationNames() {
        for (String animName : modelDef.getAnimationNames()) {
            System.out.println(animName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentAnimation(String name) {
        if (modelDef.getAnimation(name) != null) {
            this.currentAnimation = name;
            currentFrame = 0;
            frames = modelDef.getAnimation(name);
            nextFrame();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentAnimation() {
        return (currentAnimation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getAnimDuration(String animName) {
        return (-1.0f);
    }

    private void setFrame() {
        MD2RenderedFrame frame = frames.get(currentFrame);
        fans.setCoordinates(0, frame.getFans());
        strips.setCoordinates(0, frame.getStrips());
        if (modelDef.hasNormals()) {
            fans.setNormals(0, frame.getFansNorms());
            strips.setNormals(0, frame.getStripsNorms());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFrame(int frameIndex) {
        currentFrame = frameIndex % frames.size();
        targetFrame = (currentFrame + 1) % frames.size();
        setFrame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextFrame() {
        setFrame(currentFrame + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFrame() {
        return (targetFrame);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void interpolateAnimation(float delta) {
        deltaStep += delta;
        while (deltaStep >= 1) {
            nextFrame();
            deltaStep -= 1;
        }
        MD2RenderedFrame frame = frames.get(currentFrame);
        MD2RenderedFrame target = frames.get(targetFrame);
        Point3f[] currentFans = frame.getFans();
        Point3f[] currentStrips = frame.getStrips();
        Point3f[] targetFans = target.getFans();
        Point3f[] targetStrips = target.getStrips();
        Vector3f[] currentFansNorms = frame.getFansNorms();
        Vector3f[] currentStripsNorms = frame.getStripsNorms();
        Vector3f[] targetFansNorms = target.getFansNorms();
        Vector3f[] targetStripsNorms = target.getStripsNorms();
        for (int i = 0; i < currentFans.length; i++) {
            localFanData[i].x = currentFans[i].x + ((targetFans[i].x - currentFans[i].x) * deltaStep);
            localFanData[i].y = currentFans[i].y + ((targetFans[i].y - currentFans[i].y) * deltaStep);
            localFanData[i].z = currentFans[i].z + ((targetFans[i].z - currentFans[i].z) * deltaStep);
            if (modelDef.hasNormals()) {
                localFanDataN[i].x = currentFansNorms[i].x + ((targetFansNorms[i].x - currentFansNorms[i].x) * deltaStep);
                localFanDataN[i].y = currentFansNorms[i].y + ((targetFansNorms[i].y - currentFansNorms[i].y) * deltaStep);
                localFanDataN[i].z = currentFansNorms[i].z + ((targetFansNorms[i].z - currentFansNorms[i].z) * deltaStep);
                if (localFanDataN[i].length() == 0) localFanDataN[i].z = 0.1f;
                localFanDataN[i].normalize();
            }
        }
        for (int i = 0; i < currentStrips.length; i++) {
            localStripData[i].x = currentStrips[i].x + ((targetStrips[i].x - currentStrips[i].x) * deltaStep);
            localStripData[i].y = currentStrips[i].y + ((targetStrips[i].y - currentStrips[i].y) * deltaStep);
            localStripData[i].z = currentStrips[i].z + ((targetStrips[i].z - currentStrips[i].z) * deltaStep);
            if (modelDef.hasNormals()) {
                localStripDataN[i].x = currentStripsNorms[i].x + ((targetStripsNorms[i].x - currentStripsNorms[i].x) * deltaStep);
                localStripDataN[i].y = currentStripsNorms[i].y + ((targetStripsNorms[i].y - currentStripsNorms[i].y) * deltaStep);
                localStripDataN[i].z = currentStripsNorms[i].z + ((targetStripsNorms[i].z - currentStripsNorms[i].z) * deltaStep);
                if (localStripDataN[i].length() == 0) localStripDataN[i].z = 0.1f;
                localStripDataN[i].normalize();
            }
        }
        fans.setCoordinates(0, localFanData);
        strips.setCoordinates(0, localStripData);
        if (modelDef.hasNormals()) {
            fans.setNormals(0, localFanDataN);
            strips.setNormals(0, localStripDataN);
        }
    }

    /**
     * Set the appearance for this model instance, independent of the base model
     */
    public void setAppearance(Appearance argApp) {
        app = argApp;
        shape1.setAppearance(app);
        shape2.setAppearance(app);
    }

    @Override
    public Node cloneNode(boolean forceDuplicate) {
        MD2Model clone = new MD2Model(modelDef, initialAnim);
        clone.duplicateNode(this, forceDuplicate);
        return (clone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getSharedInstance(int flags) {
        return (modelDef.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getSharedInstance() {
        return (getSharedInstance(modelDef.getLoader().getFlags()));
    }
}
