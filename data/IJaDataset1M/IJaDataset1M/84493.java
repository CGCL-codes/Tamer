package org.ogre4j.eclipse.ogreface.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ogre4j.eclipse.ogreface.OgrePlugin;
import org.ogre4j.eclipse.ogreface.examples.RenderLoopSometimes;
import org.ogre4j.eclipse.ogreface.ogresys.IRenderLoop;
import org.ogre4j.eclipse.ogreface.ogresys.OgreSystem;

public class PluginTests {

    @BeforeClass
    public static void createAndStartPlugin() throws Exception {
        OgrePlugin plugin = new OgrePlugin();
        plugin.start(null);
    }

    @AfterClass
    public static void stopPlugin() throws Exception {
        OgrePlugin.getDefault().stop(null);
    }

    @Test
    public void render() throws Exception {
        IRenderLoop renderLoop = new RenderLoopSometimes();
        OgreSystem.getDefault().setRenderLoop(renderLoop);
        OgreSystem.getDefault().startRendering();
        renderLoop.redraw();
        OgreSystem.getDefault().stopRendering();
        OgreSystem.getDefault().setRenderLoop(null);
    }
}
