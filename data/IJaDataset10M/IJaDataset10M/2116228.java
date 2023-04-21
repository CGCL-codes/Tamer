package gamecomponents.graphic;

import com.threed.jpct.Texture;

/**
 * Helper methods regarding Textures.
 * @author Team Deer
 * 
 */
public class TextureHelper {

    private static Texture transparentTexture = loadTexture("Content\\Sprites\\GeneralSprites\\transparentTexture.png");

    /**
	 * 
	 * @return Returns a 2x2 transparent texture.
	 */
    public static Texture getTransparentTexture() {
        return transparentTexture;
    }

    private static Texture semiTransparentTexture = loadTexture("Content\\Sprites\\GeneralSprites\\semiTransparentTexture.png");

    /**
	 * 
	 * @return Returns a 2x2 semi-transparent texture (black, 75% alpha).
	 */
    public static Texture getSemiTransparentTexture() {
        return semiTransparentTexture;
    }

    private static Texture blankTexture = loadTexture("Content\\Sprites\\GeneralSprites\\blankTexture.png");

    /**
	 * 
	 * @return Returns a 2x2 white texture.
	 */
    public static Texture getBlankTexture() {
        return blankTexture;
    }

    /**
	 * Loads a texture from a file.
	 * @param path
	 *            - The path of the file
	 * @return a texture made from the file
	 */
    public static Texture loadTexture(final String path) {
        java.io.InputStream textureInputStream = null;
        try {
            textureInputStream = new java.io.BufferedInputStream(new java.io.FileInputStream(path));
        } catch (final java.io.FileNotFoundException e) {
            return null;
        }
        return new Texture(textureInputStream, true);
    }
}
