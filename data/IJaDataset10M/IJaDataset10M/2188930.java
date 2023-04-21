package oe65_parse_tokenize_format;

/**
 *
 * @author SCJP
 */
public class D04_Format {

    public static void main(String[] args) {
        int i = -100;
        String t = "Hello";
        double d = 34534.456457;
        System.out.printf("%2$-10s %1$(5d %3$-8.2f", i, t, d);
    }
}
