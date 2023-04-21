package model;

import ui.result.ResultPoint;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: May 1, 2007
 * Time: 7:47:04 AM
 */
public class MobilityModel extends TableObject {

    public MobilityModel(Color color) {
        super(color);
    }

    public MobilityModel(Color color, String name) {
        super(color, name);
    }

    protected void updatePointsColor() {
        for (ResultPoint point : points) {
            point.setColor2(color);
        }
    }

    public void addPoint(ResultPoint point) {
        this.points.add(point);
        point.setColor2(color);
    }

    public void removePoint(ResultPoint point) {
        this.points.remove(point);
        point.setColor(ResultPoint.DEFAULT_COLOR);
    }
}
