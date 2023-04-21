package futbol.forrest.utils;

import futbol.forrest.utils.Types.*;

/**
 *
 * @author d157743
 */
public class Parameters {

    private static double min_receptors = 2;

    private static double max_receptors = 4;

    private static double dist_dribble_uti = 5;

    private static double dist_dribble_varEst = 7;

    private static double max_dist_pass = 35;

    private static double attack_zone = 26;

    /**
     * Creates a new instance of Parameters
     */
    public Parameters() {
    }

    public static double getParameter(ParaT type) {
        switch(type) {
            case PARA_MAX_RECEPTORS:
                return max_receptors;
            case PARA_MIN_RECEPTORS:
                return min_receptors;
            case PARA_DIST_DRIBBLE_VAREST:
                return dist_dribble_varEst;
            case PARA_DIST_DRIBBLE_UTI:
                return dist_dribble_uti;
            case PARA_MAX_DIST_PASE:
                return max_dist_pass;
            case PARA_ATTACK_ZONE:
                return attack_zone;
        }
        return 0;
    }
}
