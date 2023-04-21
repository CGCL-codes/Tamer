package battlezone;

import java.awt.geom.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import javax.vecmath.Point3f;

public class GeometryObject {

    private static GeometryObject _instance;

    private Point3f[] tankLive;

    private Point3f[] tankDead;

    private Point3f[] projectile;

    public Point3f[] getTankLive() {
        return tankLive;
    }

    public Point3f[] getTankDead() {
        return tankDead;
    }

    public Point3f[] getProjectile() {
        return projectile;
    }

    private HashMap<String, ArrayList<Point3f>> _models = new HashMap<String, ArrayList<Point3f>>(3);

    public ArrayList<Point3f> getModel(String name) {
        return _models.get(name);
    }

    public static GeometryObject getInstance() {
        if (_instance == null) {
            _instance = new GeometryObject();
        }
        return _instance;
    }

    private GeometryObject() {
        try {
            File geometryFile = FileManager.getInstance().geometryFile;
            FileReader fname = new FileReader(geometryFile);
            BufferedReader reader = new BufferedReader(fname);
            ArrayList<Point3f> currentModel = null;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.startsWith(":")) {
                    currentModel = new ArrayList<Point3f>();
                    _models.put(line.substring(1), currentModel);
                } else if (line.equals("")) {
                } else {
                    String[] coordinates = line.trim().split("[\\s]+");
                    currentModel.add(new Point3f(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]), Float.parseFloat(coordinates[2])));
                    currentModel.add(new Point3f(Float.parseFloat(coordinates[3]), Float.parseFloat(coordinates[4]), Float.parseFloat(coordinates[5])));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
