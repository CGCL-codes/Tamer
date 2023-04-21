package sanguo.dataclass;

public class MapRectangle {

    public MapPoint center;

    public MapPoint left, bottom, right, top;

    public MapRectangle(MapPoint center) {
        int x = center.x;
        int y = center.y;
        this.left.x = x - 3;
        this.left.y = y - 3;
        this.bottom.x = x + 3;
        this.bottom.y = y - 3;
        this.right.x = x + 3;
        this.right.y = y + 3;
        this.top.x = x - 3;
        this.top.y = y + 3;
    }
}
