package serfsoftherealm.texture.lwjgl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import serfsoftherealm.util.LogFactory;

public class LWJGLTextureManager {

    private static final Logger log = LogFactory.getLogger(LWJGLTextureManager.class);

    private HashMap<String, LWJGLTextureArea> textureAreas = new HashMap<String, LWJGLTextureArea>();

    private HashMap<String, LWJGLTexture> textures = new HashMap<String, LWJGLTexture>();

    /**
	 * Loads a texture atlas.
	 * @param descriptionFile The file that describes texture areas keyed by virtual
	 * file names.
	 * @param textureFile The base texture.
	 * @param virtualDir The virtual base directory to which paths found in the
	 * description file are relative.
	 * @throws IOException
	 */
    public void loadTextureAtlas(File descriptionFile, File textureFile, File virtualDir) throws IOException {
        LWJGLTextureAtlas atlas = new LWJGLTextureAtlas(descriptionFile, textureFile);
        for (String key : atlas) {
            File virtualFile = new File(virtualDir, key);
            LWJGLTextureArea area = atlas.lookup(key);
            textureAreas.put(virtualFile.getCanonicalPath(), area);
        }
    }

    /**
	 * Fetches a texture area for immediate usage. This does not necessarily load the texture -
	 * so make sure to call glBindTexture() on LWJGLTextureArea.getGLIdentifier before drawing.
	 * Be sure to call a corresponding release() shortly afterwards.
	 * @param id
	 * @return
	 */
    public LWJGLTextureArea acquire(File file) {
        String id;
        try {
            id = file.getCanonicalPath();
        } catch (IOException e1) {
            throw new RuntimeException("This occurred and it shouldn't");
        }
        LWJGLTextureArea area = textureAreas.get(id);
        try {
            if (area == null) {
                area = new LWJGLTextureArea(loadCreateTexture(file));
                textureAreas.put(id, area);
            }
            area.getTexture().acquire();
        } catch (IOException e) {
            log.severe("Failed to load texture: " + id);
        }
        return area;
    }

    /**
	 * Creates a texture and tries to load it.
	 * If the texture has already been created a handle to it is returned instead.
	 * @param file
	 * @return
	 * @throws IOException 
	 */
    LWJGLTexture loadCreateTexture(File file) throws IOException {
        LWJGLTexture tex = textures.get(file);
        if (tex != null) {
            return tex;
        }
        tex = new LWJGLTexture(file);
        textures.put(file.getCanonicalPath(), tex);
        return tex;
    }

    /**
	 * Unloads all textures that aren't acquired right now.
	 */
    public void unloadTextures() {
        for (LWJGLTexture tex : textures.values()) {
            tex.tryUnload();
        }
    }
}
