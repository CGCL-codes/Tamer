package gurpsbeans;

import java.util.Random;
import java.util.ResourceBundle;
import net.etherstorm.jopenrpg.ReferenceManager;

/**
 * 
 * 
 * 
 * $Date$ <br>
 * 
 * @author tedberg
 * @author $Author$
 * @version $Revision$
 * @since Jan 17, 2005
 */
public class Rules {

    public enum RollResult {

        CriticalFailure, Failure, Success, CriticalSuccess
    }

    ;

    /**
	 * @param r
	 * @return
	 */
    public static boolean isSuccess(RollResult r) {
        if (r == RollResult.Success || r == RollResult.CriticalSuccess) return true;
        return false;
    }

    /**
	 * 
	 */
    public Rules() {
    }

    public static int getCritSuccessThreshold(int skill) {
        if (skill <= 2) return -1;
        if (skill == 3) return 3;
        if (skill < 15) return 4;
        if (skill < 16) return 5;
        if (skill < 20) return 6;
        if (skill < 25) return 7;
        if (skill < 30) return 8;
        if (skill < 35) return 9;
        return 10;
    }

    public static int getCritFailureThreshold(int skill) {
        if (skill <= 2) return -1;
        if (skill <= 7) return Math.max(13, skill + 10);
        if (skill < 16) return 17;
        return 18;
    }

    public static String getRollResultText(RollResult result) {
        switch(result) {
            case CriticalFailure:
                return getBundle().getString("critical.failure");
            case Failure:
                return getBundle().getString("failure");
            case Success:
                return getBundle().getString("success");
            case CriticalSuccess:
                return getBundle().getString("critical.success");
            default:
                return getBundle().getString("range.error");
        }
    }

    public static RollResult getResult(int skill, int roll) {
        int critFailure = getCritFailureThreshold(skill);
        int critSuccess = getCritSuccessThreshold(skill);
        if (roll >= critFailure) return RollResult.CriticalFailure;
        if (roll > skill) return RollResult.Failure;
        if (roll <= critSuccess) return RollResult.CriticalSuccess;
        return RollResult.Success;
    }

    protected static ResourceBundle _bundle;

    public static ResourceBundle getBundle() {
        if (_bundle == null) _bundle = ResourceBundle.getBundle(Rules.class.getName(), ReferenceManager.getInstance().getCurrentLocale());
        return _bundle;
    }

    public static int roll(int count) {
        Random rand = new Random();
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += rand.nextInt(6) + 1;
        }
        return total;
    }
}
