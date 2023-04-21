package tiling;

import java.io.Serializable;
import java.util.ArrayList;
import environment.IAction;
import environment.IState;

/** A Tiling is a set of Tiles, with a method for finding a Tile associated with a (state,action) couple. Specific implementations must be done separately for each problem, provided this problem implements the TileAbleEnvironment interface.
 */
public abstract class Tiling implements Serializable {

    protected ArrayList<Tile> dejaVu = null;

    protected ArrayList<Tile> getAlreadySeen() {
        return this.dejaVu;
    }

    /** Find the tile associated with (s,a) */
    protected abstract Tile computeTile(IState s, IAction a);

    /** Return the tile associated with (s,a). In case this tile was not already seen, add it to the 
	list of already seen tiles.
TODO DEBUG : USELESS (overwritten in all sub classes)
    */
    public Tile getTile(IState s, IAction a) {
        Tile t = computeTile(s, a);
        if (!dejaVu.contains(t)) dejaVu.add(t);
        return t;
    }
}
