package exercises.exercise20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadInput {

    public String readLine() {
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);
        try {
            String userInput = in.readLine();
            return userInput;
        } catch (IOException e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
