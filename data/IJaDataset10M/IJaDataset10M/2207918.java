package poli;

public class Circle extends Shape {

    private int radius;

    public String drow(int x, int y, String color, int radius) {
        this.radius = radius;
        x = getX();
        y = getY();
        color = getColor();
        return ("Drowing Circle..." + " Radius=" + radius + " X=" + x + " Y=" + y + " Collor=" + color);
    }
}
