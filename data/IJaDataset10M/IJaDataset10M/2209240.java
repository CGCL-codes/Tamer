package de.grogra.ext.x3d.exportation;

import java.io.IOException;
import javax.vecmath.Matrix4d;
import de.grogra.ext.x3d.X3DExport;
import de.grogra.ext.x3d.xmlbeans.ShapeDocument.Shape;
import de.grogra.ext.x3d.xmlbeans.TransformDocument.Transform;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.io.SceneGraphExport;
import de.grogra.imp3d.objects.LightNode;
import de.grogra.imp3d.objects.SceneTreeWithShader;
import de.grogra.imp3d.objects.Sky;
import de.grogra.imp3d.objects.SceneTree.InnerNode;
import de.grogra.imp3d.objects.SceneTree.Leaf;

public abstract class BaseExport implements SceneGraphExport.NodeExport {

    public void export(Leaf node, InnerNode transform, SceneGraphExport sge) throws IOException {
        X3DExport export = (X3DExport) sge;
        export.increaseProgress();
        Transform parentNode = export.getLastTransform();
        if (transform != null) {
            Matrix4d transMatrix = new Matrix4d();
            transform.get(transMatrix);
            Transform transformNode = TransformExport.handleTransform(transMatrix, parentNode);
            parentNode = transformNode;
        }
        if (this instanceof AxisExport) {
            Matrix4d transMatrix = new Matrix4d();
            transMatrix.setIdentity();
            Transform transformNode = TransformExport.handleTransform(transMatrix, parentNode, true, (Node) node.object, sge.getGraphState());
            parentNode = transformNode;
        }
        if ((node instanceof SceneTreeWithShader.Leaf) && (!(node.object instanceof Sky)) && (!(node.object instanceof LightNode))) {
            Shape shape = ShapeExport.handleShape(parentNode);
            AppearanceExport.handleAppearance(shape, node.object, ((SceneTreeWithShader.Leaf) node).shader, export, sge.getGraphState());
            exportImpl(node, export, shape, parentNode);
        } else {
            exportImpl(node, export, null, parentNode);
        }
    }

    protected abstract void exportImpl(Leaf node, X3DExport export, Shape shapeNode, Transform transformNode) throws IOException;
}
