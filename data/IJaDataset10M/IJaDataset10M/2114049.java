package lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nutz.lang.Dumps;
import org.nutz.lang.Lang;

public class OtherPractice {

    static void BooleanParse() {
        System.out.println(Lang.parseBoolean("off"));
    }

    static void SeeMatcher() {
        String string = "[a-g]";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher("abedfgh");
        System.out.println(Dumps.matcher(matcher));
    }

    public static void main(String[] args) {
        BooleanParse();
        SeeMatcher();
    }
}
