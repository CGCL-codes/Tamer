package game.objects;

import game.Animation;
import game.Sprite;
import java.awt.Image;

public class StaticGameObject {

    private Animation activeAnimation;

    public Sprite sprite;

    public Image spritesheet;

    private Animation none = new Animation(sprite, 0, 0, 0, false);

    public StaticGameObject(Sprite sprite) {
        this.sprite = sprite;
        this.sprite.animation = none;
    }

    public void setActiveAnimation(Animation a) {
        activeAnimation = a;
    }

    public void setActiveAnimation() {
        activeAnimation = none;
    }

    public Animation getActiveAnimation() {
        return activeAnimation;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
