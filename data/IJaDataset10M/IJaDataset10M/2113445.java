package com.uglygreencar.pong.client.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import com.threerings.media.sprite.Sprite;
import com.uglygreencar.pong.server.objects.PaddleObject;

public class PaddleSprite extends Sprite {

    public static final int WIDTH = 64;

    public static final int HEIGHT = 16;

    public PaddleSprite(PaddleObject paddleObject) {
        super(WIDTH, HEIGHT);
        super.setLocation(paddleObject.x, paddleObject.y);
    }

    public void updatePaddle(PaddleObject paddleObject) {
        super.setLocation(paddleObject.x, paddleObject.y);
        super.invalidate();
    }

    @Override
    public void paint(Graphics2D gfx) {
        gfx.setPaint(Color.YELLOW);
        gfx.fillRect(_bounds.x, _bounds.y, WIDTH, HEIGHT);
    }
}
