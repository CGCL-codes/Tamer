package lemmings;

import java.awt.image.BufferedImage;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.collision.AdvanceCollisionGroup;
import com.golden.gamedev.object.sprite.VolatileSprite;

class FigureTerrainCollision extends AdvanceCollisionGroup {

    Lemmings owner;

    public FigureTerrainCollision(Lemmings owner) {
        this.owner = owner;
        pixelPerfectCollision = true;
    }

    public void collided(Sprite s1, Sprite s2) {
        BufferedImage img = s2.getImage();
        s1.setActive(false);
        s2.setActive(false);
        BufferedImage[] images = owner.getImages("resources/explosion.png", 7, 1);
        VolatileSprite explosion = new VolatileSprite(images, s2.getX(), s2.getY());
    }
}
