package usual_training_OOP;

public class Program1 {

    public static void main(String[] args) {
        Point p1 = new Point();
        Point p2 = new Point(77, 88);
        Point p3 = new Point();
        p1.x = 131;
        p1.y = 232;
        System.out.println("p1.x= " + p1.x + "       p1.y= " + p1.y);
        System.out.println("p2.x= " + p2.x + "       p2.y= " + p2.y);
        System.out.println("p3.x= " + p3.x + "       p3.y= " + p3.y);
        System.out.println("distance (0,0) - p2 = " + p2.distance(0, 0));
        System.out.println("distance p1 - p2 = " + p2.distance(p1.x, p1.y));
        System.out.println("distance p1 - p2 = " + p2.distance(p1));
        System.out.println("-----------------------------------------------------------------");
        Point3D p4 = new Point3D();
        Point3D p5 = new Point3D(25, 37, 44);
        System.out.println("p4.x= " + p4.x + "       p4.y= " + p4.y + "       p4.z= " + p4.z);
        System.out.println("p5.x= " + p5.x + "       p5.y= " + p5.y + "       p5.z= " + p5.z);
        System.out.println("distance (0,0) - p5 = " + p5.distance(0, 0));
        System.out.println("distance (0,0,0) - p5 = " + p5.distance(0, 0, 0));
        System.out.println("distance (0,0,0) - p5 = " + p5.distance(p4));
    }
}
