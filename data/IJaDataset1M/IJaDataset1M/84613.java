package com.roslan.games.moo3d;

import java.util.ArrayList;
import com.roslan.games.moo3d.data.Star;

/**
 * @author Roslan Amir
 * @date May 28, 2009
 * 
 */
public class StarList extends ArrayList<StarNode> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor.
	 * 
	 * @param starCount int
	 */
    public StarList(int starCount) {
        super(starCount);
    }

    /**
	 * Comment here.
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
    @Override
    public boolean add(StarNode starNode) {
        Star star = starNode.getStar();
        int x1 = star.getTileX();
        int y1base = (star.getTileY() / GameManager.PARSEC_SIZE) * GameManager.PARSEC_SIZE;
        int index = 0;
        for (; index < this.size(); index++) {
            StarNode starNode2 = get(index);
            Star star2 = starNode2.getStar();
            int x2 = star2.getTileX();
            int y2base = (star2.getTileY() / GameManager.PARSEC_SIZE) * GameManager.PARSEC_SIZE;
            if (y2base > y1base) break;
            if (y2base == y1base) {
                if (x1 < x2) break;
            }
        }
        super.add(index, starNode);
        return true;
    }

    /**
	 * Return the star with the given name.
	 * 
	 * @param name String
	 * @return StarNode
	 */
    public StarNode get(String name) {
        for (StarNode starNode : this) {
            if (starNode.getName().equals(name)) {
                return starNode;
            }
        }
        return null;
    }
}
