package ucalgary.ebe.ci.gestures.recognition.impl.optimoz;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author hkolenda
 * 
 */
public class MouseDirectionList {

    private Stack<Character> directions = new Stack<Character>();

    /**
     * adds an direction to the list of directions, if it is different from the
     * last direction
     * 
     * @param direction
     *            direction to add to list of directions
     * 
     * @exception NullPointerException,
     *                if <b>direction</b> is null
     */
    public boolean add(char direction) {
        char lastDirection = Directions.NONE;
        try {
            lastDirection = directions.lastElement();
        } catch (NoSuchElementException e) {
        }
        if ((direction != Directions.NONE) && (direction != lastDirection)) {
            directions.push(direction);
            return true;
        }
        return false;
    }

    /**
     * Returns the current List of the directions
     * 
     * @return GestureCode
     */
    public String getList() {
        String gesture = "";
        for (Iterator directionIter = directions.iterator(); directionIter.hasNext(); ) {
            String direction = String.valueOf(directionIter.next());
            gesture += direction;
        }
        return gesture;
    }

    /**
     * Empties the Queue of the last directions the mouse has moved
     * 
     */
    public void clear() {
        directions.clear();
    }
}
