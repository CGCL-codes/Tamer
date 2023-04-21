package com.yosimite.secishow;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * The SimpleQuadScene class extends the SimpleQuadObject class adding the
 * ?
 * </p>
 */
class SimpleQuadScene extends SimpleQuadObject {

    private Point3f coordArray[] = null;

    private Point3f[] objectToPoint3fArray(ArrayList inList) {
        Point3f outList[] = new Point3f[inList.size()];
        for (int i = 0; i < inList.size(); i++) {
            outList[i] = (Point3f) inList.get(i);
        }
        return outList;
    }

    private SceneBase makeScene() {
        SceneBase scene = new SceneBase();
        BranchGroup group = new BranchGroup();
        scene.setSceneGroup(group);
        Shape3D shape = new Shape3D();
        coordArray = objectToPoint3fArray(coordList);
        int arrayx = 101;
        int arrayy = 101;
        int len = (arrayy * 2) * (arrayx - 1);
        int[] stripCounts = new int[arrayx - 1];
        for (int i = 0; i <= arrayx - 2; i++) {
            stripCounts[i] = arrayy * 2;
        }
        Point3f[] corrds = new Point3f[len];
        for (int i = 0; i <= arrayx - 2; i++) {
            int start = i * arrayy * 2;
            for (int j = 0; j <= arrayy - 1; j++) {
                corrds[start + 2 * j] = coordArray[i * arrayy + j];
                corrds[start + 2 * j + 1] = coordArray[(i + 1) * arrayy + j];
            }
        }
        System.out.println(corrds.length);
        GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
        gi.setCoordinates(corrds);
        gi.setStripCounts(stripCounts);
        gi.recomputeIndices();
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
        Stripifier st = new Stripifier();
        st.stripify(gi);
        GeometryArray result = gi.getGeometryArray();
        shape.setGeometry(result);
        Appearance appearance = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setBackFaceNormalFlip(true);
        Color3f objColor = new Color3f(0.9f, 0.9f, 1.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        ColoringAttributes ca = new ColoringAttributes(objColor, ColoringAttributes.SHADE_FLAT);
        appearance.setPolygonAttributes(polyAttrib);
        appearance.setColoringAttributes(ca);
        Material material = new Material();
        material.setDiffuseColor(new Color3f(1.0f, 0.0f, 0.0f));
        appearance.setMaterial(material);
        shape.setAppearance(appearance);
        group.addChild(shape);
        scene.addNamedObject("no name", shape);
        return scene;
    }

    /**
   * The File is loaded from the already opened file.
   * To attach the model to your scene, call getSceneGroup() on
   * the Scene object passed back, and attach the returned
   * BranchGroup to your scene graph.  
   */
    public Scene load(Reader reader) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        QuadFileParser st = new QuadFileParser(reader);
        coordList = new ArrayList();
        readQuadFile(st);
        return makeScene();
    }
}
