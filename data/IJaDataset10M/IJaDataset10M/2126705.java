package cmjTracer.animation;

import java.util.Collection;
import cmjTracer.math.Vec3;
import cmjTracer.raytracer.objects.SceneObject;

/**
 * A KeyFrame is a {@link Frame Frame} which allows changing position and direction of
 * the camera and object positions. It is used in a {@link Animation Animation} to 
 * create key frames between which the positions / directions should be interpolated.
 * @author Jens-Fabian Goetzmann
 *
 */
public class Keyframe extends Frame {

    /**
	 * Creates a new Keyframe object which contains the specified objects. The positions
	 * associated with the objects are taken from the objects themselves (via 
	 * getPosition()), but if the objects' positions change, the association in the
	 * frame is kept because it is is stored internally in a map (so the information
	 * is redundant at the time of creation).
	 * @param objects The objects to add to the frame.
	 * @param cameraPos The camera position for this frame.
	 * @param cameraLookAt The center of the camera's field of view for this frame.
	 */
    public Keyframe(Collection<SceneObject> objects, Vec3 cameraPos, Vec3 cameraLookAt) {
        super(objects, cameraPos, cameraLookAt);
    }

    /**
	 * Sets the center of the field of view of the camera for this frame.
	 * @param cameraLookAt The center of the field of view of the camera
	 * for this frame.
	 */
    public void setCameraLookAt(Vec3 cameraLookAt) {
        this.cameraLookAt = cameraLookAt;
    }

    /**
	 * Sets the position of the camera for this frame.
	 * @param cameraPos The position of the camera for this frame.
	 */
    public void setCameraPos(Vec3 cameraPos) {
        this.cameraPos = cameraPos;
    }

    /**
	 * Sets the position of the SceneObject o for this frame.
	 * @param o The SceneObject for which the position should be set.
	 * @param pos The position of the given SceneObject for this frame.
	 */
    public void setPositionForObject(SceneObject o, Vec3 pos) {
        positions.put(o, pos);
    }
}
