package hokutonorogue.level.tile;

import java.awt.image.*;
import hokutonorogue.game.*;
import hokutonorogue.object.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class IsometricWallSprite extends IsometricHokutoEntitySprite {

    public IsometricWallSprite(BufferedImage[] bufferedImageArray, double _double, double _double2) {
        super(bufferedImageArray, _double, _double2);
    }

    public IsometricWallSprite(BufferedImage[] bufferedImageArray) {
        super(bufferedImageArray);
    }

    public IsometricWallSprite(double _double, double _double1) {
        super(_double, _double1);
    }

    public IsometricWallSprite() {
        super();
    }

    protected void updatePosition() {
        if (model != null && model.getTile() != null) {
            Tile tile = model.getTile();
            if (tile.isVisited() && tile.isVisibleByHero() || tile.isVisited() && !HokutoNoRogue.isFogOfWarEnabled()) {
                setAlpha(1.0f);
                int newX = getIsoX(tile);
                int newY = getIsoY(tile);
                setX(newX);
                setY(newY);
                Wall wall = (Wall) model;
                int type = wall.getType();
                setAnimationFrame(type, type);
                if (MainGame.getInstance().isTralsucent()) {
                    setAlpha(0.5f);
                } else {
                    setAlpha(1.0f);
                }
            } else {
                setAlpha(0.0f);
            }
        }
    }

    public double getGroundX() {
        double ret = getX() + (getCurrentImage().getWidth() / 2);
        return ret;
    }

    public double getGroundY() {
        double ret = getY() + getCurrentImage().getHeight() - MainGame.TILE_HEIGHT / 2;
        return ret;
    }
}
