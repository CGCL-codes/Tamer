package so;

public class Point {

    private int x;

    private int y;

    private int counter = 0;

    public Point(int xP, int yP) {
        x = xP;
        y = yP;
    }

    public void add() {
        this.counter++;
        System.out.println(counter);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
