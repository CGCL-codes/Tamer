package com.android1.amarena2d.texture;

import com.android1.amarena2d.engine.IsLoadable;
import com.android1.amarena2d.engine.Resource;
import java.util.List;

public interface TextureAtlas extends IsLoadable, Resource, Resource.Consumer {

    List<AtlasTextureFrame> getTextureFrames();

    AtlasTextureFrame getTextureFrame(int index);

    AtlasTextureFrame findTextureFrame(String tag);

    boolean contains(String tag);

    ManagedTexture getManagedTexture();

    AtlasMesh getAtlasMesh();
}
