package javax.media.j3d;

import java.util.*;
import javax.vecmath.*;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;

/**
 * The Texture object is a component object of an Appearance object
 * that defines the texture properties used when texture mapping is
 * enabled. Texture object is an abstract class and all texture 
 * objects must be created as either a Texture2D object or a
 * Texture3D object.
 */
abstract class TextureRetained extends NodeComponentRetained {

    static final int ENABLE_CHANGED = 0x001;

    static final int COLOR_CHANGED = 0x002;

    static final int IMAGE_CHANGED = 0x004;

    static final int STATE_CHANGED = 0x008;

    static final int UPDATE_IMAGE = 0x010;

    static final int IMAGES_CHANGED = 0x020;

    static final int BASE_LEVEL_CHANGED = 0x040;

    static final int MAX_LEVEL_CHANGED = 0x080;

    static final int MIN_LOD_CHANGED = 0x100;

    static final int MAX_LOD_CHANGED = 0x200;

    static final int LOD_OFFSET_CHANGED = 0x400;

    static final int MIN_FILTER = 0;

    static final int MAG_FILTER = 1;

    int boundaryWidth = 0;

    int boundaryModeS = Texture.WRAP;

    int boundaryModeT = Texture.WRAP;

    int minFilter = Texture.BASE_LEVEL_POINT;

    int magFilter = Texture.BASE_LEVEL_POINT;

    int isDirty = 0xffff;

    Color4f boundaryColor = new Color4f(0.0f, 0.0f, 0.0f, 0.0f);

    int objectId = -1;

    int mipmapMode = Texture.BASE_LEVEL;

    int format = Texture.RGB;

    int width = 1;

    int height = 1;

    private boolean widthOrHeightIsNPOT = false;

    ImageComponentRetained images[][];

    int maxLevels = 0;

    private int maxMipMapLevels = 0;

    int numFaces = 1;

    int baseLevel = 0;

    int maximumLevel = 0;

    float minimumLod = -1000.0f;

    float maximumLod = 1000.0f;

    Point3f lodOffset = null;

    private boolean useAsRaster = false;

    boolean enable = true;

    boolean userSpecifiedEnable = true;

    boolean isAlphaNeedUpdate = false;

    int numSharpenTextureFuncPts = 0;

    float sharpenTextureFuncPts[] = null;

    float filter4FuncPts[] = null;

    int anisotropicFilterMode = Texture.ANISOTROPIC_NONE;

    float anisotropicFilterDegree = 1.0f;

    int resourceCreationMask = 0x0;

    int resourceUpdatedMask = 0x0;

    int resourceLodUpdatedMask = 0x0;

    int resourceInReloadList = 0x0;

    ArrayList imageUpdateInfo[][];

    int imageUpdatePruneMask[];

    private HashMap<RenderBin, Integer> textureBinRefCount = new HashMap<RenderBin, Integer>();

    private int texTimestamp = 0;

    Object resourceLock = new Object();

    private static boolean isPowerOfTwo(int val) {
        return ((val & (val - 1)) == 0);
    }

    void initialize(int format, int width, int widLevels, int height, int heiLevels, int mipmapMode, int boundaryWidth) {
        this.mipmapMode = mipmapMode;
        this.format = format;
        this.width = width;
        this.height = height;
        this.boundaryWidth = boundaryWidth;
        if (!isPowerOfTwo(width) || !isPowerOfTwo(height)) {
            this.widthOrHeightIsNPOT = true;
        }
        if (widLevels > heiLevels) {
            maxMipMapLevels = widLevels + 1;
        } else {
            maxMipMapLevels = heiLevels + 1;
        }
        if (mipmapMode != Texture.BASE_LEVEL) {
            baseLevel = 0;
            maximumLevel = maxMipMapLevels - 1;
            maxLevels = maxMipMapLevels;
        } else {
            baseLevel = 0;
            maximumLevel = 0;
            maxLevels = 1;
        }
        images = new ImageComponentRetained[numFaces][maxLevels];
        for (int j = 0; j < numFaces; j++) {
            for (int i = 0; i < maxLevels; i++) {
                images[j][i] = null;
            }
        }
    }

    final int getFormat() {
        return this.format;
    }

    final int getWidth() {
        return this.width;
    }

    final int getHeight() {
        return this.height;
    }

    final int numMipMapLevels() {
        return (maximumLevel - baseLevel + 1);
    }

    /**
     * Sets the boundary mode for the S coordinate in this texture object.
     * @param boundaryModeS the boundary mode for the S coordinate,
     * one of: CLAMP or WRAP.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final void initBoundaryModeS(int boundaryModeS) {
        this.boundaryModeS = boundaryModeS;
    }

    /**
     * Retrieves the boundary mode for the S coordinate.
     * @return the current boundary mode for the S coordinate.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final int getBoundaryModeS() {
        return boundaryModeS;
    }

    /**
     * Sets the boundary mode for the T coordinate in this texture object.
     * @param boundaryModeT the boundary mode for the T coordinate,
     * one of: CLAMP or WRAP.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final void initBoundaryModeT(int boundaryModeT) {
        this.boundaryModeT = boundaryModeT;
    }

    /**
     * Retrieves the boundary mode for the T coordinate.
     * @return the current boundary mode for the T coordinate.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final int getBoundaryModeT() {
        return boundaryModeT;
    }

    /**
     * Retrieves the boundary width.
     * @return the boundary width of this texture.
     */
    final int getBoundaryWidth() {
        return boundaryWidth;
    }

    /**
     * Sets the minification filter function.  This
     * function is used when the pixel being rendered maps to an area
     * greater than one texel.
     * @param minFilter the minification filter, one of:
     * FASTEST, NICEST, BASE_LEVEL_POINT, BASE_LEVEL_LINEAR, 
     * MULTI_LEVEL_POINT, MULTI_LEVEL_LINEAR.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final void initMinFilter(int minFilter) {
        this.minFilter = minFilter;
    }

    /**
     * Retrieves the minification filter.
     * @return the current minification filter function.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final int getMinFilter() {
        return minFilter;
    }

    /**
     * Sets the magnification filter function.  This
     * function is used when the pixel being rendered maps to an area
     * less than or equal to one texel.
     * @param magFilter the magnification filter, one of:
     * FASTEST, NICEST, BASE_LEVEL_POINT, or BASE_LEVEL_LINEAR.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final void initMagFilter(int magFilter) {
        this.magFilter = magFilter;
    }

    /**
     * Retrieves the magnification filter.
     * @return the current magnification filter function.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final int getMagFilter() {
        return magFilter;
    }

    /**
     * Sets a specified mipmap level.
     * @param level mipmap level to set: 0 is the base level
     * @param image pixel array object containing the texture image
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if an ImageComponent3D
     * is used in a Texture2D or ImageComponent2D in Texture3D
     * power of 2 OR invalid format/mipmapMode is specified.
     */
    void initImage(int level, ImageComponent image) {
        checkImageSize(level, image);
        if (this.images == null) {
            throw new IllegalArgumentException(J3dI18N.getString("TextureRetained0"));
        }
        if (this.source instanceof Texture2D) {
            if (image instanceof ImageComponent3D) throw new IllegalArgumentException(J3dI18N.getString("Texture8"));
        } else {
            if (image instanceof ImageComponent2D) throw new IllegalArgumentException(J3dI18N.getString("Texture14"));
        }
        if (this.source.isLive()) {
            if (this.images[0][level] != null) {
                this.images[0][level].clearLive(refCount);
            }
            if (image != null) {
                ((ImageComponentRetained) image.retained).setLive(inBackgroundGroup, refCount);
            }
        }
        if (image != null) {
            this.images[0][level] = (ImageComponentRetained) image.retained;
        } else {
            this.images[0][level] = null;
        }
    }

