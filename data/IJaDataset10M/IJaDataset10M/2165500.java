package bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Road extends DeepCopy<Road> {

    private static final long serialVersionUID = 8214872821031755251L;

    private int id;

    private String name;

    private Point[] point;

    private List<Point> points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point[] getPoint() {
        return point;
    }

    public void setPoint(Point[] p) {
        this.point = p;
        this.points = new ArrayList<Point>(Arrays.asList(this.point));
    }

    @JSONField(serialize = false)
    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
        if (points != null) this.point = this.points.toArray(new Point[this.points.size()]);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
