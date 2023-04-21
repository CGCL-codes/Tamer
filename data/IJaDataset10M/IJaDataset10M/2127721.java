package jbomberman.client.renderer.plugins.bitmap;

import jbomberman.client.renderer.GimmickElementImpl;
import jbomberman.client.renderer.TileElementImpl;
import jbomberman.game.ruleset.Extra;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Gimmick.java
 *
 *
 * Created: Thu May 29 15:35:06 2003
 *
 * @author <a href="mailto:manni@sbox.tugraz.at">Manfred Klopschitz</a>
 * @version 1.0
 */
public class Gimmick extends GimmickElementImpl {

    protected Image buffer_default_ = null;

    protected Image buffer_gimmick_speed_ = null;

    protected Image buffer_gimmick_bomb_ = null;

    protected Image buffer_gimmick_range_ = null;

    protected Image tile_speed_ = null;

    protected Image tile_bomb_ = null;

    protected Image tile_range_ = null;

    public Gimmick() {
        TileImageCutter cutter = TileImageCutter.getTileImageCutter();
        tile_speed_ = cutter.getGimmickSpeed();
        tile_bomb_ = cutter.getGimmickBomb();
        tile_range_ = cutter.getGimmickRange();
    }

    public void resize(Graphics2D dev, int width, int height) {
        buffer_default_ = TileElementImpl.createImage(dev, width, height);
        Graphics buffer_graphics = buffer_default_.getGraphics();
        buffer_graphics.setColor(Color.blue);
        buffer_graphics.fillRect(0, 0, width, height);
        buffer_gimmick_bomb_ = scaleGimmick(tile_bomb_, dev, width, height);
        buffer_gimmick_speed_ = scaleGimmick(tile_speed_, dev, width, height);
        buffer_gimmick_range_ = scaleGimmick(tile_range_, dev, width, height);
    }

    public Image getImage(long birth_time, byte type) {
        switch(type) {
            case Extra.SPEED:
                return buffer_gimmick_speed_;
            case Extra.BOMBS:
                return buffer_gimmick_bomb_;
            case Extra.RANGE:
                return buffer_gimmick_range_;
        }
        return buffer_default_;
    }

    protected Image scaleGimmick(Image tile, Graphics2D dev, int width, int height) {
        Image target = TileElementImpl.createImage(dev, width, height);
        System.out.println(target);
        int tile_width = tile.getWidth(null);
        int tile_height = tile.getHeight(null);
        Graphics target_graphics = target.getGraphics();
        target_graphics.drawImage(tile, 0, 0, width, height, 0, 0, tile_width, tile_height, null);
        return target;
    }
}
