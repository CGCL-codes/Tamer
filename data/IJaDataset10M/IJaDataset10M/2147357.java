package xmage.turbine.render.jgl;

import jgl.GL;
import jgl.GLU;
import xmage.raster.GrayscaleImage;
import xmage.raster.Image;
import xmage.turbine.Material;
import xmage.turbine.Texture;

/**
 * Material cache object that keeps material data in renderer-friendly format.
 * The renderer is supposed to render cache objects *only*.
 *
 * It is attached to each material as an aux object.
 */
class JGLMatCache {

    /**
	 * Default (light gray) material texture object id.
	 * Initialized in init() and should not be changed later.
	 */
    private static int defTexObId = -1;

    static void generateDefaultTexture(GL gl, GLU glu) {
        assert defTexObId == -1 : "Default texture already generated";
        GrayscaleImage defaultTexImage = new GrayscaleImage(4, 4);
        defaultTexImage.getBuffer().put(new byte[] { (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0, (byte) 0xb0 });
        Texture defTex = new Texture(defaultTexImage);
        defTex.setName("[default texture]");
        defTexObId = new JGLMatCache(new Material(defTex), gl, glu).texObIds[0];
    }

    /** Number of textures in material. */
    int numTex = 0;

    /** Texture object IDs of this material cache. */
    int[] texObIds = null;

    /** gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, envMode[n]. */
    int[] envMode = null;

    private final Material mat;

    private final GL gl;

    /**
	 * Build material cache for specified material.
	 *
	 * @param mat material
	 * @param gl GL context
	 * @param glu GLU context
	 */
    JGLMatCache(Material mat, GL gl, GLU glu) {
        assert mat != null : "Material may not be null";
        assert gl != null : "GL may not be null";
        assert glu != null : "GLU may not be null";
        this.mat = mat;
        this.gl = gl;
        numTex = mat.getNumTextures();
        texObIds = new int[numTex];
        envMode = new int[numTex];
        for (int i = 0; i < numTex; i++) {
            if (mat.getTexture(i) == null || mat.getTexture(i).getImage() == null) {
                assert defTexObId != -1 : "generateDefaultTexture() must be called " + "before using default material";
                texObIds[i] = defTexObId;
                mat.getTexture(i).setImageChanged(false);
            } else {
                texObIds[i] = genTexObId();
            }
        }
        updateTextures();
    }

    void updateTextures() {
        Texture tex = null;
        for (int i = 0; i < numTex; i++) {
            tex = mat.getTexture(i);
            if (tex.isImageChanged()) {
                if (texObIds[i] == defTexObId) {
                    texObIds[i] = genTexObId();
                }
                updateTexOb(texObIds[i], tex);
                tex.setImageChanged(false);
            }
            switch(tex.getBlendMode()) {
                case Texture.BLEND_REPLACE:
                    envMode[i] = GL.GL_REPLACE;
                    break;
                case Texture.BLEND_DECAL:
                    envMode[i] = GL.GL_DECAL;
                    break;
                case Texture.BLEND_MODULATE:
                    envMode[i] = GL.GL_MODULATE;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid blend mode: " + tex.getBlendMode());
            }
        }
    }

    /**
	 * Generate texture object ID.
	 *
	 * @return OpenGL texture object ID
	 */
    private int genTexObId() {
        int[] texID = new int[1];
        gl.glGenTextures(1, texID);
        return texID[0];
    }

    /**
	 * Update texture object's texture image.
	 *
	 * @param texObId OpenGL texture object ID
	 * @param texture texture image to use for texture object
	 */
    private void updateTexOb(int texObId, Texture texture) {
        Image image = texture.getImage();
        assert image != null : "Texture without image";
        if (image.getWidth() != 32 && image.getWidth() != 64 && image.getWidth() != 128 && image.getWidth() != 256 && image.getWidth() != 512 && image.getWidth() != 1024 && image.getWidth() != 2048 && image.getWidth() != 16 && image.getWidth() != 8 && image.getWidth() != 4 && image.getWidth() != 2 && image.getWidth() != 1) {
            throw new IllegalArgumentException("Texture \"" + texture.getName() + "\" has invalid width: " + image.getWidth());
        }
        if (image.getHeight() != 32 && image.getHeight() != 64 && image.getHeight() != 128 && image.getHeight() != 256 && image.getHeight() != 512 && image.getHeight() != 1024 && image.getHeight() != 2048 && image.getHeight() != 16 && image.getHeight() != 8 && image.getHeight() != 4 && image.getHeight() != 2 && image.getHeight() != 1) {
            throw new IllegalArgumentException("Texture \"" + texture.getName() + "\" has invalid height: " + image.getHeight());
        }
        gl.glBindTexture(GL.GL_TEXTURE_2D, texObId);
    }
}
