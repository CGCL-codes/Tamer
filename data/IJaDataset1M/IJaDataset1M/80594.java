package jpatch.entity;

import java.io.PrintStream;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.vecmath.Point3f;
import jpatch.boundary.MainFrame;
import jpatch.control.importer.JPatchImport;

public class OLDCamera extends AnimObject {

    private static final OLDModel cameraModel = new OLDModel();

    private boolean bParent;

    static {
        new JPatchImport().importModel(cameraModel, ClassLoader.getSystemResource("jpatch/models/camera.jpt").toString());
    }

    protected float fFocalLength = 50.0f;

    protected float fAperture = 16.0f;

    protected double dFocus = Double.POSITIVE_INFINITY;

    protected float fFrameRate = 25.0f;

    protected float fShutterSpeed = 25.0f;

    protected float fExposure = 1.0f;

    protected float fFilmSize = 35.0f;

    protected boolean bFocalBlur = false;

    protected boolean bMotionBlur = false;

    public OLDCamera(String name) {
        strName = name;
    }

    public void setFocalLength(float focalLength) {
        fFocalLength = focalLength;
    }

    public void setAperture(float aperture) {
        fAperture = aperture;
    }

    public void setFocus(double focus) {
        dFocus = focus;
    }

    public void setFrameRate(float frameRate) {
        fFrameRate = frameRate;
    }

    public void setShutterSpeed(float shutterSpeed) {
        fShutterSpeed = shutterSpeed;
    }

    public void setExposure(float exposure) {
        fExposure = exposure;
    }

    public void setFilmSize(float filmSize) {
        fFilmSize = filmSize;
    }

    public void setFoculBlur(boolean enabled) {
        bFocalBlur = enabled;
    }

    public void setMotionBlur(boolean enabled) {
        bMotionBlur = enabled;
    }

    public float getFieldOfView() {
        return (float) Math.atan(fFilmSize / 2 / fFocalLength) / (float) Math.PI * 360;
    }

    public float getFocalLength() {
        return fFocalLength;
    }

    public float getAperture() {
        return fAperture;
    }

    public double getFocus() {
        return dFocus;
    }

    public float getFrameRate() {
        return fFrameRate;
    }

    public float getShutterSpeed() {
        return fShutterSpeed;
    }

    public float getExposure() {
        return fExposure;
    }

    public float getFilmSize() {
        return fFilmSize;
    }

    public boolean isFoculBlur() {
        return bFocalBlur;
    }

    public boolean isMotionBlur() {
        return bMotionBlur;
    }

    public OLDModel getModel() {
        return cameraModel;
    }

    public void xml(PrintStream out, String prefix) {
        out.append(prefix).append("<camera>\n");
        out.append(prefix).append("\t<name>" + getName() + "</name>\n");
        MainFrame.getInstance().getAnimation().getCurvesetFor(this).xml(out, prefix + "\t");
        out.append(prefix).append("</camera>").append("\n");
    }

    public void removeFromParent() {
        MainFrame.getInstance().getAnimation().removeCamera(this);
    }

    public void setParent(MutableTreeNode newParent) {
        bParent = true;
    }

    public TreeNode getParent() {
        return bParent ? MainFrame.getInstance().getAnimation().getTreenodeCameras() : null;
    }
}
