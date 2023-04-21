package kku.cs.sidescroll;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import kku.cs.fgl.GamePane;
import kku.cs.fgl.Scene;
import kku.cs.fgl.SpriteManager;
import kku.cs.fgl.SpriteSheet;

public class Scene1 extends Scene {

    public Scene1(int id) {
        super(id);
    }

    @Override
    public void enter() {
    }

    @Override
    public void init() {
        SpriteSheet fmanSheet = new SpriteSheet("fireman.gif", 60, 60);
        SpriteManager fsprite = new SpriteManager();
        fsprite.add("stand", fmanSheet, 0);
        fsprite.add("run", fmanSheet, 1, 2, 3, 4);
        fsprite.add("jump", fmanSheet, 5);
        fsprite.add("sit", fmanSheet, 6);
        fsprite.add("shoot", fmanSheet, 7);
        FireManActor actor = new FireManActor(fsprite);
        actor.setLocation(300, 20);
        add(actor);
    }

    @Override
    public void leave() {
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 340, 800, 600);
        g.setColor(Color.white);
        g.drawLine(0, 340, 800, 340);
        super.paint(g);
    }
}
