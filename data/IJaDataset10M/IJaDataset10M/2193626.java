package com.android1.amarena2d.texture;

import com.android1.amarena2d.commons.Size;
import com.android1.amarena2d.engine.IsLoadable;
import com.android1.amarena2d.engine.Resource;
import com.badlogic.gdx.math.Vector2;

public interface TexturedMesh extends IsLoadable, Resource {

    public void init();

    public boolean isInit();

    public void render();

    public float getWidth();

    public float getHeight();

    public void dispose();

    public TexturedMesh copy();

    public TextureFrame getTextureFrame();

    public void setTextureFrame(final TextureFrame textureFrame);

    public ManagedTexture getManagedTexture();

    public boolean isDirty();

    public Size getSize();

    public void setSize(final float width, final float height);

    public void setWidth(final float width);

    public void setHeight(final float height);

    public void setFlipX(boolean flipX);

    public void setFlipY(boolean flipY);

    public boolean isFlipY();

    public boolean isFlipX();

    public float getX();

    public float getY();

    public void setX(float x);

    public void setY(float y);

    public void setXY(float x, float y);

    public Vector2 getXY();
}
