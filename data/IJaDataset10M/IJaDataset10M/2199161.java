package jse;

/**
 *
 * @author WangShuai
 */
public class Equals {

    public static void main(String[] args) {
        Long a = 1l;
        Long b = new Long(1l);
        Long c = 1l;
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a == c);
        System.out.println(a.equals(c));
    }
}
