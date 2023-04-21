package winecellar.util;

import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

/**
 * Stores the wine in an array list.
 * 
 * @author Dimitrij Pankratz, Anton Musichin
 * @version 1.4
 */
public class WineCellar extends ArrayList<Wine> {

    private static final long serialVersionUID = -6964435499363391690L;

    /**
	 * Returns the total price of wine cellar.
	 * 
	 * @return
	 */
    public double getTotalPrice() {
        double price = 0;
        Iterator<Wine> wineIterator = iterator();
        while (wineIterator.hasNext()) {
            Wine wine = wineIterator.next();
            if (wine != null) {
                if (wine.price != null && wine.stocks != null) {
                    price += wine.price * wine.stocks;
                }
            }
        }
        return price;
    }

    /**
	 * Sorts the contained wine.
	 * 
	 * @param c the comparator to determine the order of the list. 
	 *          A null value indicates that WineGrapeComparator 
	 *          should be used.
	 */
    public void sort(Comparator<Wine> c) {
        if (c == null) {
            c = new WineGrapeComparator();
        }
        Collections.sort(this, c);
    }

    /**
	 * Sorts the contained wine using WineGrapeComparator.
	 */
    public void sort() {
        sort(null);
    }

    /**
	 * Filters the wine cellar with the given filter.
	 * 
	 * @param f Filter to use.
	 * @return Filtered wine cellar.
	 * @throws NullPointerException if filter f is null.
	 */
    public WineCellar filter(Filter<Wine> f) throws NullPointerException {
        if (f == null) {
            throw new NullPointerException("Filter ist kein Objekt");
        } else {
            WineCellar filteredWineCeller = new WineCellar();
            Iterator<Wine> wineIterator = iterator();
            while (wineIterator.hasNext()) {
                Wine wine = wineIterator.next();
                if (f.filter(wine)) {
                    filteredWineCeller.add(wine);
                }
            }
            return filteredWineCeller;
        }
    }

    /**
	 * Returns all meals of the wine cellar.
	 * 
	 * @return
	 */
    public Meals getAllMeals() {
        Meals allMeals = new Meals();
        Iterator<Wine> wineIterator = iterator();
        while (wineIterator.hasNext()) {
            Wine wine = wineIterator.next();
            if (wine != null) {
                if (wine.meals != null) {
                    Iterator<String> mealsIterator = wine.meals.iterator();
                    while (mealsIterator.hasNext()) {
                        String meal = mealsIterator.next();
                        if (!allMeals.contains(meal) && meal != null) {
                            allMeals.add(meal);
                        }
                    }
                }
            }
        }
        return allMeals;
    }
}
