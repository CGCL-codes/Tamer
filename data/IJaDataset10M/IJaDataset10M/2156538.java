package demos.util;

import java.io.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

/** Helper class for loading cubemaps from a set of textures. */
public class Cubemap {

    private static final String[] suffixes = { "posx", "negx", "posy", "negy", "posz", "negz" };

    private static final int[] targets = { GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X, GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

    public static Texture loadFromStreams(ClassLoader scope, String basename, String suffix, boolean mipmapped) throws IOException, GLException {
        Texture cubemap = TextureIO.newTexture(GL.GL_TEXTURE_CUBE_MAP);
        for (int i = 0; i < suffixes.length; i++) {
            String resourceName = basename + suffixes[i] + "." + suffix;
            TextureData data = TextureIO.newTextureData(scope.getResourceAsStream(resourceName), mipmapped, FileUtil.getFileSuffix(resourceName));
            if (data == null) {
                throw new IOException("Unable to load texture " + resourceName);
            }
            cubemap.updateImage(data, targets[i]);
        }
        return cubemap;
    }
}
