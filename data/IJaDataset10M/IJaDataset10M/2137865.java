package strauss.flatland.geometry;

import strauss.flatland.things.geometry.Vertex;

public abstract class Calculator {

    public static boolean isPointInRectangle(double x, double y, SimpleRectangle rect) {
        boolean output = false;
        System.out.println("compare");
        System.out.println(String.format("%f>=%f&&%f<=%f", x, rect.getLeft(), x, rect.getRight()));
        System.out.println(String.format("%f>=%f&&%f<=%f", y, rect.getBottom(), y, rect.getTop()));
        if (x >= rect.getLeft() && x <= rect.getRight()) {
            if (y >= rect.getTop() && y <= rect.getBottom()) {
                output = true;
            }
        }
        return output;
    }

    public static boolean isPointInRectangle(Vertex vertex, SimpleRectangle rect) {
        double x = vertex.getX();
        double y = vertex.getY();
        return isPointInRectangle(x, y, rect);
    }

    public static SimpleRectangle createBoundingRectangle(SimpleRectangle rect, Vertex vertex) {
        if (rect == null) {
            rect = new SimpleRectangle();
            rect.setLeft(vertex.getX());
            rect.setRight(vertex.getX());
            rect.setTop(vertex.getX());
            rect.setBottom(vertex.getX());
        }
        if (rect.getLeft() > vertex.getX()) {
            rect.setLeft(vertex.getX());
        }
        if (rect.getRight() < vertex.getX()) {
            rect.setRight(vertex.getX());
        }
        if (rect.getTop() > vertex.getY()) {
            rect.setTop(vertex.getY());
        }
        if (rect.getBottom() < vertex.getY()) {
            rect.setBottom(vertex.getY());
        }
        return rect;
    }
}
