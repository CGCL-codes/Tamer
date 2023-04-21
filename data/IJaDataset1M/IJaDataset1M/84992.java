package eu.cherrytree.paj.obj;

import eu.cherrytree.paj.file.EndianConverter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

public class OBJConverter {

    private static final int MagickNumber = 20090924;

    private static final int EndNumber = 1701;

    private static Vector<Float> Vertices = new Vector<Float>();

    private static Vector<Float> Normals = new Vector<Float>();

    private static Vector<Float> Coordinates = new Vector<Float>();

    private static Vector<MeshObject> Objects = new Vector<MeshObject>();

    private static Vector<VertexObject> VertexObjects = new Vector<VertexObject>();

    private static HashMap<String, Material> Materials = new HashMap<String, Material>();

    private static Vector<MeshInput> MeshInputs = new Vector<MeshInput>();

    private static int currentObject = 0;

    private static boolean errorOccured = false;

    private static String filePath = "";

    private static boolean changeXY = false, changeYZ = false, changeZX = false;

    public static void main(String[] args) {
        System.out.println("OBJ text converter.");
        System.out.println("Copyright 2009, 2010 Cherry Tree Studio");
        if (args.length == 0) {
            System.out.println();
            System.out.println("No arguments given!");
            System.out.println();
            System.out.println("Use \"help\" for help.");
            return;
        } else if (args[0].equals("help")) {
            System.out.println();
            System.out.println("Options:");
            System.out.println("xy , yx - exchange the X and Y axes.");
            System.out.println("yz , zy - exchange the Y and Z axes.");
            System.out.println("zx , xz - exchange the Z and X axes.");
            System.out.println();
            System.out.println("Note:");
            System.out.println("You can exchange only two axes at a time.");
            System.out.println();
            System.out.println("Markers:");
            System.out.println("m: - specifiess path to an .obj file.");
            System.out.println("t: specifies path to a skin.");
            System.out.println("o: specifies output path for the converted mesh.");
            System.out.println();
            System.out.println("Example 1:");
            System.out.println("java -jar PAJMeshConverter.jar xy m:[path to .obj file]");
            System.out.println();
            System.out.println("Example 2:");
            System.out.println("java -jar PAJMeshConverter.jar m:[path to .obj file] t:[path to skin 1 for object 1] [path to skin 2 for object 1] [...] t:[path to skin 1 for object 2] [path to skin 2 for object 2] t:[...]");
            System.out.println();
            System.out.println("Example 3:");
            System.out.println("java -jar PAJMeshConverter.jar zx m:[path to .obj file 2] t:[path to skin 1 for object 1 for file 1] o:[output path for file 1] m:[path to .obj file 2] t:[path to skin 1 for object 1 for file 2] o:[output path for file 2]");
            System.out.println();
            System.out.println("Note:");
            System.out.println("If skins for object n+1 is given, a skin for object n must also be given.");
            System.out.println();
        }
        BufferedReader in = null;
        DataOutputStream out = null;
        int beg = 0;
        if (args[beg].equals("xy") || args[beg].equals("yx")) {
            changeXY = true;
            beg++;
        } else if (args[beg].equals("yz") || args[beg].equals("zy")) {
            changeYZ = true;
            beg++;
        } else if (args[beg].equals("zx") || args[beg].equals("xz")) {
            changeZX = true;
            beg++;
        }
        if (!generateObjectInputs(beg, args)) {
            System.out.println();
            System.out.println("Error in given arguments.");
            return;
        }
        for (int fileindex = 0; fileindex < MeshInputs.size(); fileindex++) {
            String path = MeshInputs.get(fileindex).objPath;
            if (!path.contains(".obj")) {
                System.out.println();
                System.out.println(path + " does not not have .obj extension.");
                System.out.println("Will atempt to convert anyway.");
            }
            System.out.println();
            System.out.println("Converting file: " + path);
            currentObject = 0;
            String data;
            int line = 0;
            int pathend = path.lastIndexOf(File.separatorChar);
            if (pathend < 0) filePath = ""; else filePath = path.substring(0, pathend + 1);
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            } catch (Exception e) {
                System.out.println();
                System.out.println("Cannot open file.");
                errorOccured = true;
                e.printStackTrace();
            }
            while (!errorOccured) {
                line++;
                try {
                    data = in.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                if (data == null) break;
                if (data.contains("o ")) {
                    Objects.add(new MeshObject(data.substring(getValueIndex(data, "o"))));
                    currentObject++;
                } else if (data.contains("v ")) {
                    String vs = data.substring(getValueIndex(data, "v"));
                    String v1 = vs.substring(0, vs.indexOf(' '));
                    vs = vs.substring(vs.indexOf(' ') + 1);
                    String v2 = vs.substring(0, vs.indexOf(' '));
                    String v3 = vs.substring(vs.indexOf(' '));
                    try {
                        if (changeXY) {
                            Vertices.add(Float.parseFloat(v2));
                            Vertices.add(Float.parseFloat(v1));
                            Vertices.add(Float.parseFloat(v3));
                        } else if (changeYZ) {
                            Vertices.add(Float.parseFloat(v1));
                            Vertices.add(Float.parseFloat(v3));
                            Vertices.add(Float.parseFloat(v2));
                        } else if (changeZX) {
                            Vertices.add(Float.parseFloat(v3));
                            Vertices.add(Float.parseFloat(v2));
                            Vertices.add(Float.parseFloat(v1));
                        } else {
                            Vertices.add(Float.parseFloat(v1));
                            Vertices.add(Float.parseFloat(v2));
                            Vertices.add(Float.parseFloat(v3));
                        }
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Vertices parsing failed at line " + line + ".");
                        e.printStackTrace();
                        errorOccured = true;
                        break;
                    }
                } else if (data.contains("vt ")) {
                    String vs = data.substring(getValueIndex(data, "vt"));
                    String v1 = vs.substring(0, vs.indexOf(' '));
                    String v2 = vs.substring(vs.indexOf(' ') + 1);
                    if (v2.contains(" ")) {
                        v2 = v2.substring(1);
                        v2 = v2.substring(0, v2.indexOf(' '));
                    }
                    try {
                        Coordinates.add(Float.parseFloat(v1));
                        Coordinates.add(Float.parseFloat(v2));
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Texture coordinates parsing failed at line " + line + ".");
                        e.printStackTrace();
                        errorOccured = true;
                        break;
                    }
                } else if (data.contains("vn ")) {
                    String vs = data.substring(getValueIndex(data, "vn"));
                    String v1 = vs.substring(0, vs.indexOf(' '));
                    vs = vs.substring(vs.indexOf(' ') + 1);
                    String v2 = vs.substring(0, vs.indexOf(' '));
                    String v3 = vs.substring(vs.indexOf(' '));
                    try {
                        Normals.add(Float.parseFloat(v1));
                        Normals.add(Float.parseFloat(v2));
                        Normals.add(Float.parseFloat(v3));
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Normals parsing failed at line " + line + ".");
                        e.printStackTrace();
                        errorOccured = true;
                        break;
                    }
                } else if (data.contains("f ")) {
                    String facestring = data.substring(getValueIndex(data, "f"));
                    int facesize = 1;
                    for (int c = 0; c < facestring.length() - 1; c++) {
                        if (facestring.charAt(c) == ' ') facesize++;
                    }
                    int[] facearray = new int[3 * facesize];
                    for (int c = 0; c < 3 * facesize; c++) {
                        int index = Math.min(facestring.indexOf('/'), facestring.indexOf(' '));
                        if (index < 0) index = Math.max(facestring.indexOf('/'), facestring.indexOf(' '));
                        if (index < 0) index = facestring.length();
                        if (index == 0) facearray[c] = -1; else facearray[c] = Integer.parseInt(facestring.substring(0, index));
                        if (index < facestring.length()) facestring = facestring.substring(index + 1);
                    }
                    int numberoftriangles = facesize - 2;
                    int[] trianglearray = new int[3 * 3 * numberoftriangles];
                    int shift = -1;
                    for (int i = 0; i < numberoftriangles * 3; i++) {
                        if (i % 3 == 0) {
                            trianglearray[0 + i * 3] = facearray[0];
                            trianglearray[1 + i * 3] = facearray[1];
                            trianglearray[2 + i * 3] = facearray[2];
                            shift++;
                        } else {
                            trianglearray[0 + i * 3] = facearray[0 + (i - shift * 2) * 3];
                            trianglearray[1 + i * 3] = facearray[1 + (i - shift * 2) * 3];
                            trianglearray[2 + i * 3] = facearray[2 + (i - shift * 2) * 3];
                        }
                    }
                    try {
                        for (int i = 0; i < numberoftriangles * 3; i++) getCurrentObject().indices.add(checkIndex(trianglearray[0 + i * 3], trianglearray[1 + i * 3], trianglearray[2 + i * 3]));
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Faces parsing failed at line " + line + ".");
                        e.printStackTrace();
                        errorOccured = true;
                        break;
                    }
                } else if (data.contains("usemtl ")) {
                    try {
                        getCurrentObject().meshMaterial = new Material((Material) (Materials.get(data.substring(getValueIndex(data, "usemtl")))));
                        getCurrentObject().meshMaterial.skins = new String[getCurrentObjectInput(fileindex).skinPaths.size()];
                        for (int i = 0; i < getCurrentObjectInput(fileindex).skinPaths.size(); i++) getCurrentObject().meshMaterial.skins[i] = getCurrentObjectInput(fileindex).skinPaths.get(i);
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("An error occured in material parsing.");
                        e.printStackTrace();
                        errorOccured = true;
                        break;
                    }
                } else if (data.contains("g ")) {
                    System.out.println();
                    System.out.println("Found group: " + data.substring(getValueIndex(data, "g ")));
                    System.out.println("However groups are not yet implemented.");
                } else if (data.contains("mtllib ")) {
                    System.out.println();
                    System.out.println("Parsing material file: " + data.substring(getValueIndex(data, "mtllib")));
                    try {
                        BufferedReader mtin = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + data.substring(7))));
                        int mtline = 0;
                        String mtdata;
                        String currentMaterial = "";
                        while (true) {
                            mtdata = mtin.readLine();
                            if (mtdata == null) break;
                            if (mtdata.contains("# ")) {
                            } else if (mtdata.contains("newmtl ")) {
                                currentMaterial = mtdata.substring(getValueIndex(mtdata, "newmtl"));
                                Materials.put(currentMaterial, new Material());
                            } else if (mtdata.contains("Ns ")) {
                                ((Material) (Materials.get(currentMaterial))).shininess = 256.0f * (Float.parseFloat(mtdata.substring(getValueIndex(mtdata, "Ns"))) / 100.0f);
                            } else if (mtdata.contains("d ") && !mtdata.contains("Kd ") && !mtdata.contains("map")) {
                                ((Material) (Materials.get(currentMaterial))).ambientColor.alpha = Float.parseFloat(mtdata.substring(getValueIndex(mtdata, "d")));
                                ((Material) (Materials.get(currentMaterial))).diffuseColor.alpha = Float.parseFloat(mtdata.substring(getValueIndex(mtdata, "d")));
                                ((Material) (Materials.get(currentMaterial))).specularColor.alpha = Float.parseFloat(mtdata.substring(getValueIndex(mtdata, "d")));
                                ((Material) (Materials.get(currentMaterial))).emissiveColor.alpha = Float.parseFloat(mtdata.substring(getValueIndex(mtdata, "d")));
                            } else if (mtdata.contains("Kd ") && !mtdata.contains("map")) {
                                String color = mtdata.substring(getValueIndex(mtdata, "Kd"));
                                String R = color.substring(0, color.indexOf(' '));
                                color = color.substring(color.indexOf(' ') + 1);
                                String G = color.substring(0, color.indexOf(' '));
                                String B = color.substring(color.indexOf(' '));
                                try {
                                    ((Material) (Materials.get(currentMaterial))).diffuseColor.red = Float.parseFloat(R);
                                    ((Material) (Materials.get(currentMaterial))).diffuseColor.green = Float.parseFloat(G);
                                    ((Material) (Materials.get(currentMaterial))).diffuseColor.blue = Float.parseFloat(B);
                                } catch (Exception e) {
                                    System.out.println();
                                    System.out.println("Material diffuse color parsing failed at line " + mtline + ".");
                                    e.printStackTrace();
                                    errorOccured = true;
                                    break;
                                }
                            } else if (mtdata.contains("Ka ") && !mtdata.contains("map")) {
                                String color = mtdata.substring(getValueIndex(mtdata, "Ka"));
                                String R = color.substring(0, color.indexOf(' '));
                                color = color.substring(color.indexOf(' ') + 1);
                                String G = color.substring(0, color.indexOf(' '));
                                String B = color.substring(color.indexOf(' '));
                                try {
                                    ((Material) (Materials.get(currentMaterial))).ambientColor.red = Float.parseFloat(R);
                                    ((Material) (Materials.get(currentMaterial))).ambientColor.green = Float.parseFloat(G);
                                    ((Material) (Materials.get(currentMaterial))).ambientColor.blue = Float.parseFloat(B);
                                } catch (Exception e) {
                                    System.out.println();
                                    System.out.println("Material ambient color parsing failed at line " + mtline + ".");
                                    e.printStackTrace();
                                    errorOccured = true;
                                    break;
                                }
                            } else if (mtdata.contains("Ks ") && !mtdata.contains("map")) {
                                String color = mtdata.substring(getValueIndex(mtdata, "Ks"));
                                String R = color.substring(0, color.indexOf(' '));
                                color = color.substring(color.indexOf(' ') + 1);
                                String G = color.substring(0, color.indexOf(' '));
                                String B = color.substring(color.indexOf(' '));
                                try {
                                    ((Material) (Materials.get(currentMaterial))).specularColor.red = Float.parseFloat(R);
                                    ((Material) (Materials.get(currentMaterial))).specularColor.green = Float.parseFloat(G);
                                    ((Material) (Materials.get(currentMaterial))).specularColor.blue = Float.parseFloat(B);
                                } catch (Exception e) {
                                    System.out.println();
                                    System.out.println("Material specular color parsing failed at line " + mtline + ".");
                                    e.printStackTrace();
                                    errorOccured = true;
                                    break;
                                }
                            } else if (mtdata.contains("Ke ") && !mtdata.contains("map")) {
                                String color = mtdata.substring(getValueIndex(mtdata, "Ke"));
                                String R = color.substring(0, color.indexOf(' '));
                                color = color.substring(color.indexOf(' ') + 1);
                                String G = color.substring(0, color.indexOf(' '));
                                String B = color.substring(color.indexOf(' '));
                                try {
                                    ((Material) (Materials.get(currentMaterial))).emissiveColor.red = Float.parseFloat(R);
                                    ((Material) (Materials.get(currentMaterial))).emissiveColor.green = Float.parseFloat(G);
                                    ((Material) (Materials.get(currentMaterial))).emissiveColor.blue = Float.parseFloat(B);
                                } catch (Exception e) {
                                    System.out.println();
                                    System.out.println("Material emission color parsing failed at line " + mtline + ".");
                                    e.printStackTrace();
                                    errorOccured = true;
                                    break;
                                }
                            } else if (mtdata.contains("map")) {
                                if (mtdata.contains("map_Kd") || mtdata.contains("map_Ka") || mtdata.contains("map_Ke")) ((Material) (Materials.get(currentMaterial))).defaultTexture = mtdata.substring(getValueIndex(mtdata, "map") + 4); else if (mtdata.contains("map_d")) ((Material) (Materials.get(currentMaterial))).defaultTexture = mtdata.substring(getValueIndex(mtdata, "map") + 3); else ((Material) (Materials.get(currentMaterial))).defaultTexture = mtdata.substring(getValueIndex(mtdata, "map"));
                            } else if (mtdata.contains("illum")) {
                                int illum = Integer.parseInt(mtdata.substring(getValueIndex(mtdata, "illum")));
                                System.out.println();
                                switch(illum) {
                                    case 0:
                                        System.out.println("Found illumination model definition: \"Color on and Ambient off.\"");
                                        break;
                                    case 1:
                                        System.out.println("Found illumination model definition: \"Color on and Ambient on.\"");
                                        break;
                                    case 2:
                                        System.out.println("Found illumination model definition: \"Highlight on.\"");
                                        break;
                                    case 3:
                                        System.out.println("Found illumination model definition: \"Reflection on and Ray trace on.\"");
                                        break;
                                    case 4:
                                        System.out.println("Found illumination model definition: \"Transparency: Glass on. Reflection: Ray trace on.\"");
                                        break;
                                    case 5:
                                        System.out.println("Found illumination model definition: \"Reflection: Fresnel on and Ray trace on.\"");
                                        break;
                                    case 6:
                                        System.out.println("Found illumination model definition: \"Transparency: Refraction on. Reflection: Fresnel off and Ray trace on.\"");
                                        break;
                                    case 7:
                                        System.out.println("Found illumination model definition: \"Transparency: Refraction on. Reflection: Fresnel on and Ray trace on.\"");
                                        break;
                                    case 8:
                                        System.out.println("Found illumination model definition: \"Reflection on and Ray trace off.\"");
                                        break;
                                    case 9:
                                        System.out.println("Found illumination model definition: \"Transparency: Glass on. Reflection: Ray trace off.\"");
                                        break;
                                    case 10:
                                        System.out.println("Found illumination model definition: \"Casts shadows onto invisible surfaces.\"");
                                        break;
                                }
                                System.out.println("However illumination models are not yet implemented.");
                            } else if (!mtdata.equals("")) {
                                System.out.println();
                                System.out.println("Unknown marker at line " + mtline + ": \"" + mtdata + "\" found int the material file.");
                            }
                            mtline++;
                        }
                        System.out.println();
                        System.out.println("Material file: " + data.substring(getValueIndex(data, "mtllib")) + " parsed successfully");
                        mtin.close();
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Material parsing failed.");
                        e.printStackTrace();
                        errorOccured = true;
                        break;
                    }
                } else if (data.contains("#")) {
                } else if (!data.equals("")) {
                    System.out.println();
                    System.out.println("Unknown marker at line " + line + ": \"" + data + "\" found int the object file.");
                }
            }
            if (VertexObjects.size() == 0 || Objects.size() == 0) errorOccured = true;
            if (!errorOccured) {
                try {
                    out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(MeshInputs.get(fileindex).outPath)));
                    out.write(EndianConverter.toByteArray(MagickNumber));
                    out.write(EndianConverter.toByteArray(VertexObjects.size()));
                    for (int a = 0; a < VertexObjects.size(); a++) {
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Vertex[0]));
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Vertex[1]));
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Vertex[2]));
                    }
                    for (int a = 0; a < VertexObjects.size(); a++) {
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Coordinate[0]));
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Coordinate[1]));
                    }
                    for (int a = 0; a < VertexObjects.size(); a++) {
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Normal[0]));
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Normal[1]));
                        out.write(EndianConverter.toByteArray(((VertexObject) (VertexObjects.get(a))).Normal[2]));
                    }
                    out.write(EndianConverter.toByteArray(Objects.size()));
                    for (int a = 0; a < Objects.size(); a++) {
                        MeshObject o = (MeshObject) Objects.get(a);
                        out.write(EndianConverter.toByteArray(o.name.length()));
                        out.write(o.name.getBytes());
                        out.write(EndianConverter.toByteArray(o.meshMaterial.ambientColor.red));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.ambientColor.green));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.ambientColor.blue));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.ambientColor.alpha));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.diffuseColor.red));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.diffuseColor.green));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.diffuseColor.blue));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.diffuseColor.alpha));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.emissiveColor.red));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.emissiveColor.green));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.emissiveColor.blue));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.emissiveColor.alpha));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.specularColor.red));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.specularColor.green));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.specularColor.blue));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.specularColor.alpha));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.shininess));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.skins.length + 1));
                        out.write(EndianConverter.toByteArray(o.meshMaterial.defaultTexture.length()));
                        out.write(o.meshMaterial.defaultTexture.getBytes());
                        for (int b = 0; b < o.meshMaterial.skins.length; b++) {
                            out.write(EndianConverter.toByteArray(o.meshMaterial.skins[b].length()));
                            out.write(o.meshMaterial.skins[b].getBytes());
                        }
                        if (changeXY || changeYZ || changeZX) {
                            System.out.println();
                            System.out.println("Rearranging axes.");
                            for (int i = 0; i < o.indices.size(); i += 3) {
                                Integer temp = o.indices.get(i);
                                o.indices.set(i, o.indices.get(i + 2));
                                o.indices.set(i + 2, temp);
                            }
                        }
                        out.write(EndianConverter.toByteArray(o.indices.size()));
                        for (int b = 0; b < o.indices.size(); b++) out.write(EndianConverter.toByteArray((Integer) o.indices.get(b)));
                    }
                    out.write(EndianConverter.toByteArray(EndNumber));
                    System.out.println();
                    System.out.println(path + " converted successfully.");
                    System.out.println();
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("Write " + path + " failed!");
                    e.printStackTrace();
                    System.out.println();
                }
                try {
                    in.close();
                    out.close();
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("Closing streams failed! Output file may be corrupt!");
                    e.printStackTrace();
                    System.out.println();
                }
            } else {
                System.out.println();
                System.out.println(path + " not converted!");
                System.out.println();
            }
            errorOccured = false;
            Vertices = new Vector<Float>();
            Normals = new Vector<Float>();
            Coordinates = new Vector<Float>();
            Objects = new Vector<MeshObject>();
            VertexObjects = new Vector<VertexObject>();
            Materials = new HashMap<String, Material>();
            filePath = "";
        }
        System.out.println();
        System.out.println("All conversions done.");
    }

    private static MeshObject getCurrentObject() {
        if (currentObject == 0) {
            if (Objects.isEmpty()) {
                System.out.println();
                System.out.println("There were no object found in the file. Creating one. There may be errors!");
                Objects.add(new MeshObject("default_object"));
                currentObject++;
            } else throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        return ((MeshObject) (Objects.get(currentObject - 1)));
    }

    private static ObjectInput getCurrentObjectInput(int currentMesh) {
        if (currentObject == 0 || Objects.isEmpty() || MeshInputs.isEmpty()) throw new java.lang.ArrayIndexOutOfBoundsException(); else if (MeshInputs.get(currentMesh).objects.size() < currentObject) return new ObjectInput();
        return ((ObjectInput) (MeshInputs.get(currentMesh).objects.get(currentObject - 1)));
    }

    private static int checkIndex(int v, int vt, int vn) {
        VertexObject vo = new VertexObject();
        v--;
        vt--;
        vn--;
        if (Vertices.isEmpty()) {
            System.out.println();
            System.out.println("No vertex information in the file!");
            errorOccured = true;
            return -1;
        }
        if (Coordinates.isEmpty()) {
            System.out.println();
            System.out.println("No texture information in the file!");
            errorOccured = true;
            return -1;
        }
        if (Normals.isEmpty()) {
            System.out.println();
            System.out.println("No vertex information in the file!");
            errorOccured = true;
            return -1;
        }
        vo.Vertex[0] = (Float) (Vertices.get(v * 3 + 2));
        vo.Vertex[1] = (Float) (Vertices.get(v * 3 + 1));
        vo.Vertex[2] = (Float) (Vertices.get(v * 3));
        vo.Coordinate[0] = (Float) (Coordinates.get(vt * 2));
        vo.Coordinate[1] = (Float) (Coordinates.get(vt * 2 + 1));
        vo.Normal[0] = (Float) (Normals.get(vn * 3));
        vo.Normal[1] = (Float) (Normals.get(vn * 3 + 1));
        vo.Normal[2] = (Float) (Normals.get(vn * 3 + 2));
        if (!VertexObjects.contains(vo)) VertexObjects.add(vo);
        return VertexObjects.indexOf(vo);
    }

    private static int getValueIndex(String s, String marker) {
        int indx;
        indx = s.indexOf(marker);
        if (indx == -1) return -1; else indx += marker.length();
        boolean found = false;
        while (!found) {
            if (s.charAt(indx) == ' ') indx++; else found = true;
        }
        return indx;
    }

    private static boolean generateObjectInputs(int beg, String[] args) {
        int currentMesh = -1;
        int current = -1;
        boolean ok = true;
        for (int i = beg; i < args.length; i++) {
            if (args[i].startsWith("m:")) {
                if (!args[i].substring(2).isEmpty()) {
                    MeshInputs.add(new MeshInput(args[i].substring(2)));
                    currentMesh++;
                    current = -1;
                } else {
                    ok = false;
                    break;
                }
            } else if (args[i].startsWith("t:")) {
                if (currentMesh >= 0) {
                    current++;
                    MeshInputs.get(currentMesh).objects.add(new ObjectInput());
                    if (!args[i].substring(2).isEmpty()) MeshInputs.get(currentMesh).objects.get(current).skinPaths.add(args[i].substring(2)); else {
                        ok = false;
                        break;
                    }
                } else {
                    ok = false;
                    break;
                }
            } else if ((args[i].startsWith("o:"))) {
                if (!args[i].substring(2).isEmpty() && currentMesh >= 0) MeshInputs.get(currentMesh).setOut(args[i].substring(2)); else {
                    ok = false;
                    break;
                }
            } else {
                if (currentMesh >= 0 && current >= 0) MeshInputs.get(currentMesh).objects.get(current).skinPaths.add(args[i]); else {
                    ok = false;
                    break;
                }
            }
        }
        return ok;
    }
}
