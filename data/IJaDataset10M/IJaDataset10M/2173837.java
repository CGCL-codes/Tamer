package esferacore.utils;

/**
 *
 * @author neo
 */
public class EsferaUtils {

    private static EsferaUtils eUtils = new EsferaUtils();

    private EsferaUtils() {
    }

    public static EsferaUtils getEsferaUtilities() {
        return eUtils;
    }
}
