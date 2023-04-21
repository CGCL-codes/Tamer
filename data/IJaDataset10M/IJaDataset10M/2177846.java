package weekone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SixTwentyOne {

    static double firstNumber, secondNumber, thirdNumber, number;

    public static int quotientInteger(double firstNumber, double secondNumber) {
        int thirdNumber;
        if (secondNumber == 0) {
            System.out.println("Cannot divide by zero, undefined.");
            System.exit(0);
        } else {
            firstNumber = Math.floor(firstNumber / secondNumber) / 1;
        }
        thirdNumber = (int) firstNumber;
        return thirdNumber;
    }

    public static int remainderInteger(double firstNumber, double secondNumber) {
        int thirdNumber;
        if (secondNumber == 0) {
            System.out.println("Cannot divide by zero, undefined.");
            System.exit(0);
        } else {
            firstNumber = Math.IEEEremainder(firstNumber, secondNumber);
        }
        thirdNumber = (int) firstNumber;
        return thirdNumber;
    }

    public static void displayDigits(double firstNumber) {
        int a, b, c, d, e;
        a = quotientInteger(firstNumber, 10000);
        firstNumber = firstNumber - (a * 10000);
        b = quotientInteger(firstNumber, 1000);
        firstNumber = firstNumber - (b * 1000);
        c = quotientInteger(firstNumber, 100);
        firstNumber = firstNumber - (c * 100);
        d = quotientInteger(firstNumber, 10);
        firstNumber = firstNumber - (d * 10);
        e = quotientInteger(firstNumber, 1);
        firstNumber = firstNumber - (e * 1);
        if (firstNumber > e) System.out.println("Wrong! You have hosed this one up big time!");
        System.out.println(a + " " + b + " " + c + " " + d + " " + e);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        double a, b, c;
        int d;
        a = 10;
        b = 3;
        System.out.println("Hello World! This is a snappy little application.");
        System.out.println("We'll give it a go right now.");
        c = a / b;
        System.out.println("Divide 10 by 3 and the quotient is " + c + ".");
        d = quotientInteger(a, b);
        System.out.println("The integer of the quotient is " + d + ".");
        d = remainderInteger(a, b);
        System.out.println("The integer of the remainder is " + d + ".");
        System.out.println("Enter a number from 1 to 99999: ");
        number = Double.parseDouble(in.readLine());
        System.out.println("Here is a new number " + number);
        displayDigits(number);
    }
}
