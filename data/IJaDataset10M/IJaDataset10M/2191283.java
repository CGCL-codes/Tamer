package part_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Price {

    public static void main(String[] args) throws IOException {
        BufferedReader inputbuffer = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Calculate ticket price based on pruchase time");
        System.out.println("---------------------------------------------");
        System.out.println("What is the basic ticket price? [euro]");
        double basicPrice = Double.parseDouble(inputbuffer.readLine());
        int day;
        do {
            System.out.println("Number of days the flight has been booked in advance:");
            day = Integer.parseInt(inputbuffer.readLine());
            if (day < 0) break;
            if (day <= 10) System.out.println("The ticket cost is:" + (basicPrice) + " Euro"); else if (day <= 20) System.out.println("The ticket cost is:" + (basicPrice * 0.8) + " Euro"); else if (day <= 30) System.out.println("The ticket cost is:" + (basicPrice * 0.7) + " Euro"); else if (day <= 40) System.out.println("The ticket cost is:" + (basicPrice * 0.67) + " Euro"); else System.out.println("The ticket cost is:" + (basicPrice * 0.63) + " Euro");
        } while (day >= 0);
        System.out.println("Done");
    }
}
