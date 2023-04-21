package ch2;

import java.awt.Rectangle;

public class P2_1 {

    static int width = 0;

    static int height = 0;

    public static void main(String[] args) {
        if (args.length > 0) {
            width = Integer.parseInt(args[0].toString());
            height = Integer.parseInt(args[1].toString());
        }
        Rectangle r = new Rectangle(width, height);
        System.out.printf("The rectangle with width: %s and height: %s has an area of: %s.", r.getWidth(), r.getHeight(), AreaTester(r));
        System.out.println();
        System.out.printf("The rectangle with width: %s and height: %s has a perimeter of: %s.", r.getWidth(), r.getHeight(), PerimeterTester(r));
    }

    public static double AreaTester(Rectangle r) {
        double area;
        area = r.getWidth() * r.getHeight();
        return area;
    }

    public static double PerimeterTester(Rectangle r) {
        double perimeter;
        perimeter = 2 * r.getWidth() + 2 * r.getHeight();
        return perimeter;
    }
}
