package env3d;

import engine.Camera;
import util.Font;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Jason Madar
 */
public class EnvCamera extends Camera {

    private String displayString;

    private long prev_time;

    private Vector3f move_forward, move_side, move_up;

    /** Creates a new instance of EnvCamera */
    public EnvCamera(float x, float y, float z) {
        super(x, y, z);
        displayString = null;
        move_forward = new Vector3f(0, 0, 0);
        move_side = new Vector3f(0, 0, 0);
        move_up = new Vector3f(0, 0, 0);
    }

    public void moveForwardBackward(int dir) {
        move_z = dir;
        move_forward.x = getDirection().x * dir;
        move_forward.z = getDirection().z * dir;
    }

    public void moveLeftRight(int dir) {
        move_x = dir;
        Vector3f.cross(getDirection(), getUp(), side);
        move_side.x = side.x * dir;
        move_side.z = side.z * dir;
    }

    public void moveUpDown(int dir) {
        move_y = dir;
        move_up.y = dir;
    }

    public void lookUpDown(float theta) {
        Vector3f.cross(getDirection(), getUp(), side);
        side.normalise();
        rotate(getDirection(), theta, side);
    }

    public void lookLeftRight(float theta) {
        rotate(getDirection(), theta, getUp());
    }

    public void move(long elapsedTime) {
        getPosition().x += move_forward.x * getSpeed() * elapsedTime;
        getPosition().y += move_forward.y * getSpeed() * elapsedTime;
        getPosition().z += move_forward.z * getSpeed() * elapsedTime;
        getPosition().x += move_side.x * getSpeed() * elapsedTime;
        getPosition().y += move_side.y * getSpeed() * elapsedTime;
        getPosition().z += move_side.z * getSpeed() * elapsedTime;
        getPosition().x += move_up.x * getSpeed() * elapsedTime;
        getPosition().y += move_up.y * getSpeed() * elapsedTime;
        getPosition().z += move_up.z * getSpeed() * elapsedTime;
    }

    public void render(long elapsedTime) {
        move(elapsedTime);
        GL11.glLoadIdentity();
        GL11.glColor3f(1f, 1f, 1f);
        boolean light = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Font font = Font.getInstance();
        if (displayString != null) font.printString(-0.25f, 0.025f, -0.5f, displayString);
        if (light) {
            GL11.glEnable(GL11.GL_LIGHTING);
        }
        if (texture) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        super.render(elapsedTime);
    }

    public void setDisplayString(String str) {
        displayString = str;
    }
}
