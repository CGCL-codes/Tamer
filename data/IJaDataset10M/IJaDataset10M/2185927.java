package jmotor.util;

/**
 * Component:
 * Description:
 * Date: 11-11-10
 *
 * @author Andy.Ai
 */
public class NumericUtils {

    private NumericUtils() {
    }

    /**
     * Generate a random number in range.
     *
     * @param range Rang
     * @return Integer
     */
    public static Integer randomInteger(String range) {
        String[] tokens = StringUtils.split(range, ",");
        String startSymbol = StringUtils.firstCharter(tokens[0]);
        String endSymbol = StringUtils.lastCharter(tokens[1]);
        int minNumber = Integer.valueOf(tokens[0].replace(startSymbol, ""));
        int maxNumber = Integer.valueOf(tokens[1].replace(endSymbol, ""));
        int basic = Math.abs(minNumber) > Math.abs(maxNumber) ? Math.abs(minNumber) : Math.abs(maxNumber);
        int random;
        while (true) {
            random = (int) ((Math.random() * 100000000) % basic++);
            if (minNumber < 0 || maxNumber < 0) {
                if (Math.random() > 0.5) {
                    random = random * -1;
                }
            }
            boolean minMatched;
            if (isOpenRange(startSymbol)) {
                minMatched = random > minNumber;
            } else {
                minMatched = random >= minNumber;
            }
            boolean maxMatched;
            if (isOpenRange(endSymbol)) {
                maxMatched = random < maxNumber;
            } else {
                maxMatched = random <= maxNumber;
            }
            if (minMatched && maxMatched) {
                break;
            }
        }
        return random;
    }

    private static boolean isOpenRange(String symbol) {
        return "(".equals(symbol) || ")".equals(symbol);
    }
}
