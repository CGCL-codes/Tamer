package ihaterobots.game.map;

import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import ihaterobots.interfaces.Drawable;
import ihaterobots.interfaces.Entity;
import ihaterobots.interfaces.Updatable;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author charly
 */
public class BreakableEntity implements Entity, Updatable, Drawable {

    private final int animationTimeOut;

    protected int posX;

    protected int posY;

    protected Animation animation;

    protected Image firstFrameImage;

    protected TileDrawer placeOverDrawer;

    private final int sheetId;

    protected int firstFrameTimeOut;

    protected int oldTileId;

    protected GameEnv env;

    public BreakableEntity(int sheetId, int firstFrameTimeOut, int animationTimeOut, int mapTileId) {
        this.sheetId = sheetId;
        this.firstFrameTimeOut = firstFrameTimeOut;
        this.animationTimeOut = animationTimeOut;
        this.oldTileId = mapTileId;
    }

    @Override
    public void register(GameEnv env) {
        this.env = env;
        SpriteSheet sheet = env.getDrawableManager().getSheet(sheetId);
        firstFrameImage = sheet.getSubImage(1, 0);
        animation = new Animation(sheet, 2, 0, sheet.getHorizontalCount() - 1, 0, true, animationTimeOut, false);
        animation.setLooping(false);
        env.getDrawableManager().addDrawable(this);
        env.getUpdatableManager().addUpdatable(this);
        env.getEntitiesManager().addEntity(this);
        env.getMap().setTile(posX, posY, Utils.MAP_Z_NORMAL, -1);
        env.getSoundManager().playBreak();
    }

    @Override
    public void unregister() {
        env.getDrawableManager().removeDrawable(this);
        env.getUpdatableManager().removeUpdatable(this);
        env.getEntitiesManager().removeEntity(this);
        env.getMap().setTile(posX, posY, Utils.MAP_Z_NORMAL, oldTileId);
    }

    @Override
    public void update(int delta) {
        if (firstFrameTimeOut >= 0) {
            firstFrameTimeOut -= delta;
            if (firstFrameTimeOut < 0) {
                animation.start();
                env.getSoundManager().playRefill();
            }
        }
        if (firstFrameTimeOut < 0) {
            animation.update(delta);
            if (animation.isStopped()) {
                unregister();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (placeOverDrawer != null && AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
            placeOverDrawer.draw(g, posX * Utils.TILE_SIZE, posY * Utils.TILE_SIZE);
        }
        if (firstFrameTimeOut >= 0) {
            g.drawImage(firstFrameImage, posX * Utils.TILE_SIZE, posY * Utils.TILE_SIZE);
        } else {
            g.drawAnimation(animation, posX * Utils.TILE_SIZE, posY * Utils.TILE_SIZE);
        }
        g.setDrawMode(Graphics.MODE_NORMAL);
        if (placeOverDrawer != null && !AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
        }
    }

    @Override
    public short getDrawablePriority() {
        return Utils.DRAWABLE_PRIORITY_BACKGROUND;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public TileDrawer getPlaceOverDrawer() {
        return placeOverDrawer;
    }

    public void setPlaceOverDrawer(TileDrawer placeOverDrawer) {
        this.placeOverDrawer = placeOverDrawer;
    }

    @Override
    public int getEntityId() {
        return Utils.ENTITY_TYPE_BACKGROUND;
    }

    @Override
    public float getPosX() {
        return posX;
    }

    @Override
    public float getPosY() {
        return posY;
    }

    @Override
    public int getWidth() {
        return Utils.TILE_SIZE;
    }

    @Override
    public int getHeigth() {
        return Utils.TILE_SIZE;
    }

    @Override
    public boolean isStuck() {
        return false;
    }

    @Override
    public void doorLockChanged() {
    }
}