    final void checkImageSize(int level, ImageComponent image) {
        if (image != null) {
            int imgWidth = ((ImageComponentRetained) image.retained).width;
            int imgHeight = ((ImageComponentRetained) image.retained).height;
            int wdh = width;
            int hgt = height;
            for (int i = 0; i < level; i++) {
                wdh >>= 1;
                hgt >>= 1;
            }
            if (wdh < 1) wdh = 1;
            if (hgt < 1) hgt = 1;
            if ((wdh != (imgWidth - 2 * boundaryWidth)) || (hgt != (imgHeight - 2 * boundaryWidth))) {
                throw new IllegalArgumentException(J3dI18N.getString("TextureRetained1"));
            }
        }
    }

    final void checkSizes(ImageComponentRetained images[]) {
        if (images != null) {
            int hgt = height;
            int wdh = width;
            for (int level = 0; level < images.length; level++) {
                int imgWidth = images[level].width;
                int imgHeight = images[level].height;
                assert (wdh == (imgWidth - 2 * boundaryWidth)) && (hgt == (imgHeight - 2 * boundaryWidth));
                wdh /= 2;
                hgt /= 2;
                if (wdh < 1) wdh = 1;
                if (hgt < 1) hgt = 1;
            }
        }
    }

    final void setImage(int level, ImageComponent image) {
        initImage(level, image);
        Object arg[] = new Object[3];
        arg[0] = new Integer(level);
        arg[1] = image;
        arg[2] = new Integer(0);
        sendMessage(IMAGE_CHANGED, arg);
        if (userSpecifiedEnable) {
            enable = userSpecifiedEnable;
            if (image != null && level >= baseLevel && level <= maximumLevel) {
                ImageComponentRetained img = (ImageComponentRetained) image.retained;
                if (img.isByReference()) {
                    if (img.getRefImage(0) == null) {
                        enable = false;
                    }
                } else {
                    if (img.getImageData(isUseAsRaster()).get() == null) {
                        enable = false;
                    }
                }
                if (!enable) sendMessage(ENABLE_CHANGED, Boolean.FALSE);
            }
        }
    }

    void initImages(ImageComponent[] images) {
        if (images.length != maxLevels) throw new IllegalArgumentException(J3dI18N.getString("Texture20"));
        for (int i = 0; i < images.length; i++) {
            initImage(i, images[i]);
        }
    }

    final void setImages(ImageComponent[] images) {
        int i;
        initImages(images);
        ImageComponent[] imgs = new ImageComponent[images.length];
        for (i = 0; i < images.length; i++) {
            imgs[i] = images[i];
        }
        Object arg[] = new Object[2];
        arg[0] = imgs;
        arg[1] = new Integer(0);
        sendMessage(IMAGES_CHANGED, arg);
        if (userSpecifiedEnable) {
            enable = userSpecifiedEnable;
            for (i = baseLevel; i <= maximumLevel && enable; i++) {
                if (images[i] != null) {
                    ImageComponentRetained img = (ImageComponentRetained) images[i].retained;
                    if (img.isByReference()) {
                        if (img.getRefImage(0) == null) {
                            enable = false;
                        }
                    } else {
                        if (img.getImageData(isUseAsRaster()).get() == null) {
                            enable = false;
                        }
                    }
                }
            }
            if (!enable) {
                sendMessage(ENABLE_CHANGED, Boolean.FALSE);
            }
        }
    }

    /**
     * Gets a specified mipmap level.
     * @param level mipmap level to get: 0 is the base level
     * @return the pixel array object containing the texture image
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    final ImageComponent getImage(int level) {
        return (((images != null) && (images[0][level] != null)) ? (ImageComponent) images[0][level].source : null);
    }

    final ImageComponent[] getImages() {
        if (images == null) return null;
        ImageComponent[] rImages = new ImageComponent[images[0].length];
        for (int i = 0; i < images[0].length; i++) {
            if (images[0][i] != null) rImages[i] = (ImageComponent) images[0][i].source; else rImages[i] = null;
        }
        return rImages;
    }

    /**
     * Sets mipmap mode for texture mapping for this texture object.  
     * @param mipMapMode the new mipmap mode for this object.  One of:
     * BASE_LEVEL or MULTI_LEVEL_MIPMAP.
     * @exception RestrictedAccessException if the method is called
     */
    final void initMipMapMode(int mipmapMode) {
        if (this.mipmapMode == mipmapMode) return;
        int prevMaxLevels = maxLevels;
        this.mipmapMode = mipmapMode;
        if (mipmapMode != Texture.BASE_LEVEL) {
            maxLevels = maxMipMapLevels;
        } else {
            baseLevel = 0;
            maximumLevel = 0;
            maxLevels = 1;
        }
        ImageComponentRetained[][] newImages = new ImageComponentRetained[numFaces][maxLevels];
        if (prevMaxLevels < maxLevels) {
            for (int f = 0; f < numFaces; f++) {
                for (int i = 0; i < prevMaxLevels; i++) {
                    newImages[f][i] = images[f][i];
                }
                for (int j = prevMaxLevels; j < maxLevels; j++) {
                    newImages[f][j] = null;
                }
            }
        } else {
            for (int f = 0; f < numFaces; f++) {
                for (int i = 0; i < maxLevels; i++) newImages[f][i] = images[f][i];
            }
        }
        images = newImages;
    }

    /**
     * Retrieves current mipmap mode.
     * @return current mipmap mode of this texture object.
     * @exception RestrictedAccessException if the method is called
     */
    final int getMipMapMode() {
        return this.mipmapMode;
    }

    /**
     * Enables or disables texture mapping for this
     * appearance component object.
     * @param state true or false to enable or disable texture mapping
     */
    final void initEnable(boolean state) {
        userSpecifiedEnable = state;
    }

    /**
     * Enables or disables texture mapping for this
     * appearance component object and sends a 
     * message notifying the interested structures of the change.
     * @param state true or false to enable or disable texture mapping
     */
    final void setEnable(boolean state) {
        initEnable(state);
        if (state == enable) {
            return;
        }
        enable = state;
        for (int j = 0; j < numFaces && enable; j++) {
            for (int i = baseLevel; i <= maximumLevel && enable; i++) {
                if (images[j][i].isByReference()) {
                    if (images[j][i].getRefImage(0) == null) {
                        enable = false;
                    }
                } else {
                    if (images[j][i].getImageData(isUseAsRaster()).get() == null) {
                        enable = false;
                    }
                }
            }
        }
        sendMessage(ENABLE_CHANGED, (enable ? Boolean.TRUE : Boolean.FALSE));
    }

    /**
     * Retrieves the state of the texture enable flag.
     * @return true if texture mapping is enabled,
     * false if texture mapping is disabled
     */
    final boolean getEnable() {
        return userSpecifiedEnable;
    }

    final void initBaseLevel(int level) {
        if ((level < 0) || (level > maximumLevel)) {
            throw new IllegalArgumentException(J3dI18N.getString("Texture36"));
        }
        baseLevel = level;
    }

    final void setBaseLevel(int level) {
        if (level == baseLevel) return;
        initBaseLevel(level);
        sendMessage(BASE_LEVEL_CHANGED, new Integer(level));
    }

