package graphics;

public class Square extends Shape {

    private double size;

    public Square(double x, double y, double s) {
        super(x, y);
        size = s;
    }

    public void setSize(double s) {
        size = s;
    }

    public double getSize() {
        return size;
    }

    public void draw(Renderer renderer) {
        renderer.draw(this);
    }
}
