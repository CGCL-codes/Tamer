package com.krobothsoftware.psn.model.list;

import java.util.ArrayList;
import java.util.ListIterator;
import com.krobothsoftware.psn.model.Trophy;

/**
 * The Class TrophyList. This holds all trophies in a list and can sort them.
 * 
 * @param <T>
 *            the generic type
 * @author Kyle Kroboth
 */
public class TrophyList<T extends Trophy> extends AbstractDataList<T> {

    private int platinum;

    private int gold;

    private int silver;

    private int bronze;

    private String gameId;

    /**
	 * Instantiates a new trophy list.
	 * 
	 * @param newList
	 *            the new list
	 */
    public TrophyList(ArrayList<T> newList) {
        super(newList);
    }

    /**
	 * Instantiates a new trophy list.
	 */
    public TrophyList() {
        super();
    }

    /**
	 * Instantiates a new trophy list.
	 * 
	 * @param gameId
	 *            the game id
	 * @param platinum
	 *            the platinum
	 * @param gold
	 *            the gold
	 * @param silver
	 *            the silver
	 * @param bronze
	 *            the bronze
	 */
    public TrophyList(String gameId, int platinum, int gold, int silver, int bronze) {
        this.gameId = gameId;
        this.platinum = platinum;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
    }

    /**
	 * Gets the trophy by id.
	 * 
	 * @param id
	 *            the id
	 * @return the trophy by id
	 */
    public Trophy getTrophyById(int id) {
        Trophy trophie = null;
        ListIterator<T> ltr = list.listIterator();
        while (ltr.hasNext()) {
            T tmp = ltr.next();
            if (tmp.getTrophyId() == id) {
                trophie = tmp;
                break;
            }
        }
        return trophie;
    }

    /**
	 * Sort by trophyType.
	 * 
	 * 
	 * @see Trophy#BRONZE
	 * @see Trophy#SILVER
	 * @see Trophy#GOLD
	 * @see Trophy#PLATINUM
	 * @see Trophy#HIDDEN
	 * @param trophyType
	 *            the type
	 * @return the trophy list
	 */
    public TrophyList<T> sortByType(int trophyType) {
        ArrayList<T> newList = new ArrayList<T>();
        ListIterator<T> ltr = list.listIterator();
        while (ltr.hasNext()) {
            T tmp = ltr.next();
            if (tmp.getTrophyType() == trophyType) {
                newList.add(tmp);
            }
        }
        return new TrophyList<T>(newList);
    }

    /**
	 * Gets the platinum count.
	 * 
	 * @return the platinum
	 */
    public int getPlatinum() {
        return platinum;
    }

    /**
	 * Gets the gold count.
	 * 
	 * @return the gold
	 */
    public int getGold() {
        return gold;
    }

    /**
	 * Gets the silver count.
	 * 
	 * @return the silver
	 */
    public int getSilver() {
        return silver;
    }

    /**
	 * Gets the bronze count.
	 * 
	 * @return the bronze
	 */
    public int getBronze() {
        return bronze;
    }

    /**
	 * Gets the game id.
	 * 
	 * @return the game id
	 */
    public String getGameId() {
        return gameId;
    }
}
