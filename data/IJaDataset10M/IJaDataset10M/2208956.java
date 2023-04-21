package org.ogre4j.demos.swt.smoke;

import org.eclipse.swt.widgets.Canvas;
import org.ogre4j.ColourValue;
import org.ogre4j.ICamera;
import org.ogre4j.IColourValue;
import org.ogre4j.IParticleSystem;
import org.ogre4j.IRenderWindow;
import org.ogre4j.ISceneNode;
import org.ogre4j.Quaternion;
import org.ogre4j.ResourceGroupManager;
import org.ogre4j.Vector3;
import org.ogre4j.demos.swt.exampleapp.ExampleApplication;
import org.ogre4j.demos.swt.exampleapp.ExampleFrameListener;

class ParticleFrameListener extends ExampleFrameListener {

    protected ISceneNode mFountainNode;

    public ParticleFrameListener(IRenderWindow win, ICamera cam, Canvas swtCanvas, ISceneNode fountainNode) {
        super(win, cam, swtCanvas);
        mFountainNode = fountainNode;
    }
}

public class Smoke extends ExampleApplication {

    protected ISceneNode mFountainNode;

    @Override
    protected void createScene() {
        IColourValue ambientLight = new ColourValue(0.5f, 0.5f, 0.5f, 1.0f);
        mSceneMgr.setAmbientLight(ambientLight);
        ambientLight.delete();
        mSceneMgr.setSkyDome(true, "Examples/CloudySky", 5, 8, 4000, true, Quaternion.getIDENTITY(), 16, 16, -1, ResourceGroupManager.getDEFAULT_RESOURCE_GROUP_NAME());
        mFountainNode = mSceneMgr.getRootSceneNode().createChildSceneNode(Vector3.getZERO(), Quaternion.getIDENTITY());
        IParticleSystem pSys2 = mSceneMgr.createParticleSystem("fountain1", "Examples/Smoke");
        ISceneNode fNode = mFountainNode.createChildSceneNode(Vector3.getZERO(), Quaternion.getIDENTITY());
        fNode.attachObject(pSys2);
    }

    protected void createFrameListener() {
        mFrameListener = new ParticleFrameListener(mWindow, mCamera, swtCanvas, mFountainNode);
        this.addFrameListener(mFrameListener);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Smoke app = new Smoke();
        try {
            app.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
