package kku.cs.hero;

import org.newdawn.slick.Color;
import kku.cs.fgl.GamePane;
import kku.cs.fgl.Scene;
import kku.cs.fgl.SpriteManager;
import kku.cs.fgl.SpriteSheet;
import kku.cs.fgl.actor.BackgroundActor;
import kku.cs.fgl.actor.ImageActor;

public class Scene1 extends Scene {

    public Scene1(int id, GamePane gamePane) {
        super(id, gamePane);
    }

    public Scene1(int id) {
        super(id);
    }

    @Override
    public void enter() {
    }

    @Override
    public void init() {
        getDefView().setBounds(0, 0, 2000, 2000);
        BackgroundActor bg = new BackgroundActor("resource/grass.png");
        bg.setWidth(2020);
        bg.setHeight(2020);
        SpriteSheet mapSheet = new SpriteSheet("resource/map1.gif", 32, 32);
        int map[][] = { { 0, 0, 0, 0, 0, 0, 0, 2, 2, 0 }, { 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 6, 6, 0, 0, 0, 0, 0, 0 }, { 0, 0, 6, 7, 7, 7, 7, 0, 0, 0 }, { 0, 0, 6, 6, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 9, 0, 0 }, { 0, 0, 5, 5, 0, 0, 3, 4, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        TileMapActor tilemap = new TileMapActor(mapSheet, map);
        add(tilemap, 0);
    }

    @Override
    public void leave() {
    }
}
