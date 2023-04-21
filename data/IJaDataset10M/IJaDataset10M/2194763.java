package Odev3_Soru2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Stack stack = new Stack();
        String str = "(x+45+((b-(a/c)*3)+w)*(z-n))";
        BufferedReader lineOfText = new BufferedReader(new InputStreamReader(System.in));
        String textLine = lineOfText.readLine();
        int aranan = 0;
        try {
            aranan = Integer.parseInt(textLine);
        } catch (NumberFormatException e) {
        }
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                stack.push(i);
            }
            if (str.charAt(i) == ')') {
                int s = stack.pop();
                if (j == aranan - 1) {
                    System.out.println(s);
                    return;
                }
                j++;
            }
        }
    }
}
