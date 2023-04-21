package tag4m.tests;

public class ByteTest {

    public static void main(String[] args) {
        byte[] b = new byte[] { -127, 2, 3, -1, -2, -3 };
        String x = new String(b);
        System.out.println(":" + x);
        byte c[] = x.getBytes();
        for (int i = 0; i < c.length; i++) System.out.print(c[i] + " ");
        System.out.println();
    }
}
