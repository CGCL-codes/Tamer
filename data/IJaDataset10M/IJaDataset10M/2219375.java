package genorm.runtime;

import java.util.Map;

public class QueryHelper {

    public static String replaceText(String input, Map<String, String> replacements) {
        String token = "%";
        StringBuilder sb = new StringBuilder();
        String[] split = input.split(token);
        String value;
        int i = 0;
        boolean tag = false;
        for (String s : split) {
            if ((value = (String) replacements.get(s)) != null) {
                tag = true;
                sb.append(value);
            } else {
                if ((i != 0) && (!tag)) sb.append(token);
                sb.append(s);
                tag = false;
            }
            i++;
        }
        return (sb.toString());
    }
}
