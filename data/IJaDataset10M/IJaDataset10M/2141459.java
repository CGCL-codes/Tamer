package name.gano.worldwind.view;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Frustum;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.view.BasicOrbitView;
import gov.nasa.worldwind.view.ViewSupport;
import javax.media.opengl.GL;

/**
 * @author dcollins
 * @version $Id: BasicOrbitView.java 3557 2007-11-17 04:10:32Z dcollins $
 */
public class BasicModelView2 extends BasicOrbitView {

    private BasicModelViewModel basicModelViewModel;

    private Frustum frustum = new Frustum();

    private Globe globe;

    private Position iniPosition;

    double minClipDist = 100;

    private double nearClipDistance = -1;

    private double farClipDistance = -1;

    private java.awt.Rectangle viewport = new java.awt.Rectangle();

    private Angle fieldOfView = Angle.fromDegrees(45);

    private DrawContext dc;

    private final ViewSupport viewSupport = new ViewSupport();

    private static final double MINIMUM_NEAR_DISTANCE = 2;

    private static final double MINIMUM_FAR_DISTANCE = 100;

    public BasicModelView2(Position centerPosition, Globe globe) {
        super();
        this.globe = globe;
        iniPosition = centerPosition;
    }

    public void setCenterPosition(Position centerPosition) {
        if (globe == null) {
            return;
        }
        Vec4 v4 = globe.computePointFromPosition(centerPosition);
        double[] centerPos = new double[3];
        centerPos[0] = v4.getX();
        centerPos[1] = v4.getY();
        centerPos[2] = v4.getZ();
        basicModelViewModel.setCenterPos(centerPos);
    }

    public void addToTheta1(float Dtheta1) {
        basicModelViewModel.addToTheta1(Dtheta1);
    }

    public void addToTheta2(float Dtheta2) {
        basicModelViewModel.addToTheta2(Dtheta2);
    }

    public void addToTheta3(float Dtheta3) {
        basicModelViewModel.addToTheta3(Dtheta3);
    }

    public void addToRadiusAboutCenter(float meters) {
        basicModelViewModel.addToRadiusAboutCenter(meters);
    }

    public void addToXOffsetAboutCenter(float meters) {
        basicModelViewModel.addToXOffsetAboutCenter(meters);
    }

    public void addToYOffsetAboutCenter(float meters) {
        basicModelViewModel.addToYOffsetAboutCenter(meters);
    }

    public void addToZOffsetAboutCenter(float meters) {
        basicModelViewModel.addToZOffsetAboutCenter(meters);
    }

    protected void doApply(DrawContext dc) {
        if (this.basicModelViewModel == null) {
            this.dc = dc;
            this.globe = this.dc.getGlobe();
            doInitialize(dc);
        }
        Vec4 eyeVec = this.basicModelViewModel.getEyeVector();
        if (eyeVec == null) return;
        this.dc = dc;
        this.globe = this.dc.getGlobe();
        int[] viewportArray = new int[4];
        dc.getGL().glGetIntegerv(GL.GL_VIEWPORT, viewportArray, 0);
        this.viewport = new java.awt.Rectangle(viewportArray[0], viewportArray[1], viewportArray[2], viewportArray[3]);
        double nearDistance = this.nearClipDistance > 0 ? this.nearClipDistance : getAutoNearClipDistance();
        double farDistance = this.farClipDistance > 0 ? this.farClipDistance : getAutoFarClipDistance();
        this.frustum = this.createFrustum(nearDistance, farDistance);
        basicModelViewModel.updateViewTransform(dc);
        Matrix modelViewMatrix = this.createModelViewMatrix(this.basicModelViewModel);
        Matrix projectionMatrix = this.createProjectionMatrix(nearDistance, farDistance);
        viewSupport.loadGLViewState(dc, modelViewMatrix, projectionMatrix);
    }

    public Frustum getFrustum() {
        return this.frustum;
    }

    private Matrix createModelViewMatrix(BasicModelViewModel orbitViewModel) {
        if (orbitViewModel == null) return null;
        return orbitViewModel.getTransformMatrix();
    }

    private Matrix createProjectionMatrix(double nearClipDistance, double farClipDistance) {
        Angle fov = this.getFieldOfView();
        if (fov == null) return null;
        java.awt.Rectangle viewport = this.getViewport();
        if (viewport == null) return null;
        return Matrix.fromPerspective(fov, viewport.width, viewport.height, nearClipDistance, farClipDistance);
    }

    private Frustum createFrustum(double nearClipDistance, double farClipDistance) {
        Angle fov = this.getFieldOfView();
        if (fov == null) return null;
        java.awt.Rectangle viewport = this.getViewport();
        return Frustum.fromPerspective(fov, viewport.width, viewport.height, nearClipDistance, farClipDistance);
    }

    protected void doInitialize(DrawContext dc) {
        this.basicModelViewModel = new BasicModelViewModel(dc);
        setCenterPosition(iniPosition);
    }

    public Angle getFieldOfView() {
        return this.fieldOfView;
    }

    public void setFieldOfView(Angle fieldOfView) {
        if (fieldOfView == null) {
            String message = Logging.getMessage("nullValue.AngleIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.fieldOfView = fieldOfView;
    }

    public double getNearClipDistance() {
        return this.nearClipDistance;
    }

    public void setNearClipDistance(double distance) {
        this.nearClipDistance = distance;
    }

    public double getFarClipDistance() {
        return this.farClipDistance;
    }

    public void setFarClipDistance(double distance) {
        this.farClipDistance = distance;
    }

    public double getAutoNearClipDistance() {
        Position eyePos = getCurrentEyePosition();
        return computeNearDistance(eyePos);
    }

    public double getAutoFarClipDistance() {
        Position eyePos = getCurrentEyePosition();
        return computeFarDistance(eyePos);
    }

    public java.awt.Rectangle getViewport() {
        return new java.awt.Rectangle(this.viewport);
    }

    public Position getCurrentEyePosition() {
        if (this.globe != null) {
            return basicModelViewModel.getEyePosition(dc);
        }
        return Position.ZERO;
    }

    protected double computeNearDistance(Position eyePosition) {
        double near = 0;
        if (eyePosition != null && this.dc != null) {
            double elevation = this.viewSupport.computeElevationAboveSurface(this.dc, eyePosition);
            double tanHalfFov = this.fieldOfView.tanHalfAngle();
            near = elevation / (2 * Math.sqrt(2 * tanHalfFov * tanHalfFov + 1));
        }
        return near < MINIMUM_NEAR_DISTANCE ? MINIMUM_NEAR_DISTANCE : near;
    }

    protected double computeFarDistance(Position eyePosition) {
        double far = 0;
        if (eyePosition != null) {
            far = computeHorizonDistance(eyePosition);
        }
        return far < MINIMUM_FAR_DISTANCE ? MINIMUM_FAR_DISTANCE : far;
    }

    protected double computeHorizonDistance(Position eyePosition) {
        if (this.globe != null && eyePosition != null) {
            double elevation = eyePosition.getElevation();
            double elevationAboveSurface = this.viewSupport.computeElevationAboveSurface(this.dc, eyePosition);
            return this.viewSupport.computeHorizonDistance(this.globe, Math.max(elevation, elevationAboveSurface));
        }
        return 0;
    }

    public double computeHorizonDistance() {
        double horizon = 0;
        Position eyePos = computeEyePositionFromModelview();
        if (eyePos != null) {
            horizon = computeHorizonDistance(eyePos);
        }
        return horizon;
    }

    protected Position computeEyePositionFromModelview() {
        return Position.ZERO;
    }
}
