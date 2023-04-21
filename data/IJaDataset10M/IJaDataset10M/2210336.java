package za.co.me23.chat;

public class Point {

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int iX, int iY) {
        x = iX;
        y = iY;
    }

    public boolean compareTo(Point point) {
        return (x == point.x) && (y == point.y);
    }

    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    public int x;

    public int y;
}
