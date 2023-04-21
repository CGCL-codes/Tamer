package pdp.scrabble.ia;

import pdp.scrabble.game.Location;
import pdp.scrabble.utility.CrossCheck;

/** Handles CrossCheck computation and storage for AI
 * 
 */
public interface CrossCheckSet {

    public void compute(Direction dir);

    public CrossCheck get(int x, int y);

    public CrossCheck get(Location loc);
}
