package io;

import java.io.*;
import java.util.*;
import basics.EternityHint;
import basics.EternityTile;
import basics.Hint;
import basics.Tile;
import basics.Tile.Side;
import exceptions.*;

public class EternityTileReader extends TileReader {

    private static final long serialVersionUID = 1895172300781744391L;

    @Override
    public Hint[] getHints(String file) throws IOException, NumberFormatException, HintException, NoFileException {
        File f = new File(file);
        if (!f.exists()) {
            throw new NoFileException("File not found");
        }
        List<Hint> HintsTmp = new ArrayList<Hint>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String str = "";
            while ((str = in.readLine()) != null) {
                String[] linecontent = str.split(" ");
                if (str.length() == 0) {
                    continue;
                }
                int[] data = new int[4];
                int dir = 0;
                for (String lc : linecontent) {
                    if (lc.length() > 0) data[dir++] = Integer.parseInt(lc);
                    if (dir >= 4) break;
                }
                if (dir != 4) throw new HintException("4 Values ! for line " + str);
                HintsTmp.add(new EternityHint(data[0], data[1], data[2], data[3]));
            }
        } catch (IOException e) {
            throw new IOException("Failed to read from " + file + ": " + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Hint[] Hints = new Hint[HintsTmp.size()];
        return HintsTmp.toArray(Hints);
    }

    @Override
    public Tile[] getTiles(String file) throws IOException, EmptyFileException, NoFileException {
        File f = new File(file);
        if (!f.exists()) {
            throw new NoFileException("File not found");
        }
        List<Tile> TilesTmp = new ArrayList<Tile>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String str = "";
            int id = 1;
            while ((str = in.readLine()) != null) {
                String[] linecontent = str.split(" ");
                if (str.length() == 0) {
                    continue;
                }
                int[] Colors = new int[4];
                int ColorsDir = 0;
                for (String lc : linecontent) {
                    if (lc.length() > 0) Colors[ColorsDir++] = Integer.parseInt(lc);
                    if (ColorsDir >= 4) break;
                }
                if (ColorsDir != 4) throw new IllegalArgumentException("4 sides ! for line " + str);
                TilesTmp.add(new EternityTile(id++, Colors[Side.TOP.get()], Colors[Side.RIGHT.get()], Colors[Side.BOTTOM.get()], Colors[Side.LEFT.get()]));
            }
        } catch (IOException e) {
            throw new IOException("Failed to read from " + file + ": " + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (TilesTmp.size() < 1) {
            throw new EmptyFileException("Not enough Tiles in File");
        }
        Tile[] Tiles = new Tile[TilesTmp.size()];
        return TilesTmp.toArray(Tiles);
    }

    @Override
    public Tile[] getTiles(String file, int dimX, int dimY) throws IOException, EmptyFileException, NoFileException {
        Tile[] tiles = this.getTiles(file);
        int dim = dimX * dimY;
        if (tiles.length != dim) {
            throw new EmptyFileException("Dimensions don't fit to read Tilecount");
        }
        return null;
    }
}
