package com.androidtowerwars.view;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import com.androidtowerwars.GameActivity;
import com.androidtowerwars.model.Team;
import com.androidtowerwars.model.Wall;

public class WallView {

    public static Texture mWallTexture;

    public static TextureRegion mWallTextureRegion;

    public static Wall goodWall;

    public static Wall badWall;

    public static void loadResources(GameActivity gameActivity) {
        mWallTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mWallTextureRegion = TextureRegionFactory.createFromAsset(mWallTexture, gameActivity, "gfx/wall.png", 0, 0);
        WorldView.getInstance().getTextureManager().loadTexture(mWallTexture);
    }

    public void loadScene(Scene scene) {
        final Sprite gWall = new Sprite(WorldView.MAP_WIDTH - WallView.mWallTextureRegion.getWidth(), WorldView.MAP_HEIGHT * 0.3f, WallView.mWallTextureRegion);
        gWall.setAlpha(0.0f);
        gWall.setScale(0.00001f);
        scene.getLastChild().attachChild(gWall);
        final Sprite bWall = new Sprite(0, WorldView.MAP_HEIGHT * 0.3f, WallView.mWallTextureRegion);
        bWall.setAlpha(0.0f);
        bWall.setScale(0.00001f);
        scene.getLastChild().attachChild(bWall);
        WallView.goodWall = new Wall(WorldView.MAP_WIDTH, WorldView.MAP_HEIGHT * 0.5f, WallView.mWallTextureRegion.getWidth(), Team.GOOD);
        WallView.badWall = new Wall(0, WorldView.MAP_HEIGHT * 0.5f, WallView.mWallTextureRegion.getWidth(), Team.EVIL);
    }
}
