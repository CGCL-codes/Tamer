package gumbo.ardor3d.app.test;

import gumbo.ardor3d.app.graphic.GraphicAppHelper;
import gumbo.ardor3d.app.graphic.GraphicManager;
import gumbo.ardor3d.app.graphic.GraphicRender;
import gumbo.ardor3d.app.graphic.GraphicScene;
import gumbo.ardor3d.app.graphic.ShadowGraphicRender;
import gumbo.ardor3d.app.graphic.TabbedGraphicAppGui;

public class TestShadowGraphicApp {

    public TestShadowGraphicApp() {
        System.out.println("TestShadowGraphicApp:\n" + "Ardor3D shadowing is very buggy!\n" + "Move camera around to see shadows (e.g. A and F keys).");
        GraphicManager graphicManager = new GraphicManager();
        GraphicScene graphicScene = new TestShadowGraphicScene();
        TestGraphicInteract graphicInteract = new TestGraphicInteract();
        GraphicRender graphicRender = new ShadowGraphicRender();
        TabbedGraphicAppGui appGui = new TabbedGraphicAppGui();
        GraphicAppHelper appHelper = new GraphicAppHelper();
        graphicInteract.setBuildPreConditions(graphicScene);
        graphicRender.setBuildPreConditions(graphicManager, graphicScene);
        graphicScene.setBuildPreConditions(graphicRender);
        appHelper.setBuildPreConditions(appGui, graphicManager, graphicScene, graphicInteract, graphicRender);
        appGui.setBuildPreConditions(appHelper);
        appHelper.buildBlock();
        appGui.addCanvasTab(graphicScene);
        appHelper.startApp();
        appHelper.destroyApp();
    }

    public static void main(final String[] args) {
        new TestShadowGraphicApp();
    }
}
