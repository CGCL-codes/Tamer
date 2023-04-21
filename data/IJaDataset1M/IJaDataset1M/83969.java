package component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import main.Model;
import types.FigureE;
import types.SuitE;

public class TileList extends ArrayList<Tile> {

    private static final long serialVersionUID = 1L;

    public TileList() {
        super();
    }

    public TileList(TileList list) {
        super(list);
    }

    public TileList(List<Tile> list) {
        super(list);
    }

    public boolean contains(Tile tile) {
        boolean result = false;
        for (Tile t : this) {
            if (t.equals(tile)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean containsHonor() {
        boolean result = false;
        for (Tile t : this) {
            if (t.isHonour()) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
	 * return the list of possible figure (no INVALID or SINGLE) 
	 */
    public FigureList getValidFigure() {
        FigureList result = new FigureList();
        Figure fig;
        for (Tile tile : this) {
            if (this.getNbInstance(tile) >= 2) {
                fig = new Figure(tile, this.getNbInstance(tile));
                if (!result.contains(fig)) {
                    result.add(fig);
                }
            }
            if (tile.getSuit().isOrdinary() && tile.getNumber() < 8 && Model.areChowAllowed()) {
                if (this.getNbInstance(new Tile(tile.getNumber() + 1, tile.getSuit())) >= 1 && this.getNbInstance(new Tile(tile.getNumber() + 2, tile.getSuit())) >= 1) {
                    fig = new Figure(tile, FigureE.CHOW);
                    if (!result.contains(fig)) {
                        result.add(fig);
                    }
                }
            }
        }
        result.sortSize();
        return result;
    }

    /**
	 * return the list of possible figure (no INVALID or SINGLE) 
	 * containing tile
	 */
    public FigureList getValidFigure(Tile tile) {
        FigureList result = new FigureList();
        Figure fig;
        if (this.getNbInstance(tile) >= 2) {
            fig = new Figure(tile, this.getNbInstance(tile));
            if (!result.contains(fig)) {
                result.add(fig);
            }
        }
        if (tile.getSuit().isOrdinary() && Model.areChowAllowed()) {
            if (tile.getNumber() < 8 && this.getNbInstance(new Tile(tile.getNumber() + 1, tile.getSuit())) >= 1 && this.getNbInstance(new Tile(tile.getNumber() + 2, tile.getSuit())) >= 1) {
                fig = new Figure(tile, FigureE.CHOW);
                if (!result.contains(fig)) {
                    result.add(fig);
                }
            } else if (tile.getNumber() > 1 && tile.getNumber() < 9 && this.getNbInstance(new Tile(tile.getNumber() - 1, tile.getSuit())) >= 1 && this.getNbInstance(new Tile(tile.getNumber() + 1, tile.getSuit())) >= 1) {
                fig = new Figure(new Tile(tile.getNumber() - 1, tile.getSuit()), FigureE.CHOW);
                if (!result.contains(fig)) {
                    result.add(fig);
                }
            } else if (tile.getNumber() > 2 && this.getNbInstance(new Tile(tile.getNumber() - 2, tile.getSuit())) >= 1 && this.getNbInstance(new Tile(tile.getNumber() - 1, tile.getSuit())) >= 1) {
                fig = new Figure(new Tile(tile.getNumber() - 2, tile.getSuit()), FigureE.CHOW);
                if (!result.contains(fig)) {
                    result.add(fig);
                }
            }
        }
        return result;
    }

    /**
	 * Recursive function which return a list of all the possible
	 * combination of PAIR, PUNG, KONG and CHOW in a TileList
	 */
    public List<FigureList> getAllArrangement() {
        List<FigureList> result = new ArrayList<FigureList>();
        if (this.size() > 0) {
            Tile t = this.get(0);
            if (t.isValid()) {
                FigureList validFigs = this.getValidFigure(t);
                for (Figure f : validFigs) {
                    TileList tmp = new TileList(this);
                    tmp.removeAll(f.toTileList());
                    FigureList resultPart = new FigureList();
                    resultPart.add(f);
                    List<FigureList> resultPart2 = tmp.getAllArrangement();
                    if (resultPart2.size() > 0) {
                        for (FigureList fl : resultPart2) {
                            result.add(resultPart.concat(fl));
                        }
                    } else {
                        result.add(resultPart);
                    }
                }
            }
        }
        return result;
    }

    /**
	 * if all the tiles are of the same suit, return this suit
	 * else return INVALID
	 */
    public SuitE getPurity() {
        SuitE result = SuitE.INVALID;
        for (Tile t : this) {
            if (result == SuitE.INVALID) {
                result = t.getSuit();
            } else if (!(result.isHonour() && t.getSuit().isHonour())) {
                if (result != t.getSuit()) {
                    result = SuitE.INVALID;
                    break;
                }
            }
        }
        return result;
    }

    public void remove(Tile tile) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(tile)) {
                this.remove(i);
                break;
            }
        }
    }

    public void removeAll(TileList tiles) {
        Iterator<Tile> removeIt = tiles.iterator();
        while (removeIt.hasNext()) {
            Tile tileToRemove = removeIt.next();
            this.remove(this.indexOf(tileToRemove));
        }
    }

    /**
	 * return the index of the first occurrence of the tile
	 * -1 if not found 
	 */
    public int indexOf(Tile t) {
        int result = -1;
        if (t != null) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).equals(t)) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    public int lastIndexOf(Tile t) {
        int result = -1;
        if (t != null) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).equals(t)) {
                    result = i;
                }
            }
        }
        return result;
    }

    public int getNbInstance(Tile tile) {
        int result = 0;
        if (tile != null) {
            for (Tile t : this) {
                if (t.equals(tile)) {
                    result++;
                }
            }
        }
        return result;
    }

    public void sortAscending() {
        Collections.sort(this, new Comparator<Tile>() {

            @Override
            public int compare(Tile t1, Tile t2) {
                return t1.compare(t2);
            }
        });
    }

    /**
	 * Return all the possible figure with the tile discard (PAIR, PUNG, KONG, CHOW)
	 */
    public FigureList getPossibleFigure(Tile discard, boolean areChowAllowed, boolean arePairAllowed) {
        FigureList result = new FigureList();
        TileList newThis = new TileList(this);
        if (discard.isValid()) {
            newThis.add(discard);
            newThis.sortAscending();
        }
        FigureList tmp = newThis.getValidFigure();
        if (tmp.contains(new Figure(discard, FigureE.PAIR)) && arePairAllowed == true) {
            result.add(new Figure(discard, FigureE.PAIR));
        }
        if (tmp.contains(new Figure(discard, FigureE.PUNG))) {
            result.add(new Figure(discard, FigureE.PUNG));
        }
        if (tmp.contains(new Figure(discard, FigureE.KONG))) {
            result.add(new Figure(discard, FigureE.KONG));
        }
        if (areChowAllowed && Model.areChowAllowed() && discard.getSuit().isOrdinary()) {
            for (int i = -2; i < 1; i++) {
                Tile tmpTile = new Tile(discard.getNumber() + i, discard.getSuit());
                if (tmp.contains(new Figure(tmpTile, FigureE.CHOW))) {
                    result.add(new Figure(tmpTile, FigureE.CHOW));
                }
            }
        }
        return result;
    }

    /**
	 * return the list of valid Mahjong feasible with the discard
	 */
    public List<FigureList> getPossibleMahjong(Tile discard, int ndExposedFig) {
        TileList newThis = new TileList(this);
        if (discard != null && discard.isValid()) {
            newThis.add(discard);
            newThis.sortAscending();
        }
        List<FigureList> list = newThis.getAllArrangement();
        List<FigureList> result = new ArrayList<FigureList>();
        for (FigureList fl : list) {
            if ((fl.getNbSets() + ndExposedFig) == 4 && fl.getNbPaire() == 1) {
                result.add(fl);
            }
        }
        return result;
    }

    /**
	 * remove from the TileList all the tile with which a figure is easily
	 * feasible. I.e remove all the PAIR, PUNG, KONG. If areChowConsidered is TRUE
	 * remove CHOW. If areIncompleteChowConsidered is TRUE remove consecutive tiles.
	 */
    public TileList removePotentialFigure(boolean areChowConsidered, boolean areIncompleteChowConsidered) {
        TileList newThis = new TileList(this);
        FigureList validFigs = newThis.getValidFigure();
        while (validFigs.size() > 0) {
            Figure f = validFigs.get(0);
            if (f.getType() == FigureE.PAIR || f.getType() == FigureE.PUNG || f.getType() == FigureE.KONG || (areChowConsidered && f.getType() == FigureE.CHOW)) {
                newThis.removeAll(f.toTileList());
                validFigs = newThis.getValidFigure();
            } else {
                validFigs.remove(0);
            }
        }
        if (areIncompleteChowConsidered) {
            this.sortAscending();
            TileList tileToRemove = new TileList();
            for (Tile t : newThis) {
                if (newThis.contains(new Tile(t.getNumber() + 1, t.getSuit()))) {
                    tileToRemove.add(t);
                    tileToRemove.add(new Tile(t.getNumber() + 1, t.getSuit()));
                }
            }
            newThis.removeAll(tileToRemove);
        }
        return newThis;
    }
}
