package zeditor.core.prefetch.complex;

import zildo.monde.util.Angle;

/**
 * @author Tchegito
 *
 */
public class Adjustment {

    Angle a;

    int matchTile;

    int[] addedTiles;

    public Adjustment(int p_matchTile, Angle p_angle, int... p_addedTiles) {
        a = p_angle;
        matchTile = p_matchTile;
        addedTiles = p_addedTiles;
    }
}
