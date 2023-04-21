package fungusEater;

import fungusEater.datatypes.OreType;
import java.awt.Color;

/**
 * @author Aarón Tavío - aaron.tavio at gmail.com
 * @version 1.0.0 - 20081019-1700
 */
public class Config {

    public static final int DEBUG_LEVEL = 0;

    public static final double PROXIMITY_THRESHOLD = 0.05;

    public static final Color A_TYPE_FUNGUS_COLOR = Color.RED;

    public static final Color B_TYPE_FUNGUS_COLOR = Color.GREEN;

    public static final Color C_TYPE_FUNGUS_COLOR = Color.BLUE;

    public static final double F_ENERGY_INIT_MAX = 1.0;

    public static final double F_ENERGY_INIT_MIN = 0.6;

    public static final double F_WEIGHT_INIT_MAX = 0.2;

    public static final double F_WEIGHT_INIT_MIN = 0.01;

    public static final double F_ENERGY_JUNIOR_DEC = 0.01;

    public static final double F_ENERGY_SENIOR_DEC = 0.05;

    public static final double F_GROW_RATE = 0.0001;

    public static final double F_GROW_ENERGY_FACTOR = 1.0;

    public static final double F_JUNIOR_SYNTH_RATE = 0.001;

    public static final double F_SENIOR_SYNTH_RATE = 0.00001;

    public static final double F_BREED_ENERGY_LOST = 0.2;

    public static final int F_BREED_TIME = 20;

    public static final double F_DISPERSION_FACTOR = 180;

    public static final double F_FEATURE_MUTATION_PROBABILITY = 0.8;

    public static final double F_QUALITY_DEV_UPPER_LIMIT = 1.0;

    public static final double F_QUALITY_DEV_LOWER_LIMIT = -1.0;

    public static final double FE_SIZE = 0.4;

    public static final double FE_ENERGY_INIT_MAX = 0.3;

    public static final double FE_ENERGY_INIT_MIN = 0.4;

    public static final double FE_BITE_SIZE = 0.3;

    public static final double FE_MOVEMENT_SPEED = 0.3;

    public static final double FE_ROTATION_SPEED = 0.005;

    public static final double FE_WAITING_ENERGY_FACTOR = 0.000007;

    public static final double FE_MOVEMENT_ENERGY_FACTOR = 0.00001;

    public static final double FE_MOVEMENT_WEIGHT_ENERGY_FACTOR = 0.00001;

    public static final double FE_MAX_CAPACITY_DEV_UPPER_LIMIT = 1.0;

    public static final double FE_MAX_CAPACITY_DEV_LOWER_LIMIT = -1.0;

    public static final double FE_FEATURE_MUTATION_PROBABILITY = 0.4;

    public static final double O_WEIGHT_INIT_MIN = 10.0;

    public static final double O_WEIGHT_INIT_MAX = 20.0;

    public static final Color A_TYPE_ORE_COLOR = Color.RED;

    public static final Color B_TYPE_ORE_COLOR = Color.GREEN;

    public static final Color C_TYPE_ORE_COLOR = Color.BLUE;

    public static final int FA_COMMUNITY_SIZE = 3;

    public static final double FA_ORE_NEEDED_TO_BUILD = 4;

    public static final OreType FA_ORETYPE_TO_BUILD = OreType.A_TYPE;

    public static final int FA_AGENT_BUILD_TIME = 20;
}
