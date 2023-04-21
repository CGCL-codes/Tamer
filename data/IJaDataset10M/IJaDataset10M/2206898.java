package zildo.platform.input;

import java.util.List;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;

/**
 * @author Tchegito
 *
 */
public class TouchMovement {

    List<Point> points;

    Angle current;

    Point save;

    Point prec;

    public TouchMovement(List<Point> points) {
        this.points = points;
        this.current = null;
        this.save = null;
        this.prec = null;
    }

    public void render() {
        if (points.size() != 0) {
            if (save == null) {
                save = new Point(points.get(0));
            } else {
                if (prec == null) {
                    prec = save;
                }
                save = new Point(points.get(0));
                if (!prec.equals(save)) {
                    if (prec.distance(save) >= 5) {
                        Angle tempAngle = Angle.fromDirection((save.x - prec.x), (save.y - prec.y));
                        if (tempAngle != current) {
                            current = tempAngle;
                            prec = new Point(save);
                        }
                    }
                }
            }
        } else {
            current = null;
            save = null;
            prec = null;
        }
    }

    public Angle getCurrent() {
        return current;
    }
}
