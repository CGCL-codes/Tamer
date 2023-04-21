package javastudy.homework;

public class Homework2 {

    public static void main(String[] args) {
        System.out.println("10! = " + factorial(10));
    }

    static int factorial(int n) {
        int result = 0;
        if (n == 1) {
            result = 1;
        } else {
            result = n * factorial(n - 1);
        }
        return result;
    }
}
