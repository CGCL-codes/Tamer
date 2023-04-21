package tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import datatypes.Vertex;

public class OpenSaveVertices {

    public static void saveVertices(List<Vertex> verts, String state) throws IOException {
        PrintWriter pw = new PrintWriter(state + ".txt");
        for (int i = 0; i < verts.size(); i++) {
            Vertex arr = verts.get(i);
            pw.printf("%f\t%f\n", arr.getX(), arr.getY());
        }
        pw.close();
    }

    public static List<Vertex> loadVertices(String state) throws IOException, ClassNotFoundException {
        Vector<Vertex> vertices = new Vector<Vertex>();
        Scanner sc = new Scanner(new File(state));
        double dx, dy;
        while (sc.hasNextDouble()) {
            dx = sc.nextDouble();
            dy = sc.nextDouble();
            vertices.addElement((new Vertex(dx, dy)));
        }
        sc.close();
        return vertices;
    }
}