    final int getBaseLevel() {
        return baseLevel;
    }

    final void initMaximumLevel(int level) {
        if ((level < baseLevel) || (level >= maxMipMapLevels)) {
            throw new IllegalArgumentException(J3dI18N.getString("Texture37"));
        }
        if ((mipmapMode == Texture.BASE_LEVEL) && (level != 0)) {
            throw new IllegalArgumentException(J3dI18N.getString("Texture48"));
        }
        maximumLevel = level;
    }

    final void setMaximumLevel(int level) {
        if (level == maximumLevel) return;
        initMaximumLevel(level);
        sendMessage(MAX_LEVEL_CHANGED, new Integer(level));
    }

    final int getMaximumLevel() {
        return maximumLevel;
    }

    final void initMinimumLOD(float lod) {
        if (lod > maximumLod) {
            throw new IllegalArgumentException(J3dI18N.getString("Texture42"));
        }
        minimumLod = lod;
    }

    final void setMinimumLOD(float lod) {
        initMinimumLOD(lod);
        sendMessage(MIN_LOD_CHANGED, new Float(lod));
    }

    final float getMinimumLOD() {
        return minimumLod;
    }

    final void initMaximumLOD(float lod) {
        if (lod < minimumLod) {
            throw new IllegalArgumentException(J3dI18N.getString("Texture42"));
        }
        maximumLod = lod;
    }

    final void setMaximumLOD(float lod) {
        initMaximumLOD(lod);
        sendMessage(MAX_LOD_CHANGED, new Float(lod));
    }

    final float getMaximumLOD() {
        return maximumLod;
    }

    final void initLodOffset(float s, float t, float r) {
        if (lodOffset == null) {
            lodOffset = new Point3f(s, t, r);
        } else {
            lodOffset.set(s, t, r);
        }
    }

    final void setLodOffset(float s, float t, float r) {
        initLodOffset(s, t, r);
        sendMessage(LOD_OFFSET_CHANGED, new Point3f(s, t, r));
    }

    final void getLodOffset(Tuple3f offset) {
        if (lodOffset == null) {
            offset.set(0.0f, 0.0f, 0.0f);
        } else {
            offset.set(lodOffset);
        }
    }

    /**
     * Sets the texture boundary color for this texture object.  The
     * texture boundary color is used when boundaryModeS or boundaryModeT
     * is set to CLAMP.
     * @param boundaryColor the new texture boundary color.
     */
    final void initBoundaryColor(Color4f boundaryColor) {
        this.boundaryColor.set(boundaryColor);
    }

    /**
     * Sets the texture boundary color for this texture object.  The
     * texture boundary color is used when boundaryModeS or boundaryModeT
     * is set to CLAMP.
     * @param r the red component of the color.
     * @param g the green component of the color.
     * @param b the blue component of the color.
     * @param a the alpha component of the color.
     */
    final void initBoundaryColor(float r, float g, float b, float a) {
        this.boundaryColor.set(r, g, b, a);
    }

    /**
     * Retrieves the texture boundary color for this texture object.
     * @param boundaryColor the vector that will receive the
     * current texture boundary color.
     */
    final void getBoundaryColor(Color4f boundaryColor) {
        boundaryColor.set(this.boundaryColor);
    }

    /**
     * Set Anisotropic Filter
     */
    final void initAnisotropicFilterMode(int mode) {
        anisotropicFilterMode = mode;
    }

    final int getAnisotropicFilterMode() {
        return anisotropicFilterMode;
    }

    final void initAnisotropicFilterDegree(float degree) {
        anisotropicFilterDegree = degree;
    }

    final float getAnisotropicFilterDegree() {
        return anisotropicFilterDegree;
    }

    /**
     * Set Sharpen Texture function
     */
    final void initSharpenTextureFunc(float[] lod, float[] pts) {
        if (lod == null) {
            sharpenTextureFuncPts = null;
            numSharpenTextureFuncPts = 0;
        } else {
            numSharpenTextureFuncPts = lod.length;
            if ((sharpenTextureFuncPts == null) || (sharpenTextureFuncPts.length != lod.length * 2)) {
                sharpenTextureFuncPts = new float[lod.length * 2];
            }
            for (int i = 0, j = 0; i < lod.length; i++) {
                sharpenTextureFuncPts[j++] = lod[i];
                sharpenTextureFuncPts[j++] = pts[i];
            }
        }
    }

    final void initSharpenTextureFunc(Point2f[] pts) {
        if (pts == null) {
            sharpenTextureFuncPts = null;
            numSharpenTextureFuncPts = 0;
        } else {
            numSharpenTextureFuncPts = pts.length;
            if ((sharpenTextureFuncPts == null) || (sharpenTextureFuncPts.length != pts.length * 2)) {
                sharpenTextureFuncPts = new float[pts.length * 2];
            }
            for (int i = 0, j = 0; i < pts.length; i++) {
                sharpenTextureFuncPts[j++] = pts[i].x;
                sharpenTextureFuncPts[j++] = pts[i].y;
            }
        }
    }

    final void initSharpenTextureFunc(float[] pts) {
        if (pts == null) {
            sharpenTextureFuncPts = null;
            numSharpenTextureFuncPts = 0;
        } else {
            numSharpenTextureFuncPts = pts.length / 2;
            if ((sharpenTextureFuncPts == null) || (sharpenTextureFuncPts.length != pts.length)) {
                sharpenTextureFuncPts = new float[pts.length];
            }
            for (int i = 0; i < pts.length; i++) {
                sharpenTextureFuncPts[i] = pts[i];
            }
        }
    }

    /**
     * Get number of points in the sharpen texture LOD function
     */
    final int getSharpenTextureFuncPointsCount() {
        return numSharpenTextureFuncPts;
    }

    /**
     * Copies the array of sharpen texture LOD function points into the
     * specified arrays
     */
    final void getSharpenTextureFunc(float[] lod, float[] pts) {
        if (sharpenTextureFuncPts != null) {
            for (int i = 0, j = 0; i < numSharpenTextureFuncPts; i++) {
                lod[i] = sharpenTextureFuncPts[j++];
                pts[i] = sharpenTextureFuncPts[j++];
            }
        }
    }

    final void getSharpenTextureFunc(Point2f[] pts) {
        if (sharpenTextureFuncPts != null) {
            for (int i = 0, j = 0; i < numSharpenTextureFuncPts; i++) {
                pts[i].x = sharpenTextureFuncPts[j++];
                pts[i].y = sharpenTextureFuncPts[j++];
            }
        }
    }

    final void initFilter4Func(float[] weights) {
        if (weights == null) {
            filter4FuncPts = null;
        } else {
            if ((filter4FuncPts == null) || (filter4FuncPts.length != weights.length)) {
                filter4FuncPts = new float[weights.length];
            }
            for (int i = 0; i < weights.length; i++) {
                filter4FuncPts[i] = weights[i];
            }
        }
    }

    final int getFilter4FuncPointsCount() {
        if (filter4FuncPts == null) {
            return 0;
        } else {
            return filter4FuncPts.length;
        }
    }

    final void getFilter4Func(float[] weights) {
        if (filter4FuncPts != null) {
            for (int i = 0; i < filter4FuncPts.length; i++) {
                weights[i] = filter4FuncPts[i];
            }
        }
    }

    /**
     * internal method only -- returns internal function points
     */
    final float[] getSharpenTextureFunc() {
        return sharpenTextureFuncPts;
    }

