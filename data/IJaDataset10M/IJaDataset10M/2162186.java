package map;

import gui.*;
import map.*;
import map.items.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * @author ivan__g__s
 */
public class MapData {

    public final int width, height;

    public char[][] fields;

    public MapObject[][] mapObjects;

    public MapData(String[] input) throws Exception {
        int x, y;
        width = input[0].length() / 2;
        height = input.length;
        fields = new char[width][height];
        mapObjects = new MapObject[width][height];
        char temp;
        for (y = 0; y < height; ++y) {
            if (input[y].length() != input[0].length()) {
                System.out.println("Map ctor: stopped reading after line: " + (y - 1));
                break;
            }
            for (x = 0; x < width; ++x) {
                temp = input[y].charAt(x * 2);
                fields[x][y] = temp;
                switch(temp) {
                    case '_':
                    case '-':
                        mapObjects[x][y] = new FieldNothing();
                        break;
                    case '=':
                    case 'x':
                    case '*':
                    case 'o':
                    case 'i':
                        mapObjects[x][y] = new FieldWall();
                        break;
                    case '~':
                        mapObjects[x][y] = new FieldWater();
                        break;
                    default:
                        System.err.println("unknown char found in map: " + temp);
                }
            }
        }
        System.out.println("map width: " + width + ", height: " + height);
    }

    public static String[] MapFileToStrings(String filename) throws Exception {
        Vector<String> list = new Vector<String>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String currentLine;
        while ((currentLine = br.readLine()) != null) list.add(currentLine);
        String[] ret = new String[0];
        ret = list.toArray(ret);
        return ret;
    }
}
