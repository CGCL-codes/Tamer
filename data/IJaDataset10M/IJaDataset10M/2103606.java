package net.sourceforge.javaheatwave.casetest.caverdude.case1;

import java.awt.*;

public class TileFactory {

    public static Color[][] get10x10Tile(char tileType) {
        char aChar = tileType;
        if (aChar == ' ') return get10x10Tile(Tiles.air2);
        if (aChar == 'w') return get10x10Tile(Tiles.water2);
        if (aChar == 'W') return get10x10Tile(Tiles.wood2);
        if (aChar == 'r') return get10x10Tile(Tiles.rock2);
        if (aChar == 'i') return get10x10Tile(Tiles.insulation2_1);
        if (aChar == 'I') return get10x10Tile(Tiles.insulation2_2);
        if (aChar == 'c') return get10x10Tile(Tiles.concrete2);
        if (aChar == 's') return get10x10Tile(Tiles.sand2);
        if (aChar == 'S') return get10x10Tile(Tiles.air2);
        if (aChar == 'e') return get10x10Tile(Tiles.earth2);
        if (aChar == 'g') return get10x10Tile(Tiles.air2);
        if (aChar == 'p') return get10x10Tile(Tiles.air2);
        if (aChar == 'G') return get10x10Tile(Tiles.air2);
        return null;
    }

    public static Color[][] get5x5Tile(char tileType) {
        char aChar = tileType;
        if (aChar == ' ') return get5x5Tile(Tiles.air2);
        if (aChar == 'w') return get5x5Tile(Tiles.air2);
        if (aChar == 'W') return get5x5Tile(Tiles.air2);
        if (aChar == 'i') return get5x5Tile(Tiles.air2);
        if (aChar == 'I') return get5x5Tile(Tiles.air2);
        if (aChar == 'c') return get5x5Tile(Tiles.air2);
        if (aChar == 's') return get5x5Tile(Tiles.air2);
        if (aChar == 'S') return get5x5Tile(Tiles.air2);
        if (aChar == 'e') return get5x5Tile(Tiles.air2);
        if (aChar == 'g') return get5x5Tile(Tiles.air2);
        if (aChar == 'p') return get5x5Tile(Tiles.air2);
        if (aChar == 'G') return get5x5Tile(Tiles.air2);
        return null;
    }

    public static Color getTileColor(char tileType) {
        char aChar = tileType;
        if (aChar == ' ') return Color.cyan;
        if (aChar == 'w') return Color.blue;
        if (aChar == 'W') ;
        if (aChar == 'i') ;
        if (aChar == 'I') ;
        if (aChar == 'c') ;
        if (aChar == 's') ;
        if (aChar == 'S') ;
        if (aChar == 'e') ;
        if (aChar == 'g') ;
        if (aChar == 'p') ;
        if (aChar == 'G') ;
        return null;
    }

    private static Color[][] get10x10Tile(StringBuilder[] tileData) {
        Color tile[][] = new Color[10][10];
        char aChar = ' ';
        for (int i = 0; i < 10; i++) {
            for (int a = 0; a < 10; a++) {
                aChar = tileData[i].charAt(a);
                if (aChar == 'c') tile[i][a] = Color.cyan;
                if (aChar == 'y') tile[i][a] = Color.yellow;
                if (aChar == 'r') tile[i][a] = Color.red;
                if (aChar == 'g') tile[i][a] = Color.green;
                if (aChar == 'B') tile[i][a] = Color.black;
                if (aChar == 'w') tile[i][a] = Color.white;
                if (aChar == 'o') tile[i][a] = Color.orange;
                if (aChar == 'b') tile[i][a] = Color.blue;
                if (aChar == 'm') tile[i][a] = Color.magenta;
                if (aChar == 'p') tile[i][a] = Color.pink;
                if (aChar == 'G') tile[i][a] = Color.gray;
                if (aChar == '!') tile[i][a] = Color.lightGray;
                if (aChar == '#') tile[i][a] = Color.darkGray;
            }
        }
        return tile;
    }

    private static Color[][] get5x5Tile(StringBuilder[] tileData) {
        Color tile[][] = new Color[10][10];
        char aChar = ' ';
        for (int i = 0; i < 10; i++) {
            for (int a = 0; a < 10; a++) {
                aChar = tileData[i].charAt(a);
                if (aChar == 'b') tile[i][a] = Color.blue;
                if (aChar == 'y') tile[i][a] = Color.yellow;
                if (aChar == 'r') tile[i][a] = Color.red;
                if (aChar == 'g') tile[i][a] = Color.green;
                if (aChar == 'B') tile[i][a] = Color.black;
                if (aChar == 'w') tile[i][a] = Color.white;
                if (aChar == 'o') tile[i][a] = Color.orange;
                if (aChar == 'c') tile[i][a] = Color.cyan;
                if (aChar == 'm') tile[i][a] = Color.magenta;
                if (aChar == 'p') tile[i][a] = Color.pink;
                if (aChar == 'G') tile[i][a] = Color.gray;
                if (aChar == '!') tile[i][a] = Color.lightGray;
                if (aChar == '#') tile[i][a] = Color.darkGray;
            }
        }
        return tile;
    }
}