    final float[] getFilter4Func() {
        return filter4FuncPts;
    }

    void setLive(boolean backgroundGroup, int refCount) {
        enable = userSpecifiedEnable;
        super.doSetLive(backgroundGroup, refCount);
        if (images != null) {
            for (int j = 0; j < numFaces; j++) {
                for (int i = 0; i < maxLevels; i++) {
                    if (images[j][i] == null) {
                        throw new IllegalArgumentException(J3dI18N.getString("TextureRetained3") + i);
                    }
                    images[j][i].setLive(backgroundGroup, refCount);
                }
            }
        }
        if (images != null) {
            for (int j = 0; j < numFaces; j++) {
                checkSizes(images[j]);
            }
        }
        J3dMessage createMessage = new J3dMessage();
        createMessage.threads = J3dThread.UPDATE_RENDERING_ATTRIBUTES;
        createMessage.type = J3dMessage.TEXTURE_CHANGED;
        createMessage.args[0] = this;
        createMessage.args[1] = new Integer(UPDATE_IMAGE);
        createMessage.args[2] = null;
        createMessage.args[3] = new Integer(changedFrequent);
        VirtualUniverse.mc.processMessage(createMessage);
        if (userSpecifiedEnable) {
            if (images != null) {
                for (int j = 0; j < numFaces && enable; j++) {
                    for (int i = baseLevel; i <= maximumLevel && enable; i++) {
                        if (images[j][i].isByReference()) {
                            if (images[j][i].getRefImage(0) == null) {
                                enable = false;
                            }
                        } else {
                            if (images[j][i].getImageData(isUseAsRaster()).get() == null) {
                                enable = false;
                            }
                        }
                    }
                }
            } else {
                enable = false;
            }
            if (!enable) sendMessage(ENABLE_CHANGED, Boolean.FALSE);
        }
        super.markAsLive();
    }

    void clearLive(int refCount) {
        super.clearLive(refCount);
        if (images != null) {
            for (int j = 0; j < numFaces; j++) {
                for (int i = 0; i < maxLevels; i++) {
                    images[j][i].clearLive(refCount);
                    images[j][i].removeUser(mirror);
                }
            }
        }
    }

    void bindTexture(Context ctx, int objectId, boolean enable) {
        Pipeline.getPipeline().bindTexture2D(ctx, objectId, enable);
    }

    void updateTextureBoundary(Context ctx, int boundaryModeS, int boundaryModeT, float boundaryRed, float boundaryGreen, float boundaryBlue, float boundaryAlpha) {
        Pipeline.getPipeline().updateTexture2DBoundary(ctx, boundaryModeS, boundaryModeT, boundaryRed, boundaryGreen, boundaryBlue, boundaryAlpha);
    }

    void updateTextureFilterModes(Context ctx, int minFilter, int magFilter) {
        Pipeline.getPipeline().updateTexture2DFilterModes(ctx, minFilter, magFilter);
    }

    void updateTextureSharpenFunc(Context ctx, int numSharpenTextureFuncPts, float[] sharpenTextureFuncPts) {
        Pipeline.getPipeline().updateTexture2DSharpenFunc(ctx, numSharpenTextureFuncPts, sharpenTextureFuncPts);
    }

    void updateTextureFilter4Func(Context ctx, int numFilter4FuncPts, float[] filter4FuncPts) {
        Pipeline.getPipeline().updateTexture2DFilter4Func(ctx, numFilter4FuncPts, filter4FuncPts);
    }

    void updateTextureAnisotropicFilter(Context ctx, float degree) {
        Pipeline.getPipeline().updateTexture2DAnisotropicFilter(ctx, degree);
    }

    void updateTextureLodRange(Context ctx, int baseLevel, int maximumLevel, float minimumLod, float maximumLod) {
        Pipeline.getPipeline().updateTexture2DLodRange(ctx, baseLevel, maximumLevel, minimumLod, maximumLod);
    }

    void updateTextureLodOffset(Context ctx, float lodOffsetX, float lodOffsetY, float lodOffsetZ) {
        Pipeline.getPipeline().updateTexture2DLodOffset(ctx, lodOffsetX, lodOffsetY, lodOffsetZ);
    }

    int getTextureId() {
        return (VirtualUniverse.mc.getTexture2DId());
    }

    void freeTextureId(int id) {
        synchronized (resourceLock) {
            if (objectId == id) {
                objectId = -1;
                VirtualUniverse.mc.freeTexture2DId(id);
            }
        }
    }

