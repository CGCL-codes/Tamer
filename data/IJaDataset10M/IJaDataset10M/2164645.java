package uy.com.pnocetti.spaceshooter.entities;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import android.content.Context;

public class Explosion extends ScreenEntity {

    private TiledTextureRegion mTextureRegion;

    public Explosion(float posX, float posY) {
        super(posX, posY);
    }

    public void Animate(float x, float y) {
        mSprite.setPosition(x, y);
        ((AnimatedSprite) mSprite).animate(20, 0, new AnimatedSprite.IAnimationListener() {

            public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
                mSprite.setPosition(-100, -100);
            }
        });
    }

    @Override
    protected void createSprite() {
        mSprite = new AnimatedSprite(mIPosX, mIPosY, mWidth, mHeight, mTextureRegion);
    }

    @Override
    protected void initializePhysics(PhysicsWorld mPhysicsWorld, Scene mScene) {
    }

    @Override
    protected void setPhysics(PhysicsWorld mPhysicsWorld, Scene mScene) {
    }

    @Override
    public void setTextureRegion(Context context, String srcLocation, int pTextureXPosition, int pTextureYPosition) {
        this.mTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, context, srcLocation, pTextureXPosition, pTextureYPosition, 4, 4);
    }
}
