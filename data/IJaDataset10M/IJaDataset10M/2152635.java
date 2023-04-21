package com.uglygreencar.breakout.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import com.threerings.media.sprite.Sprite;
import com.uglygreencar.breakout.gameObjects.BrickObject;

public class BrickSprite extends Sprite {

    private static Paint[] PAINTS = new Paint[5];

    {
        PAINTS[BrickObject.BLUE] = Color.BLUE;
        PAINTS[BrickObject.GREEN] = Color.GREEN;
        PAINTS[BrickObject.ORANGE] = Color.ORANGE;
        PAINTS[BrickObject.RED] = Color.RED;
        PAINTS[BrickObject.YELLOW] = Color.YELLOW;
    }

    public static final int WIDTH = 32;

    public static final int HEIGHT = 16;

    private BrickObject brickObject;

    /********************************************************************
	 * 
	 * @param brickObject
	 *******************************************************************/
    public BrickSprite(BrickObject brickObject) {
        super(WIDTH, HEIGHT);
        this.brickObject = brickObject;
        super.setLocation(this.brickObject.x, this.brickObject.y);
    }

    public void updateBrick() {
        super.invalidate();
    }

    @Override
    public void paint(Graphics2D gfx) {
        if (!this.brickObject.broken) {
            gfx.setPaint(PAINTS[this.brickObject.color]);
            gfx.fillRect(_bounds.x, _bounds.y, WIDTH, HEIGHT);
        }
    }
}
