package com.momosw.games.jericho.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <b>Project:</b> JerichoCardGame<br />
 * <b>Package:</b> com.momosw.games.jericho.board<br />
 * <b>Class:</b> Side.java<br />
 * <br />
 * <i>GSI 2011</i><br />
 *
 * @author Miguel Coronado (miguelcb84@gmail.com)
 * @version	Aug 20, 2011
 *
 */
public class Side {

    /** */
    Map<WallType, List<Card>> walls;

    /**
     * Constructor
     * Initiate the walls and for each type of wall initiate an empty wall
     */
    public Side() {
        this.walls = new HashMap<WallType, List<Card>>();
        for (WallType wall : WallType.getTypes()) {
            walls.put(wall, new LinkedList<Card>());
        }
    }

    /**
     * Put card in the side
     * @param card
     * @param wall
     */
    public void putCard(Card card, WallType wall) {
        List<Card> cards = walls.get(wall);
        cards.add(card);
        Collections.sort(cards);
    }

    /**
     * 
     * @param wallType
     * @return
     */
    public List<Card> getWall(WallType wallType) {
        return walls.get(wallType);
    }

    /**
     * This method does not provide access to the cards in play, so any
     * modification in cards in play will not affect the
     * 
     * @return 
     */
    public List<Card> getAllCardsInPlay() {
        List<Card> res = new LinkedList<Card>();
        for (List<Card> list : walls.values()) {
            res.addAll(list);
        }
        return res;
    }

    /**
     * 
     * @param wallType
     * @param value
     * @return
     */
    public boolean removeWall(WallType wallType, int value) {
        return this.walls.get(wallType).remove(new WallCard(wallType, value));
    }

    /**
     * Generate an inline inform
     * @return
     */
    public String inlineInform() {
        String res = "";
        for (WallType wall : walls.keySet()) {
            res += wall.getReadableName() + walls.get(wall) + " ";
        }
        return res;
    }

    public String toString() {
        return inlineInform();
    }
}
