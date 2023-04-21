package game;

import game.exceptions.RunException;
import game.exceptions.SetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author Fishman Yevgeni
 * @date Oct 30, 2009
 *
 */
public class Run extends Set {

    private Tile.TileColor _color;

    /**
	 * Constructor for Run set. Created run not violating the rules of the game. 
	 * @param color - common color of the run.
	 * @param tiles - tiles of the run.
	 */
    public Run(Tile.TileColor color, Vector<Tile> tiles) throws SetException, RunException {
        super(tiles);
        this._color = color;
        Collections.sort(this._tiles);
        int tLastTileNum = this._tiles.elementAt(0).getNumber() - 1;
        for (Iterator<Tile> iterator = this._tiles.iterator(); iterator.hasNext(); ) {
            Tile tTile = iterator.next();
            if (tTile.getColor() != this._color) {
                throw new RunException("Unable to create run of tiles with differnt colors.");
            }
            if (tTile.getNumber() != tLastTileNum + 1) {
                throw new RunException("Unable to create run with non-sequential tiles.");
            }
            tLastTileNum = tTile.getNumber();
        }
    }

    /**
	 * Adds tile to run.
	 */
    @Override
    public void addTile(Tile tile) throws RunException {
        if (tile.getColor() != this._color) {
            throw new RunException("Unable to add tile with different color.");
        }
        if ((tile.getNumber() == this._tiles.elementAt(0).getNumber() - 1) || (tile.getNumber() == this._tiles.elementAt(this._tiles.size() - 1).getNumber() + 1)) {
            this._tiles.add(tile);
            Collections.sort(this._tiles);
        } else {
            throw new RunException("Unable to add tile that non-sequential with the existing run.");
        }
    }

    /**
	 * Removes a tile from the run.
	 * @throws SetException 
	 */
    @Override
    public void removeTile(Tile tile) throws SetException {
        if ((this._tiles.elementAt(0).equals(tile) || (this._tiles.elementAt(this._tiles.size() - 1).equals(tile)))) {
            super.removeTile(tile);
        } else {
            throw new RunException("Unable to remove tile that not at the start or end of a run.");
        }
    }
}
