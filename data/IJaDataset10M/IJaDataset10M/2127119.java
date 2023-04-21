package it.diamonds.engine.video;

import it.diamonds.engine.Environment;
import it.diamonds.engine.Rectangle;

public class Background implements DrawableInterface {

    private Sprite background;

    public Background(Environment environment, String textureName, String type) {
        Rectangle area = new Rectangle(0, 0, environment.getConfig().getInteger("width") - 1, environment.getConfig().getInteger("height"));
        background = new Sprite(0, 0, area, environment.getEngine().createImage(textureName + type));
    }

    public float getX() {
        return background.getPosition().getX();
    }

    public float getY() {
        return background.getPosition().getY();
    }

    public int getWidth() {
        return background.getTexture().getWidth();
    }

    public int getHeight() {
        return background.getTexture().getHeight();
    }

    public Sprite getSprite() {
        return background;
    }

    public void draw(AbstractEngine engine) {
        background.draw(engine);
    }

    public static Background createForTesting(Environment environment) {
        return new Background(environment, "back000", ".jpg");
    }
}
