package sk.tuke.ess.editor.base.helpers;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 31.10.2011
 * Time: 21:03
 * To change this template use File | Settings | File Templates.
 */
public class TextHelper {

    public static String firstCharUpperOtherLower(String string) {
        char[] charArray = string.toLowerCase().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }

    public static String commonPrefix(String s, String t) {
        int n = Math.min(s.length(), t.length());
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != t.charAt(i)) return s.substring(0, i);
        }
        return s.substring(0, n);
    }
}
