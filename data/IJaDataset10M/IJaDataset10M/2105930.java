package env3d.android;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmadar
 */
public abstract class AndroidGame {

    private Env env;

    public AndroidGame() {
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public Env getEnv() {
        return env;
    }

    public abstract void play();
}
