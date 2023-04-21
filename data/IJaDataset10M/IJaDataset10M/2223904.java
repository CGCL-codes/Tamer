package testings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 30-mei-2005
 * Time: 12:18:26
 * To change this template use File | Settings | File Templates.
 */
public class TestRegExpression {

    public static void main(String[] args) {
        TestRegExpression testRegExpression = new TestRegExpression();
        String str = "!=(uherif2";
        str = testRegExpression.takeLastPart(str).trim();
        System.out.println(" 1 " + str);
        Pattern p1 = Pattern.compile("[!]\\w+");
        Matcher m1 = p1.matcher(str);
        boolean b1 = m1.find();
        Pattern p2 = Pattern.compile("[!][ ]\\w+");
        Matcher m2 = p2.matcher(str);
        boolean b2 = m2.find();
        Pattern p3 = Pattern.compile("[(][!]");
        Matcher m3 = p3.matcher(str);
        boolean b3 = m3.find();
        if (b1 || b2 || b3) System.out.println("matched");
    }

    public String takeLastPart(String current) {
        String big = current;
        int indexOR = big.lastIndexOf("&&");
        int indexAND = big.lastIndexOf("||");
        int index;
        if (indexAND > indexOR) index = indexAND;
        index = indexOR;
        if (index > -1) return current.substring((index + 2), current.length());
        return current;
    }
}
