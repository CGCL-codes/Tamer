package uy.com.pnocetti.spaceshooter.entities;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import android.content.Context;

public class Shield extends ScreenEntity implements IUpdateHandler {

    private boolean mActive;

    private Ship mShip;

    private TextureRegion mTextureRegion;

    private boolean mDoActivate;

    private float mElapsedShield;

    public Shield(float posX, float posY) {
        super(posX, posY);
    }

    public Shield(Ship pShip) {
        this.mIPosX = pShip.mIPosX;
        this.mIPosY = pShip.mIPosY;
        this.mShip = pShip;
    }

    public void activate(TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) this.mDoActivate = true;
    }

    @Override
    protected void createSprite() {
        mSprite = new Sprite(mIPosX, mIPosY, mTextureRegion);
        mSprite.setVisible(false);
    }

    public boolean getActive() {
        return this.mActive;
    }

    @Override
    protected void initializePhysics(PhysicsWorld mPhysicsWorld, Scene mScene) {
        this.mSprite.setPosition(this.mIPosX, this.mIPosY);
        mScene.registerUpdateHandler(this);
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        mElapsedShield += pSecondsElapsed;
        if (mElapsedShield >= 10) mElapsedShield = 0;
        if (mDoActivate) {
            if ((mElapsedShield >= 3 || mElapsedShield == 0) && !mActive) {
                mSprite.setVisible(true);
                mActive = true;
                mElapsedShield = 0;
            }
            mDoActivate = false;
        }
        if (mActive && mElapsedShield >= 5) {
            mSprite.setVisible(false);
            mActive = false;
            mElapsedShield = 0;
        }
        this.mSprite.setPosition(this.mShip.getSprite().getX(), this.mShip.getSprite().getY());
    }

    @Override
    public void reset() {
    }

    @Override
    protected void setPhysics(PhysicsWorld mPhysicsWorld, Scene mScene) {
        initializePhysics(mPhysicsWorld, mScene);
    }

    @Override
    public void setTextureRegion(Context context, String srcLocation, int pTextureXPosition, int pTextureYPosition) {
        this.mTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, context, srcLocation, pTextureXPosition, pTextureYPosition);
    }
}
