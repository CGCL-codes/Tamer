package net.todd.games.boardgame;

import java.util.List;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import net.todd.common.uitools.IListener;

public class MovementAnimation {

    private final Bounds bounds;

    private final TransformGroup transformGroup;

    private final BranchGroup animationGroup;

    private final BranchGroup parentGroup;

    private List<Vector3f> path;

    private int pathIndex;

    public MovementAnimation(BranchGroup parentGroup, Bounds bounds, TransformGroup transformGroup) {
        this.parentGroup = parentGroup;
        this.bounds = bounds;
        this.transformGroup = transformGroup;
        animationGroup = new BranchGroup();
        animationGroup.setCapability(BranchGroup.ALLOW_DETACH);
    }

    public void animateAlongPath(List<Vector3f> path) {
        this.path = path;
        animateNextStep(getNextStep());
    }

    private void animateNextStep(Vector3f currentLocation) {
        final Vector3f moveToLocation = getNextStep();
        if (moveToLocation != null) {
            AnimationBehviour animationBehaviour = new AnimationBehviour(bounds, transformGroup, currentLocation, moveToLocation);
            animationBehaviour.addAnimationFinishedListener(new IListener() {

                public void fireEvent() {
                    parentGroup.removeChild(animationGroup);
                    animateNextStep(moveToLocation);
                }
            });
            animationGroup.addChild(animationBehaviour);
            parentGroup.addChild(animationGroup);
        }
    }

    private Vector3f getNextStep() {
        Vector3f nextStep = null;
        if (pathIndex < path.size()) {
            nextStep = path.get(pathIndex);
            pathIndex++;
        }
        return nextStep;
    }
}
