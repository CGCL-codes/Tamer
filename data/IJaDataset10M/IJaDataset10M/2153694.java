package etc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pattern.RediffmailPatterns;

public class TestPatterns {

    public static String text = "http://f4plus.rediff.com/iris/Main?do=folder&folder=Inbox&login=dharapvj&session_id=4L20PK1KEKiMWvmqcnUspVwC1PVnTT0V&SrtFld=2&SrtOrd=1&MsgCnt=0&user_size=1&preloaded=1";

    public static void main(String[] args) {
        StringBuffer loginForm = (StringBuffer) matcher(RediffmailPatterns.kPatternSession_id, text).get(0);
        System.out.println(loginForm);
    }

    public static List matcher(String patternString, String searchString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(searchString);
        List matcherGroup = new ArrayList();
        while (matcher.find()) {
            matcherGroup.add(new StringBuffer(matcher.group()));
        }
        return matcherGroup;
    }
}
