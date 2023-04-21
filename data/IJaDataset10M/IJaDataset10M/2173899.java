package mapMaker;

import java.util.*;

public class MapMaker {

    @SuppressWarnings("unused")
    private static ArrayList<String> tiles = new ArrayList<String>();

    private static String name;

    private static int height, width;

    public static void main(String[] args) {
        name = "TestMap";
        height = 15;
        width = 15;
        new MapFrame(name, height, width);
    }
}
