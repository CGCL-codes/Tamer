package world;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import view.ViewPositionObserver;
import board.Constants;
import com.sun.j3d.utils.geometry.Text2D;

public class StatusObject implements ViewPositionObserver {

    private BranchGroup bg;

    private Text2D textLevel;

    private Text2D statusLevel;

    private Text2D textPoints;

    private Text2D statusPoints;

    private Transform3D transformLevelText;

    private Transform3D transformLevelStatus;

    private Transform3D transformPointsText;

    private Transform3D transformPointsStatus;

    private TransformGroup tg1;

    private TransformGroup tg2;

    private TransformGroup tg3;

    private TransformGroup tg4;

    private Transform3D globalTransform;

    private TransformGroup tgAll;

    private Transform3D transformRotate;

    private TransformGroup tgRotate;

    public StatusObject(Integer level, Integer points) {
        this.bg = new BranchGroup();
        this.bg.setCapability(BranchGroup.ALLOW_DETACH);
        this.textLevel = TextFactory.getText2D("Level");
        this.statusLevel = TextFactory.getText2D(level.toString());
        this.textPoints = TextFactory.getText2D("Punkte");
        this.statusPoints = TextFactory.getText2D(points.toString());
        this.transformLevelText = new Transform3D();
        this.transformLevelStatus = new Transform3D();
        this.transformPointsText = new Transform3D();
        this.transformPointsStatus = new Transform3D();
        tg1 = new TransformGroup();
        tg2 = new TransformGroup();
        tg3 = new TransformGroup();
        tg4 = new TransformGroup();
        this.transformLevelText.set(new Vector3d(0.0f, 0.0f, 0.0f));
        this.transformLevelStatus.set(new Vector3d(0.0f, -0.3f, 0.0f));
        this.transformPointsText.set(new Vector3d(0.0f, -0.6f, 0.0f));
        this.transformPointsStatus.set(new Vector3d(0.0f, -0.9f, 0.0f));
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg1.addChild(this.textLevel);
        tg2.addChild(this.statusLevel);
        tg3.addChild(this.textPoints);
        tg4.addChild(this.statusPoints);
        tgAll = new TransformGroup();
        this.globalTransform = new Transform3D();
        tgAll.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgAll.setTransform(this.globalTransform);
        tgRotate = new TransformGroup();
        this.transformRotate = new Transform3D();
        tgRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgRotate.setTransform(this.transformRotate);
        tgAll.addChild(tg1);
        tgAll.addChild(tg2);
        tgAll.addChild(tg3);
        tgAll.addChild(tg4);
        tg1.setTransform(transformLevelText);
        tg2.setTransform(transformLevelStatus);
        tg3.setTransform(transformPointsText);
        tg4.setTransform(transformPointsStatus);
        tgRotate.addChild(tgAll);
        this.bg.addChild(tgRotate);
    }

    public BranchGroup getBG() {
        return this.bg;
    }

    public void updatePosition(double x, double y, double z, int position) {
        switch(position) {
            case 0:
                this.globalTransform.set(new Vector3d(x + Constants.STATUS_OFFSET_X, y + Constants.STATUS_OFFSET_Y, z + Constants.STATUS_OFFSET_Z));
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        tgRotate.setTransform(this.transformRotate);
        tgAll.setTransform(this.globalTransform);
    }
}
