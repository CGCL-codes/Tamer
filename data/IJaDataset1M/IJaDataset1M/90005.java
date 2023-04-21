package jarcade.utilities;

import java.util.HashSet;

/**
 *
 * @author Owner
 */
public class MoveSet extends HashSet<Movable> implements Movable {

    public void move(long elapsedMilliseconds) {
        for (Movable m : this) {
            m.move(elapsedMilliseconds);
        }
    }
}
