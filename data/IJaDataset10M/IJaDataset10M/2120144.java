package starforge;

import scene.Game;
import starforge.scene.DemoScene;
import starforge.scene.EditorScene;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import de.lessvoid.nifty.Nifty;
import xero.app.Xero;
import xero.object.XeroDeamon;

public class Engine extends Xero {

    public Engine() {
        TITLE = "Starforge 1.0";
    }

    public void initXeroApp() {
        Xero.DATABASE.addDatabase("/data/base.dat");
        Xero.DATABASE.loadAllObjects();
        XeroDeamon.loadScene(new DemoScene("demo", Xero.APPLICATION));
    }

    public void setupGUI() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("gui/game.gui", "intro_pandora");
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
    }

    public void setupHUD() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("gui/hud.gui", "hud_loading");
        guiViewPort.addProcessor(niftyDisplay);
    }

    public void setupHUDEffect() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("gui/hudeffect.gui", "hud_blank");
        guiViewPort.addProcessor(niftyDisplay);
    }

    public void renderXeroApp(RenderManager rm) {
    }

    public void updateXeroApp(float tpf) {
    }

    public static void main(String args[]) {
        new Engine().start();
    }
}
