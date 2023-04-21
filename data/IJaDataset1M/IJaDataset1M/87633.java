package common.types.typesCheck.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class IntArrayChecker implements ITypeChecker {

    private final Pattern pattern = Pattern.compile("[+-]?[0-9]+([,][+-]?[0-9]+)+");

    /** Creates a new instance of IntArrayChecker */
    public IntArrayChecker() {
    }

    @Override
    public synchronized int check(String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) return 0; else return -10;
    }
}
