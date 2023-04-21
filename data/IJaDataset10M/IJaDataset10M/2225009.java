package diet.utils;

/**
 *
 * @author user
 */
public class StringOperations {

    /** Creates a new instance of StringOperations */
    public StringOperations() {
    }

    public static String returnOtherString(String searchString, String s1, String s2) {
        if (s1.equalsIgnoreCase(searchString)) {
            return s2;
        } else if (s2.equalsIgnoreCase(searchString)) {
            return s1;
        }
        System.err.println("NONE OF THE 2 STRINGS MATCH " + searchString + "//" + s1 + "//" + s2);
        System.exit(-123456);
        return null;
    }

    static String posts = "[\\[\\]\\ .}{|@#$%^&*()/,!?:;]+\\s*";

    public static String[] splitOnlyText(String s) {
        return s.split(posts);
    }

    public static boolean isASubWordOfB(String a, String b) {
        String bString[] = b.split(posts);
        for (int i = 0; i < bString.length; i++) {
            String s = bString[i];
            if (s.equalsIgnoreCase(a)) return true;
        }
        return false;
    }

    public static void mainOLD(String[] args) {
        String[] a = splitOnlyText("a ...b,c/d//e f[[gh]]]ij&&&&   k lm}n");
        for (int i = 0; i < a.length; i++) {
            System.err.println(a[i]);
        }
        boolean tt = isASubWordOfB("CAR", "WE LOVE /car/THE CART");
        if (tt) System.err.println("IT IS)");
    }

    public static void main(String[] args) {
        String a = "THIS IS A SINGLE STRING \nAND IT HAS ALL THE TEXT IN IT";
        System.out.println(a.replaceAll("\n", "(NEWLINE)"));
    }

    public static String appWS_ToRight(String s, int minLength) {
        int spacestoadd = minLength - s.length();
        if (spacestoadd < 1) return "" + s;
        for (int i = 0; i < spacestoadd; i++) {
            s = s + " ";
        }
        return s;
    }
}
