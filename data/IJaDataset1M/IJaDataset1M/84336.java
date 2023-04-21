package skycastle.util.location;

import com.jme.math.Quaternion;
import skycastle.util.MathUtils;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans H�ggstr�m
 */
public class LocationImpl implements MutableLocation {

    private Vector3f myPosition = new Vector3f();

    private Quat4f myOrientation = new Quat4f(0, 0, 0, 1);

    private List<LocationChangeListener> myLocationChangeListeners = null;

    /**
     * An unmodifiable location that is always 0,0,0
     */
    public static final Location ORIGO = new LocationImpl();

    /**
     * Creates a new location, positioned at origo, and pointing in the direction of the x axis, with the y axis up.
     */
    public LocationImpl() {
    }

    /**
     * Creates a new location, positioned at the specified position, and pointing in the direction of the x axis, with the y axis up.
     */
    public LocationImpl(final Vector3f position) {
        myPosition.set(position);
    }

    /**
     * Creates a new location at the same position and with the same orientation as the specified source location.
     */
    public LocationImpl(final Location location) {
        myPosition.set(location.getPosition());
        myOrientation.set(location.getOrientation());
    }

    /**
     * Creates a new location with the specified position and orientation.
     */
    public LocationImpl(final Vector3f position, final Quat4f orientation) {
        myPosition.set(position);
        myOrientation.set(orientation);
    }

    public float getX() {
        return myPosition.x;
    }

    public float getY() {
        return myPosition.y;
    }

    public float getZ() {
        return myPosition.z;
    }

    public Quat4f getOrientation() {
        return getOrientation(null);
    }

    public Quat4f getOrientation(Quat4f orientationOut) {
        if (orientationOut == null) {
            orientationOut = new Quat4f();
        }
        orientationOut.set(myOrientation);
        return orientationOut;
    }

    public Quaternion getOrientationJme(Quaternion orientationOut) {
        if (orientationOut == null) {
            orientationOut = new Quaternion();
        }
        orientationOut.set(myOrientation.x, myOrientation.y, myOrientation.z, myOrientation.w);
        return orientationOut;
    }

    public Vector3f getPosition() {
        return getPosition(null);
    }

    public Vector3f getPosition(Vector3f positionOut) {
        if (positionOut == null) {
            positionOut = new Vector3f();
        }
        positionOut.set(myPosition);
        return positionOut;
    }

    public float getOrientationX() {
        return myOrientation.x;
    }

    public float getOrientationY() {
        return myOrientation.y;
    }

    public float getOrientationZ() {
        return myOrientation.z;
    }

    public float getOrientationW() {
        return myOrientation.w;
    }

    public void addLocationChangeListener(LocationChangeListener locationChangeListener) {
        if (locationChangeListener != null && (myLocationChangeListeners == null || !myLocationChangeListeners.contains(locationChangeListener))) {
            if (myLocationChangeListeners == null) {
                myLocationChangeListeners = new ArrayList<LocationChangeListener>(2);
            }
            myLocationChangeListeners.add(locationChangeListener);
        }
    }

    public void removeLocationChangeListener(LocationChangeListener locationChangeListener) {
        if (locationChangeListener != null && myLocationChangeListeners != null) {
            myLocationChangeListeners.remove(locationChangeListener);
        }
    }

    public void setPosition(Vector3f position) {
        myPosition.set(position);
        handlePositionChange();
    }

    public void setPosition(float x, float y, float z) {
        myPosition.set(x, y, z);
        handlePositionChange();
    }

    public void setOrientation(Quat4f orientation) {
        myOrientation.set(orientation);
        handleOrientationChange();
    }

    public void setOrientation(AxisAngle4f orientation) {
        myOrientation.set(orientation);
        handleOrientationChange();
    }

    public void moveForward(final float meters) {
    }

    public void set(final Location sourceLocation) {
        setPosition(sourceLocation.getPosition());
        setOrientation(sourceLocation.getOrientation());
    }

    public void moveAbsoluteDelta(final float deltaX, final float deltaY, final float deltaZ) {
        myPosition.x += deltaX;
        myPosition.y += deltaY;
        myPosition.z += deltaZ;
        handlePositionChange();
    }

    public void moveAbsoluteDelta(final Vector3f positionDelta) {
        myPosition.add(positionDelta);
        handlePositionChange();
    }

    public void move(final Vector3f movementVector, final float multiplier) {
        myPosition.scaleAdd(multiplier, movementVector, myPosition);
        handlePositionChange();
    }

    public void setX(final float x) {
        myPosition.x = x;
        handlePositionChange();
    }

    public void setY(final float y) {
        myPosition.y = y;
        handlePositionChange();
    }

    public void setZ(final float z) {
        myPosition.z = z;
        handlePositionChange();
    }

    public void rotate(final float horizontalRotation_degrees, final float verticalRotation_degrees) {
        final Quat4f horizontalRotation = new Quat4f();
        horizontalRotation.set(new AxisAngle4f(0, 1, 0, horizontalRotation_degrees * MathUtils.DEGREES_TO_RADIANS));
        myOrientation.mul(horizontalRotation);
        final Quat4f verticalRotation = new Quat4f();
        verticalRotation.set(new AxisAngle4f(0, 0, 1, verticalRotation_degrees * MathUtils.DEGREES_TO_RADIANS));
        myOrientation.mul(verticalRotation);
        handleOrientationChange();
    }

    /**
     * Called when the position of this Location has changed.  Can be used by subclasses to listen to changes.
     */
    protected void onPositionChange() {
    }

    /**
     * Called when the orientation of this Location has changed.  Can be used by subclasses to listen to changes.
     */
    protected void onOrientationChange() {
    }

    private void handlePositionChange() {
        onPositionChange();
        if (myLocationChangeListeners != null) {
            for (LocationChangeListener locationChangeListener : myLocationChangeListeners) {
                locationChangeListener.onPositionChange(this);
            }
        }
    }

    private void handleOrientationChange() {
        onOrientationChange();
        if (myLocationChangeListeners != null) {
            for (LocationChangeListener locationChangeListener : myLocationChangeListeners) {
                locationChangeListener.onOrientationChange(this);
            }
        }
    }
}
