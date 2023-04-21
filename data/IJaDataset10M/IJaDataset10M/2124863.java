package env3d;

import engine.GameObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Write a description of class SystemAdapter here.
 *
 * @author Jason Madar
 * @version (a version number or a date)
 */
public class SystemAdapter extends GameObject {

    Object internalSystemObject;

    long updatetime;

    Method move;

    /**
     * Constructor for objects of class SystemAdapter
     */
    public SystemAdapter(Object systemObject) {
        internalSystemObject = systemObject;
        try {
            move = internalSystemObject.getClass().getDeclaredMethod("move", float.class, float.class, float.class);
        } catch (Exception e) {
            move = null;
        }
    }

    public void render(long elapsedTime) {
        float x = getGameObjectManager().getCamera().getPosition().x;
        float y = getGameObjectManager().getCamera().getPosition().y;
        float z = getGameObjectManager().getCamera().getPosition().z;
        if (move != null) {
            try {
                move.invoke(internalSystemObject, x, y, z);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
