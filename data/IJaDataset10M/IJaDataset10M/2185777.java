package slick;

import com.customwars.client.io.img.slick.ImageStrip;
import com.customwars.client.model.TestData;
import com.customwars.client.ui.GUI;
import com.customwars.client.ui.renderer.MiniMapRenderer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import java.awt.Point;

public class MiniMapTest extends BasicGame {

    private MiniMapRenderer miniMapRenderer;

    public MiniMapTest() {
        super("Mini map Render test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        TestData.storeTestData();
        ImageStrip miniMap = new ImageStrip("testData/miniMap.png", 4, 4);
        miniMapRenderer = new MiniMapRenderer(HardCodedGame.getMap());
        Point renderPoint = GUI.getCenteredRenderPoint(miniMapRenderer.getSize(), container);
        miniMapRenderer.setLocation(renderPoint.x, renderPoint.y);
        miniMapRenderer.setTerrainMiniMap(miniMap);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        miniMapRenderer.update();
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        miniMapRenderer.render(g);
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer appGameContainer = new AppGameContainer(new MiniMapTest());
        appGameContainer.setDisplayMode(200, 200, false);
        appGameContainer.setTargetFrameRate(60);
        appGameContainer.setShowFPS(false);
        appGameContainer.start();
    }
}
