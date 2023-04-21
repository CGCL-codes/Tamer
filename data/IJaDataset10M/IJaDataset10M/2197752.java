package sangria.board;

import java.awt.Polygon;
import sangria.play.Player;

/**
 * @author Wouter Lievens
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Province extends Location {

    private Player owner_;

    public Province(Player owner, Polygon shape) {
        super(shape);
        owner_ = owner;
    }

    public Player getOwner() {
        return owner_;
    }

    public String toString() {
        return owner_.getName() + "'s Province";
    }
}
