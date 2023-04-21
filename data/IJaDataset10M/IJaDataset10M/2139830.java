package kirby;

import com.googlecode.boringengine.Animator;
import com.googlecode.boringengine.lua.LuaFunction;

public abstract class GameObject {

    protected String name;

    protected int x, y, w, h;

    protected Animator anim;

    private static int levelH;

    public static void setLevel(Level lvl) {
        levelH = lvl.getHeight();
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public void update() {
        update0();
        if (y > levelH) die();
    }

    public abstract void die();

    public abstract void doCollide(GameObject obj);

    public abstract void draw();

    public abstract boolean isEdible();

    public abstract String save();

    public abstract void update0();

    @LuaFunction
    public Object[] getPosition() {
        return new Object[] { x, y };
    }

    @LuaFunction
    public void setPosition(int lx, int ly) {
        x = lx;
        y = ly;
    }
}
