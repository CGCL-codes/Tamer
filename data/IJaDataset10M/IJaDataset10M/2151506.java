package protopeer.util;

import java.util.*;
import cern.jet.random.engine.*;

public class RandomnessSource {

    private static long masterSeed;

    private static final HashMap<RandomnessSourceType, RandomEngine> type2engineMap = new HashMap<RandomnessSourceType, RandomEngine>();

    private static final HashMap<RandomnessSourceType, Random> type2randomMap = new HashMap<RandomnessSourceType, Random>();

    public static void init() {
        init(0);
    }

    public static void init(long masterSeed) {
        RandomnessSource.masterSeed = masterSeed;
        type2engineMap.clear();
        type2randomMap.clear();
    }

    public static RandomEngine getRandomEngine(RandomnessSourceType type) {
        RandomEngine engine = type2engineMap.get(type);
        if (engine == null) {
            engine = new MersenneTwister((int) (type.ordinal() + masterSeed));
            type2engineMap.put(type, engine);
        }
        return engine;
    }

    public static Random getRandom(RandomnessSourceType type) {
        Random random = type2randomMap.get(type);
        if (random == null) {
            random = new Random(type.ordinal() + masterSeed);
            type2randomMap.put(type, random);
        }
        return random;
    }

    public static double getNextTopologyDouble() {
        return getRandomEngine(RandomnessSourceType.TOPOLOGY).nextDouble();
    }

    public static double getNextNetworkDouble() {
        return getRandomEngine(RandomnessSourceType.NETWORK).nextDouble();
    }

    public static double getNextWorkloadDouble() {
        return getRandomEngine(RandomnessSourceType.WORKLOAD).nextDouble();
    }

    public static double getNextGeneralDouble() {
        return getRandomEngine(RandomnessSourceType.GENERAL).nextDouble();
    }

    public static long getNextGeneralLong() {
        return getRandomEngine(RandomnessSourceType.GENERAL).nextLong();
    }

    public static double getNextMobilityDouble() {
        return getRandomEngine(RandomnessSourceType.MOBILITY).nextDouble();
    }

    public static float getNextMobilityFloat() {
        return (float) getRandomEngine(RandomnessSourceType.MOBILITY).nextDouble();
    }

    public static Random getTopologyRandomness() {
        return getRandom(RandomnessSourceType.TOPOLOGY);
    }

    public static RandomEngine getChurnRandomEngine() {
        return getRandomEngine(RandomnessSourceType.CHURN);
    }

    public static RandomEngine getGeneralRandomEngine() {
        return getRandomEngine(RandomnessSourceType.GENERAL);
    }
}
