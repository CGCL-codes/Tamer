package uk.ac.lkl.migen.system.ai.analysis.core.rhythm.filter;

import java.util.*;
import uk.ac.lkl.migen.system.expresser.io.Tile;
import uk.ac.lkl.migen.system.expresser.io.TileList;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;

public class ContiguousStrictRhythmFilter implements RhythmFilter {

    /**
     * Returns true if all tiles are connected by the sides, 
     * false, otherwise. 
     * 
     * @return true if all tiles are connected by the sides, 
     * false, otherwise.
     */
    public boolean validWindows(TileList window1, TileList window2) {
        if (window1 == null || window2 == null) return false;
        if (window1.isStrictlyConnected() && window2.isStrictlyConnected()) return true; else return false;
    }

    /**
     * For testing
     */
    public static void main(String[] args) {
        ContiguousStrictRhythmFilter filter = new ContiguousStrictRhythmFilter();
        ArrayList<Tile> TileList = new ArrayList<Tile>();
        ModelColor white = ModelColor.WHITE;
        TileList.add(new Tile(0, 0, white));
        TileList.add(new Tile(0, 1, white));
        TileList.add(new Tile(1, 1, white));
        TileList.add(new Tile(2, 2, white));
        TileList.add(new Tile(3, 2, white));
        TileList.add(new Tile(3, 3, white));
        TileList.add(new Tile(3, 4, white));
        TileList.add(new Tile(4, 4, white));
        TileList.add(new Tile(13, 4, white));
        for (int i = 0; i < TileList.size(); i++) {
            Tile tile1 = TileList.get(i);
            for (int j = i + 1; j < TileList.size(); j++) {
                Tile tile2 = TileList.get(j);
                if (tile1.sharesSideWith(tile2)) System.out.println(tile1 + " and " + tile2 + " are contiguous."); else System.out.println(tile1 + " and " + tile2 + " are not contiguous.");
            }
        }
        TileList window1 = new TileList();
        window1.add(new Tile(0, 0, white));
        window1.add(new Tile(0, 1, white));
        window1.add(new Tile(1, 0, white));
        window1.add(new Tile(2, 0, white));
        window1.add(new Tile(3, 0, white));
        window1.add(new Tile(3, 1, white));
        window1.add(new Tile(4, 1, white));
        window1.add(new Tile(5, 1, white));
        TileList window2 = new TileList(window1);
        if (filter.validWindows(window1, window2)) System.out.println("Valid windows"); else System.out.println("Not valid windows");
    }
}
