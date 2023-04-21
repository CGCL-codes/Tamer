package saiboten;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Places {

    private static Map<String, Place> places = null;

    public static Map<String, Place> loadPlaces() {
        if (places == null) {
            places = new HashMap<String, Place>();
        }
        return places;
    }

    public static Map<String, Place> getPlaces() {
        Map<String, Place> places = loadPlaces();
        try {
            Scanner scan = new Scanner(new File("places.txt"));
            while (scan.hasNextLine()) {
                String[] lineInPieces = scan.nextLine().split(" ");
                places.put(lineInPieces[0].toLowerCase(), new Place(lineInPieces[0], Double.parseDouble(lineInPieces[1]), Double.parseDouble(lineInPieces[2])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return places;
    }
}
