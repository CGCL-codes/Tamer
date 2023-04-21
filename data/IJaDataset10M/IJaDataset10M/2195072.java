package basics;

public class ArithmeticCasting {

    public static void main(String[] args) {
        int x = (int) 2.1332;
        double y = 45;
        System.out.printf("x = %s, y = %.2f\n", x, y);
        int a = 25;
        long b = a;
        long c = 25;
        int d = (int) c;
        System.out.printf("a = %s, b = %s,  c = %s, d = %s\n", a, b, c, d);
    }
}
