package sourceforge.shinigami.graphics;

import java.awt.Graphics2D;
import sourceforge.shinigami.main.IUpdateRequester;

/**
 * The SpriteRunner is the class with the ability of running Sprite animations.
 * To do so, create an instance of SpriteRunner and give it a sprite by either
 * it's constructor or it's method swapSprite(Sprite).
 * 
 * <p>The render method of this class will display a different image depending
 * on the inner state of the object. If it's running (i.e. the play method was
 * called and it still hasn't completed the delay time of the animation) it
 * will display one of the animation textures. If it never ran, it will display
 * the initial image, and if it already did run, it will display the final
 * image.
 * 
 * <p> In case one of these images is null, SpriteRunner will display the
 * alternative texture in the Sprite's definitions. If that one is also null,
 * it will throw a NullPointerException.
 * 
 * 
 * @author kaorosorane
 * @since 5
 */
public class SpriteRunner implements IRenderable, IUpdateRequester {

    private Sprite sprite;

    public SpriteRunner(Sprite sprite) {
        swapSprite(sprite);
    }

    private Texture beforeAnimation;

    private Texture afterAnimation;

    private Texture noTextureDisplay;

    private Texture[] animation;

    final void swapSprite(Sprite newSprite) {
        this.reset();
        this.sprite = newSprite;
        this.beforeAnimation = sprite.getBeforeAnimationTexture();
        this.afterAnimation = sprite.getAfterAnimationTexture();
        this.animation = sprite.getAnimation();
        this.noTextureDisplay = sprite.getNoTextureDisplay();
    }

    private boolean running = false;

    private boolean played = false;

    private int at = 0;

    private int repeat = 0;

    private int count = 0;

    public final void play() {
        running = true;
        at = 0;
        repeat = sprite.getRepeat();
    }

    public final void stop() {
        running = false;
        played = true;
    }

    public final void reset() {
        running = false;
        played = false;
    }

    public final boolean isRunning() {
        return running;
    }

    @Override
    public final void update() {
        if (running) {
            count++;
            if (count > sprite.getDelay()) {
                count = 0;
                sys_update();
            }
        }
    }

    @Override
    public final void render(Graphics2D g, int x, int y) {
        if (running) {
            try {
                this.animation[at].render(g, x, y);
            } catch (NullPointerException e) {
                if (this.noTextureDisplay != null) noTextureDisplay.render(g, x, y); else throw new NullPointerException("The animation for this Sprite " + sprite + " at position " + at + " is null and it has no alternative display texture ");
            }
        } else if (played) {
            try {
                this.afterAnimation.render(g, x, y);
            } catch (NullPointerException e) {
                if (this.noTextureDisplay != null) noTextureDisplay.render(g, x, y); else throw new NullPointerException("The after animation texture of sprite " + sprite + " is null and it has no alternative display texture");
            }
        } else {
            try {
                this.beforeAnimation.render(g, x, y);
            } catch (NullPointerException e) {
                if (this.noTextureDisplay != null) noTextureDisplay.render(g, x, y); else throw new NullPointerException("The before animation texture of sprite " + sprite + " is null and it has no alternative display texture");
            }
        }
    }

    private final boolean sys_update() {
        System.err.println("Update: Count=" + count + " | at=" + at);
        at++;
        if (at >= sprite.getAnimation().length) {
            if (repeat == -1) {
                at = 0;
                return true;
            } else if (repeat > 0) {
                at = 0;
                repeat--;
                return true;
            } else if (repeat == 0) {
                stop();
                return false;
            } else throw new RuntimeException("Sprite repeat rate must be -1 or" + " higher");
        }
        return true;
    }
}
