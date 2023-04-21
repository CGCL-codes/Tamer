package engine;

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import org.lwjgl.util.vector.*;

/**
 *
 * @author  Jmadar
 */
public class Camera extends GameObject {

    protected int move_z = 0, move_x = 0, move_y = 0;

    protected int rotate_z = 0, rotate_x = 0, rotate_y = 0;

    private void initCamera(float x, float y, float z) {
        setPosition(x, y, z);
        setDirection(0, 0, -1f);
        setUp(0, 1f, 0);
        side = new Vector3f();
        Vector3f.cross(getDirection(), getUp(), side);
        setSpeed(0f);
    }

    /** Creates a new instance of Camera */
    public Camera() {
        initCamera(0, 0, 0);
    }

    public Camera(float x, float y, float z) {
        initCamera(x, y, z);
    }

    public void stop() {
        move_x = 0;
        move_y = 0;
        move_z = 0;
    }

    public void reset() {
    }

    public void render(long elapsedTime) {
        GL11.glLoadIdentity();
        GLU.gluLookAt(getPosition().getX(), getPosition().getY(), getPosition().getZ(), getPosition().getX() + getDirection().getX(), getPosition().getY() + getDirection().getY(), getPosition().getZ() + getDirection().getZ(), getUp().getX(), getUp().getY(), getUp().getZ());
        if (rotate_z != 0) rotateLeftRight(rotate_z);
        if (move_x != 0) moveLeftRight(move_x);
        if (move_y != 0) moveUpDown(move_y);
        if (move_z != 0) moveForwardBackward(move_z);
    }

    public void rotateLeftRight(int dir) {
        rotate_z = dir;
        rotate(getUp(), (float) (rotate_z), getDirection());
    }

    public void moveLeftRight(int dir) {
        move_x = dir;
        Vector3f.cross(getDirection(), getUp(), side);
        getPosition().x += side.x * getSpeed() * move_x;
        getPosition().y += side.y * getSpeed() * move_x;
        getPosition().z += side.z * getSpeed() * move_x;
    }

    public void moveForwardBackward(int dir) {
        move_z = dir;
        getPosition().x += getDirection().x * getSpeed() * move_z;
        getPosition().y += getDirection().y * getSpeed() * move_z;
        getPosition().z += getDirection().z * getSpeed() * move_z;
    }

    public void moveUpDown(int dir) {
        move_y = dir;
        getPosition().x += getUp().x * getSpeed() * move_y;
        getPosition().y += getUp().y * getSpeed() * move_y;
        getPosition().z += getUp().z * getSpeed() * move_y;
    }

    public void lookUpDown(float theta) {
        Vector3f.cross(getDirection(), getUp(), side);
        side.normalise();
        rotate(getDirection(), theta, side);
        rotate(getUp(), theta, side);
    }

    public void lookLeftRight(float theta) {
        rotate(getDirection(), theta, getUp());
    }
}
