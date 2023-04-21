package org.xith3d.loaders.models.impl.obj;

import java.util.List;
import java.util.Map;
import org.jagatoo.loaders.models.obj.OBJFace;
import org.jagatoo.loaders.models.obj.OBJFaceList;
import org.jagatoo.loaders.models.obj.OBJGroup;
import org.jagatoo.loaders.models.obj.OBJMaterial;
import org.jagatoo.loaders.models.obj.OBJModelPrototype;
import org.openmali.vecmath2.TexCoord2f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.loaders.models.base.LoadedGraph;
import org.xith3d.loaders.models.util.conversion.NodeFactory;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.ColoringAttributes;
import org.xith3d.scenegraph.GroupNode;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.TextureAttributes;
import org.xith3d.scenegraph.TriangleArray;

/**
 * Converts OBJ prototypes into xith scenegraph objects.
 * 
 * @author Kevin Glass
 * @author <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
 * @author Marvin Froehlich (aka Qudus)
 */
public class OBJConverter {

    private static void generateAppeances(Map<String, OBJMaterial> matMap, Map<String, Appearance> appMap) {
        for (String name : matMap.keySet()) {
            OBJMaterial oMat = matMap.get(name);
            Appearance app = new Appearance();
            app.setName(name);
            Material xMat = new Material();
            xMat.setName(name);
            xMat.setLightingEnabled(true);
            app.setColoringAttributes(new ColoringAttributes());
            if (oMat.getColor() != null) app.getColoringAttributes().setColor(oMat.getColor()[0], oMat.getColor()[1], oMat.getColor()[2]);
            if (oMat.getAmbientColor() != null) xMat.setAmbientColor(oMat.getAmbientColor()[0], oMat.getAmbientColor()[1], oMat.getAmbientColor()[2]);
            if (oMat.getDiffuseColor() != null) xMat.setDiffuseColor(oMat.getDiffuseColor()[0], oMat.getDiffuseColor()[1], oMat.getDiffuseColor()[2]);
            if (oMat.getSpecularColor() != null) xMat.setSpecularColor(oMat.getSpecularColor()[0], oMat.getSpecularColor()[1], oMat.getSpecularColor()[2]);
            xMat.setShininess(oMat.getShininess());
            if (oMat.getTextureName() != null) {
                Texture texture = TextureLoader.getInstance().getTexture(oMat.getTextureName(), Texture.MipmapMode.MULTI_LEVEL_MIPMAP);
                app.setTexture(texture);
                TextureAttributes attr = new TextureAttributes();
                attr.setTextureMode(TextureAttributes.MODULATE);
                app.setTextureAttributes(attr);
            }
            app.setMaterial(xMat);
            appMap.put(name, app);
        }
    }

    private static Vector3f[] createVectorArray(float[][] data) {
        Vector3f[] result = new Vector3f[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = new Vector3f(data[i][0], data[i][1], data[i][2]);
        }
        return (result);
    }

    private static TexCoord2f[] createTexCoordArray(float[][] data) {
        TexCoord2f[] result = new TexCoord2f[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = new TexCoord2f(data[i][0], data[i][1]);
        }
        return (result);
    }

    private static Shape3D build(Map<String, Appearance> appMap, OBJFaceList faceList, int flags, String name, NodeFactory nodeFactory) {
        int totalVerts = 0;
        int[] indexs = new int[faceList.getFaces().size()];
        for (int i = 0; i < faceList.getFaces().size(); i++) {
            OBJFace face = faceList.getFaces().get(i);
            totalVerts += face.getCount();
            if (i < faceList.getFaces().size() - 1) {
                indexs[i + 1] = face.getCount();
            }
        }
        TriangleArray array = new TriangleArray(totalVerts);
        int currentVert = 0;
        float[][] dataVerts = new float[totalVerts][3];
        float[][] dataNormals = new float[totalVerts][3];
        float[][] dataTexs = new float[totalVerts][3];
        Appearance app = null;
        for (int i = 0; i < faceList.getFaces().size(); i++) {
            OBJFace face = faceList.getFaces().get(i);
            if (face.getMaterial() != null) {
                app = appMap.get(face.getMaterial().getName());
            }
            face.configure(dataVerts, dataNormals, dataTexs, currentVert);
            currentVert += face.getCount();
        }
        array.setCoordinates(0, createVectorArray(dataVerts));
        array.setNormals(0, createVectorArray(dataNormals));
        if (faceList.texturesUsed()) {
            if (dataTexs != null) {
                array.setTextureCoordinates(0, 0, createTexCoordArray(dataTexs));
            }
        }
        Shape3D shape = nodeFactory.createShape3D(array, app, flags);
        if ((name != null) && (name.length() > 0)) shape.setName(name);
        return (shape);
    }

    private static void build(Map<String, Appearance> appMap, OBJGroup objGroup, GroupNode parentGroup, int flags, NodeFactory nodeFactory, LoadedGraph graph) {
        List<OBJGroup> children = objGroup.getChildren();
        if ((children.size() > 0) || (objGroup.getFaces().size() == 0)) {
            GroupNode childrenGroup = nodeFactory.createGroup();
            if (objGroup.hasName()) {
                if (objGroup.getFaces().size() > 0) childrenGroup.setName(objGroup.getName() + "_children"); else childrenGroup.setName(objGroup.getName());
                if (!objGroup.isTopGroup()) {
                    graph.getNamedObjects().put(childrenGroup.getName(), childrenGroup);
                }
            }
            for (int i = 0; i < children.size(); i++) {
                build(appMap, children.get(i), childrenGroup, flags, nodeFactory, graph);
            }
            parentGroup.addChild(childrenGroup);
        }
        if (objGroup.getFaces().size() > 0) {
            Shape3D shape = build(appMap, objGroup, flags, objGroup.getName(), nodeFactory);
            if ((objGroup.hasName()) && (!objGroup.isTopGroup())) {
                shape.setName(objGroup.getName());
                graph.getNamedObjects().put(objGroup.getName(), shape);
            }
            parentGroup.addChild(shape);
            if (graph instanceof OBJModel) ((OBJModel) graph).addShapeNode(shape); else ((OBJScene) graph).addShapeNode(shape);
        }
    }

    public static void convert(OBJModelPrototype prototype, LoadedGraph graph, int flags, NodeFactory nodeFactory) {
        Map<String, Appearance> appMap;
        if (graph instanceof OBJScene) appMap = ((OBJScene) graph).getAppearancesMap(); else appMap = ((OBJModel) graph).getAppearancesMap();
        generateAppeances(prototype.getMaterialMap(), appMap);
        build(appMap, prototype.getTopGroup(), (GroupNode) graph, flags, nodeFactory, graph);
    }
}
