package edu.asu.commons.foraging.fileplugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import edu.asu.commons.foraging.graphics.Point3D;
import edu.asu.commons.foraging.graphics.Triangulation;
import edu.asu.commons.foraging.graphics.Vector3D;
import edu.asu.commons.foraging.util.Tuple2f;
import edu.asu.commons.foraging.util.Tuple3i;

public class OBJFilePlugin {

    public static void readFile(String file, Triangulation object) {
        int index = -1;
        String mtlFilePath = "";
        Float x, y, z;
        int a, b, c;
        int t1 = 0, t2 = 0, t3 = 0;
        int vertexNormalIndex = 1;
        BufferedReader reader = FileLoader.getBufferedReader(file);
        try {
            String line = reader.readLine();
            while (line != null) {
                if (line.equals("")) {
                } else if (line.startsWith("#")) {
                } else if ((index = line.indexOf("mtllib")) != -1) {
                    index = file.lastIndexOf('/');
                    if (index == -1) {
                        index = file.lastIndexOf('\\');
                        mtlFilePath = file.substring(0, index);
                        mtlFilePath = mtlFilePath + "\\" + line.substring("mtllib ".length());
                    } else {
                        mtlFilePath = file.substring(0, index);
                        mtlFilePath = mtlFilePath + "/" + line.substring("mtllib ".length());
                    }
                } else if ((index = line.indexOf("usemtl")) != -1) {
                    String material = line.substring("usemtl ".length());
                    MTLFilePlugin.readMaterial(mtlFilePath, material, object);
                } else if ((index = line.indexOf("vn")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 2));
                    object.addVertexNormal(new Vector3D(Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken())), vertexNormalIndex++);
                } else if ((index = line.indexOf("vt")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 2));
                    object.addTextureCoordinate(new Tuple2f(Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken())));
                } else if ((index = line.indexOf("v")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 1));
                    x = Float.valueOf(coordinates.nextToken());
                    y = Float.valueOf(coordinates.nextToken());
                    z = Float.valueOf(coordinates.nextToken());
                    object.addVertex(new Point3D(x, y, z));
                } else if ((index = line.indexOf("f")) != -1) {
                    StringTokenizer face = new StringTokenizer(line.substring(index + 1));
                    StringTokenizer vertexTokenizer = new StringTokenizer(face.nextToken(), "/");
                    a = Integer.valueOf(vertexTokenizer.nextToken());
                    if (vertexTokenizer.hasMoreTokens()) t1 = Integer.valueOf(vertexTokenizer.nextToken());
                    vertexTokenizer = new StringTokenizer(face.nextToken(), "/");
                    b = Integer.valueOf(vertexTokenizer.nextToken());
                    if (vertexTokenizer.hasMoreTokens()) t2 = Integer.valueOf(vertexTokenizer.nextToken());
                    vertexTokenizer = new StringTokenizer(face.nextToken(), "/");
                    c = Integer.valueOf(vertexTokenizer.nextToken());
                    if (vertexTokenizer.hasMoreTokens()) t3 = Integer.valueOf(vertexTokenizer.nextToken());
                    object.addFace(new Tuple3i(a, b, c));
                } else if ((index = line.indexOf("fn")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 2));
                    object.addFaceNormal(new Vector3D(Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken())));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile(String filePath, Vector<Triangulation> objects) {
        try {
            int index = -1;
            String mtlFilePath = "";
            Triangulation object = null;
            BufferedReader reader = FileLoader.getBufferedReader(filePath);
            String line = reader.readLine();
            Float x, y, z;
            int a, b, c, d;
            int t1 = 0, t2 = 0, t3 = 0, t4 = 0;
            int vertexIndex = 0;
            int vertexCount = 0;
            int vertexNormalIndex = 1;
            while (line != null) {
                if (line.equals("")) {
                } else if (line.startsWith("#")) {
                } else if ((index = line.indexOf("mtllib")) != -1) {
                    mtlFilePath = filePath.substring(0, filePath.lastIndexOf('/'));
                    mtlFilePath = mtlFilePath + "/" + line.substring("mtllib ".length());
                } else if ((index = line.indexOf("g")) != -1) {
                    if (object != null) {
                        vertexCount += vertexIndex;
                        vertexIndex = 0;
                        vertexNormalIndex = 1;
                    }
                    object = new Triangulation();
                    objects.add(object);
                } else if ((index = line.indexOf("usemtl")) != -1) {
                    String material = line.substring("usemtl ".length());
                    MTLFilePlugin.readMaterial(mtlFilePath, material, object);
                } else if ((index = line.indexOf("vn")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 2));
                    object.addVertexNormal(new Vector3D(Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken())), vertexNormalIndex++);
                } else if ((index = line.indexOf("vt")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 2));
                    object.addTextureCoordinate(new Tuple2f(Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken())));
                } else if ((index = line.indexOf("v")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 1));
                    x = Float.valueOf(coordinates.nextToken());
                    y = Float.valueOf(coordinates.nextToken());
                    z = Float.valueOf(coordinates.nextToken());
                    object.addVertex(new Point3D(x, y, z));
                    vertexIndex++;
                } else if ((index = line.indexOf("fn")) != -1) {
                    StringTokenizer coordinates = new StringTokenizer(line.substring(index + 2));
                    object.addFaceNormal(new Vector3D(Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken()), Float.valueOf(coordinates.nextToken())));
                } else if ((index = line.indexOf("f")) != -1) {
                    StringTokenizer face = new StringTokenizer(line.substring(index + 1));
                    StringTokenizer vertexTokenizer = new StringTokenizer(face.nextToken(), "/");
                    a = Integer.valueOf(vertexTokenizer.nextToken());
                    a -= vertexCount;
                    if (vertexTokenizer.hasMoreTokens()) t1 = Integer.valueOf(vertexTokenizer.nextToken()) - vertexCount;
                    vertexTokenizer = new StringTokenizer(face.nextToken(), "/");
                    b = Integer.valueOf(vertexTokenizer.nextToken());
                    b -= vertexCount;
                    if (vertexTokenizer.hasMoreTokens()) t2 = Integer.valueOf(vertexTokenizer.nextToken()) - vertexCount;
                    vertexTokenizer = new StringTokenizer(face.nextToken(), "/");
                    c = Integer.valueOf(vertexTokenizer.nextToken());
                    c -= vertexCount;
                    if (vertexTokenizer.hasMoreTokens()) t3 = Integer.valueOf(vertexTokenizer.nextToken()) - vertexCount;
                    object.addFace(new Tuple3i(a, b, c));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
