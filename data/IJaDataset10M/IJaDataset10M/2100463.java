package com.cinosynachosama.freeland;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.input.touch.TouchEvent;

public class MultitouchControl extends Rectangle {

    private boolean pressed;

    private game.Control action;

    public MultitouchControl(float pX, float pY, float pWidth, float pHeight, game.Control action) {
        super(pX, pY, pWidth, pHeight);
        this.action = action;
    }

    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        switch(pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_MOVE:
            case TouchEvent.ACTION_DOWN:
                this.pressed = true;
                switch(this.action) {
                    case LEFT:
                        game.leftPressed = true;
                        break;
                    case RIGHT:
                        game.rightPressed = true;
                        break;
                    case JUMP:
                        game.jumpPressed = true;
                        break;
                }
                break;
            case TouchEvent.ACTION_UP:
                if (this.pressed) {
                    this.pressed = false;
                    switch(this.action) {
                        case LEFT:
                            game.leftPressed = false;
                            break;
                        case RIGHT:
                            game.rightPressed = false;
                            break;
                        case JUMP:
                            game.jumpPressed = false;
                            break;
                    }
                }
                break;
        }
        return true;
    }
}
