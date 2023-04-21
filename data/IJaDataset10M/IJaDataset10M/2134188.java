package test.helper;

import helper.StringUtils;
import helper.Utils;

public class UtilsTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        boolean[] a = new boolean[] { false, true, false };
        boolean[] b = new boolean[] { false, true, false };
        boolean[] c = new boolean[] { false, true, false };
        boolean[] d = new boolean[] { false, true, false };
        for (boolean x : Utils.joinBooleanArrays(a, b, c, d)) {
            System.out.print(x + ",");
        }
        String[] test = { "asdIASD", "9asdASd", "$asd", "$asdi239", "$asdij9213", "_asd92312asdjASD__asd" };
        System.out.println("---");
        for (String x : test) {
            System.out.print(StringUtils.isAlphaNumerical(x) + ", ");
        }
    }
}
