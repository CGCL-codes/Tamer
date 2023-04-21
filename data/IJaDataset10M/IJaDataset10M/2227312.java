package Factorial;

import java.io.*;

public class FactQuoter {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            System.out.println("FactQouter>>");
            String line = in.readLine();
            if ((line == null) || line.equals("quit")) break;
            try {
                int x = Integer.parseInt(line);
                System.out.println(x + "!" + Factorial4.factorial(x));
            } catch (Exception e) {
                System.out.println("Invalid Input");
            }
        }
    }
}
