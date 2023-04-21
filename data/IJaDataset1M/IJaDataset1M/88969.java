package com.zeldroid;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Bomb extends Item {

    private Drawable staticImage;

    private Drawable timerImage;

    private Drawable explodeAnimation1;

    private Drawable explodeAnimation2;

    private Drawable currentAnimation;

    private boolean isExploding;

    private boolean isCountingDown;

    int countdown;

    public Bomb(int x, int y, boolean isCountingDown) {
        super();
        this.isCountingDown = isCountingDown;
        isExploding = false;
        countdown = 30;
        staticImage = GameView.resources.getDrawable(R.drawable.staticbomb);
        timerImage = GameView.resources.getDrawable(R.drawable.timerbomb);
        explodeAnimation1 = GameView.resources.getDrawable(R.drawable.explosion1);
        explodeAnimation2 = GameView.resources.getDrawable(R.drawable.explosion2);
        currentAnimation = staticImage;
    }

    public void update() {
        if (isCountingDown) {
            countdown -= 1;
            if (countdown == 0) {
                isCountingDown = false;
                isExploding = true;
                countdown = 7;
            }
            if (currentAnimation == staticImage) {
                currentAnimation = timerImage;
            } else {
                currentAnimation = staticImage;
            }
        }
        if (isExploding) {
            countdown -= 1;
            if (countdown == 0) {
                isExploding = false;
                setIsAlive(false);
            }
            if (currentAnimation == explodeAnimation1) {
                currentAnimation = explodeAnimation2;
            } else {
                currentAnimation = explodeAnimation1;
            }
        }
    }

    public Rect getBounds() {
        Rect tempBounds = currentAnimation.getBounds();
        tempBounds.left = this.getX();
        tempBounds.top = this.getY();
        return tempBounds;
    }

    @Override
    public Drawable getSprite() {
        currentAnimation.setBounds(this.getBounds());
        return currentAnimation;
    }
}
