package net.sourceforge.spacebutcher2.game.spacebackground;

import java.awt.Color;

/**
 * Represents all possible stars groups with specific parameters, like:
 * <ul>
 * <li>speed,
 * <li>direction,
 * <li>color.
 * </ul>
 *
 * @author M.Olszewski
 */
public interface MovingStarGrouper {

    /**
   * Returns total number of groups.
   * 
   * @return Returns total number of groups.
   */
    public int getGroupsCount();

    /**
   * Returns specified group's direction.
   * 
   * @param groupIndex - index of the group.
   * 
   * @return Returns specified group's direction.
   */
    public boolean isGroupMovingLeft(int groupIndex);

    /**
   * Returns specified group's color.
   * 
   * @param groupIndex - index of the group.
   * 
   * @return Returns specified group's color.
   */
    public Color getGroupColor(int groupIndex);

    /**
   * Returns specified group's speed.
   * 
   * @param groupIndex - index of the group.
   * 
   * @return Returns specified group's speed.
   */
    public int getGroupSpeed(int groupIndex);
}
