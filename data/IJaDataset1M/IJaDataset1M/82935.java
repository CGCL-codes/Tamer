package homura.hde.core.scene.state;

import homura.hde.util.export.InputCapsule;
import homura.hde.util.export.JMEExporter;
import homura.hde.util.export.JMEImporter;
import homura.hde.util.export.OutputCapsule;
import java.io.IOException;

/**
 * <code>ClipState</code> specifies a plane to test for clipping of the nodes. This can be used to
 * take "slices" out of geometric objects. ClipPlane can add an additional (to the normal frustum planes) 
 * six planes to clip against.
 */
public abstract class ClipState extends RenderState {

    public static final int CLIP_PLANE0 = 0;

    public static final int CLIP_PLANE1 = 1;

    public static final int CLIP_PLANE2 = 2;

    public static final int CLIP_PLANE3 = 3;

    public static final int CLIP_PLANE4 = 4;

    public static final int CLIP_PLANE5 = 5;

    public static final int MAX_CLIP_PLANES = 6;

    protected boolean[] enabledClipPlanes = new boolean[MAX_CLIP_PLANES];

    protected double[][] planeEquations = new double[MAX_CLIP_PLANES][4];

    /**
     * <code>getType</code> returns RenderState.RS_CLIP
     * 
     * @return RenderState.RS_CLIP
     * @see RenderState#getType()
     */
    public int getType() {
        return RS_CLIP;
    }

    /**
     * Enables/disables a specific clip plane
     * 
     * @param planeIndex
     *            Plane to enable/disable (CLIP_PLANE0-CLIP_PLANE5)
     * @param enabled
     *            true/false
     */
    public void setEnableClipPlane(int planeIndex, boolean enabled) {
        if (planeIndex < 0 || planeIndex >= MAX_CLIP_PLANES) {
            return;
        }
        enabledClipPlanes[planeIndex] = enabled;
        setNeedsRefresh(true);
    }

    /**
     * Sets plane equation for a specific clip plane
     * 
     * @param planeIndex
     *            Plane to set equation for (CLIP_PLANE0-CLIP_PLANE5)
     * @param clipX
     *            plane x variable
     * @param clipY
     *            plane y variable
     * @param clipZ
     *            plane z variable
     * @param clipW
     *            plane w variable
     */
    public void setClipPlaneEquation(int planeIndex, double clipX, double clipY, double clipZ, double clipW) {
        if (planeIndex < 0 || planeIndex >= MAX_CLIP_PLANES) {
            return;
        }
        planeEquations[planeIndex][0] = clipX;
        planeEquations[planeIndex][1] = clipY;
        planeEquations[planeIndex][2] = clipZ;
        planeEquations[planeIndex][3] = clipW;
        setNeedsRefresh(true);
    }

    /**
     * @param index plane to check
     * @return true if given clip plane is enabled
     */
    public boolean getPlaneEnabled(int index) {
        return enabledClipPlanes[index];
    }

    public double getPlaneEq(int plane, int eqIndex) {
        return planeEquations[plane][eqIndex];
    }

    public void setPlaneEq(int plane, int eqIndex, double value) {
        planeEquations[plane][eqIndex] = value;
        ;
        setNeedsRefresh(true);
    }

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(enabledClipPlanes, "enabledClipPlanes", new boolean[MAX_CLIP_PLANES]);
        capsule.write(planeEquations, "planeEquations", new double[MAX_CLIP_PLANES][4]);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        enabledClipPlanes = capsule.readBooleanArray("enabledClipPlanes", new boolean[MAX_CLIP_PLANES]);
        planeEquations = capsule.readDoubleArray2D("planeEquations", new double[MAX_CLIP_PLANES][4]);
    }

    public Class getClassTag() {
        return ClipState.class;
    }
}
