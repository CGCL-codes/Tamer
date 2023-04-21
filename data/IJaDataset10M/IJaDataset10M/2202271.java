package phonetalks.logic;

import java.util.regex.Pattern;

/**
 *
 * @author Платон
 */
public class PhoneChecker {

    public static boolean isNumberCorrect(String number) {
        Pattern pattern = Pattern.compile("\\d{3,20}");
        return pattern.matcher(number).matches();
    }
}
