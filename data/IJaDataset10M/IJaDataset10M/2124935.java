package org.stellarium.ui.render;

import org.stellarium.StellariumException;
import org.stellarium.data.ResourceLocatorUtil;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import static org.stellarium.ui.SglAccess.*;

public class GlPng {

    public static final int PNG_NOMIPMAPS = 0;

    public static final int PNG_BUILDMIPMAPS = -1;

    public static final int PNG_SIMPLEMIPMAPS = -2;

    public static final int PNG_NOMIPMAP = PNG_NOMIPMAPS;

    public static final int PNG_BUILDMIPMAP = PNG_BUILDMIPMAPS;

    public static final int PNG_SIMPLEMIPMAP = PNG_SIMPLEMIPMAPS;

    public static final int PNG_CALLBACK = -3;

    public static final int PNG_ALPHA = -2;

    public static final int PNG_SOLID = -1;

    public static final int PNG_STENCIL = 0;

    public static final int PNG_BLEND1 = 1;

    public static final int PNG_BLEND2 = 2;

    public static final int PNG_BLEND3 = 3;

    public static final int PNG_BLEND4 = 4;

    public static final int PNG_BLEND5 = 5;

    public static final int PNG_BLEND6 = 6;

    public static final int PNG_BLEND7 = 7;

    public static final int PNG_BLEND8 = 8;

