package jbomberman.client.renderer.plugins.bitmap;

import jbomberman.client.renderer.TileElementImpl;
import jbomberman.util.Log;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Bitmap Tiles
 *
 * @author <a href="mailto:manni@sbox.tugraz.at">Manfred Klopschitz</a>
 * @version 1.0
 */
public class Tile extends TileElementImpl {

    public static final int FREE = 1;

    public static final int HARD = 2;

    public static final int SOFT = 3;

    protected Image buffer_;

    protected Image tile_;

    public Tile(int type) {
        TileImageCutter cutter = TileImageCutter.getTileImageCutter();
        switch(type) {
            case FREE:
                tile_ = cutter.getFreeTile();
                break;
            case HARD:
                tile_ = cutter.getHardTile();
                break;
            case SOFT:
                tile_ = cutter.getSoftTile();
                break;
            default:
                Log.log("Client.Renderer.Plugins.Bitmap: No Color set in RendererPlugin:minimal !!!");
                break;
        }
    }

    public Image getImage() {
        return buffer_;
    }

    public void resize(Graphics2D dev, int width, int height) {
        buffer_ = createImage(dev, width, height);
        Graphics buffer_graphics = buffer_.getGraphics();
        int tile_width = tile_.getWidth(null);
        int tile_height = tile_.getHeight(null);
        buffer_graphics.drawImage(tile_, 0, 0, width, height, 0, 0, tile_width, tile_height, null);
    }
}