    private boolean isEnabled(Canvas3D cv) {
        if (widthOrHeightIsNPOT && !isUseAsRaster() && ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_NON_POWER_OF_TWO) == 0)) {
            return false;
        }
        return enable;
    }

    void bindTexture(Canvas3D cv) {
        synchronized (resourceLock) {
            if (objectId == -1) {
                objectId = getTextureId();
            }
            cv.addTextureResource(objectId, this);
        }
        bindTexture(cv.ctx, objectId, isEnabled(cv));
    }

    /**
     * load level 0 explicitly with null pointer to enable
     * mipmapping when level 0 is not the base level
     */
    void updateTextureDimensions(Canvas3D cv) {
        if (images[0][0] != null) {
            updateTextureImage(cv, 0, maxLevels, 0, format, images[0][0].getImageFormatTypeIntValue(false), width, height, boundaryWidth, images[0][0].getImageDataTypeIntValue(), null);
        }
    }

    void updateTextureLOD(Canvas3D cv) {
        if ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_LOD_RANGE) != 0) {
            int max = 0;
            if (mipmapMode == Texture.BASE_LEVEL) {
                max = maxMipMapLevels;
            } else {
                max = maximumLevel;
            }
            updateTextureLodRange(cv.ctx, baseLevel, max, minimumLod, maximumLod);
        }
        if ((lodOffset != null) && ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_LOD_OFFSET) != 0)) {
            updateTextureLodOffset(cv.ctx, lodOffset.x, lodOffset.y, lodOffset.z);
        }
    }

    void updateTextureBoundary(Canvas3D cv) {
        updateTextureBoundary(cv.ctx, boundaryModeS, boundaryModeT, boundaryColor.x, boundaryColor.y, boundaryColor.z, boundaryColor.w);
    }

    void updateTextureFields(Canvas3D cv) {
        int magnificationFilter = magFilter;
        int minificationFilter = minFilter;
        if ((magnificationFilter >= Texture.LINEAR_SHARPEN) && (magnificationFilter <= Texture.LINEAR_SHARPEN_ALPHA)) {
            if ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_SHARPEN) != 0) {
                updateTextureSharpenFunc(cv.ctx, numSharpenTextureFuncPts, sharpenTextureFuncPts);
            } else {
                magnificationFilter = Texture.BASE_LEVEL_LINEAR;
            }
        } else if ((magnificationFilter >= Texture2D.LINEAR_DETAIL) && (magnificationFilter <= Texture2D.LINEAR_DETAIL_ALPHA)) {
            if ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_DETAIL) == 0) {
                magnificationFilter = Texture.BASE_LEVEL_LINEAR;
            }
        }
        if (minificationFilter == Texture.FILTER4 || magnificationFilter == Texture.FILTER4) {
            boolean noFilter4 = false;
            if ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_FILTER4) != 0) {
                if (filter4FuncPts == null) {
                    noFilter4 = true;
                } else {
                    updateTextureFilter4Func(cv.ctx, filter4FuncPts.length, filter4FuncPts);
                }
            } else {
                noFilter4 = true;
            }
            if (noFilter4) {
                if (minificationFilter == Texture.FILTER4) {
                    minificationFilter = Texture.BASE_LEVEL_LINEAR;
                }
                if (magnificationFilter == Texture.FILTER4) {
                    magnificationFilter = Texture.BASE_LEVEL_LINEAR;
                }
            }
        }
        if ((mipmapMode == Texture.BASE_LEVEL) && ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_AUTO_MIPMAP_GENERATION) == 0)) {
            if (minificationFilter == Texture.NICEST || minificationFilter == Texture.MULTI_LEVEL_LINEAR) {
                minificationFilter = Texture.BASE_LEVEL_LINEAR;
            } else if (minificationFilter == Texture.MULTI_LEVEL_POINT) {
                minificationFilter = Texture.BASE_LEVEL_POINT;
            }
        }
        updateTextureFilterModes(cv.ctx, minificationFilter, magnificationFilter);
        if ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_ANISOTROPIC_FILTER) != 0) {
            if (anisotropicFilterMode == Texture.ANISOTROPIC_NONE) {
                updateTextureAnisotropicFilter(cv.ctx, 1.0f);
            } else {
                updateTextureAnisotropicFilter(cv.ctx, anisotropicFilterDegree);
            }
        }
        updateTextureBoundary(cv);
    }

    void updateTextureImage(Canvas3D cv, int face, int numLevels, int level, int textureFormat, int imageFormat, int width, int height, int boundaryWidth, int imageDataType, Object data) {
        Pipeline.getPipeline().updateTexture2DImage(cv.ctx, numLevels, level, textureFormat, imageFormat, width, height, boundaryWidth, imageDataType, data, useAutoMipMapGeneration(cv));
    }

    void updateTextureSubImage(Canvas3D cv, int face, int level, int xoffset, int yoffset, int textureFormat, int imageFormat, int imgXOffset, int imgYOffset, int tilew, int width, int height, int imageDataType, Object data) {
        Pipeline.getPipeline().updateTexture2DSubImage(cv.ctx, level, xoffset, yoffset, textureFormat, imageFormat, imgXOffset, imgYOffset, tilew, width, height, imageDataType, data, useAutoMipMapGeneration(cv));
    }

    /**
     * reloadTextureImage is used to load a particular level of image
     * This method needs to take care of RenderedImage as well as
     * BufferedImage
     */
    void reloadTextureImage(Canvas3D cv, int face, int level, ImageComponentRetained image, int numLevels) {
        boolean useAsRaster = isUseAsRaster();
        ImageComponentRetained.ImageData imageData = image.getImageData(useAsRaster);
        assert imageData != null;
        updateTextureImage(cv, face, numLevels, level, format, image.getImageFormatTypeIntValue(useAsRaster), imageData.getWidth(), imageData.getHeight(), boundaryWidth, image.getImageDataTypeIntValue(), imageData.get());
        if (imageData == null) {
            int xoffset = 0, yoffset = 0;
            int tmpw = image.width;
            int tmph = image.height;
            int endXTile = image.tilew;
            int endYTile = image.tileh;
            int curw = endXTile;
            int curh = endYTile;
            if (tmpw < curw) {
                curw = tmpw;
            }
            if (tmph < curh) {
                curh = tmph;
            }
            int startw = curw;
            int imageXOffset = image.tilew - curw;
            int imageYOffset = image.tileh - curh;
            for (int m = 0; m < image.numYTiles; m++) {
                xoffset = 0;
                tmpw = width;
                curw = startw;
                imageXOffset = image.tilew - curw;
                for (int n = 0; n < image.numXTiles; n++) {
                    java.awt.image.Raster ras;
                    ras = ((RenderedImage) image.getRefImage(0)).getTile(n, m);
                    byte[] data = ((DataBufferByte) ras.getDataBuffer()).getData();
                    updateTextureSubImage(cv, face, level, xoffset, yoffset, format, image.getImageFormatTypeIntValue(false), imageXOffset, imageYOffset, image.tilew, curw, curh, ImageComponentRetained.IMAGE_DATA_TYPE_BYTE_ARRAY, (Object) data);
                    xoffset += curw;
                    imageXOffset = 0;
                    tmpw -= curw;
                    if (tmpw < image.tilew) curw = tmpw; else curw = image.tilew;
                }
                yoffset += curh;
                imageYOffset = 0;
                tmph -= curh;
                if (tmph < image.tileh) curh = tmph; else curh = image.tileh;
            }
        }
    }

    /**
     * update a subregion of the texture image
     * This method needs to take care of RenderedImage as well as
     * BufferedImage
     */
    void reloadTextureSubImage(Canvas3D cv, int face, int level, ImageComponentUpdateInfo info, ImageComponentRetained image) {
        int x = info.x, y = info.y, width = info.width, height = info.height;
        ImageComponentRetained.ImageData imageData = image.getImageData(isUseAsRaster());
        if (imageData != null) {
            int xoffset = x;
            int yoffset = y;
            if (!image.yUp) {
                yoffset = image.height - yoffset - height;
            }
            updateTextureSubImage(cv, face, level, xoffset, yoffset, format, image.getImageFormatTypeIntValue(false), xoffset, yoffset, image.width, width, height, image.getImageDataTypeIntValue(), imageData.get());
        } else {
            assert false;
            float mt;
            int minTileX, minTileY;
            int rx = x;
            int ry = y;
            mt = (float) (rx) / (float) image.tilew;
            if (mt < 0) {
                minTileX = (int) (mt - 1);
            } else {
                minTileX = (int) mt;
            }
            mt = (float) (ry) / (float) image.tileh;
            if (mt < 0) {
                minTileY = (int) (mt - 1);
            } else {
                minTileY = (int) mt;
            }
            int startXTile = minTileX * image.tilew;
            int startYTile = minTileY * image.tilew;
            int curw = (startXTile + image.tilew - rx);
            int curh = (startYTile + image.tileh - ry);
            if (curw > width) {
                curw = width;
            }
            if (curh > height) {
                curh = height;
            }
            int startw = curw;
            int tmpw = width;
            int tmph = height;
            int imgX = rx - startXTile;
            int imgY = ry - startYTile;
            int numXTiles = (width + imgX) / image.tilew;
            int numYTiles = (height + imgY) / image.tileh;
            if (((float) (width + imgX) % (float) image.tilew) > 0) {
                numXTiles += 1;
            }
            if (((float) (height + imgY) % (float) image.tileh) > 0) {
                numYTiles += 1;
            }
            java.awt.image.Raster ras;
            int textureX = x;
            int textureY = y;
            for (int yTile = minTileY; yTile < minTileY + numYTiles; yTile++) {
                tmpw = width;
                curw = startw;
                imgX = rx - startXTile;
                for (int xTile = minTileX; xTile < minTileX + numXTiles; xTile++) {
                    ras = ((RenderedImage) image.getRefImage(0)).getTile(xTile, yTile);
                    byte[] data = ((DataBufferByte) ras.getDataBuffer()).getData();
                    updateTextureSubImage(cv, face, level, textureX, textureY, format, image.getImageFormatTypeIntValue(false), imgX, imgY, image.tilew, curw, curh, ImageComponentRetained.IMAGE_DATA_TYPE_BYTE_ARRAY, (Object) data);
                    textureX += curw;
                    imgX = 0;
                    tmpw -= curw;
                    if (tmpw < image.tilew) {
                        curw = tmpw;
                    } else {
                        curw = image.tilew;
                    }
                }
                textureY += curh;
                imgY = 0;
                tmph -= curh;
                if (tmph < image.tileh) {
                    curh = tmph;
                } else {
                    curh = image.tileh;
                }
            }
        }
    }

    void reloadTexture(Canvas3D cv) {
        int blevel, mlevel;
        if ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_LOD_RANGE) == 0) {
            blevel = 0;
            mlevel = maxLevels - 1;
        } else {
            blevel = baseLevel;
            mlevel = maximumLevel;
        }
        if (blevel != 0) {
            updateTextureDimensions(cv);
        }
        for (int j = 0; j < numFaces; j++) {
            for (int i = blevel; i <= mlevel; i++) {
                ImageComponentRetained image = images[j][i];
                if (image != null) {
                    image.evaluateExtensions(cv);
                    reloadTextureImage(cv, j, i, image, maxLevels);
                }
            }
        }
    }

    void updateTexture(Canvas3D cv, int resourceBit) {
        ImageComponentUpdateInfo info;
        for (int k = 0; k < numFaces; k++) {
            for (int i = baseLevel; i <= maximumLevel; i++) {
                if (imageUpdateInfo[k][i] != null) {
                    for (int j = 0; j < imageUpdateInfo[k][i].size(); j++) {
                        info = (ImageComponentUpdateInfo) imageUpdateInfo[k][i].get(j);
                        synchronized (resourceLock) {
                            if ((info.updateMask & resourceBit) == 0) continue;
                            info.updateMask &= ~resourceBit;
                            if ((info.updateMask & resourceCreationMask) == 0) {
                                info.updateMask = 0;
                                if (imageUpdatePruneMask == null) {
                                    imageUpdatePruneMask = new int[numFaces];
                                }
                                imageUpdatePruneMask[k] = 1 << i;
                            }
                        }
                        if (info.entireImage == true) {
                            reloadTextureImage(cv, k, i, images[k][i], maxLevels);
                        } else {
                            reloadTextureSubImage(cv, k, i, info, images[k][i]);
                        }
                    }
                }
            }
        }
    }

    /**
     * reloadTextureSharedContext is called to reload texture
     * on a shared context. It is invoked from the Renderer
     * before traversing the RenderBin. The idea is to reload
     * all necessary textures up front for all shared contexts
     * in order to minimize the context switching overhead.
     */
    void reloadTextureSharedContext(Canvas3D cv) {
        if (!isEnabled(cv)) {
            return;
        }
        bindTexture(cv);
        updateTextureFields(cv);
        updateTextureLOD(cv);
        reloadTexture(cv);
        synchronized (resourceLock) {
            resourceCreationMask |= cv.screen.renderer.rendererBit;
            resourceUpdatedMask |= cv.screen.renderer.rendererBit;
            resourceLodUpdatedMask |= cv.screen.renderer.rendererBit;
            resourceInReloadList &= ~cv.screen.renderer.rendererBit;
        }
    }

    /**
     * updateNative is called while traversing the RenderBin to 
     * update the texture state
     */
    void updateNative(Canvas3D cv) {
        boolean reloadTexture = false;
        boolean updateTexture = false;
        boolean updateTextureLod = false;
        bindTexture(cv);
        if (!isEnabled(cv)) {
            return;
        }
        if (cv.useSharedCtx && cv.screen.renderer.sharedCtx != null) {
            if ((resourceCreationMask & cv.screen.renderer.rendererBit) == 0) {
                reloadTexture = true;
            } else {
                if (((resourceUpdatedMask & cv.screen.renderer.rendererBit) == 0) && (imageUpdateInfo != null)) {
                    updateTexture = true;
                }
                if ((resourceLodUpdatedMask & cv.screen.renderer.rendererBit) == 0) {
                    updateTextureLod = true;
                }
            }
            if (reloadTexture || updateTexture || updateTextureLod) {
                cv.makeCtxCurrent(cv.screen.renderer.sharedCtx);
                bindTexture(cv);
            }
        } else {
            if ((resourceCreationMask & cv.canvasBit) == 0) {
                reloadTexture = true;
            } else {
                if (((resourceUpdatedMask & cv.canvasBit) == 0) && (imageUpdateInfo != null)) {
                    updateTexture = true;
                }
                if ((resourceLodUpdatedMask & cv.canvasBit) == 0) {
                    updateTextureLod = true;
                }
            }
        }
        if (VirtualUniverse.mc.isD3D()) {
            if (texTimestamp != VirtualUniverse.mc.resendTexTimestamp) {
                texTimestamp = VirtualUniverse.mc.resendTexTimestamp;
                reloadTexture = true;
            }
            if (!reloadTexture) {
                updateTextureFields(cv);
            }
        }
        if (reloadTexture) {
            updateTextureFields(cv);
            updateTextureLOD(cv);
            reloadTexture(cv);
            if (cv.useSharedCtx) {
                cv.makeCtxCurrent(cv.ctx);
                synchronized (resourceLock) {
                    resourceCreationMask |= cv.screen.renderer.rendererBit;
                    resourceUpdatedMask |= cv.screen.renderer.rendererBit;
                    resourceLodUpdatedMask |= cv.screen.renderer.rendererBit;
                }
            } else {
                synchronized (resourceLock) {
                    resourceCreationMask |= cv.canvasBit;
                    resourceUpdatedMask |= cv.canvasBit;
                    resourceLodUpdatedMask |= cv.canvasBit;
                }
            }
        } else if (updateTextureLod || updateTexture) {
            if (updateTextureLod) {
                updateTextureLOD(cv);
            }
            if (updateTexture) {
                int resourceBit = 0;
                if (cv.useSharedCtx) {
                    resourceBit = cv.screen.renderer.rendererBit;
                } else {
                    resourceBit = cv.canvasBit;
                }
                updateTexture(cv, resourceBit);
            }
            if (cv.useSharedCtx) {
                cv.makeCtxCurrent(cv.ctx);
                synchronized (resourceLock) {
                    resourceUpdatedMask |= cv.screen.renderer.rendererBit;
                    resourceLodUpdatedMask |= cv.screen.renderer.rendererBit;
                }
            } else {
                synchronized (resourceLock) {
                    resourceUpdatedMask |= cv.canvasBit;
                    resourceLodUpdatedMask |= cv.canvasBit;
                }
            }
        }
    }

    synchronized void createMirrorObject() {
        if (mirror == null) {
            if (this instanceof Texture3DRetained) {
                Texture3DRetained t3d = (Texture3DRetained) this;
                Texture3D tex = new Texture3D(t3d.mipmapMode, t3d.format, t3d.width, t3d.height, t3d.depth, t3d.boundaryWidth);
                mirror = (Texture3DRetained) tex.retained;
                ;
            } else if (this instanceof TextureCubeMapRetained) {
                TextureCubeMap tex = new TextureCubeMap(mipmapMode, format, width, boundaryWidth);
                mirror = (TextureCubeMapRetained) tex.retained;
            } else {
                Texture2D tex = new Texture2D(mipmapMode, format, width, height, boundaryWidth);
                mirror = (Texture2DRetained) tex.retained;
            }
            ((TextureRetained) mirror).objectId = -1;
        }
        initMirrorObject();
    }

    /**
     * Initializes a mirror object, point the mirror object to the retained
     * object if the object is not editable
     */
    synchronized void initMirrorObject() {
        mirror.source = source;
        if (this instanceof Texture3DRetained) {
            Texture3DRetained t3d = (Texture3DRetained) this;
            ((Texture3DRetained) mirror).boundaryModeR = t3d.boundaryModeR;
            ((Texture3DRetained) mirror).depth = t3d.depth;
        }
        TextureRetained mirrorTexture = (TextureRetained) mirror;
        mirrorTexture.boundaryModeS = boundaryModeS;
        mirrorTexture.boundaryModeT = boundaryModeT;
        mirrorTexture.minFilter = minFilter;
        mirrorTexture.magFilter = magFilter;
        mirrorTexture.boundaryColor.set(boundaryColor);
        mirrorTexture.enable = enable;
        mirrorTexture.userSpecifiedEnable = enable;
        mirrorTexture.enable = enable;
        mirrorTexture.numFaces = numFaces;
        mirrorTexture.resourceCreationMask = 0x0;
        mirrorTexture.resourceUpdatedMask = 0x0;
        mirrorTexture.resourceLodUpdatedMask = 0x0;
        mirrorTexture.resourceInReloadList = 0x0;
        mirrorTexture.baseLevel = baseLevel;
        mirrorTexture.maximumLevel = maximumLevel;
        mirrorTexture.minimumLod = minimumLod;
        mirrorTexture.maximumLod = maximumLod;
        mirrorTexture.lodOffset = lodOffset;
        mirrorTexture.numSharpenTextureFuncPts = numSharpenTextureFuncPts;
        if (sharpenTextureFuncPts == null) {
            mirrorTexture.sharpenTextureFuncPts = null;
        } else {
            if ((mirrorTexture.sharpenTextureFuncPts == null) || (mirrorTexture.sharpenTextureFuncPts.length != sharpenTextureFuncPts.length)) {
                mirrorTexture.sharpenTextureFuncPts = new float[sharpenTextureFuncPts.length];
            }
            for (int i = 0; i < sharpenTextureFuncPts.length; i++) {
                mirrorTexture.sharpenTextureFuncPts[i] = sharpenTextureFuncPts[i];
            }
        }
        if (filter4FuncPts == null) {
            mirrorTexture.filter4FuncPts = null;
        } else {
            if ((mirrorTexture.filter4FuncPts == null) || (mirrorTexture.filter4FuncPts.length != filter4FuncPts.length)) {
                mirrorTexture.filter4FuncPts = new float[filter4FuncPts.length];
            }
            for (int i = 0; i < filter4FuncPts.length; i++) {
                mirrorTexture.filter4FuncPts[i] = filter4FuncPts[i];
            }
        }
        mirrorTexture.anisotropicFilterMode = anisotropicFilterMode;
        mirrorTexture.anisotropicFilterDegree = anisotropicFilterDegree;
        mirrorTexture.maxLevels = maxLevels;
        if (images != null) {
            for (int j = 0; j < numFaces; j++) {
                for (int i = 0; i < maxLevels; i++) {
                    mirrorTexture.images[j][i] = images[j][i];
                    if (images[j][i] != null) {
                        images[j][i].addUser(mirrorTexture);
                    }
                }
            }
        }
    }

    boolean useAutoMipMapGeneration(Canvas3D cv) {
        if (mipmapMode == Texture.BASE_LEVEL && (minFilter == Texture.NICEST || minFilter == Texture.MULTI_LEVEL_POINT || minFilter == Texture.MULTI_LEVEL_LINEAR) && ((cv.textureExtendedFeatures & Canvas3D.TEXTURE_AUTO_MIPMAP_GENERATION) != 0)) {
            return true;
        }
        return false;
    }

    /**
     * Go through the image update info list
     * and remove those that are already done
     * by all the resources
     */
    void pruneImageUpdateInfo() {
        ImageComponentUpdateInfo info;
        for (int k = 0; k < numFaces; k++) {
            for (int i = baseLevel; i <= maximumLevel; i++) {
                if ((imageUpdatePruneMask[k] & (1 << i)) != 0) {
                    if (imageUpdateInfo[k][i] != null) {
                        for (int j = 0; j < imageUpdateInfo[k][i].size(); j++) {
                            info = (ImageComponentUpdateInfo) imageUpdateInfo[k][i].get(j);
                            if (info.updateMask == 0) {
                                imageUpdateInfo[k][i].remove(j);
                            }
                        }
                    }
                    imageUpdatePruneMask[k] &= ~(1 << i);
                }
            }
        }
    }

    /**
     * addImageUpdateInfo(int level) is to update a particular level.
     * In this case, it supercedes all the subImage update for this level,
     * and all those update info can be removed from the update list.
     *
     * Note: this method is called from mirror only
     */
    void addImageUpdateInfo(int level, int face, ImageComponentUpdateInfo arg) {
        ImageComponentUpdateInfo info;
        if (imageUpdateInfo == null) {
            imageUpdateInfo = new ArrayList[numFaces][maxLevels];
        }
        if (imageUpdateInfo[face][level] == null) {
            imageUpdateInfo[face][level] = new ArrayList();
        }
        info = new ImageComponentUpdateInfo();
        if (arg == null) {
            info.entireImage = true;
        } else {
            info.entireImage = false;
        }
        if (info.entireImage) {
            imageUpdateInfo[face][level].clear();
            if (imageUpdatePruneMask != null) {
                imageUpdatePruneMask[face] &= ~(1 << level);
            }
        } else {
            info.x = arg.x;
            info.y = arg.y;
            info.z = arg.z;
            info.width = arg.width;
            info.height = arg.height;
        }
        info.updateMask = resourceCreationMask;
        imageUpdateInfo[face][level].add(info);
        if (imageUpdatePruneMask != null) {
            pruneImageUpdateInfo();
        }
    }

    void validate() {
        enable = true;
        for (int j = 0; j < numFaces && enable; j++) {
            for (int i = baseLevel; i <= maximumLevel && enable; i++) {
                if (images[j][i] == null) {
                    enable = false;
                }
            }
        }
    }

    /**
     * Update the "component" field of the mirror object with the 
     *  given "value"
     */
    synchronized void updateMirrorObject(int component, Object value) {
        TextureRetained mirrorTexture = (TextureRetained) mirror;
        if ((component & ENABLE_CHANGED) != 0) {
            mirrorTexture.enable = ((Boolean) value).booleanValue();
        } else if ((component & IMAGE_CHANGED) != 0) {
            Object[] arg = (Object[]) value;
            int level = ((Integer) arg[0]).intValue();
            ImageComponent image = (ImageComponent) arg[1];
            int face = ((Integer) arg[2]).intValue();
            if (mirrorTexture.images[face][level] != null) {
                mirrorTexture.images[face][level].removeUser(mirror);
            }
            if (image == null) {
                mirrorTexture.images[face][level] = null;
            } else {
                mirrorTexture.images[face][level] = (ImageComponentRetained) image.retained;
                mirrorTexture.images[face][level].addUser(mirror);
            }
            mirrorTexture.resourceUpdatedMask = 0;
            mirrorTexture.addImageUpdateInfo(level, face, null);
        } else if ((component & IMAGES_CHANGED) != 0) {
            Object[] arg = (Object[]) value;
            ImageComponent[] images = (ImageComponent[]) arg[0];
            int face = ((Integer) arg[1]).intValue();
            for (int i = 0; i < images.length; i++) {
                if (mirrorTexture.images[face][i] != null) {
                    mirrorTexture.images[face][i].removeUser(mirror);
                }
                if (images[i] == null) {
                    mirrorTexture.images[face][i] = null;
                } else {
                    mirrorTexture.images[face][i] = (ImageComponentRetained) images[i].retained;
                    mirrorTexture.images[face][i].addUser(mirror);
                }
            }
            mirrorTexture.updateResourceCreationMask();
        } else if ((component & BASE_LEVEL_CHANGED) != 0) {
            int level = ((Integer) value).intValue();
            if (level < mirrorTexture.baseLevel) {
                for (int j = 0; j < numFaces; j++) {
                    for (int i = level; i < mirrorTexture.baseLevel; i++) {
                        if (mirrorTexture.images[j][i] == null) {
                            mirrorTexture.enable = false;
                        } else {
                            mirrorTexture.addImageUpdateInfo(i, j, null);
                        }
                    }
                }
                mirrorTexture.baseLevel = level;
                mirrorTexture.resourceUpdatedMask = 0;
            } else {
                mirrorTexture.baseLevel = level;
                if (userSpecifiedEnable && (mirrorTexture.enable == false)) {
                    mirrorTexture.validate();
                }
            }
            mirrorTexture.resourceLodUpdatedMask = 0;
        } else if ((component & MAX_LEVEL_CHANGED) != 0) {
            int level = ((Integer) value).intValue();
            if (level > mirrorTexture.maximumLevel) {
                for (int j = 0; j < numFaces; j++) {
                    for (int i = mirrorTexture.maximumLevel; i < level; i++) {
                        if (mirrorTexture.images[j][i] == null) {
                            mirrorTexture.enable = false;
                        } else {
                            mirrorTexture.addImageUpdateInfo(i, j, null);
                        }
                    }
                }
                mirrorTexture.maximumLevel = level;
                mirrorTexture.resourceUpdatedMask = 0;
            } else {
                mirrorTexture.maximumLevel = level;
                if (userSpecifiedEnable && (mirrorTexture.enable == false)) {
                    mirrorTexture.validate();
                }
            }
            mirrorTexture.resourceLodUpdatedMask = 0;
        } else if ((component & MIN_LOD_CHANGED) != 0) {
            mirrorTexture.minimumLod = ((Float) value).floatValue();
            mirrorTexture.resourceLodUpdatedMask = 0;
        } else if ((component & MAX_LOD_CHANGED) != 0) {
            mirrorTexture.maximumLod = ((Float) value).floatValue();
            mirrorTexture.resourceLodUpdatedMask = 0;
        } else if ((component & LOD_OFFSET_CHANGED) != 0) {
            if ((mirrorTexture.lodOffset) == null) {
                mirrorTexture.lodOffset = new Point3f((Point3f) value);
            } else {
                mirrorTexture.lodOffset.set((Point3f) value);
            }
            mirrorTexture.resourceLodUpdatedMask = 0;
        } else if ((component & UPDATE_IMAGE) != 0) {
            mirrorTexture.updateResourceCreationMask();
        }
    }

    void notifyImageComponentImageChanged(ImageComponentRetained image, ImageComponentUpdateInfo value) {
        if (resourceCreationMask == 0) {
            if (imageUpdateInfo != null) {
                for (int face = 0; face < numFaces; face++) {
                    for (int level = 0; level < maxLevels; level++) {
                        if (imageUpdateInfo[face][level] != null) {
                            imageUpdateInfo[face][level].clear();
                        }
                    }
                    if (imageUpdatePruneMask != null) {
                        imageUpdatePruneMask[face] = 0;
                    }
                }
            }
            return;
        }
        boolean done;
        for (int j = 0; j < numFaces; j++) {
            done = false;
            for (int i = baseLevel; i <= maximumLevel && !done; i++) {
                if (images[j][i] == image) {
                    resourceUpdatedMask = 0;
                    addImageUpdateInfo(i, j, value);
                    done = true;
                }
            }
        }
    }

    void updateResourceCreationMask() {
        resourceCreationMask = 0x0;
    }

    void incTextureBinRefCount(TextureBin tb) {
        ImageComponentRetained image;
        setTextureBinRefCount(tb, getTextureBinRefCount(tb) + 1);
        for (int j = 0; j < numFaces; j++) {
            for (int i = 0; i < maxLevels; i++) {
                image = images[j][i];
                if (image != null && (image.isByReference() || (image.source != null && image.source.getCapability(ImageComponent.ALLOW_IMAGE_WRITE)))) {
                    tb.renderBin.addNodeComponent(image);
                }
            }
        }
    }

    void decTextureBinRefCount(TextureBin tb) {
        ImageComponentRetained image;
        setTextureBinRefCount(tb, getTextureBinRefCount(tb) - 1);
        for (int j = 0; j < numFaces; j++) {
            for (int i = 0; i < maxLevels; i++) {
                image = images[j][i];
                if (image != null && (image.isByReference() || (image.source != null && image.source.getCapability(ImageComponent.ALLOW_IMAGE_WRITE)))) {
                    tb.renderBin.removeNodeComponent(image);
                }
            }
        }
    }

    final void sendMessage(int attrMask, Object attr) {
        ArrayList univList = new ArrayList();
        ArrayList gaList = Shape3DRetained.getGeomAtomsList(mirror.users, univList);
        J3dMessage createMessage = new J3dMessage();
        createMessage.threads = J3dThread.UPDATE_RENDERING_ATTRIBUTES;
        createMessage.type = J3dMessage.TEXTURE_CHANGED;
        createMessage.universe = null;
        createMessage.args[0] = this;
        createMessage.args[1] = new Integer(attrMask);
        createMessage.args[2] = attr;
        createMessage.args[3] = new Integer(changedFrequent);
        VirtualUniverse.mc.processMessage(createMessage);
        for (int i = 0; i < univList.size(); i++) {
            createMessage = new J3dMessage();
            createMessage.threads = J3dThread.UPDATE_RENDER;
            createMessage.type = J3dMessage.TEXTURE_CHANGED;
            createMessage.universe = (VirtualUniverse) univList.get(i);
            createMessage.args[0] = this;
            createMessage.args[1] = new Integer(attrMask);
            createMessage.args[2] = attr;
            ArrayList gL = (ArrayList) gaList.get(i);
            GeometryAtom[] gaArr = new GeometryAtom[gL.size()];
            gL.toArray(gaArr);
            createMessage.args[3] = gaArr;
            VirtualUniverse.mc.processMessage(createMessage);
        }
    }

    void handleFrequencyChange(int bit) {
        switch(bit) {
            case Texture.ALLOW_ENABLE_WRITE:
            case Texture.ALLOW_IMAGE_WRITE:
            case Texture.ALLOW_LOD_RANGE_WRITE:
                {
                    setFrequencyChangeMask(bit, bit);
                }
            default:
                break;
        }
    }

    void setUseAsRaster(boolean useAsRaster) {
        this.useAsRaster = useAsRaster;
    }

    boolean isUseAsRaster() {
        return this.useAsRaster;
    }

    int getTextureBinRefCount(TextureBin tb) {
        Integer i = textureBinRefCount.get(tb.renderBin);
        return i == null ? 0 : i.intValue();
    }

    private void setTextureBinRefCount(TextureBin tb, int refCount) {
        if (refCount == 0) {
            textureBinRefCount.remove(tb.renderBin);
        } else {
            textureBinRefCount.put(tb.renderBin, new Integer(refCount));
        }
    }
}
