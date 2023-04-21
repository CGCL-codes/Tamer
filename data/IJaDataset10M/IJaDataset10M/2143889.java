package de.grogra.ext.x3d.importation;

import java.util.HashMap;
import de.grogra.ext.x3d.ProtoInstanceImport;
import de.grogra.ext.x3d.X3DImport;
import de.grogra.ext.x3d.util.Util;
import de.grogra.ext.x3d.xmlbeans.Rectangle2DDocument.Rectangle2D;
import de.grogra.graph.EdgePatternImpl;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.MeshNode;
import de.grogra.imp3d.objects.PolygonMesh;
import de.grogra.imp3d.objects.ShadedNull;
import de.grogra.xl.util.FloatList;
import de.grogra.xl.util.IntList;

public class Rectangle2DImport {

    public static ShadedNull createInstance(Rectangle2D rectangle, Node parent) {
        MeshNode n = null;
        HashMap<String, Object> referenceMap = X3DImport.getTheImport().getCurrentParser().getReferenceMap();
        if (rectangle.isSetUSE()) {
            try {
                n = (MeshNode) ((Node) referenceMap.get(rectangle.getUSE())).cloneGraph(EdgePatternImpl.TREE, true);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            n = new MeshNode();
            HashMap<String, String> finalValueMap = new HashMap<String, String>();
            if (rectangle.isSetIS()) {
                ProtoInstanceImport.calcFinalValueMap(finalValueMap, rectangle);
            }
            setValues(n, rectangle, finalValueMap);
            if (rectangle.isSetDEF()) {
                referenceMap.put(rectangle.getDEF(), n);
            }
        }
        n.setName("X3DRectangle2D");
        return n;
    }

    private static void setValues(MeshNode node, Rectangle2D rectangle, HashMap<String, String> valueMap) {
        float[] size = null;
        String sizeString = valueMap.containsKey("size") ? valueMap.get("size") : rectangle.getSize();
        size = Util.splitStringToArrayOfFloat(sizeString, new float[] { 2, 2 });
        boolean solid;
        solid = valueMap.containsKey("solid") ? Boolean.valueOf(valueMap.get("solid")) : rectangle.isSetSolid() ? rectangle.getSolid() : false;
        PolygonMesh p = new PolygonMesh();
        IntList indexData = new IntList();
        FloatList vertexData = new FloatList();
        FloatList textureData = new FloatList();
        vertexData.add(-size[0] / 2.0f);
        vertexData.add(0);
        vertexData.add(-size[1] / 2.0f);
        vertexData.add(size[0] / 2.0f);
        vertexData.add(0);
        vertexData.add(-size[1] / 2.0f);
        vertexData.add(size[0] / 2.0f);
        vertexData.add(0);
        vertexData.add(size[1] / 2.0f);
        vertexData.add(-size[0] / 2.0f);
        vertexData.add(0);
        vertexData.add(size[1] / 2.0f);
        textureData.add(0);
        textureData.add(0);
        textureData.add(1);
        textureData.add(0);
        textureData.add(1);
        textureData.add(1);
        textureData.add(0);
        textureData.add(1);
        indexData.add(0);
        indexData.add(1);
        indexData.add(2);
        indexData.add(2);
        indexData.add(3);
        indexData.add(0);
        p.setIndexData(indexData);
        p.setVertexData(vertexData);
        p.setTextureData(textureData.elements);
        node.setVisibleSides(solid ? 0 : 2);
        node.setPolygons(p);
    }
}
