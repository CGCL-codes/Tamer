package net.harrierattack.background;

import net.engine.GamePanel;
import net.engine.cel.Cel;
import net.engine.cel.CelHelper;
import net.engine.graphics.Sprite;
import net.harrierattack.HarrierAttack;
import java.util.ArrayList;

public class Backdrop {

    java.util.List<Sprite[]> sprites;

    GamePanel gamePanel;

    int terrainWidth;

    private int mountainLayers;

    public Backdrop(GamePanel gamePanel, int terrainWidth, int mountainLayers) {
        this.gamePanel = gamePanel;
        this.terrainWidth = terrainWidth;
        this.mountainLayers = mountainLayers;
        sprites = new ArrayList<Sprite[]>();
        generateImage(gamePanel);
    }

    public void generateImage(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        generateSunset();
        generateMountains();
    }

    private void generateMountains() {
        for (int colour = 1; colour < mountainLayers + 1; colour++) {
            Mountains mountains = new Mountains(colour, HarrierAttack.SCREEN_WIDTH);
            CelHelper celHelper = new CelHelper(mountains.picture, 2 + colour, 1, true, true);
            sprites.add(new Sprite[2 + colour]);
            for (int f = 0; f < 2 + colour; f++) {
                Sprite sprite = new Sprite(gamePanel, celHelper.get(f), (colour - 1 + f) * HarrierAttack.SCREEN_WIDTH, 0.0f);
                sprite.setName("Mountain " + colour + ":" + f);
                sprite.setLayer(39 - colour);
                sprites.get(sprites.size() - 1)[f] = sprite;
                celHelper.get(f).setAlignment(Cel.CENTERED, Cel.TOP_ALIGNED);
            }
        }
    }

    private void generateSunset() {
        Sunset sunset = new Sunset(HarrierAttack.SCREEN_WIDTH);
        CelHelper celHelper = new CelHelper(sunset.picture, 2, 1, true, true);
        celHelper.get(0).setAlignment(Cel.CENTERED, Cel.TOP_ALIGNED);
        celHelper.get(1).setAlignment(Cel.CENTERED, Cel.TOP_ALIGNED);
        sprites.add(new Sprite[2]);
        sprites.get(0)[0] = new Sprite(gamePanel, celHelper.get(0), 0, 0);
        sprites.get(0)[0].setLayer(40);
        sprites.get(0)[0].setName("Sunset 0");
        sprites.get(0)[1] = new Sprite(gamePanel, celHelper.get(1), HarrierAttack.SCREEN_WIDTH, 0);
        sprites.get(0)[1].setLayer(40);
        sprites.get(0)[1].setName("Sunset 1");
    }

    public void setFromPlayerPosition(float x) {
        for (int i = 0; i < sprites.size(); i++) {
            Sprite[] sprites = this.sprites.get(i);
            for (int j = 0; j < sprites.length; j++) {
                Sprite sprite = sprites[j];
                sprite.setStaticPosition(HarrierAttack.SCREEN_WIDTH * j + x - ((x * HarrierAttack.SCREEN_WIDTH * (i + 1)) / terrainWidth), sprite.getPosition().y);
            }
        }
    }
}
