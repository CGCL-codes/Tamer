package de.peacei.gae.foodsupplier.parser;

import de.peacei.gae.foodsupplier.data.Food;

/**
 * @author peacei
 *
 */
public interface FoodParser {

    public Food[][] parseFood();

    public int parseWeekNumber();
}
