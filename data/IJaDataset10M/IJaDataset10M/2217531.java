package playground.montinil.railDetectionPossibilities;

import java.io.IOException;
import java.text.ParseException;

public class RailDetectionApp {

    public static void main(String[] args) {
        RailDetection railDetection = new RailDetection(args);
        try {
            railDetection.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("---------\n- DONE \n---------");
    }
}
