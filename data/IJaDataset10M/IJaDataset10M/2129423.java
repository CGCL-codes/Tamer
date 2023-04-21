package render.opengl;

import render.*;
import gl4java.*;
import gl4java.awt.GLAnimCanvas;
import gl4java.utils.textures.*;

public class OpenGLTextureProvider extends TextureProvider {

    private GLAnimCanvas _canvas;

    private int[] texture = new int[1];

    public OpenGLTextureProvider(RenderCanvas canvas) {
        _canvas = (GLAnimCanvas) canvas;
    }

    protected void _init() {
    }
}
