package j2se.typestate.file;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 
 * @author yahave
 *
 */
public class BadVisitor {

    public static void main(String args[]) {
        Vector v = new Vector();
        String one = new String("one");
        String two = new String("two");
        String three = new String("three");
        String four = new String("four");
        v.add(one);
        v.add(two);
        v.add(three);
        v.add(four);
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            if (s == two) v.remove(two); else {
                System.out.println(s);
            }
        }
        System.out.println("What's really there...");
        e = v.elements();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            System.out.println(s);
        }
    }
}
