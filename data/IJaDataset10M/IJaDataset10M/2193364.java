package marten.age.graphics.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.TriangularGeometry;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.TextureCoordinate;
import marten.age.graphics.primitives.Triangle;
import marten.age.graphics.primitives.Vector;
import marten.age.graphics.primitives.Vertex;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import org.apache.log4j.Logger;

public class ObjModelLoader implements GenericModelLoader {

    private static org.apache.log4j.Logger log = Logger.getLogger(ObjModelLoader.class);

    private ArrayList<Point> vertexes = new ArrayList<Point>();

    private ArrayList<Vector> normals = new ArrayList<Vector>();

    private ArrayList<TextureCoordinate> texcoords = new ArrayList<TextureCoordinate>();

    private ArrayList<Triangle> mesh;

    private HashMap<String, Appearance> appearances = new HashMap<String, Appearance>();

    private String path;

    private int lineNum = 1;

    ComplexModel model = new ComplexModel();

    SimpleModel modelPart;

    public ComplexModel load(BufferedReader reader, String path) throws Exception {
        this.path = path;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] params = line.split(" ");
            String command = params[0];
            params = Arrays.copyOfRange(params, 1, params.length);
            if (command.equals("v")) {
                checkLine(params, 3);
                double x = Double.parseDouble(params[0]);
                double y = Double.parseDouble(params[1]);
                double z = Double.parseDouble(params[2]);
                Point p = new Point(x, y, z);
                vertexes.add(p);
            } else if (command.equals("vn")) {
                checkLine(params, 3);
                double x = Double.parseDouble(params[0]);
                double y = Double.parseDouble(params[1]);
                double z = Double.parseDouble(params[2]);
                Vector v = new Vector(x, y, z);
                normals.add(v);
            } else if (command.equals("vt")) {
                checkLine(params, 3);
                double x = Double.parseDouble(params[0]);
                double y = Double.parseDouble(params[1]);
                double z = Double.parseDouble(params[2]);
                TextureCoordinate t = new TextureCoordinate(x, y, z);
                texcoords.add(t);
            } else if (command.equals("f")) {
                checkLine(params, 3);
                Triangle t = getTriangle(params);
                mesh.add(t);
            } else if (command.equals("o")) {
                mesh = new ArrayList<Triangle>();
                TriangularGeometry geo = new TriangularGeometry(mesh);
                modelPart = new SimpleModel(geo);
                model.addPart(modelPart);
            } else if (command.equals("#") || command.equals("s")) {
            } else if (command.equals("mtllib")) {
                checkLine(params, 1);
                parseMtlFile(params[0]);
            } else if (command.equals("usemtl")) {
                checkLine(params, 1);
                Appearance a = appearances.get(params[0]);
                if (a == null) {
                    log.error("No such appearance " + params[0] + " (" + lineNum + ")");
                } else {
                    modelPart.setAppearance(a);
                }
            } else {
                log.warn("Unrecognized obj file command '" + command + "' on line " + lineNum);
            }
            lineNum++;
        }
        return model;
    }

    private Triangle getTriangle(String[] params) {
        int size = params.length;
        Vertex[] vertices = new Vertex[3];
        for (int i = 0; i < size; i++) {
            String[] vertexParams = params[i].split("/");
            int vertexid = Integer.parseInt(vertexParams[0]);
            if (vertexid < 0) vertexid = vertexes.size() - vertexid; else vertexid--;
            vertices[i] = new Vertex();
            vertices[i].coordinates = vertexes.get(vertexid);
            if (!vertexParams[1].isEmpty()) {
                int texcoordid = Integer.parseInt(vertexParams[1]);
                if (texcoordid < 0) texcoordid = texcoords.size() - texcoordid; else texcoordid--;
                vertices[i].textureCoordinates = texcoords.get(texcoordid);
            }
            if (!vertexParams[2].isEmpty()) {
                int normalid = Integer.parseInt(vertexParams[2]);
                if (normalid < 0) normalid = normals.size() - normalid; else normalid--;
                vertices[i].normal = normals.get(normalid);
            }
        }
        Triangle t = new Triangle(vertices[0], vertices[1], vertices[2]);
        return t;
    }

    private void checkLine(String[] params, int length) {
        if (params.length != length) {
            throw new RuntimeException("Invalid file format: " + params.length + " fields instead of " + length + "on line " + lineNum);
        }
    }

    private void parseMtlFile(String filename) throws Exception {
        int num = 1;
        String name = path + filename;
        File inputFile = new File(name);
        DataInputStream stream = new DataInputStream(new FileInputStream(inputFile));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        boolean firstMaterial = true;
        String title = null;
        Texture texture = null;
        float shininess = -1;
        Color specular = null;
        Color diffuse = null;
        Color ambient = null;
        Appearance app = null;
        while ((line = reader.readLine()) != null) {
            String[] params = line.split(" ");
            String command = params[0].trim();
            params = Arrays.copyOfRange(params, 1, params.length);
            if (command.equals("newmtl")) {
                app = new Appearance();
                if (!firstMaterial) {
                    if (shininess != -1) {
                        if (shininess > 128.0f) shininess = 128.0f;
                        app.setShininess(shininess);
                    }
                    if (ambient != null) app.setAmbient(ambient);
                    if (diffuse != null) app.setDiffuse(diffuse);
                    if (specular != null) app.setSpecular(specular);
                    if (texture != null) app.setTexture(texture);
                    appearances.put(title, app);
                    shininess = -1;
                    ambient = null;
                    diffuse = null;
                    specular = null;
                    texture = null;
                } else {
                    firstMaterial = false;
                }
                checkLine(params, 1);
                title = params[0];
            } else if (command.equals("Ns")) {
                checkLine(params, 1);
                shininess = Float.parseFloat(params[0]);
            } else if (command.equals("Ka")) {
                checkLine(params, 3);
                ambient = colorStringsToFloats(params);
            } else if (command.equals("Kd")) {
                checkLine(params, 3);
                diffuse = colorStringsToFloats(params);
            } else if (command.equals("Ks")) {
                checkLine(params, 3);
                specular = colorStringsToFloats(params);
            } else if (command.equals("map_Kd")) {
                checkLine(params, 1);
                texture = TextureLoader.loadTexture(new ImageData(path + params[0]));
            } else if (command.isEmpty() || command.equals("#") || command.equals("illum")) {
            } else {
                log.warn("Unrecognized mtl file command '" + command + "' on line " + num);
            }
            num++;
        }
        if (shininess != -1) {
            if (shininess > 128.0f) shininess = 128.0f;
            app.setShininess(shininess);
        }
        if (ambient != null) app.setAmbient(ambient);
        if (diffuse != null) app.setDiffuse(diffuse);
        if (specular != null) app.setSpecular(specular);
        if (texture != null) app.setTexture(texture);
        appearances.put(title, app);
    }

    private Color colorStringsToFloats(String[] strings) {
        float[] floats = new float[4];
        floats[3] = 1.0f;
        for (int i = 0; i < strings.length; i++) {
            floats[i] = Float.parseFloat(strings[i]);
        }
        return new Color(floats[0], floats[1], floats[2], floats[3]);
    }
}