    static {
        try {
            ImageIO.setUseCache(true);
            File pngCache = new File("./_png_tmp");
            if (!pngCache.exists()) pngCache.mkdirs();
            ImageIO.setCacheDirectory(pngCache);
        } catch (Exception e) {
            System.err.println("Error initializing ImageIO:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static class PngInfo {

        public int width;

        public int height;

        public int depth;

        public int alpha;
    }

    public static class PngRawInfo extends PngInfo {

        public int components;

        public byte[] data;

        public byte[] palette;
    }

    public interface AlphaCallback {

        public byte execute(byte red, byte green, byte blue);
    }

    public static AlphaCallback defaultAlphaCallback = new AlphaCallback() {

        public byte execute(byte red, byte green, byte blue) {
            return (byte) 255;
        }
    };

    static byte stencilRed, stencilGreen, stencilBlue;

    static int standardOrientation;

    static int palettedTextures = -1;

    static int maxTextureSize;

    /**
     * screenGamma = displayGamma/viewingGamma
     * displayGamma = CRT has gamma of ~2.2
     * viewingGamma depends on platform. PC is 1.0, Mac is 1.45, SGI defaults
     * to 1.7, but this can be checked and changed w/ /usr/sbin/gamma command.
     * If the environment variable VIEWING_GAMMA is set, adjust gamma per this value.
     */
    static double screenGamma;

    static {
        if (ResourceLocatorUtil.isMacOS()) {
            screenGamma = 2.2 / 1.45;
        } else if (ResourceLocatorUtil.isSgi()) {
            screenGamma = 2.2 / 1.7;
        } else {
            screenGamma = 2.2 / 1.0;
        }
    }

    static boolean gammaExplicit = false;

    static void checkForGammaEnv() {
        String gammaEnv = System.getenv("VIEWING_GAMMA");
        if (gammaEnv != null && !gammaExplicit) {
            double viewingGamma = Double.parseDouble(gammaEnv);
            screenGamma = 2.2 / viewingGamma;
        }
    }

    static int safeSize(int i) {
        int p;
        if (i > maxTextureSize) return maxTextureSize;
        for (p = 0; p < 24; p++) if (i <= (1 << p)) return 1 << p;
        return maxTextureSize;
    }

    static void resize(int components, ByteBuffer d1, int w1, int h1, ByteBuffer d2, int w2, int h2) {
        float sx = (float) w1 / w2, sy = (float) h1 / h2;
        int x, y, xx, yy, c;
        int index;
        for (y = 0; y < h2; y++) {
            yy = (int) (y * sy) * w1;
            for (x = 0; x < w2; x++) {
                xx = (int) (x * sx);
                index = (yy + xx) * components;
                for (c = 0; c < components; c++) {
                    d2.put(d1.get(index++));
                }
            }
        }
    }

    static boolean halfSize(int components, int width, int height, ByteBuffer data, ByteBuffer d, boolean filter) {
        int x, y, c;
        int line = width * components;
        if (width > 1 && height > 1) {
            if (filter) for (y = 0; y < height; y += 2) {
                for (x = 0; x < width; x += 2) {
                    for (c = 0; c < components; c++) {
                        int curPos = data.position();
                        d.put((byte) ((data.get(curPos) + data.get(curPos + components) + data.get(curPos + line) + data.get(curPos + line + components)) / 4));
                        curPos++;
                        data.position(curPos);
                    }
                    data.position(data.position() + components);
                }
                data.position(data.position() + line);
            } else for (y = 0; y < height; y += 2) {
                for (x = 0; x < width; x += 2) {
                    for (c = 0; c < components; c++) {
                        d.put(data.get());
                    }
                    data.position(data.position() + components);
                }
                data.position(data.position() + line);
            }
        } else if (width > 1 && height == 1) {
            if (filter) for (y = 0; y < height; y += 1) {
                for (x = 0; x < width; x += 2) {
                    for (c = 0; c < components; c++) {
                        int curPos = data.position();
                        d.put((byte) ((data.get(curPos) + data.get(curPos + components)) / 2));
                        curPos++;
                        data.position(curPos);
                    }
                    data.position(data.position() + components);
                }
            } else for (y = 0; y < height; y += 1) {
                for (x = 0; x < width; x += 2) {
                    for (c = 0; c < components; c++) {
                        d.put(data.get());
                    }
                    data.position(data.position() + components);
                }
            }
        } else if (width == 1 && height > 1) {
            if (filter) for (y = 0; y < height; y += 2) {
                for (x = 0; x < width; x += 1) {
                    for (c = 0; c < components; c++) {
                        int curPos = data.position();
                        d.put((byte) ((data.get(curPos) + data.get(curPos + line)) / 2));
                        curPos++;
                        data.position(curPos);
                    }
                }
                data.position(data.position() + line);
            } else for (y = 0; y < height; y += 2) {
                for (x = 0; x < width; x += 1) {
                    for (c = 0; c < components; c++) {
                        d.put(data.get());
                    }
                }
                data.position(data.position() + line);
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Replacement for gluBuild2DMipmaps so GLU isn't needed
     */
    static void build2DMipmaps(int components, int width, int height, int format, ByteBuffer data, boolean filter) {
        int level = 0;
        ByteBuffer d = ByteBuffer.allocateDirect((width / 2) * (height / 2) * components + 4);
        ByteBuffer last = data;
        glTexImage2D(GL.GL_TEXTURE_2D, level, components, width, height, 0, format, GL.GL_UNSIGNED_BYTE, data);
        level++;
        while (halfSize(components, width, height, last, d, filter)) {
            if (width > 1) width /= 2;
            if (height > 1) height /= 2;
            glTexImage2D(GL.GL_TEXTURE_2D, level, components, width, height, 0, format, GL.GL_UNSIGNED_BYTE, d);
            level++;
            last = d;
        }
        d.clear();
    }

    public static boolean pngLoad(String filename, int mipmap, int trans, PngInfo info) {
        return pngLoadF(new File(filename), mipmap, trans, info);
    }

    public static boolean pngLoadF(File file, int mipmap, int trans, PngInfo info) {
        if (!file.exists()) throw new StellariumException("PNG file " + file + " does not exists");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new StellariumException("Error reading png file " + file, e);
        }
        info.width = image.getWidth();
        info.height = image.getHeight();
        info.depth = image.getColorModel().getPixelSize();
        int color = image.getType();
        if (maxTextureSize == 0) {
            int[] max = new int[1];
            glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, max, 0);
            maxTextureSize = max[0];
        }
        if (color == BufferedImage.TYPE_BYTE_INDEXED) {
        }
        return false;
    }

    private static int setParams(int wrapst, int magfilter, int minfilter) {
        int[] ids = new int[1];
        glGenTextures(1, ids, 0);
        glBindTexture(GL.GL_TEXTURE_2D, ids[0]);
        glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrapst);
        glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrapst);
        glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, magfilter);
        glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, minfilter);
        return ids[0];
    }

    public static int pngBind(String filename, int mipmap, int trans, PngInfo info, int wrapst, int minfilter, int magfilter) {
        int id = setParams(wrapst, magfilter, minfilter);
        if (id != 0 && pngLoad(filename, mipmap, trans, info)) return id;
        return 0;
    }

    public static int pngBindF(File file, int mipmap, int trans, PngInfo info, int wrapst, int minfilter, int magfilter) {
        int id = setParams(wrapst, magfilter, minfilter);
        if (id != 0 && pngLoadF(file, mipmap, trans, info)) return id;
        return 0;
    }

    public static void pngSetStencil(byte red, byte green, byte blue) {
    }

    public static void pngSetAlphaCallback(AlphaCallback callback) {
    }

    public static void pngSetViewingGamma(double viewingGamma) {
    }

    public static void pngSetStandardOrientation(int standardorientation) {
    }
}
