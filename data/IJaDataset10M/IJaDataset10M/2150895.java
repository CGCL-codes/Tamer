package hu.sztaki.lpds.wfs.validator;

import java.util.Hashtable;

/**
 * @author krisztian
 */
public class ConfigureChecker {

    private static Hashtable<String, ConfigureValidatorFace> checkers = new Hashtable<String, ConfigureValidatorFace>();

    /**
 * Query configuration checker plugin
 * @param pMiddleware midlleware type
 * @return checker plugin instance
 * @throws java.lang.NullPointerException No plugin for middleawre
 */
    public static ConfigureValidatorFace getI(String pMiddleware) throws NullPointerException {
        if (checkers.get(pMiddleware) == null) {
            try {
                ConfigureValidatorFace tmp = (ConfigureValidatorFace) Class.forName("hu.sztaki.lpds.wfs.validator.Check_" + pMiddleware).newInstance();
                checkers.put(pMiddleware, tmp);
            } catch (Exception e) {
                new NullPointerException("configure checker plugin(" + pMiddleware + ") not avaible");
            }
        }
        return checkers.get(pMiddleware);
    }
}
