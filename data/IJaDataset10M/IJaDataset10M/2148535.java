package alice.cartagox.gui;

import alice.cartago.*;

/**
 * Basic utility class for exploiting GUI as artifacts in CARTAGO
 *  
 * @author aricci
 *
 */
public class CartagoGUISystem {

    private static ICartagoEnvironment env;

    public static void init(ICartagoEnvironment e) {
        env = e;
    }

    public static ICartagoEnvironment getEnv() {
        return env;
    }
}
