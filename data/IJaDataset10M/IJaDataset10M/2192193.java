package enterprise.servlet_stateless_ejb;

import java.util.Vector;

/**
 *
 * @author jzou
 */
public class Buznet extends EnglishNameGenerator {

    public void initialization() {
    }

    public static void main(String[] args) {
        Buznet bn = new Buznet();
        Vector<String> names = bn.readEnglishNames("englishNames.txt");
        for (String i : names) {
            System.out.println(i);
        }
        for (int i : new int[] { 1, 3, 3, 4 }) {
            System.out.println(i);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(getRandomName());
        }
    }
}
