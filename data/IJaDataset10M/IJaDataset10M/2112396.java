package common.types.typesCheck.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PasswordChecker implements ITypeChecker {

    private final Pattern pattern = Pattern.compile("[0-9a-zA-Z_]*");

    /**
     * Creates a new instance of PasswordChecker
     */
    public PasswordChecker() {
    }

    /**
     * @returns 1 if success becose you need to calculate MD5
     */
    @Override
    public synchronized int check(String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) return 1; else return -6;
    }
}
