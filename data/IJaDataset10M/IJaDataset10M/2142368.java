package org.ogre4j.demos.swt.manualobject;

import org.ogre4j.ColourValue;
import org.ogre4j.IColourValue;
import org.ogre4j.IEntity;
import org.ogre4j.ISceneNode;
import org.ogre4j.IVector3;
import org.ogre4j.ManualObject;
import org.ogre4j.MeshPtr;
import org.ogre4j.Quaternion;
import org.ogre4j.Vector3;
import org.ogre4j.RenderOperation.OperationType;
import org.ogre4j.demos.swt.exampleapp.ExampleApplication;

/**
 * A simple demo showing how to use ManualObject.
 * 
 */
public class ManualObjectDemo extends ExampleApplication {

    /**
	 * ManualObject to be rendered in this simple demo. It is a member to
	 * prevent garbage collection.
	 */
    protected ManualObject triangle;

    @Override
    protected void createScene() {
        triangle = new ManualObject("triangle");
        triangle.begin("BaseWhiteNoLighting", OperationType.OT_TRIANGLE_LIST);
        triangle.position(-50, 0, 0);
        triangle.normal(1, 0, 0);
        triangle.position(50, 0, 0);
        triangle.normal(1, 0, 0);
        triangle.position(0, 50, 0);
        triangle.normal(1, 0, 0);
        triangle.triangle(0, 1, 2);
        triangle.end();
        String meshName = "triangle.mesh";
        MeshPtr triangleMesh = new MeshPtr();
        triangle.convertToMesh(triangleMesh, meshName, "manual");
        IColourValue ambientColour = new ColourValue(1.0f, 1.0f, 1.0f, 1.0f);
        mSceneMgr.setAmbientLight(ambientColour);
        ambientColour.delete();
        IEntity meshEntity = mSceneMgr.createEntity("mesh_triangle", meshName);
        ISceneNode meshNode = mSceneMgr.getRootSceneNode().createChildSceneNode("meshNode", Vector3.getZERO(), Quaternion.getIDENTITY());
        meshNode.attachObject(meshEntity);
        IVector3 pos = new Vector3(100, 0, 0);
        ISceneNode manualNode = mSceneMgr.getRootSceneNode().createChildSceneNode("manualNode", pos, Quaternion.getIDENTITY());
        pos.delete();
        manualNode.attachObject(triangle);
    }

    @Override
    protected void destroyScene() {
        triangle.delete();
        super.destroyScene();
    }

    /**
	 * Main.
	 */
    public static void main(String[] args) {
        ManualObjectDemo app = new ManualObjectDemo();
        try {
            app.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
