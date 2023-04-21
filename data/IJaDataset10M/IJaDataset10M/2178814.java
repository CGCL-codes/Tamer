package com.android1.amarena2d.texture;

public interface AtlasTextureFrame extends TextureFrame {

    public int getIndex();

    public TextureAtlas getTextureAtlas();

    @Override
    public AtlasTexturedMesh getTexturedMesh();
}
