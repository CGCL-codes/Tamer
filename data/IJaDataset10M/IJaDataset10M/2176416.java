package kku.cs.hero;

import kku.cs.fgl.SpriteManager;

public class AutoManActor extends CharacterActor {

    public AutoManActor(SpriteManager sprites, String name) {
        super(sprites, name);
    }

    int dtime = 0;

    @Override
    public void calculateSpeed(int time) {
        super.calculateSpeed(time);
        dtime += time;
        if (dtime > 3000) {
            int w = (int) (Math.random() * 4);
            switch(w) {
                case 0:
                    moveLeft();
                    break;
                case 1:
                    moveRight();
                    break;
                case 2:
                    moveUp();
                    break;
                case 3:
                    moveDown();
                    break;
            }
            dtime = 0;
        }
    }
}
