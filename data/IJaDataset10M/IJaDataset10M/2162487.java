package hotgammon.domain;

import java.util.*;

public class BoardHotgammon implements Board {

    private ArrayList<Location> locations = new ArrayList<Location>();

    public BoardHotgammon() {
        this.reset();
    }

    /** Resets all board checkers to their initial position */
    public void reset() {
        for (Iterator<Location> i = this.iterator(); i.hasNext(); ) {
            ((Location) i.next()).setCheckers(0);
        }
        locations.clear();
        locations.add(Location.B_BAR);
        locations.add(Location.R1);
        locations.add(Location.R2);
        locations.add(Location.R3);
        locations.add(Location.R4);
        locations.add(Location.R5);
        locations.add(Location.R6);
        locations.add(Location.R7);
        locations.add(Location.R8);
        locations.add(Location.R9);
        locations.add(Location.R10);
        locations.add(Location.R11);
        locations.add(Location.R12);
        locations.add(Location.B12);
        locations.add(Location.B11);
        locations.add(Location.B10);
        locations.add(Location.B9);
        locations.add(Location.B8);
        locations.add(Location.B7);
        locations.add(Location.B6);
        locations.add(Location.B5);
        locations.add(Location.B4);
        locations.add(Location.B3);
        locations.add(Location.B2);
        locations.add(Location.B1);
        locations.add(Location.R_BAR);
        locations.add(Location.B_BEAR_OFF);
        locations.add(Location.R_BEAR_OFF);
        (locations.get(1)).setCheckers(2);
        (locations.get(6)).setCheckers(-5);
        (locations.get(8)).setCheckers(-3);
        (locations.get(12)).setCheckers(5);
        (locations.get(13)).setCheckers(-5);
        (locations.get(17)).setCheckers(3);
        (locations.get(19)).setCheckers(5);
        (locations.get(24)).setCheckers(-2);
    }

    /** Move one checker from one location to another.
     * PRECONDITION: The move must be valid on the present board!
     * @param from the location to move the checker from
     * @param to the location to move the checker to
     */
    public void move(Location from, Location to) {
        if (from.getCheckers() > 0) {
            from.setCheckers((from.getCheckers() - 1));
            to.setCheckers((to.getCheckers() + 1));
        } else {
            from.setCheckers((from.getCheckers() + 1));
            to.setCheckers((to.getCheckers() - 1));
        }
    }

    private int getCheckerNumerical(Location location) {
        return location.getCheckers();
    }

    /** Get the colour of the checkers on a given location.
     *
     * @param location the location on the board to check
     * @return the color of the checkers on this location
     */
    public Color getColor(Location location) {
        return Color.getColorFromNumerical(getCheckerNumerical(location));
    }

    /** Get the count of checkers of this location.
     * @param location the location to inspect
     * @return a integer value showing the number of checkers on this location.
     */
    public int getCount(Location location) {
        return Math.abs(getCheckerNumerical(location));
    }

    /** Return an interator that will enumerate all Locations on the board.
     * Each location is simply the location enumeration object, thus you must
     * access the location's contents through the board interface.
     * Example:
     *
     * for (Location l: board) {
     *  noOfCheckers += board.getCount(l);
     * }
     *
     * @return an iterator over locations on the board
     */
    public Iterator<Location> iterator() {
        return locations.iterator();
    }

    public int getNumberOfCheckers() {
        int res = 0;
        for (Iterator<Location> i = this.iterator(); i.hasNext(); ) {
            res += this.getCount(i.next());
        }
        return res;
    }
}
